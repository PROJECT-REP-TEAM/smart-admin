package net.lab1024.smartadmin.service.module.business.notice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.domain.PageParamForm;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.*;
import net.lab1024.smartadmin.service.module.business.notice.domain.vo.NoticeDetailVO;
import net.lab1024.smartadmin.service.module.business.notice.domain.vo.NoticeVO;
import net.lab1024.smartadmin.service.common.util.SmartEmployeeTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [ 通知公告 ]
 *
 * @author 罗伊
 * @date
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_NOTICE})
public class NoticeController extends SystemBaseController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "分页查询全部消息", notes = "@author 罗伊")
    @PostMapping("/notice/page/query")
    public ResponseDTO<PageResultDTO<NoticeVO>> queryByPage(@RequestBody NoticeQueryForm queryForm) {
        return noticeService.queryByPage(queryForm);
    }

    @ApiOperation(value = "获取已收取的所有消息", notes = "@author 罗伊")
    @PostMapping("/notice/receive/page/query")
    public ResponseDTO<PageResultDTO<NoticeReceiveForm>> queryReceiveByPage(@RequestBody NoticeReceiveQueryForm queryForm) {
        queryForm.setEmployeeId(SmartEmployeeTokenUtil.getRequestEmployeeId());
        return noticeService.queryReceiveByPage(queryForm);
    }

    @ApiOperation(value = "分页查询未读消息", notes = "@author 罗伊")
    @PostMapping("/notice/unread/page/query")
    public ResponseDTO<PageResultDTO<NoticeVO>> queryUnreadByPage(@RequestBody PageParamForm queryForm) {
        return noticeService.queryUnreadByPage(queryForm, SmartEmployeeTokenUtil.getRequestEmployeeId());
    }

    @ApiOperation(value = "添加", notes = "@author 罗伊")
    @PostMapping("/notice/add")
    public ResponseDTO<String> add(@RequestBody @Valid NoticeAddForm addForm) {
        addForm.setCreateId(SmartEmployeeTokenUtil.getRequestEmployeeId());
        return noticeService.add(addForm);
    }

    @ApiOperation(value = "修改", notes = "@author 罗伊")
    @PostMapping("/notice/update")
    public ResponseDTO<String> update(@RequestBody @Valid NoticeUpdateForm updateForm) {
        return noticeService.update(updateForm);
    }

    @ApiOperation(value = "删除", notes = "@author 罗伊")
    @GetMapping("/notice/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long id) {
        return noticeService.delete(id);
    }

    @ApiOperation(value = "详情", notes = "@author 罗伊")
    @GetMapping("/notice/detail/{id}")
    public ResponseDTO<NoticeDetailVO> detail(@PathVariable("id") Long id) {
        return noticeService.detail(id);
    }

    @ApiOperation(value = "发送", notes = "@author 罗伊")
    @GetMapping("/notice/send/{id}")
    public ResponseDTO<NoticeDetailVO> send(@PathVariable("id") Long id) {
        return noticeService.send(id, SmartEmployeeTokenUtil.getRequestEmployeeId());
    }

    @ApiOperation(value = "读取消息", notes = "@author 罗伊")
    @GetMapping("/notice/read/{id}")
    public ResponseDTO<NoticeDetailVO> read(@PathVariable("id") Long id) {
        return noticeService.read(id, SmartEmployeeTokenUtil.getRequestEmployeeId());
    }
}
