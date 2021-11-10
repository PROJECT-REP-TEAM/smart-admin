package net.lab1024.smartadmin.service.module.support.operatelog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogDTO;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * [  ]
 *
 * @author 罗伊
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.SUPPORT_OPERATE_LOG})
public class OperateLogController extends SupportBaseController {

    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation(value = "分页查询 @author 罗伊")
    @PostMapping("/userOperateLog/page/query")
    public ResponseDTO<PageResult<OperateLogDTO>> queryByPage(@RequestBody OperateLogQueryForm queryForm) {
        return operateLogService.queryByPage(queryForm);
    }

}
