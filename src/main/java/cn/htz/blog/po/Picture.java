package cn.htz.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 相册实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture {
    /**
     * 主键
     */
    private Long id;
    /**
     * 图片地址
     */
    private String pictureAddress;
    /**
     * 图片名称
     */
    private String pictureName;
    /**
     * 图片描述
     */
    private String pictureDescription;
    /**
     * 图片上传时间
     */
    private Date pictureTime;
}
