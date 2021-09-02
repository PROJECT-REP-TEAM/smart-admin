package net.lab1024.smartadmin.service.module.system.menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.service.module.system.menu.domain.MenuEntity;
import net.lab1024.smartadmin.service.module.system.menu.domain.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 菜单
 *
 * @author 卓大
 * @date 2021/7/29 16:13
 */
@Mapper
@Component
public interface MenuDao extends BaseMapper<MenuEntity> {

    /**
     * 根据名称查询同一级下的菜单
     *
     * @param menuName   菜单名
     * @param parentId   父级id
     * @param deleteFlag 是否删除
     * @return
     */
    MenuEntity getByMenuName(@Param("menuName") String menuName, @Param("parentId") Long parentId, @Param("deleteFlag") Boolean deleteFlag);

    /**
     * 根据菜单ID删除菜单（逻辑删除）
     *
     * @param menuIdList   菜单id集合
     * @param updateUserId 操作人id
     * @param deleteFlag   是否删除
     */
    void deleteByMenuIdList(@Param("menuIdList") List<Long> menuIdList, @Param("updateUserId") Long updateUserId, @Param("deleteFlag") Boolean deleteFlag);

    /**
     * 查询菜单列表
     *
     * @param deleteFlag   是否删除
     * @param disabledFlag 是否禁用
     * @param menuTypeList 菜单类型集合
     * @return
     */
    List<MenuVO> queryMenuList(@Param("deleteFlag") Boolean deleteFlag, @Param("disabledFlag") Boolean disabledFlag, @Param("menuTypeList") List<Integer> menuTypeList);


    /**
     * 根据菜单ID 查询功能点列表
     *
     * @param menuId     菜单id
     * @param menuType   菜单类型
     * @param deleteFlag 删除标记
     * @return
     */
    List<MenuEntity> getPointListByMenuId(@Param("menuId") Long menuId, @Param("menuType") Integer menuType, @Param("deleteFlag") Boolean deleteFlag);

    /**
     * 根据员工ID查询菜单列表
     *
     * @param deleteFlag   是否删除
     * @param disabledFlag 禁用标识
     * @param employeeId   员工id
     * @return
     */
    List<MenuVO> queryMenuByEmployeeId(@Param("deleteFlag") Boolean deleteFlag,
                                       @Param("disabledFlag") Boolean disabledFlag,
                                       @Param("employeeId") Long employeeId);

    /**
     * 查询有效功能点
     *
     * @param menuType 菜单类型
     * @param deleteFlag 删除已
     * @param disabledFlag 禁用
     * @return
     */
    List<MenuEntity> queryAllPoint(@Param("menuType") Integer menuType,
                                   @Param("deleteFlag") Boolean deleteFlag,
                                   @Param("disabledFlag") Boolean disabledFlag);
}
