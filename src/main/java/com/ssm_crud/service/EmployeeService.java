package com.ssm_crud.service;
/*
 *文件名: EmployeeService
 *创建者: zdx
 *创建时间:2021/3/16 14:13
 *描述: TODO
 */

import com.ssm_crud.bean.Employee;
import com.ssm_crud.bean.EmployeeExample;
import com.ssm_crud.bean.Msg;
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
        EmployeeExample  example = new EmployeeExample();
       example.setOrderByClause("emp_id");
        return employeeMapper.selectByExampleWithDept(example);
    }

    /**
     * 员工保存
     * @param employee
     */
    public void saveEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    /**
     * 检验用户名是否可用
     *
     * @param empName
     * @return true ： 代表当前姓名可用   false：不可用
     */

    public boolean checkUser(String empName) {
        EmployeeExample example =new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpNameEqualTo(empName);
       long count = employeeMapper.countByExample(example);
        return  count == 0;
    }

    /**
     * 按照员工id查询员工
     * @param id
     * @return
     */
    public Employee getEmp(Integer id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        return employee;
    }

    /**
     * 员工更新
     * @param employee
     */
    public void updateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    /**
     * 员工删除
     * @param id
     */
    public void deleteEmp(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }

    public void deleteBatch(List<Integer> ids) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        //delete  from xxx where emp_id in (1,2,3)
        criteria.andEmpIdIn(ids);
        employeeMapper.deleteByExample(example);
    }

}
