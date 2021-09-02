package net.lab1024.smartadmin.service.module.support.redismq;

import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/5/10 11:04
 */
@Data
public class RedisMsgDTO {

    /**
     * @see RedisMsgTypeEnum
     */
    private Integer msgType;

    private String jsonData;

    public RedisMsgDTO(Integer msgType, String jsonData) {
        this.msgType = msgType;
        this.jsonData = jsonData;
    }
}
