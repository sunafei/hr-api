package com.eplugger.core.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * AFei Sun
 * Created in 13:06 2018/7/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateResultListVO<T> {
    private Long total;
    private Integer page;
    private List<T> items = new ArrayList<>();
}
