package net.lab1024.smartadmin.service.module.support.repeatsubmit;

import lombok.Data;

/**
 * 
 *  重复提交的ticket
 * 
 * @author zhuoda
 */
@Data
public class RepeatSubmitTicket {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 扩展信息
     */
    private String extData;

}
