package com.lsh.base.common.net;

import com.lsh.base.common.config.PropertyUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FtpUtils {

    private static Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    private static String host = PropertyUtils.getString("ftp.host");
    private static int port = PropertyUtils.getInt("ftp.port");
    private static String username = PropertyUtils.getString("ftp.username");
    private static String password = PropertyUtils.getString("ftp.password");

    /**
     * @param file      待上传文件
     * @param remoteDir 目标目录（相对FT服务根目录）
     * @return
     * @throws Exception
     */
    public static boolean upload(File file, String remoteDir) {
        try {
            return upload(new FileInputStream(file), remoteDir);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public static boolean upload(InputStream input, String remoteDir) {
        FTPClient ftp = new FTPClient();
        try {
            FTPClientConfig config = new FTPClientConfig();
            ftp.configure(config);
            ftp.connect(host, port);
            ftp.login(username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            ftp.setControlEncoding("UTF-8");
            logger.info("Connected to {} {}", host, ftp.isConnected());
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.error("FTP server refused connection.");
                return false;
            }
            String fileName = remoteDir;
            if (remoteDir.contains("/")) {
                fileName = remoteDir.substring(remoteDir.lastIndexOf("/") + 1);
                String directory = remoteDir.substring(0, remoteDir.lastIndexOf("/") + 1);
                createDirIfNecessary(ftp, directory);
                boolean createFlag = ftp.changeWorkingDirectory(directory);
                logger.info("路由目录" + (createFlag ? "成功" : "失败"));
            }
            boolean flag = ftp.storeFile(fileName, input);
            logger.info("ftp upload " + (flag ? "success" : "fail"));
            ftp.logout();
            return flag;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 如果服务器不存在要上传的目标目录则创建，并路由到相应目录
     *
     * @return
     * @throws Exception
     */
    public static boolean createDirIfNecessary(FTPClient client, String directory) throws Exception {
        if (!directory.equalsIgnoreCase("/")
                && !client.changeWorkingDirectory(directory)) {
            // 如果远程目录不存在，则递归创建远程服务器目录
            int start = 0, end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = directory.substring(start, end);
                if (!client.changeWorkingDirectory(subDirectory)) {
                    if (client.makeDirectory(subDirectory)) {
                        client.changeWorkingDirectory(subDirectory);
                    } else {
                        logger.info("创建目录失败!");
                        return false;
                    }
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        try {
            String sourcePath = "/Users/yay/data/upload/staticfile///audio//2015/28/5/20152820155094ddb7ce9024c9ea1280b1bdd43e677.txt";
            String remoteDir = "/audio//2015/28/5/20152820155094ddb7ce9024c9ea1280b1bdd43e677.txt";
            boolean flag = FtpUtils.upload(new File(sourcePath), remoteDir);
            System.out.println("文件上传" + (flag ? "成功" : "失败"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}