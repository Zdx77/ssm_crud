package com.ssm_crud.service;
/*
 *文件名: EmployeeService
 *创建者: zdx
 *创建时间:2021/3/16 14:13
 *描述: TODO
 */

import com.ssm_crud.bean.Employee;
import com.ssm_crud.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 查询所有员工
     * @return
     */
    public List<Employee> getAll() {

        return employeeMapper.selectByExampleWithDept(null);
    }
}
