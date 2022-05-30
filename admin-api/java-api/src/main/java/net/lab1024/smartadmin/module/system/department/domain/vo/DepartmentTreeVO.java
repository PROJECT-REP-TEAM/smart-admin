package net.lab1024.smartadmin.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Data
public class DepartmentTreeVO extends DepartmentVO {

    @ApiModelProperty("子部门")
    private List<DepartmentTreeVO> children;

}
