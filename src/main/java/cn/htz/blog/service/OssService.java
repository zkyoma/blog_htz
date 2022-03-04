package cn.htz.blog.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OssService {
    /**
     * 上传文件
     * @param file
     * @param rootPath
     * @return
     */
    String uploadFile(MultipartFile file, String rootPath);

    /**
     * 删除文件
     * @param filePath
     */
    void deleteFile(String filePath);

    /**
     * 上传多个文件，返回文件路径的List<Object[]>集合 [[pos, url], [pos, url]...]
     * @param files
     * @param blogImagePath
     * @return
     */
    List<Object[]> uploadFiles(MultipartFile[] files, String blogImagePath);
}
