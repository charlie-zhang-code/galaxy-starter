package io.charlie.base;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author charlie-zhang-code
 * @version v1.0
 * @date 2025/4/13
 * @description 批量删除ID，增加批量ID
 */
@Data
public class ArrayValues<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> values;
}
