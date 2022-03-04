package cn.htz.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 留言者的名称
     */
    private String username;
    /**
     * 浏览者的邮箱
     */
    private String email;
    /**
     * 留言内容
     */
    private String content;
    /**
     * 留言者的头像
     */
    private String avatar;
    /**
     * 留言被回复后是否发邮件通知
     */
    private Boolean replyInform;
    /**
     * 留言类型
     */
    private Byte messageType;
    /**
     * 父留言的id
     */
    private Long parentMessageId;
    /**
     * 创建时间
     */
    private Date createTime;
}
