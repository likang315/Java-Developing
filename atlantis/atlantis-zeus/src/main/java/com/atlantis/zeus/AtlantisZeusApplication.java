package com.atlantis.zeus;

import com.atlantis.zeus.base.custom.SpringBootBannerCustom;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * springboot 启动类 且加载.properties文件
 * SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 19:15
 */
@SpringBootApplication
@ImportResource(value = {"classpath:spring/application-context.xml"})
public class AtlantisZeusApplication {

	/**
	 * 导入.properties文件
	 *
	 * @return
	 * @throws IOException
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
		val result = new PropertySourcesPlaceholderConfigurer();
		result.setLocations(new PathMatchingResourcePatternResolver()
				.getResources("classpath*:**/*.properties"));
		result.setIgnoreUnresolvablePlaceholders(true);
		return result;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AtlantisZeusApplication.class);
		app.setBanner(new SpringBootBannerCustom());
		app.run(args);
	}

}
