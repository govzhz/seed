package com.base.seed.webapp.common;

import com.base.seed.common.excel.ExcelUtil;
import com.base.seed.common.excel.PreValidatedCallback;
import com.base.seed.webapp.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author zz 2019/12/10
 */
public class ExcelTest extends BaseTest {

    @Test
    public void test(){
        try {
            List<Person> res = ExcelUtil.readExcel("./excel/test.xlsx", Person.class, new PreValidatedCallback() {
                @Override
                public <T> boolean validate(T t) {
                    return true;
                }
            });

            Assert.assertEquals(res.size(), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
