package com.lsh.base.tools.dev.mybatis;

public class Main {

    public static void main(String[] args) {
        // 输出路径
        String outDir = "/Users/lixin-mac/Downloads/output";
        // 表名
        String[] tableNames = new String[]{"modify_log"};
        boolean bModel = true;
        // 是否生成 dao
        boolean bDao = true;
        // 是否生成 xml
        boolean bXml = true;
        // model 包名
        String modelPackageName = "com.lsh.wms.model.system";
        // dao 包名
        String daoPackageName = "com.lsh.wms.core.dao.system";
        // MyBatisRepository 包名
        String mybatisRepPackageName = "com.lsh.wms.core.dao";
        // 生成类名时是否删除表前缀
        boolean delPrefix = false;
        // 数据库驱动
        String dbDriver = "com.mysql.jdbc.Driver";
        // 数据库链接
        // String dbUrl = "jdbc:mysql://192.168.60.59:5200/lsh_wms?useUnicode=true&amp;autoReconnect=true&amp;characterEncoding=UTF8";
        String dbUrl = "jdbc:mysql://192.168.60.48:5201/lsh_wms?useUnicode=true&amp;autoReconnect=true&amp;characterEncoding=UTF8";
        // 数据库用户名
        String dbUsername = "root";
        // 数据库密码`
        String dbPassword = "root123";
        // 生成mybatis相关文件
        GenMybatis genMybatis = new GenMybatis(outDir, tableNames, bModel, bDao, bXml,
                modelPackageName, daoPackageName, mybatisRepPackageName, delPrefix,
                dbDriver, dbUrl, dbUsername, dbPassword);
        genMybatis.gen();
    }

}
