package net.lab1024.smartadmin.service.module.system.department;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.constant.CacheModuleConst;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-31 0:48
 */
@Slf4j
@Service
public class DepartmentCacheManager {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;


    /**
     * 清除自身以及下级的id列表缓存
     */
    @CacheEvict(CacheModuleConst.Department.DEPARTMENT_SELF_CHILDREN_ID_CACHE)
    public void clearSelfAndChildrenIdCache() {
        log.info("clear DEPARTMENT_SELF_CHILDREN_ID_CACHE");
    }

    /**
     * 清除树结构缓存
     */
    @CacheEvict(CacheModuleConst.Department.DEPARTMENT_TREE_CACHE)
    public void clearTreeCache() {
        log.info("clear DEPARTMENT_TREE_CACHE");
    }

    /**
     * 缓存部门树结构
     *
     * @return
     */
    @Cacheable(CacheModuleConst.Department.DEPARTMENT_TREE_CACHE)
    public List<DepartmentTreeVO> departmentTreeCache() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        List<DepartmentTreeVO> treeList = departmentTreeService.buildTree(departmentVOList);
        return treeList;
    }

    /**
     * 缓存某个部门的下级id列表
     *
     * @param departmentId
     * @return
     */
    @Cacheable(CacheModuleConst.Department.DEPARTMENT_SELF_CHILDREN_ID_CACHE)
    public List<Long> departmentSelfAndChildrenIdCache(Long departmentId) {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        List<Long> idList = departmentTreeService.selfAndChildrenIdList(departmentId, departmentVOList);
        return idList;
    }
}
