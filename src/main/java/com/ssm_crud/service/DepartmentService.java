package com.ssm_crud.service;/*
 *文件名: DepartmentService
 *创建者: zdx
 *创建时间:2021/3/18 13:48
 *描述: TODO
 */

import com.ssm_crud.bean.Department;
import com.ssm_crud.dao.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    public List<Department> getDepts() {
        List<Department> list = departmentMapper.selectByExample(null);
        return list;
    }
}
