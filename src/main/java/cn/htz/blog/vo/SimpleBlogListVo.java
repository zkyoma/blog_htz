package cn.htz.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBlogListVo {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 文章标题
     */
    private String title;
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否为原创
     */
    private Boolean flag;
}
