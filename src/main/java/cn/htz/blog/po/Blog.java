package cn.htz.blog.po;

import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

/**
 * 文章实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    /**
     * 主键id
     */
    @Null(message = "新增博客的id必须为空", groups = {AddGroup.class})
    @NotNull(message = "更新博客的id不能为空", groups = {UpdateGroup.class})
    private Long id;
    /**
     * 文章标题
     */
    @NotNull(message = "博客的标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String title;
    /**
     * 文章描述
     */
    private String description;
    /**
     * 文章内容
     */
    @NotNull(message = "博客的内容不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String content;
    /**
     * 转化为html后的文章内容
     */
    @NotNull(message = "博客转化成html格式的内容不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String htmlContent;
    /**
     * 作者id
     */
    private Long userId;
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
     * 首图
     */
    private String firstPicture;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否推荐文章
     */
    @NotNull(message = "博客是否推荐不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Boolean recommend;
    /**
     * 发布状态（1：已发布，0：保存未发布）
     */
    @NotNull(message = "博客的发布状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Boolean publish;
    /**
     * 是否开启赞赏
     */
    @NotNull(message = "博客是否开启赞赏不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Boolean appreciation;
    /**
     * 是否开启评论
     */
    @NotNull(message = "博客是否开启评论不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Boolean comment;
    /**
     * 文章类型（1：原创，0：转载）
     */
    @NotNull(message = "博客类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Boolean flag;
    /**
     * 是否允许转载（
     */
    @NotNull(message = "博客是否允许转载不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Boolean shareStatement;
    /**
     * 分类id
     */
    @NotNull(message = "博客对应的分类id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Long categoryId;
    /**
     * 是否显示博客
     */
    private Boolean showStatus;
    /**
     * 博客与标签是多对多关系
     */
    private List<Tag> tags;
    /**
     * 一个博客对应第一个分类
     */
    private Category category;
}
