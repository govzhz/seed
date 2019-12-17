package com.base.seed.common.excel;

/**
 * @author zz 2019/12/10
 */
public interface PreValidatedCallback {

    /**
     * 是否满足条件
     *
     * @param t
     * @return 若返回false或抛出异常认为未满足条件
     */
    <T> boolean pass(T t);
}
