import {loginRouters} from './system/login';
import {homeRouters} from './system/home';
import type {RouteRecordRaw} from "vue-router";

export const routerArray: RouteRecordRaw[] = [
    ...loginRouters,
    ...homeRouters,

]
