package net.lab1024.smartadmin.module.system.department.manager;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.constant.CacheModuleConst;
import net.lab1024.smartadmin.module.system.department.dao.DepartmentDao;
import net.lab1024.smartadmin.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.module.system.department.service.DepartmentTreeService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @CacheEvict(value = CacheModuleConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE, allEntries = true)
    public void clearSelfAndChildrenIdCache() {
        log.info("clear DEPARTMENT_SELF_CHILDREN_CACHE");
    }

    /**
     * 清除树结构缓存
     */
    @CacheEvict(value = CacheModuleConst.Department.DEPARTMENT_TREE_CACHE, allEntries = true)
    public void clearTreeCache() {
        log.info("clear DEPARTMENT_TREE_CACHE");
    }

    /**
     * 清除所有缓存
     */
    @CacheEvict(value = CacheModuleConst.Department.DEPARTMENT_CACHE, allEntries = true)
    public void clearDepartmentCache() {
        log.info("clear DEPARTMENT_CACHE");
    }


    /**
     * 清除所有缓存
     */
    @CacheEvict(value = CacheModuleConst.Department.DEPARTMENT_ROUTE_CACHE, allEntries = true)
    public void clearDepartmentRouteCache() {
        log.info("clear DEPARTMENT_ROUTE_CACHE");
    }

    /**
     * 缓存部门结构
     *
     * @return
     */
    @Cacheable(CacheModuleConst.Department.DEPARTMENT_CACHE)
    public List<DepartmentVO> departmentCache() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        return departmentVOList;
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
    @Cacheable(CacheModuleConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE)
    public List<Long> departmentSelfChildrenCache(Long departmentId) {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        List<Long> idList = departmentTreeService.selfAndChildrenIdList(departmentId, departmentVOList);
        return idList;
    }


    /**
     * 部门的路径名称
     *
     * @return
     */
    @Cacheable(CacheModuleConst.Department.DEPARTMENT_ROUTE_CACHE)
    public Map<Long, String> departmentRouteCache() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        Map<Long, DepartmentVO> departmentMap = departmentVOList.stream().collect(Collectors.toMap(DepartmentVO::getDepartmentId, Function.identity()));

        Map<Long, String> routeNameMap = Maps.newHashMap();
        for (DepartmentVO departmentVO : departmentVOList) {
            String routeName = this.buildRoutePath(departmentVO, departmentMap);
            routeNameMap.put(departmentVO.getDepartmentId(), routeName);
        }

        return routeNameMap;
    }

    /**
     * 构建父级考点路径
     *
     * @param departmentVO
     * @param departmentMap
     */
    private String buildRoutePath(DepartmentVO departmentVO, Map<Long, DepartmentVO> departmentMap) {
        if (Objects.equals(departmentVO.getParentId(), NumberUtils.LONG_ZERO)) {
            return departmentVO.getName();
        }
        //父节点
        DepartmentVO parentDepartment = departmentMap.get(departmentVO.getParentId());
        if (parentDepartment == null) {
            return departmentVO.getName();
        }
        String parentRouteName = buildRoutePath(parentDepartment, departmentMap);
        return parentRouteName + "/" + departmentVO.getName();

    }
}
