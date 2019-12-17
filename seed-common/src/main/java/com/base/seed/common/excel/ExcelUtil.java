package com.base.seed.common.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.base.seed.common.exception.ExcelReadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 更多的新式使用参考：
 * https://github.com/alibaba/easyexcel/blob/master/src/test/java/com/alibaba/easyexcel/test/demo/read/ReadTest.java
 *
 * @author zz 2019/12/10
 */
public class ExcelUtil {

    /**
     * 读取Excel数据, 适用于 "Excel数据量不大，可一次性读取入内存" 的场景。
     * 使用介绍：
     *  1. 映射对象实现BaseRowModel
     *  2. 自定义对象校验逻辑
     *  List<Person> res = ExcelUtil.readExcel("./excel/test.xlsx", Person.class, new PreValidatedCallback() {
     *                 @Override
     *                 public <T> boolean pass(T t) {
     *                     return true;
     *                 }
     *             });
     *
     * @param path: 资源路径
     * @param tClass: Excel字段映射的Class对象
     * @param callback: 映射对象的回调校验
     * @param <T>
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public static <T extends BaseRowModel> List<T> readExcel(String path, Class<T> tClass, PreValidatedCallback callback) throws InterruptedException, IOException {

        CountDownLatch end = new CountDownLatch(1);
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if(inputStream == null) throw new ExcelReadException("读取excel流为空, path=" + path);

            ExcelListener<T> excelListener = new ExcelListener<>(end, callback);
            EasyExcelFactory.readBySax(inputStream, new Sheet(1, 1, tClass), excelListener);
            end.await();
            return excelListener.getRes();
        } finally {

            if(inputStream != null){
                inputStream.close();
            }
        }
    }
}
