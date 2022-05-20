export interface HeartBeatVo {

    /**
     * id
     */
    heartBeatRecordId?: number;

    /**
     * 项目路径
     */
    projectPath?: string;

    /**
     * 服务器ip
     */
    serverIp?: string;

    /**
     * 进程号
     */
    processNo?: number;

    /**
     * 进程开启时间
     */
    processStartTime?: string;

    /**
     * 心跳当前时间
     */
    heartBeatTime?: string;



}
