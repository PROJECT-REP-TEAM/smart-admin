/*
 * @Description
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 */
import Cookies from 'js-cookie';

export const COOKIE_TOKEN_KEY = 'sa_user_token';

export const clearAllCoolies = (): void => {
  Cookies.remove(COOKIE_TOKEN_KEY);
};

export const getTokenFromCookie = (): string | undefined => {
  return Cookies.get(COOKIE_TOKEN_KEY);
};

/**
 * 7 天后cookie过期
 *
 * @param token
 */
export const saveTokenToCookie = (token: string): void => {
  Cookies.set(COOKIE_TOKEN_KEY, token, { expires: 7 });
};
