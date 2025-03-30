package org.pt.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.pt.components.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用的全局配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> WHITE_LIST = new ArrayList<>();
    static {
        WHITE_LIST.add("/user/login");
        WHITE_LIST.add("/user/register");
        WHITE_LIST.add("/user/test");
        WHITE_LIST.add("/user/verifyCode");
        WHITE_LIST.add("/error");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // temporarily aborted
        registry
                .addInterceptor(new TokenInterceptor()) // 以下放行
                .excludePathPatterns(WHITE_LIST)
                .addPathPatterns("/**"); // 拦截所有，除了以上
    }

    /**
     * 添加跨域支持
     *
     * @param registry CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 设置允许跨域请求的域名
                .allowCredentials(true) // 是否允许cookies整数
                .allowedMethods("GET", "POST", "DELETE", "PUT") // 设置允许的请求方式
                .maxAge(3600 * 24); // 跨域允许时间
    }

    /**
     * 使用fast json配置消息转换器
     *
     * @param converters List<HttpMessageConverter<?>>
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1.需要定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastJson的配置信息，比如：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        // 3.处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        // 4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        // 5.将convert添加到converters当中.
        converters.add(fastJsonHttpMessageConverter);
    }
}