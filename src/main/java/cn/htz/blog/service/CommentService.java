package cn.htz.blog.service;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.po.Comment;
import cn.htz.blog.vo.CommentVo;

import java.util.List;

public interface CommentService {
    /**
     * 分页按关键字查询
     * @param pageRequestQuery
     * @return
     */
    PageResult<CommentVo> selectPage(PageRequestQuery pageRequestQuery);

    /**
     * 根据id删除评论
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id集合批量删除
     * @param ids
     */
    void deleteBatchByIds(List<Long> ids);

    /**
     * 根据博客id分页查询评论信息
     * @param blogId
     * @param commentPage
     * @return
     */
    PageResult<CommentVo> getCommentPageByBlogIdAndPageNum(Long blogId, Integer commentPage);

    /**
     * 保存评论
     * @param comment
     */
    void saveComment(Comment comment);
}
