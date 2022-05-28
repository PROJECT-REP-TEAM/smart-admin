import {PageParamModel} from "/@/api/base-model/page-param-model";

export interface DictKeyQueryForm extends PageParamModel {
    /**
     * 搜索词
     */
    searchWord?: string;

}

