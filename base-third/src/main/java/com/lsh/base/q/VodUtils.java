package com.lsh.base.q;

import com.lsh.base.q.Common.QcloudApiModuleCenter;
import com.lsh.base.q.Module.Live;
import com.lsh.base.q.Module.Vod;
import com.lsh.base.q.Utilities.Json.JSONArray;
import com.lsh.base.q.Utilities.Json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * 点播服务工具类
 */
public class VodUtils {
    private static Logger logger = LoggerFactory.getLogger(VodUtils.class);

    private static QcloudApiModuleCenter VOD_MODULE = null;

    /**
     * 获取视频播放列表（源文件+转码后文件）
     * @param fileId
     * @throws Exception
     */
    public static JSONArray getVideoPlayUrls(String fileId) throws Exception {
        QcloudApiModuleCenter module = getVodModule();
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("fileId", fileId);
        String result = module.call("DescribeVodPlayUrls", params);
        JSONObject json_result = new JSONObject(result);
        logger.info("DescribeVodPlayUrls,json_result={}",json_result);
        Integer code = json_result.getInt("code");
        String message = json_result.getString("message");
        if(code != 0){
            System.out.println(message);
            throw new Exception();
        }
        JSONArray fileArr = json_result.getJSONArray("playSet");
        return fileArr;
    }

    /**
     * 获取文件的原始存放地址
     * @param fileId
     * @return
     * @throws Exception
     */
    public static String getVideoOriginalUrls(String fileId) throws Exception {
        JSONArray fileArr = getVideoPlayUrls(fileId);

        for(int i = 0;i < fileArr.length(); i++){
            JSONObject video = (JSONObject)fileArr.get(i);
            if(video.getInt("definition") == 0){
                return video.getString("url");
            }
        }
        logger.info("can not find original file! fileid={}",fileId);
        return "";
    }



    private static QcloudApiModuleCenter getVodModule(){
        if(VOD_MODULE == null){
            TreeMap<String, Object> config = new TreeMap<String, Object>();
            config.put("SecretId", "AKIDJkTtMp8oiqTeSXnv4s9ZxRz6JFUwnhmD");
            config.put("SecretKey", "Wnym7fdOGvdbVMPsSFlCRyKxUXVe4KC4");
            config.put("RequestMethod", "POST");
            config.put("DefaultRegion", "bj");
            VOD_MODULE = new QcloudApiModuleCenter(new Vod(), config);
        }
        return VOD_MODULE;
    }

}
