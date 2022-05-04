import { defineStore } from 'pinia';
import { clearAllCoolies, getTokenFromCookie } from '/@/lib/cookie-util';
import localKey from '/@/constants/system/local-storage-key';
import { localSave, localRead } from '/@/lib/local-util';
import _ from 'lodash';
import { appDefaultConfig } from '/@/config/app-config';
import { localClear } from '/@/lib/local-util';
import { smartSentry } from '/@/lib/smart-sentry';

// 构建菜单上级ID列表map 方便左侧菜单选中
function buildMenuParentIdListMap() {
  useUserStore().menuParentIdListMap = new Map();
  let menuTree = useUserStore().getMenuTree;
  recursionMenuTree(menuTree || [], []);
}
// 递归
function recursionMenuTree(menuList, parentMenuList) {
  for (const e of menuList) {
    // 顶级parentMenuList清空
    if (e.parentId == 0) {
      parentMenuList = [];
    }
    let menuIdStr = e.menuId.toString();
    if (!_.isEmpty(e.children) && e.menuName) {
      // 递归
      parentMenuList.push({ name: menuIdStr, title: e.menuName });
      recursionMenuTree(e.children || [], parentMenuList);
    } else {
      useUserStore().menuParentIdListMap?.set(menuIdStr, parentMenuList);
    }
  }
}

export const useUserStore = defineStore({
  id: 'userStore',
  state: () => ({
    token: '',
    pointsList: [],
    menuTree: [],
    menuList: [],
    allMenuList: [],
    tagNav: [],
    userInfo: {},
    menuParentIdListMap: new Map(),
  }),
  getters: {
    getToken(state) {
      if (state.token) {
        return state.token;
      }
      return getTokenFromCookie();
    },
    getUserInfo(state) {
      if (_.isEmpty(state.userInfo)) {
        let localUserInfo = localRead(localKey.USER_INFO) || '';
        state.userInfo = localUserInfo ? JSON.parse(localUserInfo) : {};
      }
      return state.userInfo;
    },
    getMenuTree(state) {
      let userInfo = this.getUserInfo;
      if (_.isEmpty(state.menuTree) && !_.isEmpty(userInfo)) {
        state.menuTree = userInfo.menuTree;
      }
      return state.menuTree;
    },
    getMenuList(state) {
      let userInfo = this.getUserInfo;
      if (_.isEmpty(state.menuList) && !_.isEmpty(userInfo)) {
        state.menuList = userInfo.menuList;
      }
      return state.menuList;
    },
    getAllMenuList(state) {
      let userInfo = this.getUserInfo;
      if (_.isEmpty(state.allMenuList) && !_.isEmpty(userInfo)) {
        state.allMenuList = userInfo.allMenuList;
      }
      return state.allMenuList;
    },
    getPointList(state) {
      let userInfo = this.getUserInfo;
      if (_.isEmpty(state.pointsList) && !_.isEmpty(userInfo)) {
        state.pointsList = userInfo.pointsList;
      }
      return state.pointsList;
    },
    getMenuParentIdListMap(state) {
      if (_.isEmpty(state.menuParentIdListMap)) {
        buildMenuParentIdListMap();
      }
      return state.menuParentIdListMap;
    },
    getTagNav(state) {
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
    logout() {
      //清空本地数据以后直接刷新
      localClear();
      clearAllCoolies();
      this.tagNav = [];
      this.pointsList = [];
      this.menuTree = [];
      this.menuList = [];
      this.allMenuList = [];
      this.userInfo = {};
      this.menuParentIdListMap = new Map();
      smartSentry.clearUser();
    },
    setUserSession(data) {
      this.token = data.token;
      this.pointsList = data.pointsList;
      this.menuTree = data.menuTree;
      this.menuList = data.menuList;
      this.allMenuList = data.allMenuList;
      this.userInfo = data;
      localSave(localKey.USER_INFO, JSON.stringify(data));
      buildMenuParentIdListMap();
      smartSentry.setUser(data);
    },
    setToken(token) {
      this.token = token;
    },
    setTagNav(route, from) {
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
        findTag.menuQuery = route.query;
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
    closeTagNav(menuName, closeAll) {
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
    closePage(route, router, path) {
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
            router.push({
              name: tagNav.fromMenuName,
              query: tagNav.fromMenuQuery,
            });
          } else {
            // 查询左侧tag
            let leftTagNav = this.getTagNav[index - 1];
            router.push({
              name: leftTagNav.menuName,
              query: leftTagNav.menuQuery,
            });
          }
        }
      }
      this.closeTagNav(route.name, false);
    },
  },
});
