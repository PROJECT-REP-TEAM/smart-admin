package net.lab1024.smartadmin.service.module.system.systemcache;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/11 20:07
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_SYSTEM_CACHE})
public class SystemCacheController {

    @Autowired
    private SystemCacheService systemCacheService;

    @ApiOperation(value = "获取所有缓存", notes = "@author 罗伊")
    @GetMapping("/cache/names")
    public ResponseDTO<List<String>> cacheNames() {
        return ResponseDTO.ok(systemCacheService.cacheNames());
    }


    @ApiOperation(value = "移除某个缓存", notes = "@author 罗伊")
    @GetMapping("/cache/remove/{cacheName}")
    public ResponseDTO<String> removeCache(@PathVariable String cacheName) {
        systemCacheService.removeCache(cacheName);
        return ResponseDTO.ok();
    }


    @ApiOperation(value = "获取某个缓存的所有key", notes = "@author 罗伊")
    @GetMapping("/cache/keys/{cacheName}")
    public ResponseDTO<List<String>> cacheKeys(@PathVariable String cacheName) {
        return ResponseDTO.ok(systemCacheService.cacheKey(cacheName));
    }

}
