import {loginRouters} from './system/login';
import {homeRouters} from './system/home';

export const routerArray = [
    ...loginRouters,
    ...homeRouters,

]
