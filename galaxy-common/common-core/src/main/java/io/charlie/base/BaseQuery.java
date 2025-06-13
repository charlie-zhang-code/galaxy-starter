package io.charlie.base;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author charlie-zhang-code
 * @version v1.0
 * @date 2025/4/13
 * @description 基础查询对象
 */
@Data
public class BaseQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int pageNumber = 1; // 当前页码

    private int pageSize = 10; // 每 1 页的数据量

    private String keyword; // 关键词

}
