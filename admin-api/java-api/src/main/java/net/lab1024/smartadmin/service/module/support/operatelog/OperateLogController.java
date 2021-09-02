package net.lab1024.smartadmin.service.module.support.operatelog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogDTO;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [  ]
 *
 * @author 罗伊
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.USER_OPERATE_LOG})
public class OperateLogController extends SupportBaseController {

    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation(value = "分页查询 @author 罗伊")
    @PostMapping("/userOperateLog/page/query")
    public ResponseDTO<PageResultDTO<OperateLogDTO>> queryByPage(@RequestBody OperateLogQueryDTO queryDTO) {
        return operateLogService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "删除 @author 罗伊")
    @GetMapping("/userOperateLog/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long id) {
        return operateLogService.delete(id);
    }


    @ApiOperation(value = "详情 @author 罗伊")
    @GetMapping("/userOperateLog/detail/{id}")
    public ResponseDTO<OperateLogDTO> detail(@PathVariable("id") Long id) {
        return operateLogService.detail(id);
    }
}
