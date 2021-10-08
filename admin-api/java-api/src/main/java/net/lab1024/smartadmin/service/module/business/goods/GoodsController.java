package net.lab1024.smartadmin.service.module.business.goods;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.business.goods.domain.*;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.common.util.SmartEmployeeTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 商品业务 路由
 *
 * @author 胡克
 * @date 2021/08/21 19:10
 */
@Api(tags = SwaggerTagConst.Admin.MANAGER_GOODS)
@RestController
public class GoodsController extends SystemBaseController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation("添加商品 by listen")
    @PostMapping("/goods/add")
    public ResponseDTO<String> add(@RequestBody @Valid GoodsAddForm addForm) {
        EmployeeLoginInfoDTO employee = SmartEmployeeTokenUtil.getRequestEmployee();
        addForm.setUpdateId(employee.getEmployeeId());
        addForm.setUpdateName(employee.getActualName());
        return goodsService.add(addForm);
    }

    @ApiOperation("更新商品 by listen")
    @PostMapping("/goods/update")
    public ResponseDTO<String> update(@RequestBody @Valid GoodsUpdateForm updateForm) {
        EmployeeLoginInfoDTO employee = SmartEmployeeTokenUtil.getRequestEmployee();
        updateForm.setUpdateId(employee.getEmployeeId());
        updateForm.setUpdateName(employee.getActualName());
        return goodsService.update(updateForm);
    }

    @ApiOperation("删除 by listen")
    @PostMapping("/goods/del")
    public ResponseDTO<String> del(@RequestBody @Valid GoodsDelForm delForm) {
        EmployeeLoginInfoDTO employee = SmartEmployeeTokenUtil.getRequestEmployee();
        delForm.setUpdateId(employee.getEmployeeId());
        delForm.setUpdateName(employee.getActualName());
        return goodsService.del(delForm);
    }

    @ApiOperation("分页查询 by listen")
    @PostMapping("/goods/query")
    public ResponseDTO<PageResultDTO<GoodsAdminVO>> query(@RequestBody @Valid GoodsQueryForm queryForm) {
        return goodsService.query(queryForm);
    }
}
