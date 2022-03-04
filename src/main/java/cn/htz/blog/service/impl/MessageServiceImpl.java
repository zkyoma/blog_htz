package cn.htz.blog.service.impl;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.mapper.BlogMapper;
import cn.htz.blog.mapper.MessageMapper;
import cn.htz.blog.po.Message;
import cn.htz.blog.service.MessageService;
import cn.htz.blog.vo.MessageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public PageResult<MessageVo> selectPage(PageRequestQuery pageRequestQuery) {
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
        List<Message> messages = messageMapper.selectListByKey(key);
        if (CollectionUtils.isEmpty(messages)) {
            return new PageResult<>();
        }
        PageHelper.startPage(pageNum, pageSize);
        // 根据关键字分页查询一级评论集合
        List<Message> maxLevelMessages = messageMapper.selectMaxLevelMessagesByKey(key);
        PageInfo<Message> pageInfo = new PageInfo<>(maxLevelMessages);

        // 把messages转化成messageVos，并过滤掉顶级留言
        List<MessageVo> messageVos = messages.stream()
                .filter(message -> message.getParentMessageId() != 0)
                .map(message -> {
                    MessageVo messageVo = new MessageVo();
                    BeanUtils.copyProperties(message, messageVo);
                    return messageVo;
                }).collect(Collectors.toList());

        // 把maxLevelMessages转化成maxLevelMessageVos
        List<MessageVo> maxLevelMessageVos = maxLevelMessages.stream()
                .map(message -> {
                    MessageVo messageVo = new MessageVo();
                    BeanUtils.copyProperties(message, messageVo);
                    return messageVo;
                }).collect(Collectors.toList());

        // 把所有子级留言封装到父级留言中
        combineChildren(maxLevelMessageVos, messageVos);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), maxLevelMessageVos, pageInfo.getPageNum());
    }

    @Transactional
    @Override
    public void saveMessage(Message message) {
        message.setId(null);
        message.setCreateTime(new Date());
        messageMapper.save(message);
    }

    //存放迭代找出的所有子代的集合
    private List<MessageVo> tempReplies = new ArrayList<>();

    private void combineChildren(List<MessageVo> firstLevelMessageVos, List<MessageVo> allMessages) {
        // 遍历所有一级评论
        for (MessageVo messageVo : firstLevelMessageVos) {
            // 过滤出一级评论的直接子评论
            List<MessageVo> replyMessages = allMessages.stream()
                    .filter(message -> message.getParentMessageId().equals(messageVo.getId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(replyMessages)) {
                // 遍历所有的子评论
                for (MessageVo messageChild : replyMessages) {
                    // 设置父评论
                    MessageVo newMessage = new MessageVo();
                    BeanUtils.copyProperties(messageVo, newMessage);
                    // 直接设置messageVo会报错Infinite recursion (StackOverflowError)  through reference chain
                    messageChild.setParentMessage(newMessage);
                    //循环迭代，找出子代，存放在tempReplies中
                    recursively(messageChild, allMessages);
                }
            }
            //修改顶级节点的children集合为迭代处理后的集合
            messageVo.setReplyMessages(tempReplies);
            //清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    /**
     * 递归迭代，剥洋葱
     * @param messageVo 被迭代的对象
     * @return
     */
    private void recursively(MessageVo messageVo, List<MessageVo> allMessages) {
        // 顶节点添加到临时存放集合
        tempReplies.add(messageVo);
        // 过滤出直接子评论
        List<MessageVo> replyMessages = allMessages.stream()
                .filter(message -> message.getParentMessageId().equals(messageVo.getId()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(replyMessages)) {
            for (MessageVo reply : replyMessages) {
                // 设置父评论
                reply.setParentMessage(messageVo);
                tempReplies.add(reply);
                List<MessageVo> replyMessages2 = allMessages.stream()
                        .filter(message -> message.getParentMessageId().equals(reply.getId()))
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(replyMessages2)) {
                    recursively(reply, allMessages);
                }
            }
        }
    }


    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new MyException(ErrorEnum.PATH_NOT_FOUND);
        }
        messageMapper.deleteById(id);
    }

    @Override
    public void deleteBatchByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new MyException(ErrorEnum.PATH_NOT_FOUND);
        }
        messageMapper.deleteBatchByIds(ids);
    }

    @Override
    public Long getMessageTotal() {
        return messageMapper.getCountMessages();
    }
}
