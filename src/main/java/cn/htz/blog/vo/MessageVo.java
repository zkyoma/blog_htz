package cn.htz.blog.vo;

import cn.htz.blog.po.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo extends Message {
    /**
     * 子级评论，即恢复当前评论的评论集合
     */
    private List<MessageVo> replyMessages;
    /**
     * 父评论
     */
    private MessageVo parentMessage;
}
