package net.lab1024.smartadmin.service.module.system.employee;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.constant.CacheModuleBaseConst;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKey;
import net.lab1024.smartadmin.service.module.support.beancache.load.CacheLoad;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.RoleEmployeeDao;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Slf4j
@Service
public class EmployeeCacheService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    /**
     * 缓存某个部门下的员工
     * cacheKey = CacheKeyConst.Employee
     * businessId = departmentId
     *
     * @param cacheKey
     * @return
     */
    @CacheLoad(CacheModuleBaseConst.Employee.DEPARTMENT_EMPLOYEE_CACHE)
    public List<EmployeeEntity> departmentEmployeeCache(String cacheKey) {
        String businessId = CacheKey.getBusinessIdByCacheKey(cacheKey);
        Long departmentId = Long.valueOf(businessId);
        List<EmployeeEntity> employeeEntityList = employeeDao.selectByDepartmentId(departmentId, false, false);
        return employeeEntityList;
    }

    /**
     * 单个员工的缓存
     *
     * @param cacheKey
     * @return
     */
    @CacheLoad(CacheModuleBaseConst.Employee.SINGLE_EMPLOYEE_CACHE)
    public EmployeeEntity singleEmployeeCache(String cacheKey) {
        String businessId = CacheKey.getBusinessIdByCacheKey(cacheKey);
        Long employeeId = Long.valueOf(businessId);
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return null;
        }
        if (employeeEntity.getDeletedFlag()) {
            return null;
        }
        return employeeEntity;
    }

    /**
     * 单个员工的缓存
     *
     * @param cacheKey
     * @return
     */
    @CacheLoad(CacheModuleBaseConst.Employee.SINGLE_EMPLOYEE_ROLE_CACHE)
    public List<Long> singleEmployeeRoleCache(String cacheKey) {
        String businessId = CacheKey.getBusinessIdByCacheKey(cacheKey);
        Long employeeId = Long.valueOf(businessId);
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return roleIdList;
    }
}
