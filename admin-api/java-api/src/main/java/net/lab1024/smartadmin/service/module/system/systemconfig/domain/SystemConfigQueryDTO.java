package net.lab1024.smartadmin.service.module.system.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;
import org.hibernate.validator.constraints.Length;

/**
 * 分页查询 系统配置
 *
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
@Data
public class SystemConfigQueryDTO extends PageBaseDTO {

    @ApiModelProperty("参数KEY")
    @Length(max = 50, message = "参数Key最多50字符")
    private String configKey;
}
