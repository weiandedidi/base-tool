package com.lsh.base.q;

import com.lsh.base.q.Utilities.Json.JSONObject;
import com.lsh.base.q.video.VideoRequest;
import com.lsh.base.q.video.VideoSign;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 杨爱友 on 16/3/22.
 * 微视频服务
 */
public class VideoUtils {

    public final static String VIDEO_CGI_URL = "http://web.video.myqcloud.com/files/v1/";
    public final static int appId = 10023840;
    public final static String secretId = "AKIDoqin7mezwd0Sb7E0tA4aGfB6jMQ9cHYD";
    private final static String secretKey = "z2GBJdOKv6a8PqPqLUVeSdRuvzcNAnYF";
    private final static int timeOut = 60 * 1000;

    /**
     * 获取签名
     * @return
     */
    public static String getSign(String bucketName){
        long expired = System.currentTimeMillis() / 1000 + 60;//一分钟后过期
        return VideoSign.appSign(appId, secretId, secretKey, expired, bucketName);
    }

    /**
     * 获取文件ID，删除文件用
     * @param bucketName bucket名称
     * @param remotePath 远程文件路径及文件名，格式/path/filename
     */
    public static String getFid(String bucketName, String remotePath) throws Exception{
        String url = VIDEO_CGI_URL + appId + "/" + bucketName + encodeRemotePath(remotePath);
        return url;
    }

    /**
     * 获取文件信息
     * @param bucketName bucket名称
     * @param remotePath 远程文件路径
     * @return
     * @throws Exception
     */
    public static String getFileStat(String bucketName, String remotePath) throws Exception{
        String url = VIDEO_CGI_URL + appId + "/" + bucketName + encodeRemotePath(remotePath);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("op", "stat");
        long expired = System.currentTimeMillis() / 1000 + 60;
        String sign = getSign(bucketName);
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Authorization", sign);
        return VideoRequest.sendRequest(url, data, "GET", header, timeOut);
    }
    /**
     * 获取标清信息，可用于判断转码是否完成
     * @param bucketName
     * @param remotePath
     * @return 转码未完成返回空{coverUrl:"",playUrl:""}
     * @throws Exception
     */
    public static Map getQVideoInfo(String bucketName,String remotePath) throws Exception{
        Map resultMap = new HashMap();
        String result = getFileStat(bucketName, remotePath);
        JSONObject json_result = new JSONObject(result);
        int code = json_result.getInt("code");
        if(code != 0){
            return null;
        }

        JSONObject videoInfo = new JSONObject(result).getJSONObject("data");
        JSONObject trans_status = videoInfo.getJSONObject("trans_status");
        if(trans_status.getInt("f20") == 2){
            //标清转码完成
            resultMap.put("coverUrl",videoInfo.getString("video_cover"));
            resultMap.put("duration", videoInfo.getInt("video_play_time"));
            resultMap.put("filesize", videoInfo.getInt("filesize"));
            JSONObject urlObj = videoInfo.getJSONObject("video_play_url");
            resultMap.put("playUrl",urlObj.getString("f20"));
            return resultMap;
        }else{
            return null;
        }
    }
    /**
     * 远程路径Encode处理
     * @param remotePath
     * @return
     */
    private static String encodeRemotePath(String remotePath)
    {
        if(remotePath.equals("/")){
            return remotePath;
        }
        boolean endWith = remotePath.endsWith("/");
        String[] part = remotePath.split("/");
        remotePath = "";
        for(String s : part){
            if (!s.equals(""))
            {
                if(!remotePath.equals("")){
                    remotePath += "/";
                }
                remotePath += URLEncoder.encode(s);
            }
        }
        remotePath = (remotePath.startsWith("/") ? "" : "/") + remotePath + (endWith ? "/" : "");
        return remotePath;
    }

//    /**
//     * 获取标清地址，可用于判断转码是否完成
//     * @param bucketName
//     * @param remotePath
//     * @return
//     * @throws Exception
//     */
//    public static String getF20Url(String bucketName,String remotePath) throws Exception{
//        String result = getFileStat(bucketName, remotePath);
//        JSONObject videoInfo = new JSONObject(result).getJSONObject("data");
//        JSONObject trans_status = videoInfo.getJSONObject("trans_status");
////        System.out.println("filesize="+videoInfo.getString("filesize"));
////        System.out.println("access_url="+videoInfo.getString("access_url"));
//        if(trans_status.getInt("f20") == 2){
//            //标清转码完成
////            System.out.println("video_cover="+videoInfo.getString("video_cover"));
////            System.out.println("video_play_time=" + videoInfo.getInt("video_play_time"));
//            JSONObject urlObj = videoInfo.getJSONObject("video_play_url");
////            System.out.println("f20 url ="+urlObj.getString("f20"));
//            return urlObj.getString("f20");
//        }else{
//            return "";
//        }
//    }

//    /**
//     * 获取播放地址
//     * @param bucketName
//     * @param remotePath
//     * @return
//     * @throws Exception
//     */
//    public static String getAccessUrl(String bucketName, String remotePath) throws Exception{
//        String url = "http://" + bucketName + "-"+appId + ".video.myqcloud.com" + remotePath;
//        return url;
//    }

}
