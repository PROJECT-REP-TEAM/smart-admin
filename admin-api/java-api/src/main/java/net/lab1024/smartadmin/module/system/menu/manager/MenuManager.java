package net.lab1024.smartadmin.module.system.menu.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lab1024.smartadmin.module.system.menu.constant.MenuTypeEnum;
import net.lab1024.smartadmin.module.system.menu.dao.MenuDao;
import net.lab1024.smartadmin.module.system.menu.domain.entity.MenuEntity;

import java.util.List;

/**
 * 菜单Manager层
 *
 * @author 李善逸
 * @date 2021/7/30 23:26
 */
@Service
public class MenuManager extends ServiceImpl<MenuDao, MenuEntity> {

    /**
     * 添加菜单
     *
     * @param menuEntity
     * @param pointEntityList
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMenu(MenuEntity menuEntity, List<MenuEntity> pointEntityList) {
        //添加菜单
        save(menuEntity);
        //构建功能点
        pointEntityList.forEach(e->{
            e.setParentId(menuEntity.getMenuId());
            e.setMenuType(MenuTypeEnum.POINTS.getValue());
            e.setCreateUserId(menuEntity.getCreateUserId());
        });
        //批量添加功能点
        saveBatch(pointEntityList);
    }

    /**
     * 更新菜单
     * @param menuEntity
     * @param savePointList
     * @param deletePointList
     * @param updatePointList
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuEntity menuEntity, List<MenuEntity> savePointList, List<MenuEntity> deletePointList, List<MenuEntity> updatePointList) {
        //更新菜单
        updateById(menuEntity);
        //构建新增功能点
        savePointList.forEach(e->{
            e.setParentId(menuEntity.getMenuId());
            e.setMenuType(MenuTypeEnum.POINTS.getValue());
            //因为更新操作人在menuEntity的UpdateUserId字段
            e.setCreateUserId(menuEntity.getUpdateUserId());
        });
        //批量添加功能点
        saveBatch(savePointList);
        //批量删除功能点
        updateBatchById(deletePointList);
        //批量更新功能点
        updateBatchById(updatePointList);
    }
}
