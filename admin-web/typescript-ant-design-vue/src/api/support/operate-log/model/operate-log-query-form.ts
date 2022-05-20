import {PageParamModel} from "/@/api/base-model/page-param-model";

export interface OperateLogQueryForm extends PageParamModel{

    /**
     * 开始日期
     */
    startDate?: string;

    /**
     * 结束日期
     */
    endDate?: string;

    /**
     * 用户名称
     */
    userName?: string;

    /**
     * 请求结果
     */
    successFlag?: boolean;

}

