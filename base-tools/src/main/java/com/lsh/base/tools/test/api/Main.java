package com.lsh.base.tools.test.api;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhuminghua on 15/7/23.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 测试文件数组
        List<String> inList = Lists.newArrayList();
        inList.add("api/case/test.xml");
        // 输出文件夹路径
        String output = "/Users/zhuminghua/Downloads/";
        // 执行批量测试
        logger.info("=====测试开始=====");
        TestApi api = new TestApi();
        for (String in : inList) {
            api.testApi(in, output);
        }
        logger.info("=====测试结束=====");
    }

}
