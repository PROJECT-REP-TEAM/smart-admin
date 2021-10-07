package net.lab1024.smartadmin.service.module.business.category;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.constant.CacheModuleConst;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.module.business.category.constant.CategoryConst;
import net.lab1024.smartadmin.service.module.business.category.domain.CategoryEntity;
import net.lab1024.smartadmin.service.module.business.category.domain.CategorySimpleDTO;
import net.lab1024.smartadmin.service.module.business.category.domain.CategoryTreeQueryDTO;
import net.lab1024.smartadmin.service.module.business.category.domain.CategoryTreeVO;
import net.lab1024.smartadmin.service.module.support.beancache.cache.IBeanCache;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKey;
import net.lab1024.smartadmin.service.module.support.beancache.anno.CacheLoad;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartStringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类目 查询 业务类
 *
 * @author 胡克
 * @date 2021/1/20 16:26
 */
@Service
@Slf4j
public class CategoryQueryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private IBeanCache cache;

    /**
     * 查詢类目
     *
     * @param cacheKey
     * @return
     */
    @CacheLoad(CacheModuleConst.Category.CATEGORY)
    public CategoryEntity queryCategory(String cacheKey) {
        String businessId = CacheKey.getBusinessIdByCacheKey(cacheKey);
        return categoryDao.selectById(businessId);
    }

    /**
     * 查询类目 子级
     *
     * @param cacheKey
     * @return
     */
    @CacheLoad(CacheModuleConst.Category.CATEGORY_SUB)
    public List<CategoryEntity> querySubCategory(String cacheKey) {
        /**
         * 下划线 分隔 key
         * 左边 categoryId 右边 type
         */
        String businessId = CacheKey.getBusinessIdByCacheKey(cacheKey);
        String[] split = businessId.split(StringConst.UNDERLINE);
        Integer categoryType = split.length > 1 ? Integer.valueOf(split[1]) : null;
        return categoryDao.queryByParentId(Lists.newArrayList(Long.valueOf(split[0])), categoryType, false);
    }

    /**
     * 以 类目id+下划线+类型 作为缓存key
     *
     * @param categoryId
     * @param categoryType
     * @return
     */
    private static String getCacheId(Long categoryId, Integer categoryType) {
        return categoryId + StringConst.UNDERLINE + categoryType;
    }

    /**
     * 批量查询类目 子级
     *
     * @param categoryIdList
     * @return
     */
    public Map<Long, List<CategoryEntity>> querySubCategoryFromCache(List<Long> categoryIdList) {
        return categoryIdList.stream().collect(Collectors.toMap(Function.identity(), e -> {
            String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY_SUB, e.toString());
            return cache.get(cacheKey);
        }));
    }

    /**
     * 根据 id 查询未删除的类目
     *
     * @param categoryId
     * @return 可能 null
     */
    public Optional<CategoryEntity> queryCategory(Long categoryId) {
        if (null == categoryId) {
            return Optional.empty();
        }
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY, categoryId.toString());
        CategoryEntity entity = cache.get(cacheKey);
        if (null == entity || entity.getDeletedFlag()) {
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    /**
     * 根据 类目id 查询未删除的子类
     *
     * @param categoryId
     * @return 没有返回空集合
     */
    public List<CategoryEntity> queryCategoryByParent(Long categoryId, Integer categoryType) {
        if (null == categoryId) {
            return Collections.emptyList();
        }
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY_SUB, getCacheId(categoryId, categoryType));
        return cache.get(cacheKey);
    }

    /**
     * 根据 类目id集合 查询未删除的类目集合
     *
     * @param categoryIdList
     * @return
     */
    public Map<Long, CategoryEntity> queryCategoryList(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.emptyMap();
        }
        categoryIdList = categoryIdList.stream().distinct().collect(Collectors.toList());

        return categoryIdList.stream().collect(Collectors.toMap(Function.identity(), e -> {
            String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY, e.toString());
            return cache.get(cacheKey);
        }));
    }

    /**
     * 根据类目id 移除缓存
     */
    public void removeCache() {
        cache.removeByModule(CacheModuleConst.Category.CATEGORY);
        cache.removeByModule(CacheModuleConst.Category.CATEGORY_SUB);
        // 移除整个类目树缓存
        cache.removeByModule(CacheModuleConst.Category.CATEGORY_TREE);
    }

    /**
     * 根据类目id 递归查询该id的所有子类id 递归查询
     * 同时存入缓存
     * 注意：查询出来的集合 不包含传递的父类参数
     *
     * @param categoryIdList
     */
    public List<Long> queryCategorySubId(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.emptyList();
        }
        // 查询所有子类
        Map<Long, List<CategoryEntity>> subTypeMap = this.querySubCategoryFromCache(categoryIdList);
        if (MapUtils.isEmpty(subTypeMap)) {
            return Lists.newArrayList();
        }
        // 递归查询子类
        categoryIdList = subTypeMap.values().stream().flatMap(Collection::stream).map(CategoryEntity::getCategoryId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Lists.newArrayList();
        }
        categoryIdList.addAll(this.queryCategorySubId(categoryIdList));
        return categoryIdList;
    }

    /**
     * 查询自身以及所有子节点
     * @param categoryIdList
     * @return
     */
    public List<Long> queryCategorySelfAndSubId(List<Long> categoryIdList) {
        List<Long> subIdList = this.queryCategorySubId(categoryIdList);
        subIdList.addAll(categoryIdList);
        return subIdList;
    }

    /**
     * 查询类目 层级树
     * 优先查询缓存
     *
     * @return
     */
    public List<CategoryTreeVO> queryCategoryTree(CategoryTreeQueryDTO queryDTO) {
        // 查询缓存
        Long parentId = queryDTO.getParentId();
        Integer categoryType = queryDTO.getCategoryType();
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY_TREE, getCacheId(parentId, categoryType));
        List<CategoryTreeVO> treeList = cache.get(cacheKey);
        if (null != treeList) {
            return treeList;
        }
        // 查询一级类目
        List<CategoryEntity> categoryEntityList = this.queryCategoryByParent(parentId, categoryType);
        treeList = SmartBeanUtil.copyList(categoryEntityList, CategoryTreeVO.class);
        treeList.forEach(e -> {
            e.setLabel(e.getCategoryName());
            e.setValue(e.getCategoryId());
            e.setCategoryFullName(e.getCategoryName());
        });
        // 递归设置子类
        this.queryAndSetSubCategory(treeList);
        // 放入缓存
        cache.put(cacheKey, treeList);
        return treeList;
    }

    /**
     * 递归查询设置类目子类
     * 从缓存查询子类
     *
     * @param treeList
     */
    private void queryAndSetSubCategory(List<CategoryTreeVO> treeList) {
        if (CollectionUtils.isEmpty(treeList)) {
            return;
        }
        List<Long> parentIdList = treeList.stream().map(CategoryTreeVO::getValue).collect(Collectors.toList());
        Map<Long, List<CategoryEntity>> categorySubMap = this.querySubCategoryFromCache(parentIdList);
        treeList.forEach(e -> {
            List<CategoryEntity> childrenEntityList = categorySubMap.getOrDefault(e.getValue(), Lists.newArrayList());
            List<CategoryTreeVO> childrenVOList = SmartBeanUtil.copyList(childrenEntityList, CategoryTreeVO.class);
            childrenVOList.forEach(item -> {
                item.setLabel(item.getCategoryName());
                item.setValue(item.getCategoryId());
                item.setCategoryFullName(e.getCategoryFullName() + StringConst.SEPARATOR_SLASH + item.getCategoryName());
            });
            // 递归查询
            this.queryAndSetSubCategory(childrenVOList);
            e.setChildren(childrenVOList);
        });
    }

    /**
     * 根据类目id 查询类目详情 包含类目全称 如：医考/医师资格/临床执业
     *
     * @param categoryId
     * @return
     */
    public CategorySimpleDTO queryCategoryInfo(Long categoryId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY, categoryId.toString());
        CategoryEntity categoryEntity = cache.get(cacheKey);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return null;
        }

        // 递归查询分类和所有父级类目
        List<CategoryEntity> parentCategoryList = this.queryCategoryAndParent(categoryId);
        // 拼接父级类目名称 斜杠分隔返回
        List<String> nameList = parentCategoryList.stream().map(CategoryEntity::getCategoryName).collect(Collectors.toList());

        // 返回DTO
        CategorySimpleDTO categoryDTO = new CategorySimpleDTO();
        categoryDTO.setCategoryId(categoryId);
        categoryDTO.setCategoryName(categoryEntity.getCategoryName());
        categoryDTO.setCategoryFullName(SmartStringUtil.join(nameList, StringConst.SEPARATOR_SLASH));
        categoryDTO.setParentId(categoryEntity.getParentId());
        return categoryDTO;
    }

    /**
     * 递归查询分类和所有父级类目 ps:特别注意返回的集合中 包含自己
     *
     * @param categoryId
     * @return
     */
    public List<CategoryEntity> queryCategoryAndParent(Long categoryId) {
        List<CategoryEntity> parentCategoryList = Lists.newArrayList();
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY, categoryId.toString());
        CategoryEntity categoryEntity = cache.get(cacheKey);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return parentCategoryList;
        }

        // 父级始终放在第一位
        parentCategoryList.add(0, categoryEntity);
        Long parentId = categoryEntity.getParentId();
        if (Objects.equals(CategoryConst.DEFAULT_PARENT_ID, parentId)) {
            return parentCategoryList;
        }
        parentCategoryList.addAll(0, this.queryCategoryAndParent(parentId));
        return parentCategoryList;
    }

    /**
     * 处理类目名称
     *
     * @param categoryIdList
     */
    public List<String> queryCategoryName(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return null;
        }
        Map<Long, CategoryEntity> categoryMap = this.queryCategoryList(categoryIdList);
        List<String> categoryNameList = Lists.newArrayList();
        categoryIdList.forEach(e -> {
            CategoryEntity categoryEntity = categoryMap.get(e);
            if (categoryEntity != null) {
                categoryNameList.add(categoryMap.get(e).getCategoryName());
            }
        });
        return categoryNameList;
    }

    /**
     * 根据类目id 查询类目名称
     *
     * @param categoryId
     * @return
     */
    public String queryCategoryName(Long categoryId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Category.CATEGORY, categoryId.toString());
        CategoryEntity categoryEntity = cache.get(cacheKey);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return null;
        }
        return categoryEntity.getCategoryName();
    }

}
