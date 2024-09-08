package com.gwm.marketing.filter.buffertrigger;

import lombok.*;

import java.util.Objects;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/6/18 17:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tag {

    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 环境dev/test/uat/prod
     */
    private String env;
    /**
     * ip地址
     */
    private String ip;
    /**
     * traceId
     */
    private String traceId;
    /**
     * 异常信息
     */
    private String exMessage;
    /**
     * 接口名称
     */
    private String requestUri;
    /**
     * 请求方法get/post/put/delete
     */
    private String method;

    private Long timeOut;

    public Tag(String exMessage, String requestUri) {
        this.exMessage = exMessage;
        this.requestUri = requestUri;
    }

    public Tag(String requestUri) {
        this.requestUri = requestUri;
    }

    /**
     * todo:goupBy分组时候是根据equals和hashCode分组的
     * @param o 对于异常告警需要异常信息和接口名称都是同一个
     *          对于超时告警，接口名称相同即可
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        if(tag.exMessage == null){
            return requestUri.equals(tag.requestUri);
        }
        return requestUri.equals(tag.requestUri) && exMessage.equals(tag.exMessage);
    }

    /**
     * 根据请求地址和异常信息分组统计
     * @return
     */
    @Override
    public int hashCode() {
        if(exMessage == null){
            return Objects.hash(requestUri);
        }
        return Objects.hash(requestUri, exMessage);
    }

}
