package net.lab1024.smartadmin.service.module.support.datatracer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerQuery;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:00
 */

@Api(tags = {SwaggerTagConst.Admin.MANAGER_DATA_TRACER})
@RestController
public class DataTracerController extends SystemBaseController {

    @Autowired
    private DataTracerService dataTracerService;

    @ApiOperation("分页查询业务操作日志 - by listen")
    @PostMapping("/data/tracer/log/query")
    public ResponseDTO<PageResult<DataTracerVO>> query(@Valid @RequestBody DataTracerQuery queryForm) {
        return dataTracerService.query(queryForm);
    }
}
