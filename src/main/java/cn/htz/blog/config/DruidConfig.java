package cn.htz.blog.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid() {
        return new DruidDataSource();
    }

    //1. 配置 Druid 监控 之  管理后台的 Servlet
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();

        //登录查看信息的账号密码
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "123456");
        //白名单
        initParams.put("allow", ""); //默认就是允许所有访问
        //IP黑名单 (存在共同时，deny优先于allow): 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        // initParams.put("deny", "127.0.0.1");
        bean.setInitParameters(initParams);
        return bean;
    }

    //2.配置一个Web监控的Filter
    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        bean.setInitParameters(initParams);  //添加不需要过滤的格式信息
        //添加过滤规则
        bean.setUrlPatterns(Collections.singletonList("/*"));
        return bean;
    }
}
