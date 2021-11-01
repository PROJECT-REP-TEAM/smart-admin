package net.lab1024.smartadmin.service.module.system.department.service;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhuoda
 */
@Service
public class DepartmentTreeService {

    /**
     * 构建部门树结构
     *
     * @param voList
     * @return
     */
    public List<DepartmentTreeVO> buildTree(List<DepartmentVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return Lists.newArrayList();
        }
        List<DepartmentVO> rootList = voList.stream().filter(e -> e.getParentId() == null || Objects.equals(e.getParentId(), DepartmentService.DEFAULT_PARENT_ID)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rootList)) {
            return Lists.newArrayList();
        }
        List<DepartmentTreeVO> treeVOList = SmartBeanUtil.copyList(rootList, DepartmentTreeVO.class);
        this.buildTree(treeVOList, voList);
        return treeVOList;
    }

    /**
     * 构建所有根节点的下级树形结构
     *
     * @param nodeList
     * @param voList
     */
    private void buildTree(List<DepartmentTreeVO> nodeList, List<DepartmentVO> voList) {
        int nodeSize = nodeList.size();
        for (int i = 0; i < nodeSize; i++) {
            int preIndex = i - 1;
            int nextIndex = i + 1;
            DepartmentTreeVO node = nodeList.get(i);
            if (preIndex > -1) {
                node.setPreId(nodeList.get(preIndex).getId());
            }
            if (nextIndex < nodeSize) {
                node.setNextId(nodeList.get(nextIndex).getId());
            }
            buildTree(node, voList);
        }
    }

    /**
     * 构建子元素的下级
     *
     * @param node
     * @param voList
     */
    private void buildTree(DepartmentTreeVO node, List<DepartmentVO> voList) {
        List<DepartmentTreeVO> children = getChildren(node.getId(), voList);
        if (CollectionUtils.isNotEmpty(children)) {
            node.setChildren(children);
            this.buildTree(children, voList);
        }
    }

    /**
     * 获取子元素
     *
     * @param departmentId
     * @param voList
     * @return
     */
    private List<DepartmentTreeVO> getChildren(Long departmentId, List<DepartmentVO> voList) {
        List<DepartmentVO> childrenEntityList = voList.stream().filter(e -> departmentId.equals(e.getParentId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childrenEntityList)) {
            return Lists.newArrayList();
        }
        return SmartBeanUtil.copyList(childrenEntityList, DepartmentTreeVO.class);
    }


    /**
     * 通过部门id,获取当前以及下属部门
     *
     * @param departmentId
     * @param voList
     */
    public List<Long> selfAndChildrenIdList(Long departmentId, List<DepartmentVO> voList) {
        List<Long> selfAndChildrenIdList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(voList)) {
            return selfAndChildrenIdList;
        }
        selfAndChildrenIdList.add(departmentId);
        List<DepartmentTreeVO> children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return selfAndChildrenIdList;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getId).collect(Collectors.toList());
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
        return selfAndChildrenIdList;
    }

    /**
     * 递归查询
     *
     * @param selfAndChildrenIdList
     * @param departmentId
     * @param voList
     */
    public void selfAndChildrenRecursion(List<Long> selfAndChildrenIdList, Long departmentId, List<DepartmentVO> voList) {
        List<DepartmentTreeVO> children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getId).collect(Collectors.toList());
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
    }


}
