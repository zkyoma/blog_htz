package cn.htz.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BlogUpdateVo {
    @NotNull(message = "更新博客的boolean属性，id不能为空")
    private Long id;
    /**
     * 是否推荐文章
     */
    private Boolean recommend;
    /**
     * 发布状态（1：已发布，0：保存未发布）
     */
    private Boolean publish;
    /**
     * 是否置顶
     */
    private Boolean top;
    /**
     * 是否开启赞赏
     */
    private Boolean appreciation;
    /**
     * 是否开启评论
     */
    private Boolean comment;
    /**
     * 文章类型（1：原创，0：转载）
     */
    private Boolean flag;
}
