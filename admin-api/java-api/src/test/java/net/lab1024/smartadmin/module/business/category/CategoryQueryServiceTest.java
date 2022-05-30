package net.lab1024.smartadmin.module.business.category;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.SmartAdminApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

class CategoryQueryServiceTest extends SmartAdminApplicationTest {

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Test
    void queryFullName() {

        Map<Long, String> fullName = categoryQueryService.queryFullName(Lists.newArrayList(344L, 345L));
        System.out.println(fullName);
        fullName = categoryQueryService.queryFullName(Lists.newArrayList(344L, 345L));
        System.out.println(fullName);

    }
}