package net.lab1024.smartadmin.service.common.codeconst;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
public class FileResponseCodeConst extends ResponseCodeConst {

    /**
     * 4001 -4999
     */
    public static final FileResponseCodeConst FILE_EMPTY = new FileResponseCodeConst(4001, "上传文件不能为空");

    public static final FileResponseCodeConst FILE_SIZE_ERROR = new FileResponseCodeConst(4002, "上传文件超过%s，请重新上传！");

    public static final FileResponseCodeConst UNKNOWN_FILE_TYPE = new FileResponseCodeConst(4003, "未知的文件类型！");

    public static final FileResponseCodeConst LOCAL_UPDATE_PREFIX_ERROR = new FileResponseCodeConst(4004, "文件本地上传缺少URL前缀配置[local_upload_url_prefix]");

    public static final FileResponseCodeConst UPLOAD_ERROR = new FileResponseCodeConst(4005, "上传失败");

    public static final FileResponseCodeConst URL_ERROR = new FileResponseCodeConst(4006, "获取URL失败");

    public static final FileResponseCodeConst FILE_MODULE_ERROR = new FileResponseCodeConst(4007, "文件目录类型错误");

    public static final FileResponseCodeConst FILE_NOT_EXIST = new FileResponseCodeConst(4008, "文件不存在");

    public static final FileResponseCodeConst DOWNLOAD_ERROR = new FileResponseCodeConst(4009, "文件下载失败");

    public static final FileResponseCodeConst VOD_SERVICE_ERROR = new FileResponseCodeConst(4010, "VOD服务错误：");

    public static final FileResponseCodeConst VOD_FILE_ERROR = new FileResponseCodeConst(4011, "请上传正确的音/视频格式");

    public static final FileResponseCodeConst VOD_FILE_NOT_EXIST = new FileResponseCodeConst(4012, "视频文件不存在");

    public static final FileResponseCodeConst FILE_NAME_ERROR = new FileResponseCodeConst(4013, "文件名称必须1-100个字符");

    public static final FileResponseCodeConst VOD_TOKEN_ERROR = new FileResponseCodeConst(4014, "视频文件TOKEN失效");

    public static final FileResponseCodeConst VOD_CIPHER_TEXT_ERROR = new FileResponseCodeConst(4015, "视频文件CIPHER_TEXT无效");

    public FileResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
