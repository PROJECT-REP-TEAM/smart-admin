package net.lab1024.smartadmin.service.module.business.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.business.category.domain.form.CategoryAddForm;
import net.lab1024.smartadmin.service.module.business.category.domain.form.CategoryTreeQueryForm;
import net.lab1024.smartadmin.service.module.business.category.domain.form.CategoryUpdateForm;
import net.lab1024.smartadmin.service.module.business.category.domain.vo.CategoryTreeVO;
import net.lab1024.smartadmin.service.module.business.category.domain.vo.CategoryVO;
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
@Api(tags = SwaggerTagConst.Business.MANAGER_CATEGORY)
@RestController
public class CategoryController extends SystemBaseController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("添加类目 by listen")
    @PostMapping("/category/add")
    public ResponseDTO<String> add(@RequestBody @Valid CategoryAddForm addForm) {
        return categoryService.add(addForm);
    }

    @ApiOperation("更新类目 by listen")
    @PostMapping("/category/update")
    public ResponseDTO<String> update(@RequestBody @Valid CategoryUpdateForm updateForm) {
        return categoryService.update(updateForm);
    }

    @ApiOperation("查询类目详情 by listen")
    @GetMapping("/category/{categoryId}")
    public ResponseDTO<CategoryVO> queryDetail(@PathVariable Long categoryId) {
        return categoryService.queryDetail(categoryId);
    }

    @ApiOperation("查询类目层级树 by listen")
    @PostMapping("/category/tree")
    public ResponseDTO<List<CategoryTreeVO>> queryTree(@RequestBody @Valid CategoryTreeQueryForm queryForm) {
        return categoryService.queryTree(queryForm);
    }

    @ApiOperation("删除类目 by listen")
    @GetMapping("/category/del/{categoryId}")
    public ResponseDTO<String> delete(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}
