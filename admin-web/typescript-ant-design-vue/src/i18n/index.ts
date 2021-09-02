import {LanguageType} from "/@/types/config";

// i18n 选项
interface I18nSelectOption {
    text: String,
    value: LanguageType
}

// 语言选择数组
export const i18nList: I18nSelectOption[] = [
    {
        text: '简体中文',
        value: 'zh_CN',
    },
    {
        text: 'English',
        value: 'en',
    },
]