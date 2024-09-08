package com.gwm.marketing.filter.slowsql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/8/14 10:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DruidSlowSqlDto {

    /**druid慢sql的id值*/
    @JsonProperty(value = "ID")
    private Long id;
    /**慢sql语句*/
    @JsonProperty(value = "SQL")
    private String sql;

    /**耗时分布直方图,用于跟踪查询执行所花费的时间以及保持结果的时间 比如[0, 0, 2, 0, 0, 0, 0, 0],
     * 第一个索引位置表示小于1毫秒的时间范围，第二个索引位置表示1毫秒到10毫秒的时间范围，以此类推.
     * 这个表示2条时间范围在10到100毫秒的数据*/
    @JsonProperty(value = "ExecuteAndResultHoldTimeHistogram")
    private String[] executeAndResultHoldTimeHistogram;

    /**effectedRowCountHistogram表示查询执行期间受影响的行数的直方图。第一个索引位置表示受影响行数为1到10的查询次数，
     * 第二个索引位置表示受影响行数为10到100的查询次数，以此类推*/
    @JsonProperty(value = "EffectedRowCountHistogram")
    private String[] effectedRowCountHistogram;

    /**Druid中表示从查询结果中获取的行数的直方图,跟踪查询结果中行数的分布情况。第一个索引位置表示受影响行数为1到10的查询次数，
     * 第二个索引位置表示受影响行数为10到100的查询次数，以此类推*/
    @JsonProperty(value = "FetchRowCountHistogram")
    private String[] fetchRowCountHistogram;

    /**耗时最久的sql执行的时间*/
    @JsonProperty(value = "MaxTimespanOccurTime")
    private Long maxTimespanOccurTime;

    /**最近一次的慢sql执行时间*/
    @JsonProperty(value = "LastTime")
    private Long lastTime;

    /**最后一次慢sql的where条件参数*/
    @JsonProperty(value = "LastSlowParameters")
    private String lastSlowParameters;

    /**执行次数*/
   @JsonProperty(value = "ExecuteCount")
    private Long executeCount;

    /**最大执行耗时 单位：毫秒*/
    @JsonProperty(value = "MaxTimespan")
    private Long maxTimespan;

    /**当前并发数*/
    @JsonProperty(value = "ConcurrentMax")
    private Long concurrentMax;

    /**正在执行的SQL数量*/
    @JsonProperty(value = "RunningCount")
    private Long runningCount;

    /**添加请求的traceId**/
    private String traceId;


    /**
     * 根据id做分组统计次数
     * @param id
     */
    public DruidSlowSqlDto(Long id) {
        this.id = id;
    }

}
