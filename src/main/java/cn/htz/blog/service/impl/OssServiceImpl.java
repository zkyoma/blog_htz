package cn.htz.blog.service.impl;

import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.service.OssService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.webUrl}")
    private String webUrl;

    @Override
    public String uploadFile(MultipartFile file, String rootPath) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String fileSrc = null;
        try {
            fileSrc = upload(ossClient, file, rootPath, bucketName);
        } catch (Exception e) {
            throw new MyException("用户头像上传失败", ErrorEnum.OOS_UPLOAD_ERROR.getCode());
        } finally {
            if (null != ossClient) {
                ossClient.shutdown();
            }
        }
        return fileSrc;
    }

    @Override
    public List<Object[]> uploadFiles(MultipartFile[] files, String rootPath) {
        OSS ossClient = null;
        List<Object[]> imgUrls = new ArrayList<>();
        int index = 0;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            for (MultipartFile file : files) {
                String imageUrl = upload(ossClient, file, rootPath, bucketName);
                imgUrls.add(new Object[]{index++, imageUrl});
            }
        } catch (Exception e) {
            throw new MyException("博客图片上传失败", ErrorEnum.OOS_UPLOAD_ERROR.getCode());
        } finally {
            if (null != ossClient) {
                ossClient.shutdown();
            }
        }
        return imgUrls;
    }

    private String upload(OSS ossClient, MultipartFile file, String rootPath, String bucketName) throws Exception {
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileName = null;
        // 判断容器是否存在,不存在就创建
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
        // 设置权限(公开读)
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 设置文件路径和名称
        String fileUrl = rootPath + format + "/" + UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename();
        // 上传文件
        PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file.getInputStream()));
        if (result != null) {
            fileName = webUrl + "/" + fileUrl;
        }
        return fileName;
    }

    @Override
    public void deleteFile(String filePath) {
        // https://blog-htz.oss-cn-hangzhou.aliyuncs.com/image/avatar/2020-10-05/77eea5b3021fedb823bbc62642d65ddc.jpeg
        String objectName = filePath.substring(46);
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if (ossClient.doesObjectExist(bucketName, objectName)) {
            ossClient.deleteObject(bucketName, objectName);
        }
        ossClient.shutdown();
    }
}
