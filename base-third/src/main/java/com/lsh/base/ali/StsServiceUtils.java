package com.lsh.base.ali;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.net.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoma on 16/3/29.
 */
public class StsServiceUtils {

    private static Logger logger = LoggerFactory.getLogger(StsServiceUtils.class);
    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    // 当前 STS API 版本
    public static final String STS_API_VERSION = "2015-04-01";

    // 获取sts arm 用户accessKey
    public static  final String accessKeyId = PropertyUtils.getString("sts.accessKeyId");
    public static final String accessKeySecret = PropertyUtils.getString("sts.accessKeySecret");
    public static final String roleArn = PropertyUtils.getString("sts.roleArn");
    public static final  String roleSessionName = PropertyUtils.getString("sts.roleSessionName");

    /**
     * 图片bucket
     */
    public static final String[] IMAGE_BUCKET = PropertyUtils.getStringArray("sts.image.bucket");
    /**
     * 图片cname
     */
    public static final String[] IMAGE_CNAME = PropertyUtils.getStringArray("sts.image.cname");
    /**
     * 图片endpoint
     */
    public static final String IMAGE_ENDPOINT = PropertyUtils.getString("sts.image.endpoint");

    /**
     * 获取图片信息
     * @param url
     * @return
     */
    public static StsServiceUtils.ImageInfo getImageInfo(String url){
        if(StringUtils.isEmpty(url)) return null;
        String json = HttpClientUtils.get(url+"@info");
        String dest = "";
        if (json!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(json);
            dest = m.replaceAll("");
        }
        return JsonUtils.json2Obj(dest, StsServiceUtils.ImageInfo.class);
    }

    /**
     * 获取token
     * @param accessKeyId
     * @param accessKeySecret
     * @param roleArn
     * @param roleSessionName
     * @param policy
     * @param protocolType
     * @return
     * @throws ClientException
     */
    public static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,
                                         String roleArn, String roleSessionName, String policy,
                                         ProtocolType protocolType) throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);

            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);

            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);

            return response;
        } catch (ClientException e) {
            throw e;
        }
    }

    public static String getCname(String buketname){
        int index = Arrays.binarySearch(IMAGE_BUCKET,buketname);
        if(index < 0){
            index = 0;
        }
        String cname = IMAGE_CNAME[index];
        return cname;

    }

    /**
     * 获取图片sts访问token
     * @return
     */
    public static StsServiceUtils.Credentials getImageToken(){
        // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
        String accessKeyId = StsServiceUtils.accessKeyId;
        String accessKeySecret = StsServiceUtils.accessKeySecret;
        // AssumeRole API 请求参数: RoleArn, RoleSessionName, Polciy, and DurationSeconds
        // RoleArn 需要在 RAM 控制台上获取
        String roleArn = StsServiceUtils.roleArn;
        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        // String roleSessionName = "external-username";
        String roleSessionName = StsServiceUtils.roleSessionName;
        StsServiceUtils.Credentials credentials = new StsServiceUtils.Credentials();
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:*\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        ProtocolType protocolType = ProtocolType.HTTPS;
        try {
            final AssumeRoleResponse response = assumeRole(accessKeyId, accessKeySecret,
                    roleArn, roleSessionName, policy, protocolType);

            credentials.setAccessKeyId(response.getCredentials().getAccessKeyId());
            credentials.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
            credentials.setEndpoint(IMAGE_ENDPOINT);
            credentials.setExpiration(response.getCredentials().getExpiration());
            credentials.setSecurityToken(response.getCredentials().getSecurityToken());
            int index = Calendar.getInstance().get(Calendar.MILLISECOND) % IMAGE_BUCKET.length;
            credentials.setBucketName(IMAGE_BUCKET[index]);
            String buketKey = getBuketKey();
            credentials.setBucketKey(buketKey);

            logger.info("Expiration: " + response.getCredentials().getExpiration());
            logger.info("Access Key Id: " + response.getCredentials().getAccessKeyId());
            logger.info("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
            logger.info("Security Token: " + response.getCredentials().getSecurityToken());
        } catch (ClientException e) {
            logger.info("Failed to get a token.");
            logger.info("Error code: " + e.getErrCode());
            logger.info("Error message: " + e.getErrMsg());
        }

        return  credentials;
    }

    /**
     * 自定义Object Key
     * @return
     */
    public static String getBuketKey() {
        StringBuffer sb = new StringBuffer();
        Calendar cl = Calendar.getInstance();
        sb.append(File.separator);
        sb.append(cl.get(Calendar.YEAR));
        sb.append(File.separator);
        sb.append((cl.get(Calendar.MONTH) + 1) < 10 ? "0" + (cl.get(Calendar.MONTH) + 1) : cl.get(Calendar.MONTH) + 1);
        sb.append(File.separator);
        sb.append((cl.get(Calendar.DATE)) < 10 ? "0" + (cl.get(Calendar.DATE)) : cl.get(Calendar.DATE));
        sb.append(File.separator);
        logger.info("getKey relative path={}", sb.toString());
        return sb.toString();
    }

    /**
     * 图片信息
     */
    public static class ImageInfo{
        Integer width;
        Integer height;
        Long size;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }
    }

    public static class Credentials {
        private String securityToken;
        private String accessKeySecret;
        private String accessKeyId;
        private String expiration;
        private String bucketName;
        private String bucketKey;
        private String endpoint;

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public Credentials() {

        }

        public String getSecurityToken() {
            return this.securityToken;
        }

        public void setSecurityToken(String securityToken) {
            this.securityToken = securityToken;
        }

        public String getAccessKeySecret() {
            return this.accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getAccessKeyId() {
            return this.accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getExpiration() {
            return this.expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getBucketKey() {
            return bucketKey;
        }

        public void setBucketKey(String bucketKey) {
            this.bucketKey = bucketKey;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }
}
