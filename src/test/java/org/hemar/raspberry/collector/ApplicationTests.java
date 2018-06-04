package org.hemar.raspberry.collector;

import org.hemar.raspberry.collector.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfig.class })
public class ApplicationTests {

	@Test
	public void context_starts() {
	}

}
