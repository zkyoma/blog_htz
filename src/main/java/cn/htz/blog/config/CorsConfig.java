package cn.htz.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    /**
     * 配置corsFilter，解决跨域问题
     * @return CorsFilter对象
     */
    @Bean
    public CorsFilter corsFilter() {
        //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名，如果要携带cookie,不能写*，*：代表所有域名都可以跨域访问
        configuration.addAllowedOrigin("*");
        //允许携带cookie
        configuration.setAllowCredentials(true);
        //代表所有的请求方法：GET，POST，DELETE，PUT...
        configuration.addAllowedMethod("*");
        //允许携带任何头信息
        configuration.addAllowedHeader("*");
        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        //拦截所有请求
        configurationSource.registerCorsConfiguration("/**", configuration);
        //返回CorsFilter对象
        return new CorsFilter(configurationSource);
    }
}
