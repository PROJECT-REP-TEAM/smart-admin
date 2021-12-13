package net.lab1024.smartadmin.service.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Data
public class DepartmentVO {

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门简称")
    private String shortName;

    @ApiModelProperty("部门负责人姓名")
    private String managerName;

    @ApiModelProperty("部门负责人id")
    private Long managerId;

    @ApiModelProperty("父级部门id")
    private Long parentId;

    @ApiModelProperty("同级上一个元素id")
    private Long preId;

    @ApiModelProperty("同级下一个元素id")
    private Long nextId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("父级部门名称")
    private String parentName;

}
