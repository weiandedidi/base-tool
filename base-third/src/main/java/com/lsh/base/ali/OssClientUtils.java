package com.lsh.base.ali;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.utils.RandomUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class OssClientUtils {

    private static Logger logger = LoggerFactory.getLogger(OssClientUtils.class);

    public static String accessKeyId = PropertyUtils.getString("oss.accessKeyId");
    public static String accessKeySecret = PropertyUtils.getString("oss.accessKeySecret");

    /**
     * 文件bucket
     */
    public static final String FILE_BUCKET = PropertyUtils.getString("oss.file.bucket");
    /**
     * 文件cname
     */
    public static final String FILE_CNAME = PropertyUtils.getString("oss.file.cname");
    /**
     * 文件endpoint
     */
    public static final String FILE_ENDPOINT = PropertyUtils.getString("oss.file.endpoint");

    /**
     * 图片bucket
     */
    public static final String[] IMAGE_BUCKET = PropertyUtils.getStringArray("oss.image.bucket");
    /**
     * 图片cname
     */
    public static final String[] IMAGE_CNAME = PropertyUtils.getStringArray("oss.image.cname");
    /**
     * 图片endpoint
     */
    public static final String IMAGE_ENDPOINT = PropertyUtils.getString("oss.image.endpoint");

    //音频域名
    public static final String AUDIO_CNAME = PropertyUtils.getString("oss.audio_cname");
    public static final String AUDIO_BUCKET = PropertyUtils.getString("oss.audio_bucket");
    public static final String AUDIO_PRESET = PropertyUtils.getString("oss.audio.preset");
    public static final String AUDIO_MAIN_PRESET = PropertyUtils.getString("oss.audio.mainPreset");

    //视频域名
    public static final String VIDEO_CNAME = PropertyUtils.getString("oss.video_cname");
    public static final String VIDEO_BUCKET = PropertyUtils.getString("oss.video_bucket");
    public static final String VIDEO_PRESET = PropertyUtils.getString("oss.video.preset");
    public static final String VIDEO_MAIN_PRESET = PropertyUtils.getString("oss.video.mainPreset");

    private static OSSClient OSS_CLIENT_FILE = null;
    private static OSSClient OSS_CLIENT_IMAGE = null;
    private static OSSClient OSS_CLIENT_AUDIO = null;
    private static OSSClient OSS_CLIENT_VIDEO = null;

    public static OSSClient getFileOssClient() {
        if (OSS_CLIENT_FILE == null) {
            // 创建ClientConfiguration实例
            ClientConfiguration conf = new ClientConfiguration();
            // 设置HTTP最大连接数为10
            conf.setMaxConnections(10);
            // 设置TCP连接超时为5000毫秒
            conf.setConnectionTimeout(5000);
            // 设置最大的重试次数为3
            conf.setMaxErrorRetry(3);
            // 设置Socket传输数据超时的时间为2000毫秒
            conf.setSocketTimeout(2000);
            OSS_CLIENT_FILE = new OSSClient(FILE_ENDPOINT, accessKeyId, accessKeySecret, conf);
        }
        return OSS_CLIENT_FILE;
    }

    public static OSSClient getImageOssClient() {
        if (OSS_CLIENT_IMAGE == null) {
            // 创建ClientConfiguration实例
            ClientConfiguration conf = new ClientConfiguration();
            // 设置HTTP最大连接数为10
            conf.setMaxConnections(10);
            // 设置TCP连接超时为5000毫秒
            conf.setConnectionTimeout(5000);
            // 设置最大的重试次数为3
            conf.setMaxErrorRetry(3);
            // 设置Socket传输数据超时的时间为2000毫秒
            conf.setSocketTimeout(2000);
            OSS_CLIENT_IMAGE = new OSSClient(IMAGE_ENDPOINT, accessKeyId, accessKeySecret, conf);
        }
        return OSS_CLIENT_IMAGE;
    }

    public static OSSClient getAudioOssClient() {
        if (OSS_CLIENT_AUDIO == null) {
            // 创建ClientConfiguration实例
            ClientConfiguration conf = new ClientConfiguration();
            // 设置HTTP最大连接数为10
            conf.setMaxConnections(10);
            // 设置TCP连接超时为5000毫秒
            conf.setConnectionTimeout(5000);
            // 设置最大的重试次数为3
            conf.setMaxErrorRetry(4);
            // 设置Socket传输数据超时的时间为2000毫秒
            conf.setSocketTimeout(2000);
            OSS_CLIENT_AUDIO = new OSSClient(AUDIO_CNAME, accessKeyId, accessKeySecret, conf);
        }
        return OSS_CLIENT_AUDIO;
    }

    public static OSSClient getVideoOssClient() {
        if (OSS_CLIENT_VIDEO == null) {
            // 创建ClientConfiguration实例
            ClientConfiguration conf = new ClientConfiguration();
            // 设置HTTP最大连接数为10
            conf.setMaxConnections(10);
            // 设置TCP连接超时为5000毫秒
            conf.setConnectionTimeout(5000);
            // 设置最大的重试次数为3
            conf.setMaxErrorRetry(5);
            // 设置Socket传输数据超时的时间为2000毫秒
            conf.setSocketTimeout(2000);
            OSS_CLIENT_VIDEO = new OSSClient(VIDEO_CNAME, accessKeyId, accessKeySecret, conf);
        }
        return OSS_CLIENT_VIDEO;
    }

    public static String uploadFile(String key, File file) {
        try {
            if (StringUtils.isEmpty(key) || file == null) {
                return null;
            }
            while (key.startsWith("/")) {
                key = key.substring(1);
            }
            key = StringUtils.replace(key, "\\", "/");
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            getFileOssClient().putObject(FILE_BUCKET, key, FileUtils.openInputStream(file), meta);
            return FILE_CNAME + "/" + key;
        } catch (Exception e) {
            logger.error("上传到阿里云OSS失败：", e);
        }
        return null;
    }

    public static String uploadAudio(String key, File file) {
        try {
            if (StringUtils.isEmpty(key) || file == null) {
                return null;
            }
            while (key.startsWith("/")) {
                key = key.substring(1);
            }
            key = StringUtils.replace(key, "\\", "/");
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            getFileOssClient().putObject(AUDIO_BUCKET, key, FileUtils.openInputStream(file), meta);
            return AUDIO_CNAME + "/" + key;
        } catch (Exception e) {
            logger.error("上传到阿里云OSS失败：", e);
        }
        return null;
    }

    public static String uploadVideo(String key, File file) {
        try {
            if (StringUtils.isEmpty(key) || file == null) {
                return null;
            }
            while (key.startsWith("/")) {
                key = key.substring(1);
            }
            key = StringUtils.replace(key, "\\", "/");
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            getFileOssClient().putObject(VIDEO_BUCKET, key, FileUtils.openInputStream(file), meta);
            return VIDEO_CNAME + "/" + key;
        } catch (Exception e) {
            logger.error("上传到阿里云OSS失败：", e);
        }
        return null;
    }

    public static String uploadImage(String key, File file) {
        try {
            if (StringUtils.isEmpty(key) || file == null) {
                return null;
            }
            while (key.startsWith("/")) {
                key = key.substring(1);
            }
            key = StringUtils.replace(key, "\\", "/");
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            int index = Calendar.getInstance().get(Calendar.MILLISECOND) % IMAGE_BUCKET.length;
            getImageOssClient().putObject(IMAGE_BUCKET[index], key, FileUtils.openInputStream(file), meta);
            return IMAGE_CNAME[index] + "/" + key;
        } catch (Exception e) {
            logger.error("上传到阿里云OSS失败：", e);
        }
        return null;
    }

    /**
     * 生成图片名称  规则：201507uuid.png
     *
     * @return
     */
    public static String getImageId() {
        return new SimpleDateFormat("yyyyMM").format(new Date()) + RandomUtils.uuid2();
    }

    /**
     * 获取自定义Bucket路径
     * @param picTypeName
     * @param picName
     * @param uuid
     * @return
     */
    public static String getKey(String picTypeName,String picName,String uuid) {
        logger.info("getKey fileName={}", picName);
        StringBuffer sb = new StringBuffer();
        sb.append(File.separator);
        sb.append(picTypeName);
        Calendar cl = Calendar.getInstance();
        sb.append(File.separator);
        sb.append(cl.get(Calendar.YEAR));
        sb.append(File.separator);
        sb.append((cl.get(Calendar.MONTH) + 1) < 10 ? "0" + (cl.get(Calendar.MONTH) + 1) : cl.get(Calendar.MONTH) + 1);
        sb.append(File.separator);
        sb.append((cl.get(Calendar.DATE)) < 10 ? "0" + (cl.get(Calendar.DATE)) : cl.get(Calendar.DATE));
        sb.append(File.separator);
        sb.append(uuid);
        String extName = ".jpg";
        if(StringUtils.isNotBlank(picName) && picName.indexOf(".") > 0){
            if(picName.contains("@")){
                extName = picName.substring(picName.lastIndexOf("."),picName.lastIndexOf("@"));
            }else {
                extName = picName.substring(picName.lastIndexOf("."));
            }
        }
        sb.append(extName.toLowerCase());
        logger.info("getKey relative path={}", sb.toString());
        return sb.toString();
    }

}
