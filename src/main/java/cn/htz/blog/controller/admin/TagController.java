package cn.htz.blog.controller.admin;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.common.R;
import cn.htz.blog.po.Tag;
import cn.htz.blog.service.TagService;
import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 分页条件查询标签
     * @param pageRequestQuery
     * @return
     */
    @PostMapping("/page")
    public R list(@Validated @RequestBody PageRequestQuery pageRequestQuery) {
        PageResult<Tag> tags = tagService.selectPage(pageRequestQuery);
        return R.ok().put("data", tags);
    }

    /**
     * 查询所有标签
     * @return
     */
    @GetMapping("/tags")
    public R tags() {
        List<Tag> tags = tagService.selectList();
        return R.ok().put("data", tags);
    }

    /**
     * 保存标签
     * @param tag
     * @return
     */
    @PostMapping("/save")
    public R addTag(@Validated(AddGroup.class) @RequestBody Tag tag) {
        tagService.save(tag);
        return R.ok();
    }

    /**
     * 根据id查询标签信息
     * @return
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        Tag tag = tagService.getTagById(id);
        return R.ok().put("data", tag);
    }

    /**
     * 更新标签
     * @param tag
     * @return
     */
    @PutMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody Tag tag) {
        tagService.update(tag);
        return R.ok();
    }

    /**
     * 更新id删除标签
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        tagService.deleteById(id);
        return R.ok();
    }

    /**
     * 批量删除标签
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R deleteBatch(@NotEmpty(message = "根据id集合删除标签，id集合不能为空") @RequestBody List<Long> ids) {
        tagService.deleteByIds(ids);
        return R.ok();
    }

    /**
     * 检查标签名是否唯一
     * @param name
     * @return
     */
    @GetMapping("/checkName")
    public R checkName(@NotBlank(message = "根据名称查询标签，名称不能为空") @RequestParam("name") String name) {
        Boolean isExist = tagService.checkName(name);
        return R.ok().put("data", isExist);
    }
}
