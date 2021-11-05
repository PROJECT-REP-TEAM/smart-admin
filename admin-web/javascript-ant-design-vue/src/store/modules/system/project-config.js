import {defineStore} from 'pinia';
import {projectDefaultConfig} from "@/config/project-config";


export const useProjectConfigStore = defineStore({
    id: 'projectConfig',
    state: () => ({
        ...projectDefaultConfig
    }),
    getters: {},
    actions: {},
});
