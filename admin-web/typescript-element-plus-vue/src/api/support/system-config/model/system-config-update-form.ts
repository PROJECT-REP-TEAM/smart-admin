import {SystemConfigAddForm} from "/@/api/support/system-config/model/system-config-add-form";

export interface SystemConfigUpdateForm extends SystemConfigAddForm {

    /**
     * 主键
     */
    systemConfigId?: number;

}
