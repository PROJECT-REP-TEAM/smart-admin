/**
 * smart-admin-local
 * http://localhost:50110/v2/api-docs?group=Admin
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { DataScopeBatchSetDto } from './data-scope-batch-set-dto';


export interface DataScopeBatchSetRoleDto { 
    /**
     * 设置信息
     */
    batchSetList?: Array<DataScopeBatchSetDto>;
    /**
     * 角色id
     */
    roleId?: number;
}

