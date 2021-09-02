import {useSpinStore} from "/@/store/modules/system/spin";

interface ISmartLoading {

    show(): void;

    hide(): void;

}


export const SmartLoading: ISmartLoading = {

    show: () => {
        useSpinStore().show();
    },

    hide: () => {
        useSpinStore().hide();
    }
};