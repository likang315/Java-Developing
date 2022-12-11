package com.atlantis.atlantiszeus;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 单元测试
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-22 16:11
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlantisZeusApplicationTests.class)
public class AtlantisZeusApplicationTests {

	/**
	 * 单元测试
	 */
	@Test
	void contextLoads() throws InterruptedException {
		Thread.sleep(10 * 1000);
		log.warn("AtlantisZeusApplicationTests_contextLoads: test !!!");
	}

}
