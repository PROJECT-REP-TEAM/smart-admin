package net.lab1024.smartadmin.service.module.system.role.roleemployee;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.domain.RoleEmployeeEntity;

import java.util.List;

/**
 * 角色员工 manager
 *
 * @author listen
 * @date 2021年7月29日 21:15:50
 */
@Service
public class RoleEmployeeManager extends ServiceImpl<RoleEmployeeDao, RoleEmployeeEntity> {

    /**
     * 保存 角色员工
     *
     * @param roleId
     * @param roleEmployeeList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveRoleEmployee(Long roleId, List<RoleEmployeeEntity> roleEmployeeList) {
        this.getBaseMapper().deleteByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(roleEmployeeList)) {
            this.saveBatch(roleEmployeeList);
        }
    }
}
