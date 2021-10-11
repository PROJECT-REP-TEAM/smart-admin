package net.lab1024.smartadmin.service.module.business.category;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.business.category.domain.CategoryEntity;
import org.apache.commons.collections4.CollectionUtils;
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
    private CategoryCacheManager categoryCacheManager;


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
        CategoryEntity entity = categoryCacheManager.queryCategory(categoryId);
        if (null == entity || entity.getDeletedFlag()) {
            return Optional.empty();
        }
        return Optional.of(entity);
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
            CategoryEntity categoryEntity = categoryCacheManager.queryCategory(e);
            return categoryEntity;
        }));
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
        //所有子类
        List<CategoryEntity> categoryEntityList = Lists.newArrayList();
        categoryIdList.forEach(e -> {
            categoryEntityList.addAll(categoryCacheManager.querySubCategory(e));
        });
        Map<Long, List<CategoryEntity>> subTypeMap = categoryEntityList.stream().collect(Collectors.groupingBy(CategoryEntity::getCategoryId));
        // 递归查询子类
        categoryIdList = subTypeMap.values().stream().flatMap(Collection::stream).map(CategoryEntity::getCategoryId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Lists.newArrayList();
        }
        categoryIdList.addAll(this.queryCategorySubId(categoryIdList));
        return categoryIdList;
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
        CategoryEntity categoryEntity = categoryCacheManager.queryCategory(categoryId);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return null;
        }
        return categoryEntity.getCategoryName();
    }

}
