import {defineStore} from "pinia";

export const useSpinStore = defineStore({
    id: "spin",
    state: () => ({
        loading: false
    }),

    actions: {
        hide(): void {
            this.loading = false;
        },
        show(): void {
            this.loading = true;
        }
    }
});
