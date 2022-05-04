/**
 * localStorage存取
 * @param key
 * @param value
 */
import localKey from '/@/constants/system/local-storage-key';
export const localSave = (key, value) => {
  localStorage.setItem(key, value);
};

export const localRead = (key) => {
  return localStorage.getItem(key) || "";
};

export const localClear = () => {
  localStorage.removeItem(localKey.USER_INFO);
  localStorage.removeItem(localKey.USER_MENU);
  localStorage.removeItem(localKey.USER_POINTS);
  localStorage.removeItem(localKey.USER_TAG_NAV);
};
