package com.ssm_crud.controller;/*
 *文件名: DepartmentController
 *创建者: zdx
 *创建时间:2021/3/18 13:46
 *描述:  处理和部门有关的请求
 */

import com.ssm_crud.bean.Department;
import com.ssm_crud.bean.Msg;
import com.ssm_crud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    /**
     *  返回所有的部门信息
     */
    @RequestMapping("/depts")
    @ResponseBody
    public Msg getDepts(){
        //查出的所有部门信息
        List<Department> list = departmentService.getDepts();
        return Msg.success().add("depts",list);
    }

}
