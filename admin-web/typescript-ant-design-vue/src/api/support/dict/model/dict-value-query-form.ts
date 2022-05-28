import {PageParamModel} from "/@/api/base-model/page-param-model";

export interface DictValueQueryForm extends PageParamModel {
    /**
     * 搜索词
     */
    searchWord?: string;
    /**
     * dictKeyId
     */
    dictKeyId?: number;

}

