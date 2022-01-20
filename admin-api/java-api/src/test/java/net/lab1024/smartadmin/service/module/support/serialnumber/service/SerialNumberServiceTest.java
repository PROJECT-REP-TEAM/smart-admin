package net.lab1024.smartadmin.service.module.support.serialnumber.service;

import net.lab1024.smartadmin.service.SmartAdminApplicationTest;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberIdEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

class SerialNumberServiceTest extends SmartAdminApplicationTest {

    @Autowired
    private SerialNumberService generatorService;

    /**
     * id 生成测试
     */
    @Test
    void generateTest() throws InterruptedException {

        int thread = 10;
        CountDownLatch countDownLatch = new CountDownLatch(thread);

        Runnable task = () -> {
            for (int i = 0; i < 50; i++) {
                String id = generatorService.generate(SerialNumberIdEnum.ORDER);
                System.out.println(countDownLatch.getCount() + "生成id->" + id);
            }
            countDownLatch.countDown();
        };

        for (int i = 0; i < thread; i++) {
            new Thread(task).start();
        }

        countDownLatch.await();
    }

    @Test
    void generateManyTest() throws InterruptedException {

        int thread = 10;
        CountDownLatch countDownLatch = new CountDownLatch(thread);

        Runnable task = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(countDownLatch.getCount() + "生成id->" + generatorService.generate(SerialNumberIdEnum.ORDER, 5));
            }
            countDownLatch.countDown();
        };

        for (int i = 0; i < thread; i++) {
            new Thread(task).start();
        }

        countDownLatch.await();
    }
}