package cn.htz.blog.controller.admin;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.common.R;
import cn.htz.blog.service.CommentService;
import cn.htz.blog.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 分页按关键字查询评论
     * @param pageRequestQuery
     * @return
     */
    @PostMapping("/page")
    public R page(@Validated @RequestBody PageRequestQuery pageRequestQuery) {
        PageResult<CommentVo> commentVos = commentService.selectPage(pageRequestQuery);
        return R.ok().put("data", commentVos);
    }

    /**
     * 根据id删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        commentService.deleteById(id);
        return R.ok();
    }

    /**
     * 批量删除评论
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R deleteBatch(@NotEmpty(message = "根据id集合删除评论，id集合不能为空") @RequestBody List<Long> ids) {
        commentService.deleteBatchByIds(ids);
        return R.ok();
    }
}
