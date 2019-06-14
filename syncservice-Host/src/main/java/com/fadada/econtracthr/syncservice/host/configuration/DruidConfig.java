package com.fadada.econtracthr.syncservice.host.configuration;


import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Druid配置
 *
 * @author zhaojiatao
 */
@Configuration
public class DruidConfig {

    private final Logger logger = LoggerFactory.getLogger(DruidConfig.class);

    @Value("${druid.configPath:#{null}}")
    private String dbConfigPath;

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() throws Exception {
        DruidDataSource datasource = new DruidDataSource();
        Properties prop = this.readProperties(dbConfigPath);
        datasource.setUrl(prop.getProperty("url"));
        datasource.setUsername(prop.getProperty("username"));
        datasource.setPassword(prop.getProperty("password"));
        datasource.setDriverClassName(prop.getProperty("driverClassName"));
        datasource.setConnectionProperties(prop.getProperty("connect-properties"));
        datasource.setMaxActive(Integer.parseInt(prop.getProperty("maxActive")));
        datasource.setMinIdle(Integer.parseInt(prop.getProperty("minIdle")));
        List<Filter> filters = new ArrayList<>();
        filters.add(configFilter());
        filters.add(statFilter());
        filters.add(wallFilter());
        datasource.setProxyFilters(filters);
        return datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
    public ConfigFilter configFilter(){
        ConfigFilter configFilter = new ConfigFilter();
        return configFilter;
    }

    @Bean
    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true); //slowSqlMillis用来配置SQL慢的标准，执行时间超过slowSqlMillis的就是慢。
        statFilter.setMergeSql(true); //SQL合并配置
        statFilter.setSlowSqlMillis(300);//slowSqlMillis的缺省值为3000，也就是3秒。
        return statFilter;
    }

    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }

    private Properties readProperties(String path) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            prop.load(input);

        } catch (IOException ex) {
            logger.error(ex.getMessage(),ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
        return prop;
    }
}