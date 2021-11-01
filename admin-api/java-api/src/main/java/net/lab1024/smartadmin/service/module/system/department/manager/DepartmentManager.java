package net.lab1024.smartadmin.service.module.system.department.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.smartadmin.service.module.system.department.dao.DepartmentDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;

import java.util.List;

/**
 * Description
 *
 * @author lihaifan
 * @date 2021/8/31 17:19
 */
@Service
public class DepartmentManager extends ServiceImpl<DepartmentDao, DepartmentEntity> {


    /**
     * 批量添加 编辑
     *
     * @param insertDepartmentList
     * @param updateDepartmentList
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertUpdate(List<DepartmentEntity> insertDepartmentList, List<DepartmentEntity> updateDepartmentList) {
        if(!CollectionUtils.isEmpty(insertDepartmentList)){
            saveBatch(insertDepartmentList);
        }
        if(!CollectionUtils.isEmpty(updateDepartmentList)){
            updateBatchById(updateDepartmentList);
        }
    }
}
