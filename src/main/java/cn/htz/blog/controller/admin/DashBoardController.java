package cn.htz.blog.controller.admin;

import cn.htz.blog.common.R;
import cn.htz.blog.service.*;
import cn.htz.blog.vo.DashboardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
public class DashBoardController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/info")
    public R baseInfo() {
        long blogsCount = blogService.countBlog();
        long categoriesCount = categoryService.countCategory();
        long tagsCount = tagService.countTag();
        long readNum = blogService.getBlogReadNumTotal();
        long commentsCount = blogService.getBlogCommentTotal();
        long messagesCount = messageService.getMessageTotal();
        DashboardVo dashboardVo = new DashboardVo(readNum, messagesCount, commentsCount, blogsCount, categoriesCount, tagsCount);
        return R.ok().put("data", dashboardVo);
    }
}
