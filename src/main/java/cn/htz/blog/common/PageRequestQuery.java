package cn.htz.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageRequestQuery extends PageRequest {

    /**
     * 查询关键字
     */
    private String key;
}
