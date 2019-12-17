package com.base.seed.webapp.common;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @author zz 2019-05-15
 */
@Data
public class Person extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty(index = 1)
    private Integer age;

    @ExcelProperty(index = 2)
    private String hobby;
}