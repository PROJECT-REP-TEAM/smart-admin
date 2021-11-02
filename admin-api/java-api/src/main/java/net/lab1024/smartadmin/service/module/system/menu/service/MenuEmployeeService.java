package net.lab1024.smartadmin.service.module.system.menu.service;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import net.lab1024.smartadmin.service.common.util.SmartStringUtil;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeService;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.constant.MenuTypeEnum;
import net.lab1024.smartadmin.service.module.system.menu.domain.bo.MenuLoginBO;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuTreeVO;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuVO;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleMenuDao;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleMenuEntity;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工菜单缓存
 *
 * @author lihaifan
 * @date 2021/8/5 11:47
 */
@Service
public class MenuEmployeeService {

    private static final long SUPER_ROLE_ID = -1L;

    /**
     * 员工菜单权限
     */
    private ConcurrentLinkedHashMap<Long, List<MenuVO>> roleMenuListMap = new ConcurrentLinkedHashMap.Builder<Long, List<MenuVO>>().maximumWeightedCapacity(1000).build();

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 初始化角色-菜单权限Map
     */
    @PostConstruct
    public void initRoleMenuListMap() {
        roleMenuListMap.clear();
        // 查询所有可用菜单列表
        List<MenuVO> menuVOList = menuService.queryMenuList(Boolean.FALSE);
        // 查询所有角色菜单
        List<RoleMenuEntity> roleMenuList = roleMenuDao.queryAllRoleMenu();
        Map<Long, List<Long>> roleMenuIdListMap = roleMenuList.stream().collect(Collectors.groupingBy(RoleMenuEntity::getRoleId, Collectors.mapping(RoleMenuEntity::getMenuId, Collectors.toList())));
        for (Map.Entry<Long, List<Long>> roleMenuId : roleMenuIdListMap.entrySet()) {
            List<Long> menuIdList = roleMenuId.getValue();
            List<MenuVO> roleMenuVOList = menuVOList.stream().filter(e -> menuIdList.contains(e.getMenuId())).collect(Collectors.toList());
            roleMenuListMap.put(roleMenuId.getKey(), roleMenuVOList);
        }
        // map中放入超管权限
        roleMenuListMap.put(SUPER_ROLE_ID, menuVOList);
    }

    /**
     * 校验用户是否有功能权限
     *
     * @param loginInfoDTO
     * @param perms
     * @return
     */
    public Boolean checkEmployeeHavePrivilege(RequestEmployee loginInfoDTO, String perms) {
        if (StringUtils.isBlank(perms)) {
            return false;
        }
        Boolean isSuperman = loginInfoDTO.getIsSuperMan();
        if (isSuperman) {
            return true;
        }
        List<MenuVO> menuList = this.getMenuByRoleIdList(loginInfoDTO.getRoleList(), loginInfoDTO.getIsSuperMan());
        Optional<MenuVO> findRes = menuList.stream().filter(e -> {
            List<String> permsList = e.getPermsList();
            if (CollectionUtils.isEmpty(permsList)) {
                return false;
            }
            return permsList.contains(perms);
        }).findFirst();
        return findRes.isPresent();
    }

    /**
     * 判断是否为超级管理员
     *
     * @param employeeId
     * @return
     */
    public Boolean isSuperman(Long employeeId) {
        String systemConfigValue = systemConfigService.getConfigValue(SystemConfigKeyEnum.EMPLOYEE_SUPERMAN);
        List<Long> superManIdsList = SmartStringUtil.splitConverToLongList(systemConfigValue, ",");
        return superManIdsList.contains(employeeId);
    }

    /**
     * 根据角色列表查询菜单列表
     *
     * @param roleIdList
     * @return
     */
    private List<MenuVO> getMenuByRoleIdList(List<Long> roleIdList, Boolean isSuperman) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        // 超管返回全部菜单权限
        if (isSuperman) {
            return roleMenuListMap.get(SUPER_ROLE_ID);
        }
        List<MenuVO> menuVOList = Lists.newArrayList();
        for (Long roleId : roleIdList) {
            menuVOList.addAll(roleMenuListMap.getOrDefault(roleId, Lists.newArrayList()));
        }
        if (CollectionUtils.isEmpty(menuVOList)) {
            return Lists.newArrayList();
        }
        Map<Long, MenuVO> distinctMap = menuVOList.stream().collect(Collectors.toMap(MenuVO::getMenuId, Function.identity(), (f, s) -> f));
        List<MenuVO> resultMenuList = distinctMap.values().stream().collect(Collectors.toList());
        // 合并后排序
        return resultMenuList.stream().sorted(Comparator.comparing(MenuVO::getSort,Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
    }


    /**
     * 查询用户拥有的前端菜单项 用于登陆返回 前端动态路由配置
     *
     * @param employeeId
     * @return
     */
    public MenuLoginBO queryMenuTreeByEmployeeId(Long employeeId) {
        // 获取用户权限
        RequestEmployee requestEmployee = employeeService.getById(employeeId);
        // 获取角色菜单权限
        List<MenuVO> menuVOList = this.getMenuByRoleIdList(requestEmployee.getRoleList(), requestEmployee.getIsSuperMan());
        // 角色菜单类型页面
        List<MenuVO> menuList = menuVOList.stream().filter(e -> e.getMenuType().equals(MenuTypeEnum.MENU.getValue())).collect(Collectors.toList());
        //获取菜单树
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().filter(e -> !e.getMenuType().equals(MenuTypeEnum.POINTS.getValue())).collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuTreeVO> menuTreeVOList = menuService.buildMenuTree(parentMap, 0L);
        //获取权限列表
        List<String> pointsList = menuVOList.stream().filter(e -> e.getMenuType().equals(MenuTypeEnum.POINTS.getValue())).map(MenuVO::getWebPerms).collect(Collectors.toList());
        // 所有菜单
        List<MenuVO> allMenuList = roleMenuListMap.get(SUPER_ROLE_ID);
        MenuLoginBO menuLoginBO = new MenuLoginBO();
        menuLoginBO.setMenuTree(menuTreeVOList);
        menuLoginBO.setMenuList(menuList);
        menuLoginBO.setAllMenuList(allMenuList);
        menuLoginBO.setPointsList(pointsList);
        return menuLoginBO;
    }

}
