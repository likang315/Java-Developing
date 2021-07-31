package com.atlantis.zeus.base.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 配置数据源
 * MapperScan 把指定包的mapper 实例化放到容器中
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 19:28
 */
@MapperScan(basePackages = {"com.atlantis.zeus.index.dao"},
        sqlSessionTemplateRef = "writeSqlSessionTemplate")
@Configuration
public class DataSourceWriteConfig {
    /**
     * pxc config
     *
     * @return pxc config
     */
    @Bean(name = "pxcDataSourceConfig")
    @ConfigurationProperties(prefix = "zeus.datasource.db")
    @Primary
    public PxcDataSourceConfig pxcDataSourceConfig() {
        return new PxcDataSourceConfig();
    }

    /**
     * 创建写数据源
     *
     * @return datasource
     */
    @Bean(name = "writeDataSource")
    @Primary
    public DataSource merchantDataSource(@Qualifier("pxcDataSourceConfig") PxcDataSourceConfig pxcDataSourceConfig) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(pxcDataSourceConfig.getDbName());
        dataSource.setUrl(pxcDataSourceConfig.getNamespace());
        dataSource.setUsername(pxcDataSourceConfig.getUsername());
        dataSource.setPassword(pxcDataSourceConfig.getPassword());
        dataSource.setMaxActive(pxcDataSourceConfig.getMaxPoolSize());
        return dataSource;
    }

    /**
     * 创建SessionFactory
     *
     * @param dataSource
     * @return sessionFactory
     * @throws Exception
     */
    @Bean(name = "writeSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("writeDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mappers/**/*.xml"));
        return bean.getObject();
    }

    /**
     * 创建事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("writeDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建SqlSessionTemplate
     *
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "writeSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("writeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
