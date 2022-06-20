/*
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-20
 * @LastEditors: zhuoda
 */
import antd from 'ant-design-vue/es/locale-provider/zh_CN'
import dayjsCN from 'dayjs/locale/zh-cn'
import global from './zh-CN/global'

import menu from './zh-CN/menu'
import setting from './zh-CN/setting'
import user from './zh-CN/user'
import dashboard from './zh-CN/dashboard'
import form from './zh-CN/form'
import result from './zh-CN/result'
import account from './zh-CN/account'

const components = {
    antLocale: antd,
    dayName: 'zh-cn',
    dayLocale: dayjsCN
}

export default {
    message: '-',

    'layouts.usermenu.dialog.title': '信息',
    'layouts.usermenu.dialog.content': '您确定要注销吗？',
    'layouts.userLayout.title': 'Ant Design 是西湖区最具影响力的 Web 设计规范',
    ...components,
    ...global,
    ...menu,
    ...setting,
    ...user,
    ...dashboard,
    ...form,
    ...result,
    ...account
}
