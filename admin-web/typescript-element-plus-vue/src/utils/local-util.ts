/**
 * localStorage存取
 * @param key
 * @param value
 */
export const localSave = (key: string, value: string) => {
  localStorage.setItem(key, value);
};

export const localRead = (key: string): string | null => {
  return localStorage.getItem(key) || '';
};

export const localClear = (): void => {
  localStorage.clear();
};
