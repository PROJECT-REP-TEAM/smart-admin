package net.lab1024.smartadmin.service.module.support.beancache.key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/1/23 17:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheKeyBuilder {

    private String cacheModule;

    private String group;

    private String businessId;
}
