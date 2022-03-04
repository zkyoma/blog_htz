package cn.htz.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    /**
     * 评论id
     */
    private Long id;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 文章id
     */
    private Long blogId;
    /**
     * 父评论id
     */
    private Long parentCommentId;
    /**
     * 评论时间
     */
    private Date createTime;
    /**
     * 评论用户的名称
     */
    private String username;
    /**
     * 评论用户的头像
     */
    private String avatar;
    /**
     * 评论者类型，0：游客，1：访客，2：gayhub，3：博主
     */
    private Byte commentatorType;
    /**
     * 邮箱
     */
    private String email;
}
