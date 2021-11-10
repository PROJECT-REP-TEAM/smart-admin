package net.lab1024.smartadmin.service.module.system.employee.manager;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.constant.CacheModuleConst;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeDao;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleEmployeeDao;

import java.util.List;

/**
 * @author zhuoda
 */
@Slf4j
@Service
public class EmployeeCacheManager {

    @Autowired
    private EmployeeDao employeeDao;
    
    @Autowired
    private RoleEmployeeDao roleEmployeeDao;


    /**
     * 清除businessId为员工id的缓存信息
     *
     * @param employeeId
     */
    @CacheEvict({CacheModuleConst.Employee.SINGLE_EMPLOYEE_CACHE, CacheModuleConst.Employee.SINGLE_EMPLOYEE_ROLE_CACHE})
    public void clearCacheByEmployeeId(Long employeeId) {
        log.info("clear SINGLE_EMPLOYEE_CACHE and SINGLE_EMPLOYEE_ROLE_CACHE {}", employeeId);
    }

    /**
     * 清除businessId为部门id的缓存信息
     *
     * @param departmentId
     */
    @CacheEvict({CacheModuleConst.Employee.DEPARTMENT_EMPLOYEE_CACHE})
    public void clearCacheByDepartmentId(Long departmentId) {
        log.info("clear DEPARTMENT_EMPLOYEE_CACHE {}", departmentId);
    }

    /**
     * 缓存某个部门下的员工
     * cacheKey = CacheKeyConst.Employee
     * businessId = departmentId
     *
     * @param departmentId
     * @return
     */
    @Cacheable(CacheModuleConst.Employee.DEPARTMENT_EMPLOYEE_CACHE)
    public List<EmployeeEntity> departmentEmployeeCache(Long departmentId) {
        List<EmployeeEntity> employeeEntityList = employeeDao.selectByDepartmentId(departmentId,  false);
        return employeeEntityList;
    }

    /**
     * 单个员工的缓存
     *
     * @param employeeId
     * @return
     */
    @Cacheable(CacheModuleConst.Employee.SINGLE_EMPLOYEE_CACHE)
    public EmployeeEntity singleEmployeeCache(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return null;
        }
        if (employeeEntity.getDisabledFlag()) {
            return null;
        }
        return employeeEntity;
    }

    /**
     * 单个员工的缓存
     *
     * @param employeeId
     * @return
     */
    @Cacheable(CacheModuleConst.Employee.SINGLE_EMPLOYEE_ROLE_CACHE)
    public List<Long> singleEmployeeRoleCache(Long employeeId) {
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return roleIdList;
    }
}
