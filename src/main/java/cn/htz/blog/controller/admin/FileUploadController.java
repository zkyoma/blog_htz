package cn.htz.blog.controller.admin;

import cn.htz.blog.common.R;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 */
@Slf4j
@RestController
@RequestMapping("/admin/upload")
public class FileUploadController {

    private static final String AVATAR_PATH = "image/avatar/";
    private static final String BLOG_IMAGE_PATH = "image/blog/";
    private static final String CATEGORY_ICON_PATH = "image/category/";

    @Autowired
    private OssService ossService;

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @PostMapping("/avatar")
    public R uploadAvatar(@RequestParam(value = "avatar", required = false) MultipartFile file) {
        if (file == null) {
            return R.error(ErrorEnum.OOS_UPLOAD_ERROR).put("data", "文件为空！");
        }
        String fileName = ossService.uploadFile(file, AVATAR_PATH);
        return R.ok().put("data", fileName);
    }

    @GetMapping("/file/delete")
    public R deleteAvatar(@RequestParam("filePath") String filePath) {
        ossService.deleteFile(filePath);
        return R.ok();
    }

    @PostMapping("/blogImage")
    public R uploadBlogImage(@RequestParam(value = "blogImage", required = false) MultipartFile file) {
        if (file == null) {
            return R.error(ErrorEnum.OOS_UPLOAD_ERROR).put("data", "文件为空！");

        }
        String fileName = ossService.uploadFile(file, BLOG_IMAGE_PATH);
        return R.ok().put("data", fileName);
    }

    @PostMapping("/categoryIcon")
    public R uploadCategoryIcon(@RequestParam(value = "categoryIcon", required = false) MultipartFile file) {
        if (file == null) {
            return R.error(ErrorEnum.OOS_UPLOAD_ERROR).put("data", "文件为空！");
        }
        String fileName = ossService.uploadFile(file, CATEGORY_ICON_PATH);
        return R.ok().put("data", fileName);
    }
}
