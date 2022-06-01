package net.lab1024.smartadmin.module.support.serialnumber.service;

import net.lab1024.smartadmin.SmartAdminApplicationTest;
import net.lab1024.smartadmin.module.support.serialnumber.constant.SerialNumberIdEnum;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;

class SerialNumberServiceTest extends SmartAdminApplicationTest {

    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * id 生成测试
     */
    @Test
    void generateTest() throws InterruptedException {

        int thread = 10;
        int length = 50;

        CopyOnWriteArraySet<String> numberList = new CopyOnWriteArraySet<>();
        CountDownLatch countDownLatch = new CountDownLatch(thread);


        Runnable task = () -> {
            for (int i = 0; i < length; i++) {
                String id = serialNumberService.generate(SerialNumberIdEnum.ORDER);
                numberList.add(id);
                System.out.println(countDownLatch.getCount() + "生成id->" + id);
            }
            countDownLatch.countDown();
        };

        for (int i = 0; i < thread; i++) {
            new Thread(task).start();
        }

        Assert.assertEquals(thread * length, numberList.size());

        countDownLatch.await();
    }

    @Test
    void generateManyTest() throws InterruptedException {

        int thread = 10;
        int length = 50;
        int count = 5;

        CopyOnWriteArraySet<String> numberList = new CopyOnWriteArraySet<>();
        CountDownLatch countDownLatch = new CountDownLatch(thread);

        Runnable task = () -> {
            for (int i = 0; i < length; i++) {
                List<String> stringList = serialNumberService.generate(SerialNumberIdEnum.ORDER, count);
                numberList.addAll(stringList);
                System.out.println(countDownLatch.getCount() + "生成id->" + stringList);
            }
            countDownLatch.countDown();
        };

        for (int i = 0; i < thread; i++) {
            new Thread(task).start();
        }

        Assert.assertEquals(thread * length * count, numberList.size());
        countDownLatch.await();
    }
}