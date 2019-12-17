package com.base.seed.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.fastjson.JSON;
import com.base.seed.common.exception.ExcelReadException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 在原先的Excel读取基础上增加同步逻辑，
 * 若 "Excel数据量不大，可一次性读取入内存"，那么可使用该抽象类快速读取
 *
 * @param <T>
 */
public class ExcelListener<T extends BaseRowModel> extends AnalysisEventListener<T> {

    private List<T> res = new ArrayList<>();
    private CountDownLatch end;
    private PreValidatedCallback callback;

    public ExcelListener(CountDownLatch end, PreValidatedCallback callback){
        this.end = end;
        this.callback = callback;
    }

    @Override
    public void invoke(T target, AnalysisContext context) {
        if(!callback.pass(target)){
            throw new ExcelAnalysisException("读取Excel内容时校验失败, object=" + JSON.toJSONString(target));
        }
        res.add(target);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        end.countDown();
    }

    public List<T> getRes() {
        return res;
    }
}