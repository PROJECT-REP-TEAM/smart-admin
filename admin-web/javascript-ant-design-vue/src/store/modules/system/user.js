import { defineStore } from 'pinia';
import { getTokenFromCookie } from '/@/utils/cookie-util';
import localKey from '/@/constants/system/local-storage-key';
import { localSave, localRead } from '/@/utils/local-util';
import _ from 'lodash';
import { appDefaultConfig } from '/@/config/app-config';
import { localClear } from '/@/utils/local-util';

export const useUserStore = defineStore({
  id: 'userStore',
  state: () => ({
    token: '',
    pointsList: [],
    menuTree: [],
    tagNav: [],
    userInfo: {},
  }),
  getters: {
    getToken (state) {
      if (state.token) {
        return state.token;
      }
      return getTokenFromCookie();
    },
    getUserInfo (state) {
      if (_.isEmpty(state.userInfo)) {
        let localUserInfo = localRead(localKey.USER_INFO) || '';
        state.userInfo = localUserInfo ? JSON.parse(localUserInfo) : {};
      }
      return state.userInfo;
    },
    getMenuTree (state) {
      if (_.isEmpty(state.menuTree)) {
        let localUserMenu = localRead(localKey.USER_MENU) || '';
        state.menuTree = localUserMenu ? JSON.parse(localUserMenu) : [];
      }
      return state.menuTree;
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
      this.pointsList = [];
      this.menuTree = [];
      this.tagNav = [];
      this.userInfo = {};
      localClear();
    },
    setUserSession (data) {
      this.token = data.token;
      this.pointsList = data.pointsList;
      this.menuTree = data.menuTree;
      this.userInfo = data;
      this.setUserMenu(data);
      localSave(localKey.USER_INFO, JSON.stringify(data));
    },
    setUserMenu (data) {
      this.pointsList = [];
      this.menuTree = [];
      localSave(localKey.USER_MENU, JSON.stringify(data.menuTree));
      localSave(localKey.USER_POINTS, JSON.stringify(data.pointsList));
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
