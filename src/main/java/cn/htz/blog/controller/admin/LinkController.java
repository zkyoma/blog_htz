package cn.htz.blog.controller.admin;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.common.R;
import cn.htz.blog.po.Link;
import cn.htz.blog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 根据关键字分页查询友链信息
     * @param pageRequestQuery
     * @return
     */
    @PostMapping("/page")
    public R page(@RequestBody PageRequestQuery pageRequestQuery) {
        PageResult<Link> pageResult = linkService.selectPage(pageRequestQuery);
        return R.ok().put("data", pageResult);
    }

    /**
     * 保存友链
     * @param link
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody Link link) {
        linkService.save(link);
        return R.ok();
    }

    /**
     * 根据id查询友链信息，用于修改页面的显示
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public R editPage(@PathVariable("id") Long id) {
        Link link = linkService.getLinkById(id);
        return R.ok().put("data", link);
    }

    /**
     * 更新友链
     * @param link
     * @return
     */
    @PutMapping("/update")
    public R update(@RequestBody Link link) {
        linkService.update(link);
        return R.ok();
    }

    /**
     * 根据id删除友链
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        linkService.deleteById(id);
        return R.ok();
    }

    /**
     * 根据id集合批量删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R deleteBatch(@RequestBody List<Long> ids) {
        linkService.deleteBatch(ids);
        return R.ok();
    }
}
