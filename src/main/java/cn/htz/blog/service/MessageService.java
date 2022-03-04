package cn.htz.blog.service;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.po.Message;
import cn.htz.blog.vo.MessageVo;

import java.util.List;

public interface MessageService {
    /**
     * 根据关键字分页查询留言，把子级留言封装到父级留言中去
     * @param pageRequestQuery
     * @return
     */
    PageResult<MessageVo> selectPage(PageRequestQuery pageRequestQuery);

    /**
     * 根据id删除留言
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id集合批量删除留言
     * @param ids
     */
    void deleteBatchByIds(List<Long> ids);

    /**
     * 保存留言
     * @param message
     */
    void saveMessage(Message message);

    /**
     * 查询全部留言的数量
     * @return
     */
    Long getMessageTotal();
}
