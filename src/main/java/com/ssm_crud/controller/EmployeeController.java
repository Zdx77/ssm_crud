package com.ssm_crud.controller;/*
 *文件名: EmployeeController
 *创建者: zdx
 *创建时间:2021/3/16 14:07
 *描述: 处理员工CRUD请求
 */

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssm_crud.bean.Employee;
import com.ssm_crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    /**
     * 查询员工数据（分页查询）
     * @return
     */
    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model){
        //这不是一个分页查询
        //引入pageHelper分页查询插件
        //在查询之前只需要调用,传入页码以及每页的大小
        PageHelper.startPage(pn,5);
        //StartPage后面紧跟的这个查询就是分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
        //封装了详细的分页信息，包括有我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps, 5);
        model.addAttribute("pageInfo",page);
        return  "list";
    }
}
