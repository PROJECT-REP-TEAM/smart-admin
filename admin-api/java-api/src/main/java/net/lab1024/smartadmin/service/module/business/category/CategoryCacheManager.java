package net.lab1024.smartadmin.service.module.business.category;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.constant.CacheModuleConst;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.business.category.domain.CategoryEntity;
import net.lab1024.smartadmin.service.module.business.category.domain.vo.CategoryTreeVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类目 查询 业务类
 *
 * @author 胡克
 * @date 2021/1/20 16:26
 */
@Service
@Slf4j
public class CategoryCacheManager {

    @Autowired
    private CategoryDao categoryDao;


    /**
     * 根据类目id 移除缓存
     */
    @CacheEvict(value = {CacheModuleConst.Category.CATEGORY, CacheModuleConst.Category.CATEGORY_SUB, CacheModuleConst.Category.CATEGORY_TREE}, allEntries = true)
    public void removeCache() {
        log.info("clear CATEGORY ,CATEGORY_SUB ,CATEGORY_TREE");
    }

    /**
     * 查詢类目
     *
     * @param categoryId
     * @return
     */
    @Cacheable(CacheModuleConst.Category.CATEGORY)
    public CategoryEntity queryCategory(Long categoryId) {
        return categoryDao.selectById(categoryId);
    }

    /**
     * 查询类目 子级
     *
     * @param categoryId
     * @return
     */
    @Cacheable(CacheModuleConst.Category.CATEGORY_SUB)
    public List<CategoryEntity> querySubCategory(Long categoryId) {
        return categoryDao.queryByParentId(Lists.newArrayList(categoryId), false);
    }


    /**
     * 查询类目 层级树
     * 优先查询缓存
     *
     * @return
     */
    @Cacheable(CacheModuleConst.Category.CATEGORY_TREE)
    public List<CategoryTreeVO> queryCategoryTree(Long parentId, Integer categoryType) {
        List<CategoryEntity> allCategoryEntityList = categoryDao.queryByParentIdAndType(Lists.newArrayList(NumberUtils.LONG_ZERO), categoryType, false);

        List<CategoryEntity> categoryEntityList = allCategoryEntityList.stream().filter(e -> e.getParentId().equals(parentId)).collect(Collectors.toList());
        List<CategoryTreeVO> treeList = SmartBeanUtil.copyList(categoryEntityList, CategoryTreeVO.class);
        treeList.forEach(e -> {
            e.setLabel(e.getCategoryName());
            e.setValue(e.getCategoryId());
            e.setCategoryFullName(e.getCategoryName());
        });
        // 递归设置子类
        this.queryAndSetSubCategory(treeList, allCategoryEntityList);
        return treeList;
    }

    /**
     * 递归查询设置类目子类
     * 从缓存查询子类
     *
     * @param treeList
     */
    private void queryAndSetSubCategory(List<CategoryTreeVO> treeList, List<CategoryEntity> allCategoryEntityList) {
        if (CollectionUtils.isEmpty(treeList)) {
            return;
        }
        List<Long> parentIdList = treeList.stream().map(CategoryTreeVO::getValue).collect(Collectors.toList());
        List<CategoryEntity> categoryEntityList = allCategoryEntityList.stream().filter(e -> parentIdList.contains(e.getParentId())).collect(Collectors.toList());
        Map<Long, List<CategoryEntity>> categorySubMap = categoryEntityList.stream().collect(Collectors.groupingBy(CategoryEntity::getCategoryId));
        treeList.forEach(e -> {
            List<CategoryEntity> childrenEntityList = categorySubMap.getOrDefault(e.getValue(), Lists.newArrayList());
            List<CategoryTreeVO> childrenVOList = SmartBeanUtil.copyList(childrenEntityList, CategoryTreeVO.class);
            childrenVOList.forEach(item -> {
                item.setLabel(item.getCategoryName());
                item.setValue(item.getCategoryId());
                item.setCategoryFullName(e.getCategoryFullName() + StringConst.SEPARATOR_SLASH + item.getCategoryName());
            });
            // 递归查询
            this.queryAndSetSubCategory(childrenVOList, allCategoryEntityList);
            e.setChildren(childrenVOList);
        });
    }


}
