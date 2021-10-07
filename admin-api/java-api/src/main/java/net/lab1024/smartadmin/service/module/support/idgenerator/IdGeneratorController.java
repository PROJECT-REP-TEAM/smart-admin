package net.lab1024.smartadmin.service.module.support.idgenerator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.common.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.service.module.support.idgenerator.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * id生成器路由
 *
 * @author listen
 * @date 2019/09/26 21:13
 */
@Api(tags = SwaggerTagConst.Support.ID_GENERATOR)
@RestController
public class IdGeneratorController extends SupportBaseController {

    @Autowired
    private IdGeneratorService idGeneratorService;

    @ApiOperation("生成id")
    @GetMapping("/id/generator/{generatorId}")
    public ResponseDTO<String> generate(@PathVariable Integer generatorId) {
        IdGeneratorEnum idGeneratorEnum = SmartBaseEnumUtil.getEnumByValue(generatorId, IdGeneratorEnum.class);
        if (null == idGeneratorEnum) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "IdGenerator，不存在" + generatorId);
        }
        return ResponseDTO.ok(idGeneratorService.generate(idGeneratorEnum));
    }

}
