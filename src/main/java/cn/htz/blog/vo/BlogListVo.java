package cn.htz.blog.vo;

import cn.htz.blog.po.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogListVo {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章描述
     */
    private String description;
    /**
     * 首图
     */
    private String firstPicture;
    /**
     * 文章类型（1：原创，0：转载）
     */
    private Boolean flag;
    /**
     * 阅读量
     */
    private Integer readNum;
    /**
     * 评论量
     */
    private Integer commentNum;
    /**
     * 点赞量
     */
    private Integer likeNum;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 分类名称，拼接后的
     */
    private String categoryNames;
    /**
     * 对应分类id
     */
    private Long categoryId;
    /**
     * 标签集合
     */
    private List<Tag> tags;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户头像
     */
    private String avatar;
}
