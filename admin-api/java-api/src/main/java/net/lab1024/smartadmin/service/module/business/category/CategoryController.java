package net.lab1024.smartadmin.service.module.business.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.business.category.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类目 路由
 *
 * @author 胡克
 * @date 2021/1/21 9:10
 */
@Api(tags = SwaggerTagConst.Admin.MANAGER_CATEGORY)
@RestController
public class CategoryController extends AdminBaseController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("添加类目 by listen")
    @PostMapping("/category/add")
    public ResponseDTO<String> add(@RequestBody @Valid CategoryAddDTO addDTO) {
        return categoryService.add(addDTO);
    }

    @ApiOperation("更新类目 by listen")
    @PostMapping("/category/update")
    public ResponseDTO<String> update(@RequestBody @Valid CategoryUpdateDTO updateDTO) {
        return categoryService.update(updateDTO);
    }

    @ApiOperation("查询类目详情 by listen")
    @GetMapping("/category/{categoryId}")
    public ResponseDTO<CategoryVO> queryDetail(@PathVariable Long categoryId) {
        return categoryService.queryDetail(categoryId);
    }

    @ApiOperation("查询类目层级树 by listen")
    @PostMapping("/category/tree")
    public ResponseDTO<List<CategoryTreeVO>> queryTree(@RequestBody @Valid CategoryTreeQueryDTO queryDTO) {
        return categoryService.queryTree(queryDTO);
    }

    @ApiOperation("删除类目 by listen")
    @GetMapping("/category/del/{categoryId}")
    public ResponseDTO<String> delete(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}
