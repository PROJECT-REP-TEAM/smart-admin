package net.lab1024.smartadmin.service.module.support.systemconfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.systemconfig.domain.*;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [  ]
 *
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
@Api(tags = {SwaggerTagConst.Support.SYSTEM_CONFIG})
@RestController
public class SystemConfigController extends SupportBaseController {

    @Autowired
    private SystemConfigService systemConfigService;

    @ApiOperation("分页查询系统配置")
    @PostMapping("/system/config/query")
    public ResponseDTO<PageResultDTO<SystemConfigVO>> querySystemConfigPage(@RequestBody @Valid SystemConfigQueryDTO queryDTO) {
        return systemConfigService.queryConfigPage(queryDTO);
    }

    @ApiOperation("添加配置参数")
    @PostMapping("/system/config/add")
    public ResponseDTO<String> addSystemConfig(@RequestBody @Valid SystemConfigAddDTO configAddDTO) {
        return systemConfigService.add(configAddDTO);
    }

    @ApiOperation("修改配置参数")
    @PostMapping("/system/config/update")
    public ResponseDTO<String> updateSystemConfig(@RequestBody @Valid SystemConfigUpdateDTO updateDTO) {
        return systemConfigService.updateSystemConfig(updateDTO);
    }

    @ApiOperation("查询配置详情")
    @GetMapping("/system/config/queryByKey")
    public ResponseDTO<SystemConfigVO> queryByKey(@RequestParam String configKey) {
        SystemConfigDTO configDTO = systemConfigService.getConfig(configKey);
        SystemConfigVO configVO = SmartBeanUtil.copy(configDTO, SystemConfigVO.class);
        return ResponseDTO.succData(configVO);
    }

}
