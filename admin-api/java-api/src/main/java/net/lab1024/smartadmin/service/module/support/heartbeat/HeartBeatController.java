package net.lab1024.smartadmin.service.module.support.heartbeat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.common.domain.PageParam;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.support.heartbeat.domain.HeartBeatRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 
 * [  ]
 * 
 * @author yandanyang
 * @date
 */
@Api(tags = {SwaggerTagConst.Support.SUPPORT_HEART_BEAT})
@RestController
public class HeartBeatController extends SupportBaseController {

    @Autowired
    private HeartBeatService heartBeatService;

    @PostMapping("/heartBeat/query")
    @ApiOperation("查询心跳记录 @author 卓大")
    public ResponseDTO<PageResult<HeartBeatRecordVO>> query(@RequestBody @Valid PageParam pageParam) {
        return heartBeatService.pageQuery(pageParam);
    }

}
