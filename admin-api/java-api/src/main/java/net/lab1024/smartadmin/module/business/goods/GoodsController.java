package net.lab1024.smartadmin.module.business.goods;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.common.controller.SystemBaseController;
import net.lab1024.smartadmin.common.domain.PageResult;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.common.util.SmartRequestUtil;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.business.goods.domain.form.GoodsAddForm;
import net.lab1024.smartadmin.module.business.goods.domain.form.GoodsDeleteForm;
import net.lab1024.smartadmin.module.business.goods.domain.form.GoodsQueryForm;
import net.lab1024.smartadmin.module.business.goods.domain.form.GoodsUpdateForm;
import net.lab1024.smartadmin.module.business.goods.domain.vo.GoodsVO;
import net.lab1024.smartadmin.module.system.login.domain.RequestEmployee;
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
@Api(tags = SwaggerTagConst.Business.MANAGER_GOODS)
@RestController
public class GoodsController extends SystemBaseController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation("添加商品 by 胡克")
    @PostMapping("/goods/add")
    public ResponseDTO<String> add(@RequestBody @Valid GoodsAddForm addForm) {
        RequestEmployee employee = SmartRequestUtil.getRequestEmployee();
        addForm.setUpdateId(employee.getEmployeeId());
        addForm.setUpdateName(employee.getActualName());
        return goodsService.add(addForm);
    }

    @ApiOperation("更新商品 by 胡克")
    @PostMapping("/goods/update")
    public ResponseDTO<String> update(@RequestBody @Valid GoodsUpdateForm updateForm) {
        RequestEmployee employee = SmartRequestUtil.getRequestEmployee();
        updateForm.setUpdateId(employee.getEmployeeId());
        updateForm.setUpdateName(employee.getActualName());
        return goodsService.update(updateForm);
    }

    @ApiOperation("删除 by 胡克")
    @PostMapping("/goods/del")
    public ResponseDTO<String> del(@RequestBody @Valid GoodsDeleteForm delForm) {
        RequestEmployee employee = SmartRequestUtil.getRequestEmployee();
        delForm.setUpdateId(employee.getEmployeeId());
        delForm.setUpdateName(employee.getActualName());
        return goodsService.del(delForm);
    }

    @ApiOperation("分页查询 by 胡克")
    @PostMapping("/goods/query")
    public ResponseDTO<PageResult<GoodsVO>> query(@RequestBody @Valid GoodsQueryForm queryForm) {
        return goodsService.query(queryForm);
    }
}
