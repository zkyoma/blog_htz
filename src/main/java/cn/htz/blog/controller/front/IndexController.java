package cn.htz.blog.controller.front;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.po.Category;
import cn.htz.blog.po.Comment;
import cn.htz.blog.po.Message;
import cn.htz.blog.po.User;
import cn.htz.blog.service.*;
import cn.htz.blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author : xsh
 * @create : 2020-02-06 - 21:13
 * @describe:
 */
@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MessageService messageService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum){
        model.addAttribute("page", blogService.getBlogsForIndexPage(pageNum));
        model.addAttribute("categories", categoryService.listCategoryTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendHotBlogs",blogService.listRecommendBlogsTop(1, 5));
        model.addAttribute("recommendNewBlogs",blogService.listRecommendBlogsTop(0, 5));
        return "index";
    }

    /**
     * 详情页
     * @return
     */
    @GetMapping("/blog/{blogId}")
    public String detail(Model model, @PathVariable("blogId") Long blogId) {
        BlogDetailVo blogVo = blogService.getDetailBlogById(blogId);
        if (blogVo != null) {
            model.addAttribute("blog", blogVo);
        }
        return "blog";
    }

    /**
     * 查询博客对应的评论信息
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        PageResult<CommentVo> pageResult = commentService.getCommentPageByBlogIdAndPageNum(blogId, 1);
        model.addAttribute("comments", pageResult.getItems());
        return "blog :: commentList";
    }

    /**
     * 保存评论
     * @param comment
     * @param session
     * @return
     */
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session, Model model) {
        Long blogId = comment.getBlogId();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setCommentatorType((byte) 3);
        } else {
            //设置头像
            comment.setAvatar(avatar);
            comment.setCommentatorType((byte) 0);
        }
        commentService.saveComment(comment);
        PageResult<CommentVo> pageResult = commentService.getCommentPageByBlogIdAndPageNum(blogId, 1);
        model.addAttribute("comments", pageResult.getItems());
        return "redirect:/comments/" + blogId;
    }


    /**
     * 标签页
     * @param id
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}")
    public String tags(@PathVariable("id") Long id,
                       @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       Model model) {
        List<TagListVo> tags = tagService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        PageResult<BlogListVo> pageResult = blogService.getBlogsPageByTagId(id, page);
        model.addAttribute("page", pageResult);
        model.addAttribute("activeTagId", id);
        return "tags";
    }

    /**
     * 归档
     * @param model
     * @return
     */
    @GetMapping("/archives")
    public String archives(Model model) {
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount", blogService.countBlog());
        return "archives";
    }

    /**
     * 关于我
     * @return
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /**
     * 分类
     * @param id
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/categories/{id}")
    public String categories(@PathVariable("id") Long id,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             Model model) {
        List<Category> categories = categoryService.listWithTree();
        if (id == -1) {
            id = categories.get(0).getChildren().get(0).getId();
        }
        int categoryCount = 0;
        for (Category category : categories) {
            categoryCount += category.getChildren().size();
        }
        model.addAttribute("categories", categories);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("page", blogService.getBlogsPageByCategoryId(id, page));
        model.addAttribute("activeTypeId", id);
        return "categories";
    }

    /**
     * 搜索
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                         Model model){
        model.addAttribute("page", blogService.getBlogsPageBySearch(query, page));
        model.addAttribute("query", query);
        return "search";
    }

    /**
     * 分页查询留言
     * @param key
     * @param page
     * @return
     */
    @GetMapping("/messages")
    public String messages(@RequestParam(value = "key", required = false) String key,
                           @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                           Model model) {
        PageRequestQuery pageRequestQuery = new PageRequestQuery();
        pageRequestQuery.setKey(key);
        pageRequestQuery.setPageNum(page);
        pageRequestQuery.setPageSize(8);
        PageResult<MessageVo> pageResult = messageService.selectPage(pageRequestQuery);
        model.addAttribute("page", pageResult);
        return "message_board";
    }

    /**
     * 分页查询
     * @param key
     * @param page
     * @return
     */
    @GetMapping("/initmessages")
    public String initMessages(@RequestParam(value = "key", required = false) String key,
                           @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                           Model model) {
        PageRequestQuery pageRequestQuery = new PageRequestQuery();
        pageRequestQuery.setKey(key);
        pageRequestQuery.setPageNum(page);
        pageRequestQuery.setPageSize(8);
        PageResult<MessageVo> pageResult = messageService.selectPage(pageRequestQuery);
        model.addAttribute("page", pageResult);
        return "message_board :: messageList";
    }

    /**
     * 提交留言
     * @param message
     * @param session
     * @return
     */
    @PostMapping("/messages")
    public String saveMessage(Message message, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            message.setAvatar(user.getAvatar());
            message.setMessageType((byte) 3);
        } else {
            message.setMessageType((byte) 0);
            message.setAvatar(avatar);
        }
        messageService.saveMessage(message);
        // return "message_board :: messageList";
        return "redirect:/initmessages";
    }

    /**
     * 页面底部博客信息
     * @return
     */
    @GetMapping("/footer/blogmessage")
    public String blogMessage(Model model){
        long blogTotal = blogService.countBlog();
        long blogViewTotal = blogService.getBlogReadNumTotal();
        long blogCommentTotal = blogService.getBlogCommentTotal();
        long blogMessageTotal = messageService.getMessageTotal();

        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("blogViewTotal",blogViewTotal);
        model.addAttribute("blogCommentTotal",blogCommentTotal);
        model.addAttribute("blogMessageTotal",blogMessageTotal);
        return "_fragments :: blogMessage";
    }
}
