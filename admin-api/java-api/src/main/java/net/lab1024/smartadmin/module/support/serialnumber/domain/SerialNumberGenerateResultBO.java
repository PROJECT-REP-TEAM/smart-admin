package net.lab1024.smartadmin.module.support.serialnumber.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生成结果
 *
 * @author zhuoda
 * @Date 2022-03-02
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerialNumberGenerateResultBO {

    /**
     * 序号id
     */
    private Integer serialNumberId;

    /**
     *  是否重置的初始值
     */
    private Boolean isReset;

    /**
     * 上次生成的数字
     */
    private Long lastNumber;

    /**
     * 上次生成的时间
     */
    private LocalDateTime lastTime;

    /**
     * 生成的 number  集合
     */
    private List<Long> numberList;


}
