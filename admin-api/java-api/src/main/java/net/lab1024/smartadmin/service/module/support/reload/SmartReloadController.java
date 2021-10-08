package net.lab1024.smartadmin.service.module.support.reload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadItemUpdateDTO;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadItemVO;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/***
 *
 * @author 开云
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.SMART_RELOAD})
public class SmartReloadController extends SupportBaseController {

    @Autowired
    private SmartReloadService smartReloadService;

    @ApiOperation(value = "查询reload列表 by 开云")
    @GetMapping("/reload/query")
    public ResponseDTO<List<ReloadItemVO>> query() {
        return smartReloadService.query();
    }

    @ApiOperation(value = "获取reload result by 开云")
    @GetMapping("/smartReload/result/{tag}")
    public ResponseDTO<List<ReloadResultVO>> queryReloadResult(@PathVariable("tag") String tag) {
        return smartReloadService.queryReloadItemResult(tag);
    }

    @ApiOperation(value = "通过tag更新标识 by 开云")
    @PostMapping("/smartReload/update")
    public ResponseDTO<String> updateByTag(@RequestBody @Valid ReloadItemUpdateDTO updateDTO) {
        return smartReloadService.updateByTag(updateDTO);
    }
}
