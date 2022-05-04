import Cookies from 'js-cookie';

export const COOKIE_TOKEN_KEY = 'smart_admin_token';

export const clearAllCoolies = () => {
  Cookies.remove(COOKIE_TOKEN_KEY);
};

export const getTokenFromCookie = () => {
  return Cookies.get(COOKIE_TOKEN_KEY);
};

/**
 * 7 天后cookie过期
 *
 * @param token
 */
export const saveTokenToCookie = (token) => {
  Cookies.set(COOKIE_TOKEN_KEY, token, { expires: 7 });
};
