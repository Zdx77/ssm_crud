package com.ssm_crud.controller;/*
 *文件名: EmployeeController
 *创建者: zdx
 *创建时间:2021/3/16 14:07
 *描述: 处理员工CRUD请求
 */

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssm_crud.bean.Employee;
import com.ssm_crud.bean.Msg;
import com.ssm_crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.HttpPutFormContentFilter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 单个批量二合一
     * 批量删除：1- 2 -3
     * 单个删除：1
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
    public  Msg deletEmpById(@PathVariable("id") String ids){
        //批量删除
       if(ids.contains("-")){
           List<Integer> del_ids =new ArrayList<>();
            String[] str_ids = ids.split("-");
            //组装id的集合
           for (String string :str_ids){
               del_ids.add(Integer.parseInt(string));

           }
            employeeService.deleteBatch(del_ids);

       }else {
           Integer id = Integer.parseInt(ids);
           employeeService.deleteEmp(id);

       }
        return  Msg.success();
    }


    /**
     *如果直接发送ajax=PUT形式的请求
     *封装的数据
     * Employee
     * 除了 empId = 1000 , 其它的都是NULL
     *
     * 问题： 请求体中有数据，但是Employee对象封装不上
     *         update tbl_emp where emp_id  = 1000
     * 原因：   1.Tomcat将请求体中的数据，封装一个map
     *         2.request.getParameter("empName")就会从这个map中取值
     *         3.Springmvc封装POJO对象的时候，会把POJO中每个属性的值调用  request.getParameter("email")；
     *   Ajax发送PUT请求引发的血案；
     *          PUT请求，请求体中的数据， request.getParameter("empName")拿不到
     *          Tomcat一看是PUT请求，就不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
     *          org.apache.catalina.connector.Request--parseParameter()  (3113);
     *
     *  解决方案：
     *  我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
     *  1.配置上HttpPutFormContentFilter;
     *  2.他的作用：将请求体中的数据包装成一个map，
     *  3.request被重新包装，request.getParameter（）被重写，就会从自己封装的map中取数据
     * 员工更新方法
     * @param employee
     * @return
     */



    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    @ResponseBody
    public Msg saveEmp(Employee employee, HttpServletRequest request){
        System.out.println("请求体中得值"+request.getParameter("gender"));
        System.out.println("将要更新的员工数据:"+ employee);
        employeeService.updateEmp(employee);
        return  Msg.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */

    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return  Msg.success().add("emp",employee);
    }

    /**
     * 检查用户名是否可用
     * @param empName
     * @return
     */

    @RequestMapping("/checkuser")
    @ResponseBody
    public  Msg checkuser(@RequestParam("empName") String empName){
        //先判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
        if(!empName.matches(regx)){
            return  Msg.fail().add("va_msg","用户名必须是6-16位数字和字母的组合或者2-5位中文");
        }
        //数据库用户名重复校验
        boolean b = employeeService.checkUser(empName);
        if( b ) {
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg","用户名不可用");
        }
    }

    /**
     * 员工保存
     * 1.支出JSR303校验
     * 2.导入 Hibernate-Validator
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    @ResponseBody
//    返回json数据
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        if (result.hasErrors()){
            //校验失败，应该返回失败，在模态框中显示效验失败的错误信息
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for ( FieldError fieldError : errors) {
                System.out.println("错误的字段名"+fieldError.getField());
                System.out.println("错误信息"+fieldError.getDefaultMessage());
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return  Msg.fail().add("errorFields",map);
        }else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }

    }

    /**
     * ResponseBody正常工作需要引入Jackson包
     * @param pn
     * @param model
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model){
        PageHelper.startPage(pn,5);
        //StartPage后面紧跟的这个查询就是分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
        //封装了详细的分页信息，包括有我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo",page);

    }

    /**
     * 查询员工数据（分页查询）
     * @return
     */
    //@RequestMapping("/emps")
//    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model){
//        //这不是一个分页查询
//        //引入pageHelper分页查询插件
//        //在查询之前只需要调用,传入页码以及每页的大小
//        PageHelper.startPage(pn,5);
//        //StartPage后面紧跟的这个查询就是分页查询
//        List<Employee> emps = employeeService.getAll();
//        //使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
//        //封装了详细的分页信息，包括有我们查询出来的数据,传入连续显示的页数
//        PageInfo page = new PageInfo(emps, 5);
//        model.addAttribute("pageInfo",page);
//        return  "list";
//    }
}
