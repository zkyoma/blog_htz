package cn.htz.blog.mapper;

import cn.htz.blog.po.Message;

import java.util.List;

public interface MessageMapper {
    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id批量删除
     * @param ids
     */
    void deleteBatchByIds(List<Long> ids);

    /**
     * 根据关键字，查询所有的留言集合
     * @param key
     * @return
     */
    List<Message> selectListByKey(String key);

    /**
     * 根据关键字，查询所有的一级留言集合
     * @param key
     * @return
     */
    List<Message> selectMaxLevelMessagesByKey(String key);

    /**
     * 保存留言
     * @param message
     */
    void save(Message message);

    /**
     * 查询所有留言的数量
     * @return
     */
    Long getCountMessages();
}
