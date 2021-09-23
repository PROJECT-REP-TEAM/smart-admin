package net.lab1024.smartadmin.service.module.support.heartbeat.core;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/9/23 19:39
 */
@Data
public class HeartBeatServer {

    /**
     * 项目路径
     */
    private String projectPath;
    /**
     * 服务器ip（多网卡）
     */
    private List<String> serverIps;
    /**
     * 进程号
     */
    private Integer processNo;
    /**
     * 进程开启时间
     */
    private Date processStartTime;
}
