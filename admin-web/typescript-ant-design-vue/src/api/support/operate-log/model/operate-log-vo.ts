export interface OperateLogVo {


    /**
     * 主键
     */
    operateLogId?:number;

    /**
     * 用户id
     */
    operateUserId?:number;

    /**
     * 用户名称
     */
    operateUserName?:string;

    /**
     * 操作模块
     */
    module?:string;

    /**
     * 操作内容
     */
    content?:string;

    /**
     * 请求路径
     */
    url?:string;

    /**
     * 请求方法
     */
    method?:string;

    /**
     * 请求参数
     */
    param?:string;

    /**
     * 请求结果
     */
   successFlag?:boolean;

    /**
     * 失败原因
     */
    failReason?:string;


    /**
     * 创建时间
     */
    createTime?:string;


}
