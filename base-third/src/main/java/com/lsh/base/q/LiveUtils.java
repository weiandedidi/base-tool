package com.lsh.base.q;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.q.Common.QcloudApiModuleCenter;
import com.lsh.base.q.Module.Live;
import com.lsh.base.q.Utilities.Json.JSONArray;
import com.lsh.base.q.Utilities.Json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 * 直播服务工具类
 */
public class LiveUtils {

    private static Logger logger = LoggerFactory.getLogger(LiveUtils.class);

    private static QcloudApiModuleCenter LIVE_MODULE = null;

    //开启频道
    public static String openChn(String chnid) throws Exception {
        QcloudApiModuleCenter module = getLiveModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("channelIds.1", chnid);
        String result = module.call("StartLVBChannel", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("open chn json_result={}", json_result);
        Integer code = json_result.getInt("code");
        if(code != 0){
            logger.error("open chn error,chnid={}",chnid);
            throw new Exception();
        }
        return result;
    }

    //关闭频道
    public static String stopChn(String chnid) throws Exception {
        QcloudApiModuleCenter module = getLiveModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("channelIds.1", chnid);
        String result = module.call("StopLVBChannel", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("stop chn json_result={}", json_result);
        Integer code = json_result.getInt("code");
        if(code != 0){
            logger.error("stop chn error,chnid={}",chnid);
            throw new Exception();
        }
        return result;
    }
    //关闭频道
    public static String closeChn(String chnid) throws Exception {
        QcloudApiModuleCenter module = getLiveModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("channelIds.1", chnid);
        String result = module.call("StopLVBChannel", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("stop chn json_result={}", json_result);
        Integer code = json_result.getInt("code");
        if(code != 0){
            logger.error("stop chn error,chnid={}",chnid);
            throw new Exception();
        }
        return result;
    }


    /**
     * 开始录制
     * @param chnid 频道ID
     * @return 录制任务ID
     * @throws Exception
     */
    public static Integer startRecord(String chnid) throws Exception {
        QcloudApiModuleCenter module = getLiveModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("channelId", chnid);
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        params.put("start_time", now);
        String result = module.call("CreateRecord", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("describRecord,json_result={}", json_result);
        Integer code = json_result.getInt("code");
        String message = json_result.getString("message");
        if(code != 0){
            logger.error("start live record failure,{}", message);
            throw new Exception();
        }
        return json_result.getInt("task_id");
    }


    public static void stopRecord(String chnid,Integer taskId) throws Exception {
        QcloudApiModuleCenter module = getLiveModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("channelId", chnid);
        params.put("taskId", taskId);
        String result = module.call("StopRecord", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("describRecord,json_result={}", json_result);
        Integer code = json_result.getInt("code");
        String message = json_result.getString("message");
        if(code != 0){
            logger.error("stop live record failure,{}", message);
            throw new Exception();
        }
    }
    //获取录制分片
    public static JSONArray describRecord(String chnid,Integer taskId) throws Exception {
        QcloudApiModuleCenter module = getLiveModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("channelId", chnid);
        params.put("taskId", taskId);
        String result = module.call("DescribeRecord", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("describRecord,json_result={}", json_result);
        Integer code = json_result.getInt("code");
        String message = json_result.getString("message");
        Integer totalCount = json_result.getInt("totalCount");
        if(code != 0){
            logger.error("describRecord failure,{}", message);
            throw new Exception();
        }
        JSONArray resultArr = new JSONArray();
        if(totalCount == 0){
            logger.error("无法获取到录制文件信息！");
            return resultArr;
        }
        logger.info("分片信息数为{}",totalCount);

        resultArr = json_result.getJSONArray("fileSet");
        return resultArr;
    }

    /**
     * 获取频道状态
     */
    public static Integer getChnStatus(String chnid) throws Exception {
        QcloudApiModuleCenter module = getLiveModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("channelId", chnid);
        String result = module.call("DescribeLVBChannel", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("getChnStatus,json_result={}", json_result);
        Integer code = json_result.getInt("code");
        String message = json_result.getString("message");
        if(code != 0){
            logger.error("getChnStatus failure,{}", message);
            throw new Exception();
        }
        JSONArray channelInfoAdrr = json_result.getJSONArray("channelInfo");

        if(channelInfoAdrr == null || channelInfoAdrr.length() == 0){
            logger.error("getChnStatus failure,查询不到相关频道！");
            throw new Exception();
        }
        JSONObject channelInfo = (JSONObject)channelInfoAdrr.get(0);
        return channelInfo.getInt("channel_status");
    }

    private static QcloudApiModuleCenter getLiveModule(){
        if(LIVE_MODULE == null){
            TreeMap<String, Object> config = new TreeMap<String, Object>();
            config.put("SecretId", "AKIDJkTtMp8oiqTeSXnv4s9ZxRz6JFUwnhmD");
            config.put("SecretKey", "Wnym7fdOGvdbVMPsSFlCRyKxUXVe4KC4");
            config.put("RequestMethod", "POST");
            config.put("DefaultRegion", "bj");
            LIVE_MODULE = new QcloudApiModuleCenter(new Live(), config);
        }
        return LIVE_MODULE;
    }
}
