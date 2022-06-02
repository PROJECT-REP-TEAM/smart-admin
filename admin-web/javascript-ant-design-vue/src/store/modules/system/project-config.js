/*
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 */
import { defineStore } from 'pinia';
import { projectDefaultConfig } from "/@/config/project-config";


export const useProjectConfigStore = defineStore({
    id: 'projectConfig',
    state: () => ({
        ...projectDefaultConfig
    }),
    getters: {},
    actions: {},
});
