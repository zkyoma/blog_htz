package cn.htz.blog.vo;

import cn.htz.blog.po.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo extends Comment {
    /**
     * 文章标题
     */
    private String title;
    /**
     * 子级评论，即恢复当前评论的评论集合
     */
    private List<CommentVo> replyComments;
    /**
     * 父评论
     */
    private CommentVo parentComment;
}
