package com.gwm.marketing.filter;

import com.alibaba.fastjson.JSONObject;
import com.gwm.marketing.filter.buffertrigger.Tag;
import com.gwm.marketing.filter.slowsql.DruidSlowSqlDto;
import com.gwm.marketing.filter.slowsql.SlowSqlBufferTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author fanht
 * @Description
 * @Date 2021/11/12 下午6:34
 * @Version 1.0
 */
@Component
public class DingdingAlarmUtil {

    private static Logger logger = LoggerFactory.getLogger(DingdingAlarmUtil.class);

    private static  String SPLIT = ",";

    public static String dingdingTokenUrl;

    /**
     * 开关
     **/
    public static String dingdingAlermSwitch;

    /**
     * 超时时间 单位 毫秒
     */
    public static int dingdingAlermTimeOut;

    /**
     * 应用名称
     */
    public static String applicationName;

    /**
     * 当前环境
     */
    public static String env;

    @Value("${dingding.token.url}")
    public void setDingdingTokenUrl(String dingdingTokenUrl) {
        DingdingAlarmUtil.dingdingTokenUrl = dingdingTokenUrl;
    }

    @Value("${dingding.alerm.timeOut}")
    public void setDingdingAlermTimeOut(int dingdingAlermTimeOut) {
        DingdingAlarmUtil.dingdingAlermTimeOut = dingdingAlermTimeOut;
    }

    @Value("${dingding.alerm.switch}")
    public void setDingdingAlermSwitch(String dingdingAlermSwitch) {
        DingdingAlarmUtil.dingdingAlermSwitch = dingdingAlermSwitch;
    }

    @Value("${spring.cloud.nacos.discovery.metadata.env}")
    public void setEnv(String env) {
        DingdingAlarmUtil.env = env;
    }

    @Value("${spring.application.name}")
    public  void setApplicationName(String applicationName) {
        DingdingAlarmUtil.applicationName = applicationName;
    }

    /**
     * 钉钉报警
     */
    public static void sendDingdingAlerm(String message) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        JSONObject content = new JSONObject();
        content.put("content", message);
        jsonObject.put("text", content);
        logger.info("钉钉开关：" + dingdingAlermSwitch);
        if (VerifyConst.DINGDING_SWITCH.equals(dingdingAlermSwitch)) {

            String webHook = "https://oapi.dingtalk.com/robot/send?access_token=e5e9b37044085714c4ee6951f5f67ab1137be794f43e343e3c796539c21bbfe9";

            PostExample example = new PostExample();
            String response = null;
            try {
                response = example.post(webHook, jsonObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 发送钉钉消息
     */
    public static void sendDingdingAlerm(String message, String dingdingTokenUrl) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        JSONObject content = new JSONObject();
        content.put("content", message);
        jsonObject.put("text", content);
        logger.info("钉钉开关：" + dingdingAlermSwitch);
        if (VerifyConst.DINGDING_SWITCH.equals(dingdingAlermSwitch)) {
            PostExample example = new PostExample();
            String response = null;
            try {
                String[]  dingdingArr =  dingdingTokenUrl.split(SPLIT);
                if(dingdingArr != null &&dingdingArr.length>0){
                    for(int i = 0;i<dingdingArr.length;i++){
                        response = example.post(dingdingArr[i], jsonObject.toJSONString());
                        logger.info("response:" + response);
                    }
                }
            } catch (IOException e) {
                logger.error("dingding error result" ,e);
                e.printStackTrace();
            }
        }
    }


    /**
     * 启动异常发送钉钉消息
     */
    public static void sendDingdingInitAlerm(String message) {
        String dingdingTokenUrl = "https://oapi.dingtalk.com/robot/send?access_token=4ac606935c696e44a1eb55727f06eee78d81f572a812446108a0760e1435f40e";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        JSONObject content = new JSONObject();
        content.put("content", message);
        jsonObject.put("text", content);
        JSONObject isAtAll = new JSONObject();
        isAtAll.put("isAtAll", true);
        jsonObject.put("at", isAtAll);
        jsonObject.put("at", isAtAll);
        PostExample example = new PostExample();
        String response = null;
        try {
            String[]  dingdingArr =  dingdingTokenUrl.split(SPLIT);
            if(dingdingArr != null &&dingdingArr.length>0){
                for(int i = 0;i<dingdingArr.length;i++){
                    response = example.post(dingdingArr[i], jsonObject.toJSONString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 告警通过markdown方式
     * @param slowSql
     * @param tag
     */
    public static void sendAlarm(String slowSql, Tag tag,Long sum) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "markdown");
        JSONObject content = new JSONObject();
        content.put("title", "慢sql超时告警");
        content.put("text", "![screenshot](https://images.cnblogs.com/cnblogs_com/blogs/718800/galleries/2294157/o_230330085502_1.png) \n" +
                slowSql +
                "\n\n**1分钟内慢sql总条数**: " + sum + "条 \n" +
                "\n\n**应用名称**:" + tag.getApplicationName() + "\n" +
                "\n\n**登录环境**:" + tag.getEnv() + "\n" +
                "\n\n**登录地址**:" + tag.getIp() + "\n");
        jsonObject.put("markdown", content);
        PostExample example = new PostExample();
        String response = null;
        try {
            String[]  dingdingArr =  dingdingTokenUrl.split(SPLIT);
            if(dingdingArr != null &&dingdingArr.length>0){
                for(int i = 0;i<dingdingArr.length;i++){
                    response = example.post(dingdingArr[i], jsonObject.toJSONString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
