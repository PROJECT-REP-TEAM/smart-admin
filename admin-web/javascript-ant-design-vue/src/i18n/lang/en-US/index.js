/*
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-20
 * @LastEditors: zhuoda
 */
import antdEnUS from 'ant-design-vue/es/locale-provider/en_US'
import dayEu from 'dayjs/locale/eu'
import global from './global'

import menu from './menu'
import setting from './setting'
import user from './user'

import dashboard from './dashboard'
import form from './form'
import result from './result'
import account from './account'

const components = {
  antLocale: antdEnUS,
  dayName: 'eu',
  dayLocale: dayEu
}

export default {
  message: '-',

  'layouts.usermenu.dialog.title': 'Message',
  'layouts.usermenu.dialog.content': 'Are you sure you would like to logout?',
  'layouts.userLayout.title': 'Ant Design is the most influential web design specification in Xihu district',
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
