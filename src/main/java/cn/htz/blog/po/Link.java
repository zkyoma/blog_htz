package cn.htz.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 友链地址
     */
    private String linkUrl;
    /**
     * 友链名称
     */
    private String linkName;
    /**
     * 图片链接
     */
    private String pictureUrl;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 友链类型，友链类别 0-友链 1-推荐 2-个人网站
     */
    private Byte type;
    /**
     * 是否删除 1-未删除 0-已删除
     */
    private Boolean showStatus;

    /**
     * 友链描述
     */
    private String description;
}
