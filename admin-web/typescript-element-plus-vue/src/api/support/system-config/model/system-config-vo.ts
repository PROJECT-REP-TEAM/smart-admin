export interface SystemConfigVo {

    /**
     * 主键
     */
    systemConfigId?: number;

    /**
     * 参数key
     */
    configKey?: string;

    /**
     * 参数的值
     */
    configValue?: string;

    /**
     * 参数名称
     */
    configName?: string;

    /**
     * 备注
     */
    remark?: string;

    /**
     * 上次修改时间
     */
    updateTime?: string;

    /**
     * 创建时间
     */
    createTime?: string;


}
