package net.lab1024.smartadmin.service.module.system.menu;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.codeconst.ResponseCodeConst;
import net.lab1024.smartadmin.service.common.constant.CommonConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.menu.constant.MenuTypeEnum;
import net.lab1024.smartadmin.service.module.system.menu.domain.*;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 菜单
 *
 * @author 卓大
 * @date 2021/7/29 16:11
 */
@Service
public class MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuManager menuManager;

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    @Autowired
    private RequestUrlService requestUrlService;

    /**
     * 添加菜单
     *
     * @param menuAddForm
     * @return
     */
    public ResponseDTO<String> addMenu(MenuAddForm menuAddForm) {
        // 校验菜单名称
        if (this.validateMenuName(menuAddForm)) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "菜单名称已存在");
        }
        MenuEntity menuEntity = SmartBeanUtil.copy(menuAddForm, MenuEntity.class);
        // 处理接口权限
        List<String> permsList = menuAddForm.getPermsList();
        if(!CollectionUtils.isEmpty(permsList)){
            String perms = StringUtils.join(permsList, ",");
            menuEntity.setPerms(perms);
        }
        // 功能点列表为空 直接添加菜单
        List<MenuPointsOperateForm> pointList = menuAddForm.getPointList();
        if (CollectionUtils.isEmpty(pointList)) {
            menuDao.insert(menuEntity);
            // 更新角色权限缓存
            menuEmployeeService.initRoleMenuListMap();
            return ResponseDTO.succ();
        }
        // 若功能点列表不为空
        ResponseDTO<List<MenuEntity>> responseDTO = this.validateBuildPointList(menuAddForm.getMenuType(), pointList);
        if (!responseDTO.isSuccess()) {
            return ResponseDTO.wrap(responseDTO);
        }
        menuManager.addMenu(menuEntity, responseDTO.getData());
        // 更新角色权限缓存
        menuEmployeeService.initRoleMenuListMap();
        return ResponseDTO.succ();
    }

    /**
     * 更新菜单
     *
     * @param menuUpdateForm
     * @return
     */
    public ResponseDTO<String> updateMenu(MenuUpdateForm menuUpdateForm) {
        //校验菜单是否存在
        MenuEntity selectMenu = menuDao.selectById(menuUpdateForm.getMenuId());
        if (selectMenu == null) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "菜单不存在");
        }
        if (selectMenu.getDeleteFlag()) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "菜单已被删除");
        }
        //校验菜单名称
        if (this.validateMenuName(menuUpdateForm)) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "菜单名称已存在");
        }
        if (menuUpdateForm.getMenuId().equals(menuUpdateForm.getParentId())) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "上级菜单不能为自己");
        }
        MenuEntity menuEntity = SmartBeanUtil.copy(menuUpdateForm, MenuEntity.class);
        // 处理接口权限
        List<String> permsList = menuUpdateForm.getPermsList();
        if(!CollectionUtils.isEmpty(permsList)){
            String perms = StringUtils.join(permsList, ",");
            menuEntity.setPerms(perms);
        }
        //功能点列表为空 直接修改菜单
        List<MenuPointsOperateForm> pointList = menuUpdateForm.getPointList();
        if (CollectionUtils.isEmpty(pointList)) {
            menuDao.updateById(menuEntity);
            // 更新角色权限缓存
            menuEmployeeService.initRoleMenuListMap();
            return ResponseDTO.succ();
        }
        //若功能点列表不为空
        ResponseDTO<List<MenuEntity>> validateBuildPointList = this.validateBuildPointList(menuUpdateForm.getMenuType(), pointList);
        if (!validateBuildPointList.isSuccess()) {
            return ResponseDTO.wrap(validateBuildPointList);
        }
        List<MenuEntity> pointEntityList = validateBuildPointList.getData();
        //查询当前菜单下的功能点列表
        List<MenuEntity> pointListByMenuId = menuDao.getPointListByMenuId(menuEntity.getMenuId(), MenuTypeEnum.POINTS.getValue(), Boolean.FALSE);
        //新增的功能点
        List<MenuEntity> savePointList = pointEntityList.stream().filter(e -> e.getMenuId() == null).collect(Collectors.toList());
        //删除的功能点
        List<MenuEntity> deletePointList = Lists.newArrayList();
        //更新的功能点
        List<MenuEntity> updatePointList = Lists.newArrayList();
        //获取修改、移除的功能点列表
        for (MenuEntity menu : pointListByMenuId) {
            Optional<MenuEntity> findMenu = pointEntityList.stream().filter(e -> e.getMenuId() != null && e.getMenuId().equals(menu.getMenuId())).findFirst();
            //删除的
            if (!findMenu.isPresent()) {
                menu.setDeleteFlag(Boolean.TRUE);
                menu.setUpdateUserId(menuUpdateForm.getUpdateUserId());
                deletePointList.add(menu);
                continue;
            }
            MenuEntity requestMenu = findMenu.get();
            //是否更新
            Long requestContextMenuId = requestMenu.getContextMenuId() == null ? -1L : requestMenu.getContextMenuId();
            Long menuContextMenuId = menu.getContextMenuId() == null ? -1L : menu.getContextMenuId();
            if (!requestMenu.getMenuName().equals(menu.getMenuName())
                    || !requestMenu.getDisabledFlag().equals(menu.getDisabledFlag())
                    || !requestMenu.getPerms().equals(menu.getPerms())
                    || !requestContextMenuId.equals(menuContextMenuId)) {
                requestMenu.setUpdateUserId(menuUpdateForm.getUpdateUserId());
                updatePointList.add(requestMenu);
            }
        }
        menuManager.updateMenu(menuEntity, savePointList, deletePointList, updatePointList);
        // 更新角色权限缓存
        menuEmployeeService.initRoleMenuListMap();
        return ResponseDTO.succ();
    }

    /**
     * 校验构建功能点列表
     *
     * @param menuType
     * @param pointList
     * @return
     */
    private ResponseDTO<List<MenuEntity>> validateBuildPointList(Integer menuType, List<MenuPointsOperateForm> pointList) {
        //判断 目录/功能点不能添加功能点
        if (!MenuTypeEnum.MENU.equalsValue(menuType)) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "目录/功能点不能添加子功能点");
        }
        //构建功能点对象
        List<MenuEntity> pointEntityList = pointList.stream().map(e -> {
            MenuEntity menu = SmartBeanUtil.copy(e, MenuEntity.class);
            // 处理接口权限
            List<String> permsList = e.getPermsList();
            if(!CollectionUtils.isEmpty(permsList)){
                String perms = StringUtils.join(permsList, ",");
                menu.setPerms(perms);
            }
            return menu;
        }).collect(Collectors.toList());
        //校验功能点名称是否重复
        Map<String, Long> nameGroupBy = pointEntityList.stream().collect(Collectors.groupingBy(MenuEntity::getMenuName, Collectors.counting()));
        List<String> repeatName = nameGroupBy.entrySet().stream().filter(e -> e.getValue() > 1).map(e -> e.getKey()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(repeatName)) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "功能点：" + String.join("、", repeatName) + "，名称重复");
        }
        return ResponseDTO.succData(pointEntityList);
    }

    /**
     * 批量删除菜单
     *
     * @param menuIdList
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> batchDeleteMenu(List<Long> menuIdList, Long employeeId) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "所选菜单不能为空");
        }
        menuDao.deleteByMenuIdList(menuIdList, employeeId, Boolean.TRUE);
        // 更新角色权限缓存
        menuEmployeeService.initRoleMenuListMap();
        return ResponseDTO.succ();
    }

    /**
     * 校验菜单名称
     *
     * @param menuDTO
     * @param <T>
     * @return true 重复 false 未重复
     */
    public <T extends MenuBasicDTO> Boolean validateMenuName(T menuDTO) {
        MenuEntity menu = menuDao.getByMenuName(menuDTO.getMenuName(), menuDTO.getParentId(), Boolean.FALSE);
        if (menuDTO instanceof MenuAddForm) {
            return menu != null;
        }
        if (menuDTO instanceof MenuUpdateForm) {
            Long menuId = ((MenuUpdateForm) menuDTO).getMenuId();
            return menu != null && menu.getMenuId().longValue() != menuId.longValue();
        }
        return true;
    }

    /**
     * 查询菜单列表
     *
     * @return
     */
    public List<MenuVO> queryMenuList(Boolean disabledFlag) {
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, disabledFlag, null);
        //根据ParentId进行分组
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuVO> filterMenuVOList = this.filterNoParentMenu(parentMap, CommonConst.DEFAULT_PARENT_ID);
        return filterMenuVOList;
    }

    /**
     * 过滤没有上级菜单的菜单列表
     *
     * @param parentMap
     * @param parentId
     * @return
     */
    private List<MenuVO> filterNoParentMenu(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        // 获取本级菜单树List
        List<MenuVO> res = parentMap.getOrDefault(parentId, Lists.newArrayList());
        List<MenuVO> childMenu = Lists.newArrayList();
        // 循环遍历下级菜单
        res.forEach(e -> {
            //处理接口权限
            String perms = e.getPerms();
            if(!StringUtils.isBlank(perms)){
                List<String> permsList = Lists.newArrayList(StringUtils.split(perms, ","));
                e.setPermsList(permsList);
            }
            List<MenuVO> menuList = this.filterNoParentMenu(parentMap, e.getMenuId());
            childMenu.addAll(menuList);
        });
        res.addAll(childMenu);
        return res;
    }

    /**
     * 查询菜单树
     *
     * @param onlyMenu 不查询功能点
     * @return
     */
    public ResponseDTO<List<MenuTreeVO>> queryMenuTree(Boolean onlyMenu) {
        List<Integer> menuTypeList = Lists.newArrayList();
        if (onlyMenu) {
            menuTypeList = Lists.newArrayList(MenuTypeEnum.CATALOG.getValue(), MenuTypeEnum.MENU.getValue());
        }
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, null, menuTypeList);
        //根据ParentId进行分组
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuTreeVO> menuTreeVOList = this.buildMenuTree(parentMap, CommonConst.DEFAULT_PARENT_ID);
        return ResponseDTO.succData(menuTreeVOList);
    }

    /**
     * 构建菜单树
     *
     * @return
     */
    List<MenuTreeVO> buildMenuTree(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        // 获取本级菜单树List
        List<MenuTreeVO> res = parentMap.getOrDefault(parentId, Lists.newArrayList()).stream()
                .map(e -> SmartBeanUtil.copy(e, MenuTreeVO.class)).collect(Collectors.toList());
        // 循环遍历下级菜单
        res.forEach(e -> {
            //处理接口权限
            String perms = e.getPerms();
            if(!StringUtils.isBlank(perms)){
                List<String> permsList = Lists.newArrayList(StringUtils.split(perms, ","));
                e.setPermsList(permsList);
            }
            e.setChildren(this.buildMenuTree(parentMap, e.getMenuId()));
        });
        return res;
    }

    /**
     * 查询菜单详情
     *
     * @param menuId
     * @return
     */
    public ResponseDTO<MenuVO> getMenuDetail(Long menuId) {
        //校验菜单是否存在
        MenuEntity selectMenu = menuDao.selectById(menuId);
        if (selectMenu == null) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.SYSTEM_ERROR, "菜单不存在");
        }
        if (selectMenu.getDeleteFlag()) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.SYSTEM_ERROR, "菜单已被删除");
        }
        MenuVO menuVO = SmartBeanUtil.copy(selectMenu, MenuVO.class);
        //处理接口权限
        String perms = menuVO.getPerms();
        if(!StringUtils.isBlank(perms)){
            List<String> permsList = Lists.newArrayList(StringUtils.split(perms, ","));
            menuVO.setPermsList(permsList);
        }
        return ResponseDTO.succData(menuVO);
    }

    /**
     * 获取系统所有请求路径
     *
     * @return
     */
    public ResponseDTO<List<RequestUrlVO>> getPrivilegeUrlDTOList() {
        List<RequestUrlVO> privilegeUrlList = requestUrlService.getPrivilegeList();
        return ResponseDTO.succData(privilegeUrlList);
    }
}
