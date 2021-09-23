package net.lab1024.smartadmin.service.module.support.file.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.codeconst.FileResponseCodeConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.file.domain.dto.FileDownloadDTO;
import net.lab1024.smartadmin.service.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.smartadmin.service.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.service.module.support.systemconfig.SystemConfigService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */

@Slf4j
@ConditionalOnProperty(prefix = "file.storage", name = {"mode"}, havingValue = "local")
public class FileStorageLocalServiceImpl implements IFileStorageService {

    @Value("${file.storage.local.path}")
    private String localPath;
    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public ResponseDTO<FileUploadVO> fileUpload(MultipartFile multipartFile, String path) {
        if (null == multipartFile) {
            return ResponseDTO.wrap(FileResponseCodeConst.FILE_EMPTY);
        }
        String filePath = localPath + path;
        File directory = new File(filePath);
        if (!directory.exists()) {
            // 目录不存在，新建
            directory.mkdirs();
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        FileUploadVO fileUploadVO = new FileUploadVO();
        //原文件名
        String originalFileName = multipartFile.getOriginalFilename();
        //新文件名
        String newFileName = this.generateFileName(originalFileName);
        //生成文件key
        String fileKey = path + newFileName;
        //创建文件
        File fileTemp = new File(new File(filePath + newFileName).getAbsolutePath());
        try {
            multipartFile.transferTo(fileTemp);
            fileUploadVO.setFileUrl(this.generateFileUrl(fileKey));
            fileUploadVO.setFileName(newFileName);
            fileUploadVO.setFileKey(fileKey);
            fileUploadVO.setFileSize(multipartFile.getSize());
            fileUploadVO.setFileType(FileUtils.getExtension(originalFileName));
        } catch (IOException e) {
            if (fileTemp.exists() && fileTemp.isFile()) {
                fileTemp.delete();
            }
            log.error("", e);
            return ResponseDTO.wrap(FileResponseCodeConst.UPLOAD_ERROR);
        }
        return ResponseDTO.succData(fileUploadVO);
    }

    /**
     * 生成fileUrl地址
     *
     * @param fileKey
     * @return
     */
    public String generateFileUrl(String fileKey) {
        String configValue = systemConfigService.getConfigValue(SystemConfigKeyEnum.LOCAL_UPLOAD_URL_PREFIX);
        String fileUrl = configValue + fileKey;
        return fileUrl;
    }

    /**
     * 获取文件Url
     * @param fileKey
     * @return
     */
    @Override
    public ResponseDTO<String> getFileUrl(String fileKey) {
        String fileUrl = this.generateFileUrl(fileKey);
        return ResponseDTO.succData(fileUrl);
    }

    /**
     * 文件下载
     * @param fileKey
     * @return
     */
    @Override
    public ResponseDTO<FileDownloadDTO> fileDownload(String fileKey) {
        String filePath = localPath + fileKey;
        File localFile = new File(filePath);
        InputStream in = null;
        try {
            in = new FileInputStream(localFile);
            // 输入流转换为字节流
            byte[] buffer = FileCopyUtils.copyToByteArray(in);
            FileDownloadDTO fileDownloadDTO = new FileDownloadDTO();
            fileDownloadDTO.setData(buffer);
            return ResponseDTO.succData(fileDownloadDTO);
        } catch (IOException e) {
            log.error("文件下载-发生异常：", e);
            return ResponseDTO.wrap(FileResponseCodeConst.DOWNLOAD_ERROR);
        } finally {
            try {
                // 关闭输入流
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("文件下载-发生异常：", e);
            }
        }
    }

    @Override
    public ResponseDTO<String> delete(String fileKey) {
        String filePath = localPath + fileKey;
        File localFile = new File(filePath);
        try {
            FileUtils.forceDelete(localFile);
        } catch (IOException e) {
            log.error("删除本地文件失败：{}", e);
        }
        return ResponseDTO.succ();
    }
}
