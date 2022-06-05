import {defineStore} from 'pinia';
import {ProjectConfig} from "/@/types/config";
import {projectDefaultConfig} from "/@/config/project-config";


export const useProjectConfigStore = defineStore({
    id: 'projectConfig',
    state: (): ProjectConfig => ({
        ...projectDefaultConfig
    }),
    getters: {},
    actions: {},
});
