import { error404Routers } from './system/error';
import { homeRouters } from './system/home';
import { loginRouters } from './system/login';

export const routerArray = [
  ...loginRouters,
  ...homeRouters,
  ...error404Routers,
];
