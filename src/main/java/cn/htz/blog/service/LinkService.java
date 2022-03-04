package cn.htz.blog.service;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.po.Link;

import java.util.List;
import java.util.Map;

public interface LinkService {
    /**
     * 根据关键字分页查询友链信息
     * @param pageRequestQuery
     * @return
     */
    PageResult<Link> selectPage(PageRequestQuery pageRequestQuery);

    /**
     * 保存友链
     * @param link
     */
    void save(Link link);

    /**
     * 根据id查询友链信息
     * @param id
     * @return
     */
    Link getLinkById(Long id);

    /**
     * 更新友链
     * @param link
     */
    void update(Link link);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除，根据id集合
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 查询所有友链信息，用于友链页面显示
     * @return
     */
    Map<Byte, List<Link>> getLinksForLinkPage();
}
