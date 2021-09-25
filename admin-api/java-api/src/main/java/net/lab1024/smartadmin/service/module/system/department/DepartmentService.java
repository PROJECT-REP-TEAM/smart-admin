package net.lab1024.smartadmin.service.module.system.department;

import net.lab1024.smartadmin.service.common.codeconst.ResponseCodeConst;
import net.lab1024.smartadmin.service.common.constant.CacheModuleConst;
import net.lab1024.smartadmin.service.common.constant.CommonConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.beancache.cache.IBeanCache;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKey;
import net.lab1024.smartadmin.service.module.system.department.domain.dto.DepartmentCreateDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.dto.DepartmentUpdateDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentEmployeeTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理业务类
 *
 * @author listen
 * @date 2017/12/19 14:25
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;

    @Autowired
    protected IBeanCache beanCache;

    /**
     * 获取部门树形结构
     *
     * @return
     */
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Department.DEPARTMENT_TREE_CACHE);
        List<DepartmentTreeVO> treeVOList = beanCache.get(cacheKey);
        return ResponseDTO.succData(treeVOList);
    }

    /**
     * 自身以及所有下级的部门id列表
     *
     * @param departmentId
     * @return
     */
    public List<Long> selfAndChildrenIdList(Long departmentId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Department.DEPARTMENT_TREE_ID_CACHE, departmentId.toString());
        return beanCache.get(cacheKey);
    }

    /**
     * 部门员工树
     *
     * @return
     */
    public ResponseDTO<List<DepartmentEmployeeTreeVO>> departmentEmployeeTree() {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Department.DEPARTMENT_TREE_CACHE);
        List<DepartmentTreeVO> treeVOList = beanCache.get(cacheKey);
        if (CollectionUtils.isEmpty(treeVOList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        // 获取全部员工列表
        List<EmployeeDTO> employeeList = employeeDao.listAll();
        if (CollectionUtils.isEmpty(employeeList)) {
            return ResponseDTO.succData(SmartBeanUtil.copyList(treeVOList, DepartmentEmployeeTreeVO.class));
        }
        Map<Long, List<EmployeeDTO>> employeeMap = employeeList.stream().collect(Collectors.groupingBy(EmployeeDTO::getDepartmentId));
        //构建各部门的员工信息
        List<DepartmentEmployeeTreeVO> departmentEmployeeTreeVOList = this.buildTreeEmployee(treeVOList, employeeMap);

        return ResponseDTO.succData(departmentEmployeeTreeVOList);
    }

    /**
     * 递归构建每部门的员工信息
     *
     * @param treeVOList
     * @param employeeMap
     * @return
     */
    private List<DepartmentEmployeeTreeVO> buildTreeEmployee(List<DepartmentTreeVO> treeVOList, Map<Long, List<EmployeeDTO>> employeeMap) {
        List<DepartmentEmployeeTreeVO> departmentEmployeeTreeVOList = Lists.newArrayList();
        for (DepartmentTreeVO departmentTreeVO : treeVOList) {
            DepartmentEmployeeTreeVO departmentEmployeeTreeVO = SmartBeanUtil.copy(departmentTreeVO, DepartmentEmployeeTreeVO.class);
            departmentEmployeeTreeVO.setEmployees(employeeMap.getOrDefault(departmentEmployeeTreeVO.getId(), Lists.newArrayList()));
            List<DepartmentTreeVO> children = departmentTreeVO.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            List<DepartmentEmployeeTreeVO> childrenList = this.buildTreeEmployee(children, employeeMap);
            departmentEmployeeTreeVO.setChildren(childrenList);
            departmentEmployeeTreeVOList.add(departmentEmployeeTreeVO);
        }
        return departmentEmployeeTreeVOList;
    }

    /**
     * 获取所有部门和员工信息
     *
     * @param departmentName
     * @return
     */
    public ResponseDTO<List<DepartmentTreeVO>> departmentTreeByName(String departmentName) {
        // 获取全部部门列表
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        if (StringUtils.isNotBlank(departmentName)) {
            // 检索条件不为空的时候 过滤部门列表
            departmentVOList = this.filterDepartment(departmentVOList, departmentName);
        }
        List<DepartmentTreeVO> result = departmentTreeService.buildTree(departmentVOList);
        return ResponseDTO.succData(result);
    }

    /**
     * 过滤部门名称，获取过滤后的结果
     *
     * @author 开云
     * @date 2019/4/28 20:17
     */
    private List<DepartmentVO> filterDepartment(List<DepartmentVO> departmentVOList, String departmentName) {
        Map<Long, DepartmentVO> departmentMap = new HashMap<>(departmentVOList.size());
        departmentVOList.forEach(item -> {
            if (!item.getName().contains(departmentName)) {
                return;
            }
            // 当前部门包含关键字
            departmentMap.put(item.getId(), item);
            Long parentId = item.getParentId();
            if (null != parentId) {
                List<DepartmentVO> filterResult = new ArrayList<>();
                getParentDepartment(departmentVOList, parentId, filterResult);
                for (DepartmentVO dto : filterResult) {
                    if (!departmentMap.containsKey(dto.getId())) {
                        departmentMap.put(dto.getId(), dto);
                    }
                }
            }
        });
        return new ArrayList<>(departmentMap.values());
    }

    /**
     * 递归获取部门的所有上级元素
     *
     * @param departmentVOList
     * @param parentId
     * @param result
     * @return
     */
    private List<DepartmentVO> getParentDepartment(List<DepartmentVO> departmentVOList, Long parentId, List<DepartmentVO> result) {
        List<DepartmentVO> deptList = departmentVOList.stream().filter(e -> e.getId().equals(parentId)).collect(Collectors.toList());
        for (DepartmentVO item : deptList) {
            result.add(item);
            if (item.getParentId() != CommonConst.DEFAULT_PARENT_ID && item.getParentId() != null) {
                result.addAll(getParentDepartment(departmentVOList, item.getParentId(), result));
            }
        }
        return result;
    }

    /**
     * 新增添加部门
     *
     * @param departmentCreateDTO
     * @return AjaxResult
     */

    public ResponseDTO<String> addDepartment(DepartmentCreateDTO departmentCreateDTO) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentCreateDTO, DepartmentEntity.class);
        departmentEntity.setSort(0L);
        DepartmentService departmentService = (DepartmentService) AopContext.currentProxy();
        departmentService.addDepartmentHandle(departmentEntity);
        this.clearTreeCache();
        this.clearSelfAndChildrenIdCache();
        return ResponseDTO.succ();
    }

    /**
     * 新增部门数据处理
     *
     * @param departmentEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDepartmentHandle(DepartmentEntity departmentEntity) {
        departmentDao.insert(departmentEntity);
        DepartmentEntity updateSortEntity = new DepartmentEntity();
        updateSortEntity.setId(departmentEntity.getId());
        updateSortEntity.setSort(departmentEntity.getId());
        departmentDao.updateById(updateSortEntity);
    }

    /**
     * 更新部门信息
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateDepartment(DepartmentUpdateDTO updateDTO) {
        if (updateDTO.getParentId() == null) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "父级部门id不能为空");
        }
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getId());
        if (entity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(updateDTO, DepartmentEntity.class);
        departmentEntity.setSort(entity.getSort());
        departmentDao.updateById(departmentEntity);
        this.clearTreeCache();
        return ResponseDTO.succ();
    }

    /**
     * 根据id删除部门
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     *
     * @param deptId
     * @return
     */
    public ResponseDTO<String> delDepartment(Long deptId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(deptId);
        if (null == departmentEntity) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        // 是否有子级部门
        int subDepartmentNum = departmentDao.countSubDepartment(deptId);
        if (subDepartmentNum > 0) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "请先删除子级部门");
        }

        // 是否有未删除员工
        int employeeNum = employeeDao.countByDepartmentId(deptId, false);
        if (employeeNum > 0) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "请先删除部门员工");
        }
        departmentDao.deleteById(deptId);
        // 清除缓存
        this.clearTreeCache();
        this.clearSelfAndChildrenIdCache();
        return ResponseDTO.succ();
    }

    /**
     * 根据id获取部门信息
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<DepartmentVO> getDepartmentById(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        DepartmentVO departmentVO = SmartBeanUtil.copy(departmentEntity, DepartmentVO.class);
        return ResponseDTO.succData(departmentVO);
    }

    /**
     * 获取所有部门
     *
     * @return
     */
    public ResponseDTO<List<DepartmentVO>> listAll() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        return ResponseDTO.succData(departmentVOList);
    }

    /**
     * 上下移动
     *
     * @param departmentId
     * @param swapId
     * @return
     */
    public ResponseDTO<String> upOrDown(Long departmentId, Long swapId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity swapEntity = departmentDao.selectById(swapId);
        if (swapEntity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity updateEntity = new DepartmentEntity();
        updateEntity.setId(departmentId);
        updateEntity.setSort(swapEntity.getSort());

        DepartmentEntity swapUpdateEntity = new DepartmentEntity();
        swapUpdateEntity.setId(swapEntity.getId());
        swapUpdateEntity.setSort(departmentEntity.getSort());
        DepartmentService departmentService = (DepartmentService) AopContext.currentProxy();
        departmentService.upOrDownUpdate(updateEntity, swapEntity);
        //清除缓存
        this.clearTreeCache();
        return ResponseDTO.succ();
    }

    /**
     * 上下移动数据库处理
     *
     * @param departmentEntity
     * @param swapEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public void upOrDownUpdate(DepartmentEntity departmentEntity, DepartmentEntity swapEntity) {
        departmentDao.updateById(departmentEntity);
        departmentDao.updateById(swapEntity);
    }

    /**
     * 部门升级
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<String> upgrade(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        if (departmentEntity.getParentId() == null || departmentEntity.getParentId().equals(CommonConst.DEFAULT_PARENT_ID)) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "此部门已经是根节点无法移动");
        }
        DepartmentEntity parentEntity = departmentDao.selectById(departmentEntity.getParentId());

        DepartmentEntity updateEntity = new DepartmentEntity();
        updateEntity.setId(departmentId);
        updateEntity.setParentId(parentEntity.getParentId());
        departmentDao.updateById(updateEntity);
        //清除缓存
        this.clearTreeCache();
        return ResponseDTO.succ();
    }

    /**
     * 部门降级
     *
     * @param departmentId
     * @param preId
     * @return
     */
    public ResponseDTO<String> downgrade(Long departmentId, Long preId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity preEntity = departmentDao.selectById(preId);
        if (preEntity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity updateEntity = new DepartmentEntity();
        updateEntity.setId(departmentId);
        updateEntity.setParentId(preEntity.getId());
        departmentDao.updateById(updateEntity);
        //清除缓存
        this.clearTreeCache();
        return ResponseDTO.succ();
    }


    /**
     * 清除自身以及下级的id列表缓存
     */
    private void clearSelfAndChildrenIdCache() {
        beanCache.removeByModule(CacheModuleConst.Department.DEPARTMENT_TREE_ID_CACHE);
    }

    /**
     * 清除树结构缓存
     */
    private void clearTreeCache() {
        String treeCacheKey = CacheKey.cacheKey(CacheModuleConst.Department.DEPARTMENT_TREE_CACHE);
        beanCache.remove(treeCacheKey);
    }

    /**
     * 获取最顶级部门id
     *
     * @return
     */
    public Long queryTopDeptId() {
        ResponseDTO<List<DepartmentTreeVO>> departmentTree = departmentTree();
        List<DepartmentTreeVO> data = departmentTree.getData();
        return data.get(0).getId();
    }

    /**
     * 获取校区列表 即第二级部门列表
     *
     * @return
     */
    public ResponseDTO<List<DepartmentVO>> querySchoolList() {
        ResponseDTO<List<DepartmentTreeVO>> res = departmentTree();
        if (!res.isSuccess()) {
            return ResponseDTO.wrap(res);
        }
        List<DepartmentTreeVO> data = res.getData();
        // 拿到第二级部门列表
        List<DepartmentVO> resList = Lists.newArrayList();
        for (DepartmentTreeVO tree : data) {
            List<DepartmentTreeVO> children = tree.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                resList.addAll(SmartBeanUtil.copyList(children, DepartmentVO.class));
            }
        }
        return ResponseDTO.succData(resList);
    }

    /**
     * 根据部门ID获取父级名称
     *
     * @param departmentId
     * @return
     */
    public String getParentNameTreeByDepartmentId(Long departmentId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Department.DEPARTMENT_CACHE);
        List<DepartmentVO> departmentList = beanCache.get(cacheKey);
        //递归寻找上级直到校区(第二级)
        List<String> departmentNameList = Lists.newArrayList();
        this.recursionFindParentDepartmentName(departmentNameList, departmentList, departmentId);
        return StringUtils.join(departmentNameList, "/");
    }

    /**
     * 递归查询父级部门名称 到校区（第二级）
     *
     * @param departmentNameList
     * @param departmentList
     * @param departmentId
     */
    private void recursionFindParentDepartmentName(List<String> departmentNameList, List<DepartmentVO> departmentList, Long departmentId) {
        Optional<DepartmentVO> findRes = departmentList.stream().filter(e -> e.getId().equals(departmentId)).findFirst();
        if (!findRes.isPresent() || findRes.get().getParentId() == CommonConst.DEFAULT_PARENT_ID) {
            return;
        }
        DepartmentVO departmentVO = findRes.get();
        departmentNameList.add(0, departmentVO.getName());
        this.recursionFindParentDepartmentName(departmentNameList, departmentList, departmentVO.getParentId());
    }

    /**
     * 寻找员工所属校区
     *
     * @param employeeId
     * @return
     */
    public Long getSchoolIdByEmployeeId(Long employeeId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.SINGLE_EMPLOYEE_CACHE, employeeId.toString());
        EmployeeEntity employeeEntity = beanCache.get(cacheKey);
        Long departmentId = employeeEntity.getDepartmentId();
        DepartmentVO schoolIdByDepartment = this.getSchoolIdByDepartment(departmentId);
        if (schoolIdByDepartment != null) {
            return schoolIdByDepartment.getId();
        }
        return null;
    }

    /**
     * 寻找部门所属校区
     *
     * @param departmentId
     * @return
     */
    public DepartmentVO getSchoolIdByDepartment(Long departmentId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Department.DEPARTMENT_CACHE);
        List<DepartmentVO> departmentList = beanCache.get(cacheKey);
        //递归寻找校区(第二级)
        return this.recursionFindSchoolDepartmentId(departmentList, departmentId);
    }

    /**
     * 寻找校区ID
     *
     * @param departmentList
     * @param departmentId
     * @return
     */
    private DepartmentVO recursionFindSchoolDepartmentId(List<DepartmentVO> departmentList, Long departmentId) {
        Optional<DepartmentVO> findRes = departmentList.stream().filter(e -> e.getId().equals(departmentId)).findFirst();
        // 如果查询不到 或者自己本身为最顶级 返回null
        if (!findRes.isPresent() || findRes.get().getParentId() == CommonConst.DEFAULT_PARENT_ID) {
            return null;
        }
        DepartmentVO departmentVO = findRes.get();
        // 寻找父级 判断父级的父级是不是最顶级
        Optional<DepartmentVO> parentFindRes = departmentList.stream().filter(e -> e.getId().equals(departmentVO.getParentId())).findFirst();
        // 若找不到父级则返回null
        if (!parentFindRes.isPresent()) {
            return null;
        }
        // 若父级为最顶级 则返回本级ID
        if (parentFindRes.get().getParentId() == CommonConst.DEFAULT_PARENT_ID) {
            return departmentVO;
        }
        // 若父级不为最顶级 进入递归
        return this.recursionFindSchoolDepartmentId(departmentList, departmentVO.getParentId());
    }

}
