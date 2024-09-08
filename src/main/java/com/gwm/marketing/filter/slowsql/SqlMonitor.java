package com.gwm.marketing.filter.slowsql;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.support.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;


/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:自定义慢sql拦截器
 * @Date: 2023/8/16 15:40
 */
@Component
public class SqlMonitor extends FilterEventAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SlowSqlConfig slowSqlConfig;

    @Resource
    private SlowSqlBufferTrigger slowSqlBufferTrigger;

    private static final String IGNORE_SQL = "SELECT 1";

    @Override
    protected void statementExecuteBefore(StatementProxy statement, String sql) {
        super.statementExecuteBefore(statement, sql);
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
        if (IGNORE_SQL.equals(sql)) {
            return;
        }
        final long nowNano = System.nanoTime();
        final long nanos = nowNano - statement.getLastExecuteStartNano();
        long millis = nanos / (1000 * 1000);
        if (millis >= slowSqlConfig.getSlowSqlMillis()) {
            String slowParameters = buildSlowParameters(statement);
            DruidSlowSqlDto dto = DruidSlowSqlDto.builder()
                    .sql(sql).maxTimespan(millis).lastSlowParameters(slowParameters).traceId(MDC.get("traceId")).build();
            logger.info("slow sql " + millis + " millis. " + sql + "" + slowParameters);
            slowSqlBufferTrigger.enqueue(dto);
        }
    }

    @Override
    protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
        super.statementExecuteUpdateBefore(statement, sql);
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteBatchBefore(StatementProxy statement) {
        super.statementExecuteBatchBefore(statement);
        final String sql = statement.getBatchSql();
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        final long nowNano = System.nanoTime();
        final long nanos = nowNano - statement.getLastExecuteStartNano();
        long millis = nanos / (1000 * 1000);
        if (millis >= slowSqlConfig.getSlowSqlMillis()) {
            String batchSql = statement.getBatchSql();
            String slowParameters = buildSlowParameters(statement);
            DruidSlowSqlDto dto = DruidSlowSqlDto.builder()
                    .sql(batchSql).maxTimespan(millis).lastSlowParameters(slowParameters).traceId(MDC.get("traceId")).build();
            logger.info("slow sql " + millis + " millis. " + batchSql + "" + slowParameters);
            slowSqlBufferTrigger.enqueue(dto);
        }
    }

    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        if (IGNORE_SQL.equals(sql)) {
            return;
        }
        final long nowNano = System.nanoTime();
        final long nanos = nowNano - statement.getLastExecuteStartNano();
        long millis = nanos / (1000 * 1000);
        if (millis >= slowSqlConfig.getSlowSqlMillis()) {
            String slowParameters = buildSlowParameters(statement);
            DruidSlowSqlDto dto = DruidSlowSqlDto.builder()
                    .sql(sql).maxTimespan(millis).lastSlowParameters(slowParameters).traceId(MDC.get("traceId")).build();
            logger.info("slow sql " + millis + " millis. " + sql + "" + slowParameters);
            slowSqlBufferTrigger.enqueue(dto);
        }

    }

    @Override
    protected void statementExecuteQueryBefore(StatementProxy statement, String sql) {
        super.statementExecuteQueryBefore(statement, sql);
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        if (IGNORE_SQL.equals(sql)) {
            return;
        }
        final long nowNano = System.nanoTime();
        final long nanos = nowNano - statement.getLastExecuteStartNano();
        long millis = nanos / (1000 * 1000);
        if (millis >= slowSqlConfig.getSlowSqlMillis()) {
            String slowParameters = buildSlowParameters(statement);
            DruidSlowSqlDto dto = DruidSlowSqlDto.builder()
                    .sql(sql).maxTimespan(millis).lastSlowParameters(slowParameters).traceId(MDC.get("traceId")).build();
            logger.info("slow sql " + millis + " millis. " + sql + "" + slowParameters);
            slowSqlBufferTrigger.enqueue(dto);
        }
    }


    protected String buildSlowParameters(StatementProxy statement) {
        JSONWriter out = new JSONWriter();

        out.writeArrayStart();
        for (int i = 0, parametersSize = statement.getParametersSize(); i < parametersSize; ++i) {
            JdbcParameter parameter = statement.getParameter(i);
            if (i != 0) {
                out.writeComma();
            }
            if (parameter == null) {
                continue;
            }

            Object value = parameter.getValue();
            if (value == null) {
                out.writeNull();
            } else if (value instanceof String) {
                String text = (String) value;
                if (text.length() > 100) {
                    out.writeString(text.substring(0, 97) + "...");
                } else {
                    out.writeString(text);
                }
            } else if (value instanceof Number) {
                out.writeObject(value);
            } else if (value instanceof java.util.Date) {
                out.writeObject(value);
            } else if (value instanceof Boolean) {
                out.writeObject(value);
            } else if (value instanceof InputStream) {
                out.writeString("<InputStream>");
            } else if (value instanceof NClob) {
                out.writeString("<NClob>");
            } else if (value instanceof Clob) {
                out.writeString("<Clob>");
            } else if (value instanceof Blob) {
                out.writeString("<Blob>");
            } else {
                out.writeString('<' + value.getClass().getName() + '>');
            }
        }
        out.writeArrayEnd();

        return out.toString();
    }

}
