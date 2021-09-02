package net.lab1024.smartadmin.service.module.support.file;

import net.lab1024.smartadmin.service.common.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.controller.SupportBaseController;
import net.lab1024.smartadmin.service.module.support.file.domain.FileFolderTypeEnum;
import net.lab1024.smartadmin.service.module.support.file.domain.dto.*;
import net.lab1024.smartadmin.service.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.smartadmin.service.module.support.file.domain.vo.FileVO;
import net.lab1024.smartadmin.service.module.support.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @Description: 文件服务
 * @Author: 1024lab
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.FILE})
public class FileController extends SupportBaseController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传 by listen", notes = FileFolderTypeEnum.INFO)
    @PostMapping("/file/upload/{folder}")
    public ResponseDTO<FileUploadVO> upload(MultipartFile file, @PathVariable Integer folder) {
        return fileService.fileUpload(file, folder, 0L, null);
    }

    @ApiOperation(value = "文件上传，通过url上传 by listen", notes = FileFolderTypeEnum.INFO)
    @PostMapping("/file/upload/url")
    public ResponseDTO<FileUploadVO> uploadByUrl(@RequestBody @Valid FileUrlUploadDTO urlUploadDTO) {
        return fileService.fileUpload(urlUploadDTO);
    }

    @ApiOperation("获取文件URL：根据fileKey by listen")
    @GetMapping("/file/url")
    public ResponseDTO<String> getUrl(@RequestParam String fileKey) {
        return fileService.getFileUrl(fileKey);
    }

    @ApiOperation("批量获取文件url：根据fileKey by listen")
    @PostMapping("/file/url/query")
    public ResponseDTO<List<FileUrlResultDTO>> getBatchFileUrl(@RequestBody @Valid FileUrlQueryDTO queryDTO) {
        return fileService.getBatchFileUrl(queryDTO);
    }

    @ApiOperation(value = "系统文件查询 by listen")
    @PostMapping("/file/query")
    public ResponseDTO<PageResultDTO<FileVO>> queryListByPage(@RequestBody @Valid FileQueryDTO queryDTO) {
        return fileService.queryListByPage(queryDTO);
    }

    @ApiOperation(value = "下载文件流（根据fileId） by listen")
    @GetMapping("/file/downLoad/{id}")
    public ResponseEntity<Object> downLoadById(@PathVariable Long id, HttpServletRequest request) throws IOException {
        String ua = request.getHeader("User-Agent");
        return fileService.downLoadById(id, ua);
    }

    @ApiOperation(value = "下载文件流（根据fileKey） by listen")
    @GetMapping("/file/downLoad")
    public ResponseEntity<Object> downLoad(String fileKey, HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return fileService.downloadByFileKey(fileKey, ua);
    }

    @ApiOperation("删除文件（根据fileKey） by listen")
    @GetMapping("/file/delete")
    public ResponseDTO<String> deleteByFileKey(@RequestParam String fileKey) {
        return fileService.deleteByFileKey(fileKey);
    }

    @ApiOperation("根据文件服务和key 查询文件元数据 by listen")
    @GetMapping("/file/query/metadata")
    public ResponseDTO<FileMetadataDTO> queryFileMetadata(@RequestParam String fileKey) {
        return fileService.queryFileMetadata(fileKey);
    }
}
