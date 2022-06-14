import { defineStore } from 'pinia';
import { getTokenFromCookie } from '/@/utils/cookie-util';
import localKey from '/@/constants/system/local-storage-key';
import { localSave, localRead } from '/@/utils/local-util';
import _ from 'lodash';
import { appDefaultConfig } from '/@/config/app-config';
import { localClear } from '/@/utils/local-util';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu/menu-enum';
import menu from '/@/i18n/lang/en-US/menu';


export const useUserStore = defineStore({
  id: 'userStore',
  state: () => ({
    token: '',
    employeeId: '',
    loginName: '',
    actualName: '',
    phone: '',
    departmentId: '',
    departmentName: '',
    administratorFlag: false,
    //左侧菜单树形结构
    menuTree: [],
    //存在页面路由的菜单集合
    menuRouterList: [],
    //是否完成menuRouter初始化
    menuRouterInitFlag: false,
    //父类菜单集合
    menuParentIdListMap: new Map(),
    // 功能点集合
    pointsList: [],
    // 标签页
    tagNav: [],
  }),
  getters: {
    getToken (state) {
      if (state.token) {
        return state.token;
      }
      return getTokenFromCookie();
    },
    getMenuRouterInitFlag (state) {
      return state.menuRouterInitFlag;
    },
    getUserInfo (state) {
      if (_.isEmpty(state.userInfo)) {
        let localUserInfo = localRead(localKey.USER_INFO) || '';
        state.userInfo = localUserInfo ? JSON.parse(localUserInfo) : {};
      }
      return state.userInfo;
    },
    getMenuTree (state) {
      return state.menuTree;
    },
    getMenuRouterList (state) {
      return state.menuRouterList;
    },
    getMenuParentIdListMap (state) {
      return state.menuParentIdListMap;
    },
    getPointList (state) {
      if (_.isEmpty(state.pointsList)) {
        let localUserPoints = localRead(localKey.USER_POINTS) || '';
        state.pointsList = localUserPoints ? JSON.parse(localUserPoints) : [];
      }
      return state.pointsList;
    },
    getTagNav (state) {
      if (_.isEmpty(state.tagNav)) {
        let localTagNav = localRead(localKey.USER_TAG_NAV) || '';
        state.tagNav = localTagNav ? JSON.parse(localTagNav) : [];
      }
      let tagNavList = _.cloneDeep(state.tagNav) || [];
      tagNavList.unshift({
        menuName: appDefaultConfig.homePageName,
        menuTitle: '首页',
      });
      return tagNavList;
    },
  },

  actions: {
    logout () {
      this.token = '';
      this.menuList = [];
      this.tagNav = [];
      this.userInfo = {};
      localClear();
    },
    setUserLoginInfo (data) {
      // 用户基本信息
      this.token = data.token;
      this.employeeId = data.employeeId;
      this.loginName = data.loginName;
      this.actualName = data.actualName;
      this.phone = data.phone;
      this.departmentId = data.departmentId;
      this.departmentName = data.departmentName;
      this.administratorFlag = data.administratorFlag;

      //菜单权限
      this.menuTree = buildMenuTree(data.menuList);

      //拥有路由的菜单
      this.menuRouterList = data.menuList.filter(e => e.path);

      //父级菜单集合
      this.menuParentIdListMap = buildMenuParentIdListMap(this.menuTree);

      //功能点
      this.pointsList = data.menuList.filter(menu =>
        menu.menuType === MENU_TYPE_ENUM.POINTS.value
        && menu.visibleFlag
        && !menu.disabledFlag
      );
    },
    setToken (token) {
      this.token = token;
    },
    setTagNav (route, from) {
      if (_.isEmpty(this.getTagNav)) this.tagNav = [];
      // name唯一标识
      let name = route.name;
      if (!name || name == appDefaultConfig.homePageName) {
        return;
      }
      let findTag = (this.tagNav || []).find((e) => e.menuName == name);
      if (findTag) {
        // @ts-ignore
        findTag.fromMenuName = from.name;
        findTag.fromMenuQuery = from.query;
      } else {
        // @ts-ignore
        this.tagNav.push({
          // @ts-ignore
          menuName: name,
          // @ts-ignore
          menuTitle: route.meta.title,
          menuQuery: route.query,
          // @ts-ignore
          fromMenuName: from.name,
          fromMenuQuery: from.query,
        });
      }
      localSave(localKey.USER_TAG_NAV, JSON.stringify(this.tagNav));
    },
    closeTagNav (menuName, closeAll) {
      if (_.isEmpty(this.getTagNav)) return;
      if (closeAll && !menuName) {
        this.tagNav = [];
      } else {
        let findIndex = (this.tagNav || []).findIndex((e) => e.menuName == menuName);
        if (closeAll) {
          if (findIndex == -1) {
            this.tagNav = [];
          } else {
            let tagNavElement = (this.tagNav || [])[findIndex];
            this.tagNav = [tagNavElement];
          }
        } else {
          (this.tagNav || []).splice(findIndex, 1);
        }
      }
      localSave(localKey.USER_TAG_NAV, JSON.stringify(this.tagNav));
    },
    closePage (route, router, path) {
      if (!this.getTagNav || _.isEmpty(this.getTagNav)) return;
      if (path) {
        router.push({ path });
      } else {
        // 寻找tagNav
        let index = this.getTagNav.findIndex((e) => e.menuName == route.name);
        if (index == -1) {
          router.push({ name: appDefaultConfig.homePageName });
        } else {
          let tagNav = this.getTagNav[index];
          if (tagNav.fromMenuName && this.getTagNav.some((e) => e.menuName == tagNav.fromMenuName)) {
            router.push({ name: tagNav.fromMenuName, query: tagNav.fromMenuQuery });
          } else {
            // 查询左侧tag
            let leftTagNav = this.getTagNav[index - 1];
            router.push({ name: leftTagNav.menuName, query: leftTagNav.menuQuery });
          }
        }
      }
      this.closeTagNav(route.name, false);
    },
  },
});



/**
 * 构建菜单父级集合
 */
function buildMenuParentIdListMap (menuTree) {
  let menuParentIdListMap = new Map();
  recursiveBuildMenuParentIdListMap(menuTree, [], menuParentIdListMap);
  return menuParentIdListMap;
}

function recursiveBuildMenuParentIdListMap (menuList, parentMenuList, menuParentIdListMap) {
  for (const e of menuList) {
    // 顶级parentMenuList清空
    if (e.parentId == 0) {
      parentMenuList = [];
    }
    let menuIdStr = e.menuId.toString();
    let cloneParentMenuList = _.cloneDeep(parentMenuList);
    if (!_.isEmpty(e.children) && e.menuName) {
      // 递归
      cloneParentMenuList.push({ name: menuIdStr, title: e.menuName });
      recursiveBuildMenuParentIdListMap(e.children, cloneParentMenuList, menuParentIdListMap);
    } else {
      menuParentIdListMap.set(menuIdStr, cloneParentMenuList);
    }
  }
}


/**
 * 构建菜单树
 * 
 * @param  menuList 
 * @returns 
 */
function buildMenuTree (menuList) {
  //1 获取所有 有效的 目录和菜单
  let catalogAndMenuList = menuList.filter(menu =>
    menu.menuType !== MENU_TYPE_ENUM.POINTS.value
    && menu.visibleFlag
    && !menu.disabledFlag
  );

  //2 获取顶级目录
  let topCatalogList = catalogAndMenuList.filter(menu => menu.parentId === 0);
  for (const topCatalog of topCatalogList) {
    buildMenuChildren(topCatalog, catalogAndMenuList)
  }
  return topCatalogList;
}

function buildMenuChildren (menu, allMenuList) {
  let children = allMenuList.filter(e => e.parentId === menu.menuId);
  if (children.length === 0) {
    return;
  }
  menu.children = children;
  for (const item of children) {
    buildMenuChildren(item, allMenuList);
  }
}
