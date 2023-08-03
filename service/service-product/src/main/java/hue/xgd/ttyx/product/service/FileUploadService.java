package hue.xgd.ttyx.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author:xgd
 * @Date:2023/8/3 9:32
 * @Description:
 */
public interface FileUploadService {
    String fileUpload(MultipartFile file) throws Exception;
}
