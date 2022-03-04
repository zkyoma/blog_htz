package cn.htz.blog.service.impl;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.mapper.BlogMapper;
import cn.htz.blog.mapper.CommentMapper;
import cn.htz.blog.po.Blog;
import cn.htz.blog.po.Comment;
import cn.htz.blog.service.BlogService;
import cn.htz.blog.service.CommentService;
import cn.htz.blog.vo.CommentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public PageResult<CommentVo> selectPage(PageRequestQuery pageRequestQuery) {
        // 当前页
        int pageNum = pageRequestQuery.getPageNum();
        // 每页记录数
        int pageSize = pageRequestQuery.getPageSize();
        // 查询关键字
        String key = pageRequestQuery.getKey();
        if (key != null) {
            key = key.trim();
        }
        // 根据关键字查询出评论的集合
        List<Comment> comments = commentMapper.selectListByKey(key);
        if (CollectionUtils.isEmpty(comments)) {
            return new PageResult<>();
        }
        PageHelper.startPage(pageNum, pageSize);
        // 根据关键字分页查询一级评论集合
        List<Comment> maxLevelComments = commentMapper.selectMaxLevelCommentsByKey(key);
        PageInfo<Comment> pageInfo = new PageInfo<>(maxLevelComments);

        // 把子级评论封装到对应的顶级评论中
        // combineChildren(firstLevelCommentVos, commentVos);
        // return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), firstLevelCommentVos, pageInfo.getPageNum());
        return getFirstLevelCommentVos(comments, maxLevelComments, pageInfo);
    }

    /**
     * 把comment集合转化为Vo集合，并对每个顶级comment进行封装，填充其子comment
     * @param comments
     * @param maxLevelComments
     * @param pageInfo
     * @return
     */
    private PageResult<CommentVo> getFirstLevelCommentVos(List<Comment> comments, List<Comment> maxLevelComments, PageInfo<Comment> pageInfo) {
        // 把comments转化成commentVos，过滤掉顶级评论
        List<CommentVo> commentVos = comments.stream()
                .filter(comment -> comment.getParentCommentId() != 0)
                .map(comment -> {
                    CommentVo commentVo = new CommentVo();
                    BeanUtils.copyProperties(comment, commentVo);
                    // 查询当前评论对应的博客
                    Blog blog = blogService.getById(comment.getBlogId());
                    if (blog != null) {
                        // 设置博客标题
                        commentVo.setTitle(blog.getTitle());
                    }
                    return commentVo;
                }).collect(Collectors.toList());

        // 把最顶级comment转化为对应的vo
        List<CommentVo> firstLevelCommentVos = maxLevelComments.stream()
                .map(comment -> {
                    CommentVo commentVo = new CommentVo();
                    BeanUtils.copyProperties(comment, commentVo);
                    // 查询当前评论对应的博客
                    Blog blog = blogService.getById(comment.getBlogId());
                    if (blog != null) {
                        // 设置博客标题
                        commentVo.setTitle(blog.getTitle());
                    }
                    return commentVo;
                }).collect(Collectors.toList());
        combineChildren(firstLevelCommentVos, commentVos);
        firstLevelCommentVos.forEach(firstLevelCommentVo -> {
            firstLevelCommentVo.getReplyComments().sort(new Comparator<CommentVo>() {
                @Override
                public int compare(CommentVo o1, CommentVo o2) {
                    long time1 = o1.getCreateTime().getTime();
                    long time2 = o2.getCreateTime().getTime();
                    return Long.compare(time2, time1);
                }
            });
        });
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), firstLevelCommentVos, pageInfo.getPageNum());
    }

    @Override
    public PageResult<CommentVo> getCommentPageByBlogIdAndPageNum(Long blogId, Integer commentPage) {
        // 当前页
        int pageNum = commentPage;
        // 每页记录数
        int pageSize = 1000;
        // 根据关键字查询出评论的集合
        List<Comment> comments = commentMapper.getCommentsByBlogId(blogId);
        if (CollectionUtils.isEmpty(comments)) {
            return new PageResult<>();
        }
        PageHelper.startPage(pageNum, pageSize);
        // 根据关键字分页查询一级评论集合
        List<Comment> maxLevelComments = commentMapper.getMaxLevelCommentsByBlogId(blogId);
        PageInfo<Comment> pageInfo = new PageInfo<>(maxLevelComments);

        // 把comments转化成commentVos
        return getFirstLevelCommentVos(comments, maxLevelComments, pageInfo);
    }

    @Transactional
    @Override
    public void saveComment(Comment comment) {
        // 对应的博客评论数量加 1
        blogMapper.updateCommentNumById(comment.getBlogId());
        comment.setId(null);
        comment.setCreateTime(new Date());
        commentMapper.save(comment);
    }

    //存放迭代找出的所有子代的集合
    private List<CommentVo> tempReplies = new ArrayList<>();

    private void combineChildren(List<CommentVo> firstLevelCommentVos, List<CommentVo> allComments) {
        // 遍历所有一级评论
        for (CommentVo commentVo : firstLevelCommentVos) {
            // 过滤出一级评论的直接子评论
            List<CommentVo> replyComments = allComments.stream()
                    .filter(comment -> comment.getParentCommentId().equals(commentVo.getId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(replyComments)) {
                // 遍历所有的子评论
                for (CommentVo commentChild : replyComments) {
                    // 设置父评论
                    CommentVo newComment = new CommentVo();
                    BeanUtils.copyProperties(commentVo, newComment);
                    // 直接设置commentVo会报错Infinite recursion (StackOverflowError)  through reference chain
                    commentChild.setParentComment(newComment);
                    //循环迭代，找出子代，存放在tempReplies中
                    recursively(commentChild, allComments);

                }
            }
            //修改顶级节点的children集合为迭代处理后的集合
            commentVo.setReplyComments(tempReplies);
            //清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    /**
     * 递归迭代，剥洋葱
     * @param commentVo 被迭代的对象
     * @return
     */
    private void recursively(CommentVo commentVo, List<CommentVo> allComments) {
        // 顶节点添加到临时存放集合
        tempReplies.add(commentVo);
        // 过滤出直接子评论
        List<CommentVo> replyComments = allComments.stream()
                .filter(comment -> comment.getParentCommentId().equals(commentVo.getId()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(replyComments)) {
            for (CommentVo reply : replyComments) {
                // 设置父评论
                reply.setParentComment(commentVo);
                tempReplies.add(reply);
                List<CommentVo> replyComments2 = allComments.stream()
                        .filter(comment -> comment.getParentCommentId().equals(reply.getId()))
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(replyComments2)) {
                    recursively(reply, allComments);
                }
            }
        }
    }


    @Transactional
    @Override
    public void deleteById(Long id) {
        if (id != null) {
            commentMapper.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void deleteBatchByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new MyException(ErrorEnum.PATH_NOT_FOUND);
        }
        commentMapper.deleteByIds(ids);
    }
}
