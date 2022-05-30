package net.lab1024.smartadmin.module.system.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.smartadmin.module.system.menu.constant.MenuTypeEnum;

import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2021/7/27
 */
@Data
@TableName(value = "t_menu")
public class MenuEntity {

    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long  menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 类型
     * @see MenuTypeEnum
     */
    private Integer menuType;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 是否为外链
     */
    private Boolean frameFlag;

    /**
     * 是否缓存
     */
    private Boolean cacheFlag;

    /**
     * 显示状态
     */
    private Boolean visibleFlag;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 权限字符串
     */
    private String perms;

    /**
     * 前端权限字符串
     */
    private String webPerms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 功能点关联菜单ID
     */
    private Long contextMenuId;

    /**
     * 删除状态
     */
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private Long updateUserId;
}
