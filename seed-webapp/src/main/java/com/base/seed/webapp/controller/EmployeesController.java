package com.base.seed.webapp.controller;

import com.base.seed.dal.entity.employees.EmployeesDo;
import com.base.seed.facade.support.Page;
import com.base.seed.service.demo.EmployeesService;
import com.base.seed.webapp.support.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zz 2019/12/12
 */
@Controller
public class EmployeesController {

    @Autowired
    private EmployeesService employeesService;

    @ResponseBody
    @RequestMapping(value = "/employ", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public HttpResult<Page<EmployeesDo>> employ(){
        List<EmployeesDo> employees = employeesService.employees();
        return HttpResult.success(new Page<>(((com.github.pagehelper.Page<EmployeesDo>) employees).getTotal(), employees));
    }
}
