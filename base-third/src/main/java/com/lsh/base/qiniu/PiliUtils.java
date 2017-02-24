package com.lsh.base.qiniu;

import com.lsh.base.qiniu.pili.API;
import com.lsh.base.qiniu.pili.Hub;
import com.lsh.base.qiniu.pili.PiliException;
import com.lsh.base.qiniu.pili.Stream;
import com.lsh.base.qiniu.pili.Stream.SaveAsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yay on 16/5/11.
 */
public class PiliUtils {
    private static Logger logger = LoggerFactory.getLogger(PiliUtils.class);

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String ACCESS_KEY = "YYTMah4hc628o12rhSOlY_8fez34C44y1Xs-Xo5A";
    private static final String SECRET_KEY = "FOAo6zWP107Jdfjti_ntwDRWrN-xHceRx0cymlEz";
    private static Credentials credentials = new Credentials(ACCESS_KEY, SECRET_KEY);


    public static String getStatus(String hubName,String title) throws PiliException{
        String streamId = "z1." + hubName + "." + title;
        String status = "";
        try {
            status = API.getStreamStatus(credentials, streamId).getStatus();
        } catch (PiliException e) {
            if(e.code() == 404){
                status = "disconnected";
            }else{
               throw e;
            }
        }
        return status;
    }
    public static Stream.Segment getSegment(String streamId) {
        long start = 0;
        long end = 0;
        int limit = 0;
        try {
            Stream.SegmentList segmentList = API.getStreamSegments(credentials, streamId, start, end, limit);
            if(segmentList != null && segmentList.getSegmentList() != null && segmentList.getSegmentList().size() > 0){
                return segmentList.getSegmentList().get(0);
            }else{
                return null;
            }
        } catch (PiliException e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }
    public static Stream getStream(String hubName,String title) throws PiliException{
        String streamId = "z1." + hubName + "." + title;
        Hub hub = new Hub(credentials, hubName);
        Stream stream = null;
        try {
            stream = hub.getStream(streamId);
        } catch (PiliException e) {
            if(e.code() == 404){
                logger.info("can not find stream,streamId={}",streamId);
            }else{
                throw e;
            }
        }

        return  stream;
    }

//    public static Stream getAndCreatStream(String hubName,String title) throws PiliException {
//        Stream stream  = getStream(hubName,title);
//        if(stream == null){
//            stream = createStream(hubName,title);
//        }
//        return  stream;
//    }

    public static Stream createStream(String hubName,String title) throws PiliException {
        Hub hub = new Hub(credentials, hubName);
        String publishKey      = null;     // optional, auto-generated as default
        String publishSecurity = "dynamic";     // optional, can be "dynamic" or "static", "dynamic" as default
        Stream stream = hub.createStream(title, publishKey, publishSecurity);
        logger.info("createStream:" + stream.toJsonString());
        return stream;
    }
    public static Stream enabledStream(String hubName,String title) throws PiliException {
        String streamId = "z1." + hubName + "." + title;
        Stream stream =  API.updateStream(credentials, streamId, null, null, true);
        return stream;
    }
    public static Stream disableStream(String hubName,String title) throws PiliException {
        String streamId = "z1." + hubName + "." + title;
        Stream stream =  API.updateStream(credentials, streamId, null, null, true);
        return stream;
    }

    public static SaveAsResponse streamSave(String hubName,String streamId,String saveAsName,long startTime,long endTime,String notifyUrl) throws PiliException {
        String saveAsFormat = "mp4";
//        String saveAsName= title + "_" + startTime + "_" + endTime + "." + saveAsFormat;
//        String saveAsNotifyUrl = "http://test.api.pub.lsh.com/api/pub/java/v1/piliCall/statusChange";
        Hub hub = new Hub(credentials, hubName);
        Stream stream = hub.getStream(streamId);
        SaveAsResponse response = stream.saveAs(saveAsName, saveAsFormat, startTime, endTime, notifyUrl, null);
        logger.info(response.toString());

        //{"url":"http://pili-static.test.live.lsh.com/recordings/z1.lajin-test.10027/10003.m3u8","duration":56,"targetUrl":"http://pili-media.test.live.lsh.com/recordings/z1.lajin-test.10027/10003.mp4","persistentId":"z1.57330411f51b825e885468fd"}
        return response;
    }
    public static List<Map> getDownList(Stream stream) throws PiliException {
        List<Map> resultList = new ArrayList<Map>();
        Map hlsMap = new HashMap();
        hlsMap.put("playType","1");
        hlsMap.put("vType","1");
        hlsMap.put("url",stream.hlsLiveUrls().get("ORIGIN"));
        resultList.add(hlsMap);

        Map rtmpMap = new HashMap();
        rtmpMap.put("playType","2");
        rtmpMap.put("vType","1");
        rtmpMap.put("url",stream.rtmpLiveUrls().get("ORIGIN"));
        resultList.add(rtmpMap);

        Map flvMap = new HashMap();
        flvMap.put("playType","3");
        flvMap.put("vType","1");
        flvMap.put("url",stream.httpFlvLiveUrls().get("ORIGIN"));
        resultList.add(flvMap);
        return resultList;
    }

}
