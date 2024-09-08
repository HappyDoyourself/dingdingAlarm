package com.gwm.marketing.filter.slowsql;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.phantomthief.collection.BufferTrigger;
import com.gwm.marketing.filter.DingdingAlarmUtil;
import com.gwm.marketing.filter.IpUtil;
import com.gwm.marketing.filter.buffertrigger.BufferTriggerConfig;
import com.gwm.marketing.filter.buffertrigger.Tag;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static java.util.Collections.synchronizedList;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/8/9 18:11
 */
@Component
public class SlowSqlBufferTrigger {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SlowSqlConfig slowSqlConfig;

    @Resource
    private BufferTriggerConfig bufferTriggerConfig;

    private static String[] colors = {
            // 蓝色
            "#0000FF",
            // 红色
            "#FF0000",
            // 绿色
            "#00FF00",
            // 紫色
            "#FF00FF",
            // 青色
            "#00FFFF",
            // 橙色
            "#FFA500",
            // 粉色
            "#FFC0CB",
            // 黑色
            "#000000"
    };

    BufferTrigger<DruidSlowSqlDto> stringBufferTrigger = BufferTrigger.<DruidSlowSqlDto, List<DruidSlowSqlDto>>simple()
            .maxBufferCount(1000)
            .interval(bufferTriggerConfig==null?60:bufferTriggerConfig.getErrorLogInterval(), TimeUnit.SECONDS)
            .setContainer(() -> synchronizedList(new ArrayList<>()), List::add)
            .consumer(this::slowSqlSendAlarm)
            .build();

    private void slowSqlSendAlarm(List<DruidSlowSqlDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            //根据druid ID做分组，统计每个慢sql次数
            Map<DruidSlowSqlDto, Long> druidMap = list.stream().collect(Collectors.groupingBy(t -> t, Collectors.mapping(m -> new DruidSlowSqlDto(m.getId()), Collectors.counting())));
            if (druidMap.size() > 0) {
                long sum = druidMap.values().stream().mapToLong(Long::longValue).sum();
                String slowSqlStr = druidMap.entrySet().stream().map(m -> buildSql(m.getKey()) + "执行次数:" + m.getValue() + "次;").collect(Collectors.joining());
                Tag tag = Tag.builder().applicationName(DingdingAlarmUtil.applicationName).env(DingdingAlarmUtil.env).ip(IpUtil.initIp()).build();
                DingdingAlarmUtil.sendAlarm(slowSqlStr, tag, sum);
            }
        }
    }

    private String buildSql(DruidSlowSqlDto dto) {
        Random random = new Random();
        String randomColor = colors[random.nextInt(colors.length)];
        String randomTimeColor = colors[random.nextInt(colors.length)];
        StringBuilder builder = new StringBuilder();
        builder.append("").append("\n\n**sql语句:** <font color=").append(randomColor).append(" size=6>" + dto.getSql() + " </font>\n" +
                "\n\n**参数  <font color=" + randomTimeColor + " face=\"黑体\">**:" +dto.getLastSlowParameters() + " </font>;\n" +
                "\n\n**traceId  <font color=" + randomTimeColor + " face=\"黑体\">**:" +(StringUtils.isEmpty(dto.getTraceId())?"无":dto.getTraceId()) + " </font>;\n" +
                "\n\n**maxTime  <font color=" + randomTimeColor + " face=\"黑体\">**:" + dto.getMaxTimespan() + "毫秒 </font>;\n");
        return builder.toString();
    }

    public void enqueue(DruidSlowSqlDto druidSlowSqlDto) {
        stringBufferTrigger.enqueue(druidSlowSqlDto);
    }


    /**
     * 定时扫描慢sql
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Deprecated
    @XxlJob("getSlowSqlEveryMinutesJobHandler")
    public ReturnT<String> getSlowSqlEveryMinutesJobHandler(String param) throws Exception {
        XxlJobLogger.log("get slow sql every minutes");
        try {
            List<Map<String, Object>> mapList = DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
            if (mapList != null && mapList.size() > 0) {
                for (Map<String, Object> map : mapList) {
                    Integer dataSourceId = (Integer) map.get("Identity");
                    List<Map<String, Object>> lists = DruidStatManagerFacade.getInstance().getSqlStatDataList(dataSourceId);
                    if (lists != null && lists.size() > 0) {
                        logger.info("存储的sql数据量:" + lists.size() + "条");
                        lists.forEach(t -> {
                            //todo 获取ExecuteAndResultSetHoldTime当前sql的执行时间,只有sql时间大于慢sql时间在进行打印
                            if(t.get("MaxTimespan") != null && Long.valueOf(t.get("MaxTimespan").toString()) > slowSqlConfig.getSlowSqlMillis()){
                                logger.info("druid慢sql信息:" + JSONObject.toJSON(t));
                                DruidSlowSqlDto dto = new DruidSlowSqlDto();
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                    try {
                                        dto = mapper.readValue(JSONObject.toJSON(t).toString(), DruidSlowSqlDto.class);
                                    } catch (Exception e) {
                                        logger.error("转换异常");
                                    }
                                    this.enqueue(dto);
                                } catch (Exception e) {
                                    logger.info("定时获取慢sql异常", e);
                                    dto.setSql(t.get("SQL").toString());
                                    dto.setLastSlowParameters(t.get("LastSlowParameters").toString());
                                    this.enqueue(dto);
                                }
                            }

                        });
                    }
                }
            }
        } catch (Exception e) {
            logger.error("定时获取慢sql任务异常",e);
        }
        return ReturnT.SUCCESS;
    }
}