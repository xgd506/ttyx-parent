package hue.xgd.ttyx.product.controller;

import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.product.service.FileUploadService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Author:xgd
 * @Date:2023/8/3 9:32
 * @Description:
 */
@RestController
@RequestMapping("/admin/product")
@CrossOrigin
public class FileUploadController {
    @Resource
    private FileUploadService fileUploadService;

    //文件上传
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) throws Exception{
        return Result.ok(fileUploadService.fileUpload(file));
    }
}
