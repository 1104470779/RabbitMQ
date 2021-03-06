package com.ljm.api.a_rabbitmq;

import com.ljm.api.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ConsumerTest {

    @Autowired
    private Consumer consumer;

    @Test
    public void consume() throws InterruptedException, TimeoutException, IOException {
        consumer.consume();
    }
}