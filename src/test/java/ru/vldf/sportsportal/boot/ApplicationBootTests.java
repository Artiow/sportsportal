package ru.vldf.sportsportal.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBoot.class)
@TestPropertySource("classpath:application-test.properties")
public class ApplicationBootTests {

    @Test
    public void contextLoads() {

    }
}
