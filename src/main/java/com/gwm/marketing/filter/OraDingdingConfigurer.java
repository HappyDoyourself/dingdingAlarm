package com.gwm.marketing.filter;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwm.marketing.filter.exception.DingdingHandleException;
import com.gwm.marketing.filter.intercepter.*;
import com.gwm.marketing.filter.slowsql.DruidStatLogger;
import com.gwm.marketing.filter.urlconfig.OptionalAlarmUriUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @Author fanht
 * @Description 拦截器参数校验
 * @Date 2021/11/12 2:05 下午
 * @Version 1.0
 */
@Configuration
public class OraDingdingConfigurer implements WebMvcConfigurer, Interceptor {

    /**
     * 拦截器参数校验
     *
     * @param interceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        //注意拦截器的顺序
        interceptorRegistry.addInterceptor(new CharacterValidInterceptors());
        interceptorRegistry.addInterceptor(new HttpParamVerifyInterceptor());
        interceptorRegistry.addInterceptor(new HttpRequestStatisticsInterceptor());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {

    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {

    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {

    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {
        //新版本中的
        Optional<HttpMessageConverter<?>> optional = list.stream()
                .filter(o -> o instanceof MappingJackson2HttpMessageConverter)
                .findFirst();
        if (optional.isPresent()) {
            MappingJackson2HttpMessageConverter converter =
                    (MappingJackson2HttpMessageConverter) optional.get();
            /*注意不要使用JsonInclude.Include.NON_NULL,会影响端上取data的问题*/
           /* ObjectMapper mapper = converter.getObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);*/

        }
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
        //todo 添加全局异常，因为HandlerInterceptor不能显示异常 之前是写到了configureHandlerExceptionResolvers 里面，使用哪个会和全局异常DefaultGlobalExceptionHandler有先后顺序问题
       list.add(0,new DingdingHandleException());
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }

    @Bean
    public DispatcherServlet dispatcherServlet(){
        return new OraDispatcherServlet();
    }

    @Bean
    public DruidStatLogger druidStatLogger(){
        return new DruidStatLogger();
    }

    @Bean
    public OptionalAlarmUriUtils optionalAlarmUriUtils(){
        return new OptionalAlarmUriUtils();
    }

}
