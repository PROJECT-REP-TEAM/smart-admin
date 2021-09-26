package net.lab1024.smartadmin.service.module.support.file.service;

import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.file.domain.dto.FileDownloadDTO;
import net.lab1024.smartadmin.service.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.smartadmin.service.util.date.SmartDateFormatterEnum;
import net.lab1024.smartadmin.service.util.date.SmartLocalDateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
public interface IFileStorageService {

    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    ResponseDTO<FileUploadVO> fileUpload(MultipartFile file, String path);

    /**
     * 获取文件url
     *
     * @param fileKey
     * @return
     */
    ResponseDTO<String> getFileUrl(String fileKey);

    /**
     * 流式下载（名称为原文件）
     * @param key
     * @return
     */
    ResponseDTO<FileDownloadDTO> fileDownload(String key);


    /**
     * 单个删除文件
     * @param fileKey
     * @return
     */
   ResponseDTO<String> delete(String fileKey);


    /**
     * 缓存过期秒数
     * @return
     */
   default Long cacheExpireSecond(){
       return 3600L;
   }


    /**
     * 不带后缀名的文件名
     *
     * @param file
     * @return
     */
    default String getNameWithoutExtension(String file) {
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * 判断指定目录的文件是否存在
     * @param filePath
     * @return
     */
    default boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 验证文件是否存在，如果不存在则抛出异常
     *
     * @param filePath
     * @throws IOException
     */
    default void isFileExistThrowException(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
    }

    /**
     * 文件流读取
     * @param file
     * @param charset
     * @return
     * @throws FileNotFoundException
     */
    default BufferedReader newBufferedReader(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    /**
     * 文件写
     * @param file
     * @param charset
     * @return
     * @throws FileNotFoundException
     */
    default BufferedWriter newBufferedWriter(File file, Charset charset) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    /**
     * 创建文件所在目录
     * @param file
     * @return
     * @throws IOException
     */
    default boolean createParentDirs(File file) throws IOException {
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return false;
        }
        return parent.mkdirs();
    }

    default boolean createNotExistParentDirFile(File file) throws IOException {
        boolean createParentDirsRes = createParentDirs(file);
        if (!createParentDirsRes) {
            throw new IOException("cannot create parent Directory of " + file.getName());
        }
        return file.createNewFile();
    }

    /**
     * 生成文件名字
     * 当前年月日时分秒 +32位 uuid + 文件格式后缀
     *
     * @param originalFileName
     * @return String
     */
    default String generateFileName(String originalFileName) {
        String time = SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMDHMS);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileType = FileUtils.getExtension(originalFileName);
        return time + uuid + "." + fileType;
    }

    /**
     * 根据文件类型 生成文件名
     * @param fileType
     * @return
     */
    default String generateFileNameByType(String fileType) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHH"));
        return uuid + time + "." + fileType;
    }

    /**
     * 获取文件类型
     *
     * @param originalFileName
     * @return
     */
    default String getFileType(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件格式
     *
     * @param fileName
     * @return 返回内容：png、mp4 等
     */
    default String getFormat(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        return fileName.substring(index + 1);
    }

    /**
     * 获取文件类型
     *
     * @param fileExt
     * @return
     */
    default String getContentType(String fileExt) {
        // 文件的后缀名
        if ("bmp".equalsIgnoreCase(fileExt)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExt)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExt) || "jpg".equalsIgnoreCase(fileExt)) {
            return "image/jpeg";
        }
        if ("png".equalsIgnoreCase(fileExt)) {
            return "image/png";
        }
        if ("html".equalsIgnoreCase(fileExt)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExt)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExt)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
            return "application/msword";
        }
        if ("pdf".equalsIgnoreCase(fileExt)) {
            return "application/pdf";
        }
        if ("xml".equalsIgnoreCase(fileExt)) {
            return "text/xml";
        }
        return "";
    }

    /**
     * 获取文件格式 根据 content-type
     *
     * @param contentType
     * @return
     */
    default String getFileTypeByContentType(String contentType) {
        // 文件的后缀名
        if ("image/bmp".equalsIgnoreCase(contentType)) {
            return "bmp";
        }
        if ("image/gif".equalsIgnoreCase(contentType)) {
            return "gif";
        }
        if ("image/jpeg".equalsIgnoreCase(contentType) || "image/jpg".equalsIgnoreCase(contentType)) {
            return "jpg";
        }
        if ("image/png".equalsIgnoreCase(contentType)) {
            return "png";
        }
        if ("text/html".equalsIgnoreCase(contentType)) {
            return "html";
        }
        if ("text/plain".equalsIgnoreCase(contentType)) {
            return "txt";
        }
        if ("application/vnd.visio".equalsIgnoreCase(contentType)) {
            return "vsd";
        }
        if ("application/vnd.ms-powerpoint".equalsIgnoreCase(contentType)) {
            return "pptx";
        }
        if ("application/msword".equalsIgnoreCase(contentType)) {
            return "docx";
        }
        if ("application/pdf".equalsIgnoreCase(contentType)) {
            return "pdf";
        }
        if ("text/xml".equalsIgnoreCase(contentType)) {
            return "xml";
        }
        return "";
    }

    /**
     * 根据不同的浏览器 返回对应编码的文件名称
     *
     * @param fileName
     * @param userAgent
     * @return
     */
    default String getDownloadFileNameByUA(String fileName, String userAgent) {
        try {
            if (userAgent.toLowerCase().indexOf("firefox") > 0) {
                // firefox浏览器
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else if (userAgent.toUpperCase().indexOf("MSIE") > 0) {
                // IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (userAgent.toUpperCase().indexOf("EDGE") > 0) {
                // WIN10浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (userAgent.toUpperCase().indexOf("CHROME") > 0) {
                // 谷歌
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else {
                //万能乱码问题解决
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return fileName;
    }


}
