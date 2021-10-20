package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import net.lab1024.smartadmin.service.SmartAdminApplicationTest;
import net.lab1024.smartadmin.service.module.support.idgenerator.IdGeneratorService;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

class IdGeneratorServiceTest extends SmartAdminApplicationTest {

    @Autowired
    private IdGeneratorService generatorService;

    /**
     * id 生成测试
     */
    @Test
    void generateTest() throws InterruptedException {

        int thread = 10;
        CountDownLatch countDownLatch = new CountDownLatch(thread);

        Runnable task = () -> {
            String id = generatorService.generate(IdGeneratorEnum.CONTRACT);
            System.out.println(countDownLatch.getCount() + "生成id->" + id);
            countDownLatch.countDown();
        };

        for (int i = 0; i < thread; i++) {
            new Thread(task).start();
        }

        countDownLatch.await();
    }
}