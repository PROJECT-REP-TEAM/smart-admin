package net.lab1024.smartadmin.module.business.category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.smartadmin.module.business.category.domain.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 类目 dao
 *
 * @author 胡克
 * @date 2021/1/20 16:29
 */
@Component
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

    /**
     * 根据父级id 类型 查询子类
     *
     * @param parentIdList
     * @param deletedFlag
     * @return
     */
    List<CategoryEntity> queryByParentId(@Param("parentIdList") List<Long> parentIdList,
                                         @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据父级id 类型 查询子类
     *
     * @param parentIdList
     * @param categoryType {@link CategoryTypeEnum}
     * @param deletedFlag
     * @return
     */
    List<CategoryEntity> queryByParentIdAndType(@Param("parentIdList") List<Long> parentIdList,
                                         @Param("categoryType") Integer categoryType,
                                         @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据类型和id查询
     * @param categoryType
     * @param categoryId
     * @return
     */
    CategoryEntity selectByTypeAndId(@Param("categoryType") Integer categoryType, @Param("categoryId") Long categoryId);

    /**
     * 查看类目 具体条件 看sql
     *
     * @param entity
     * @return
     */
    CategoryEntity selectOne(CategoryEntity entity);

}
