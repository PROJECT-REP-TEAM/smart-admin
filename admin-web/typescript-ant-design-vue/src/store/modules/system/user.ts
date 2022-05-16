import {defineStore} from 'pinia';
import {UserState} from '/@/types/user';
import {getTokenFromCookie} from '/@/utils/cookie-util';
import localKey from '/@/constants/system/local-storage-key';
import {localSave, localRead} from '/@/utils/local-util';
import {RouteLocationNormalized, RouteLocationNormalizedLoaded, Router} from 'vue-router';
import {UserTagNav} from '/@/store/modules/model/UserTagNav';
import _ from 'lodash';
import {appDefaultConfig} from '/@/config/app-config';
import {MenuTreeVo} from '/@/api/system/menu/model/menu-tree-vo';
import {LoginResultVo} from '/@/api/system/login/model/login-result-vo';
import {localClear} from "/@/utils/local-util";
import {MENU_TYPE_ENUM} from "/@/constants/system/menu";
import {MenuVo} from "/@/api/system/menu/model/menu-vo";

export const useUserStore = defineStore({
    id: 'userStore',
    state: (): UserState => ({
        token: '',
        pointsList: [],
        menuTree: [],
        tagNav: [],
        userInfo: {},
    }),
    getters: {
        getToken(state: UserState): string | undefined {
            if (state.token) {
                return state.token;
            }
            return getTokenFromCookie();
        },
        getUserInfo(state: UserState): LoginResultVo {
            if (_.isEmpty(state.userInfo)) {
                let localUserInfo = localRead(localKey.USER_INFO) || '';
                state.userInfo = localUserInfo ? JSON.parse(localUserInfo) : {};
            }
            return state.userInfo;
        },
        getMenuTree(state: UserState): Array<MenuTreeVo> | undefined {
            if (_.isEmpty(state.menuTree)) {
                let localUserMenu = localRead(localKey.USER_MENU) || '';
                state.menuTree = localUserMenu ? JSON.parse(localUserMenu) : [];
            }
            return state.menuTree;
        },
        getPointList(state: UserState): Array<string> | undefined {
            if (_.isEmpty(state.pointsList)) {
                let localUserPoints = localRead(localKey.USER_POINTS) || '';
                state.pointsList = localUserPoints ? JSON.parse(localUserPoints) : [];
            }
            return state.pointsList;
        },
        getTagNav(state: UserState): Array<UserTagNav> | undefined {
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
            this.token = '';
            this.pointsList = [];
            this.menuTree = [];
            this.tagNav = [];
            this.userInfo = {};
            localClear();
        },
        setUserSession(data: LoginResultVo): void {
            this.token = data.token;
            this.setPointsList(data);
            this.setMenuTree(data);
            this.userInfo = data;
            localSave(localKey.USER_INFO, JSON.stringify(data));
        },
        setPointsList(data: LoginResultVo): void {
            if (_.isEmpty(data.menuList)) {
                return;
            }
            this.pointsList = data.menuList?.filter(e => MENU_TYPE_ENUM.POINTS.value == e.menuType && e.permsIdentifier).map(e => e.permsIdentifier);
            localSave(localKey.USER_POINTS, JSON.stringify(this.pointsList));
        },
        setMenuTree(data: LoginResultVo): void {
            if (_.isEmpty(data.menuList)) {
                return;
            }
            let rootMenu = data.menuList?.filter(e => e.parentId == 0);
            if (_.isEmpty(rootMenu)) {
                return;
            }
            this.menuTree = [...rootMenu];
            this.buildMenuTree(data.menuList, this.menuTree)
            localSave(localKey.USER_MENU, JSON.stringify(this.menuTree));
        },
        buildMenuTree(menuList?: Array<MenuVo>, menuTreeList: Array<MenuTreeVo>) {
            if (_.isEmpty(menuTreeList)) {
                return;
            }
            for (let treeElement of menuTreeList) {
                let children = menuList.filter(e => e.parentId == treeElement.menuId);
                if (_.isEmpty(children)) {
                    continue;
                }
                treeElement.children = [...children];
            }
        },
        setTagNav(route: RouteLocationNormalized, from: RouteLocationNormalized) {
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
        closeTagNav(menuName: string | null, closeAll: boolean) {
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
        closePage(route: RouteLocationNormalizedLoaded, router: Router, path?: string) {
            if (!this.getTagNav || _.isEmpty(this.getTagNav)) return;
            if (path) {
                router.push({path});
            } else {
                // 寻找tagNav
                let index = this.getTagNav.findIndex((e) => e.menuName == route.name);
                if (index == -1) {
                    router.push({name: appDefaultConfig.homePageName});
                } else {
                    let tagNav = this.getTagNav[index];
                    if (tagNav.fromMenuName && this.getTagNav.some((e) => e.menuName == tagNav.fromMenuName)) {
                        router.push({name: tagNav.fromMenuName, query: tagNav.fromMenuQuery});
                    } else {
                        // 查询左侧tag
                        let leftTagNav = this.getTagNav[index - 1];
                        router.push({name: leftTagNav.menuName, query: leftTagNav.menuQuery});
                    }
                }
            }
            this.closeTagNav(<string>route.name, false);
        },
    },
});
