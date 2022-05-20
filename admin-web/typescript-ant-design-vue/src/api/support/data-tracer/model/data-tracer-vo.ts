/*
 * @Description: 
 * @version: 
 * @Author: lidoudou
 * @Date: 2021-08-27 17:52:02
 * @LastEditors: lidoudou
 * @LastEditTime: 2021-08-27 17:52:28
 */
export interface DataTracerVo { 
    /**
     * 单据id
     */
    businessId?: number;
    /**
     * 单据类型:  <br>  export const <br> DATA_TRACER_BUSINESS_TYPE_ENUM = <BR> {<br>&nbsp;&nbsp;CLUE_USER:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:1,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'线索\'<br>&nbsp;&nbsp;}<br>} <br>
     */
    businessType: number;
    /**
     * 单据类型描述
     */
    businessTypeDesc?: string;
    /**
     * 操作时间
     */
    createTime?: string;
    /**
     * 操作内容
     */
    operateContent?: string;
    /**
     * 操作类型
     */
    operateType?: number;
    /**
     * 操作类型描述
     */
    operateTypeDesc?: string;
    /**
     * 操作人
     */
    operatorId?: number;
    /**
     * 操作人名称
     */
    operatorName?: string;
}

