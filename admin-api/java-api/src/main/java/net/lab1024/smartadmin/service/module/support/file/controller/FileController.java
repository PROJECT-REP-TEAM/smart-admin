package net.lab1024.smartadmin.service.module.support.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.support.file.constant.FileFolderTypeEnum;
import net.lab1024.smartadmin.service.module.support.file.domain.form.FileQueryForm;
import net.lab1024.smartadmin.service.module.support.file.domain.form.FileUrlUploadForm;
import net.lab1024.smartadmin.service.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.smartadmin.service.module.support.file.domain.vo.FileVO;
import net.lab1024.smartadmin.service.module.support.file.service.FileService;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Description: 文件服务
 * @Author: 1024lab
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.SUPPORT_FILE})
public class FileController extends SupportBaseController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传 by listen", notes = FileFolderTypeEnum.INFO)
    @PostMapping("/file/upload/{folder}")
    public ResponseDTO<FileUploadVO> upload(MultipartFile file, @PathVariable Integer folder) {
        return fileService.fileUpload(file, folder, null, null);
    }

    @ApiOperation(value = "文件上传，通过url上传 by listen", notes = FileFolderTypeEnum.INFO)
    @PostMapping("/file/upload/url")
    public ResponseDTO<FileUploadVO> uploadByUrl(@RequestBody @Valid FileUrlUploadForm uploadForm) {
        return fileService.fileUpload(uploadForm);
    }

    @ApiOperation("获取文件URL：根据fileKey by listen")
    @GetMapping("/file/url")
    public ResponseDTO<String> getUrl(@RequestParam String fileKey) {
        return fileService.getFileUrl(fileKey);
    }

    @ApiOperation(value = "文件分页查询 by listen")
    @PostMapping("/file/query")
    public ResponseDTO<PageResult<FileVO>> queryListByPage(@RequestBody @Valid FileQueryForm queryForm) {
        return fileService.queryListByPage(queryForm);
    }

    @ApiOperation(value = "下载文件流（根据fileKey） by listen")
    @GetMapping("/file/downLoad")
    public ResponseEntity<Object> downLoad(@RequestParam String fileKey, HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return fileService.downloadByFileKey(fileKey, ua);
    }

    @RepeatSubmit(1000)
    @ApiOperation("删除文件（根据fileKey） by listen")
    @GetMapping("/file/delete")
    public ResponseDTO<String> deleteByFileKey(@RequestParam String fileKey) {
        return fileService.deleteByFileKey(fileKey);
    }
}
