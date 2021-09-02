package net.lab1024.smartadmin.service.module.system.department;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.constant.CacheModuleBaseConst;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKey;
import net.lab1024.smartadmin.service.module.support.beancache.load.CacheLoad;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-31 0:48
 */
@Slf4j
@Service
public class DepartmentCacheService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;

    /**
     * 缓存部门结构
     * cacheKey = CacheKeyConst.DEPARTMENT_TREE_CACHE
     * 无businessId
     *
     * @return
     */
    @CacheLoad(CacheModuleBaseConst.Department.DEPARTMENT_CACHE)
    public List<DepartmentVO> departmentCache() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        return departmentVOList;
    }

    /**
     * 缓存部门树结构
     * cacheKey = CacheKeyConst.DEPARTMENT_TREE_CACHE
     * 无businessId
     *
     * @return
     */
    @CacheLoad(CacheModuleBaseConst.Department.DEPARTMENT_TREE_CACHE)
    public List<DepartmentTreeVO> departmentTreeCache() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        List<DepartmentTreeVO> treeList = departmentTreeService.buildTree(departmentVOList);
        return treeList;
    }

    /**
     * 缓存某个部门的下级id列表
     * cacheKey = CacheKeyConst.DEPARTMENT_TREE_ID_CACHE
     * businessId = departmentId
     *
     * @param cacheKey
     * @return
     */
    @CacheLoad(CacheModuleBaseConst.Department.DEPARTMENT_TREE_ID_CACHE)
    public List<Long> departmentTreeCache(String cacheKey) {
        String businessId = CacheKey.getBusinessIdByCacheKey(cacheKey);
        Long departmentId = Long.valueOf(businessId);
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        List<Long> idList = departmentTreeService.selfAndChildrenIdList(departmentId, departmentVOList);
        return idList;
    }
}
