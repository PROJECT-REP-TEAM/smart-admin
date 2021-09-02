package net.lab1024.smartadmin.service.module.system.menu.domain;

import lombok.Data;

import java.util.List;

/**
 * 登陆时需要的菜单BO
 *
 * @author 卓大
 * @date 2021/8/5 11:35
 */
@Data
public class MenuLoginBO {

    /**
     * 菜单树
     */
    private List<MenuTreeVO> menuTree;

    /**
     * 功能点权限列表
     */
    private List<String> pointsList;

    public MenuLoginBO() {
    }

    public MenuLoginBO(List<MenuTreeVO> menuTree, List<String> pointsList) {
        this.menuTree = menuTree;
        this.pointsList = pointsList;
    }
}
