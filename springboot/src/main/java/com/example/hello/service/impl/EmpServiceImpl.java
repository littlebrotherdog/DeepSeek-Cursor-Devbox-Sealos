package com.example.hello.service.impl;

import com.example.hello.entity.Emp;
import com.example.hello.entity.EmpExpr;
import com.example.hello.mapper.EmpExprMapper;
import com.example.hello.mapper.EmpMapper;
import com.example.hello.service.EmpService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    @Override
    public Map<String, Object> pageList(String name, Integer gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Emp> emps = empMapper.selectByCondition(name, gender, begin, end);
        emps.forEach(emp -> emp.setExprList(empExprMapper.selectByEmpId(emp.getId())));
        Page<Emp> p = (Page<Emp>) emps;
        Map<String, Object> result = new HashMap<>();
        result.put("total", p.getTotal());
        result.put("rows", emps);
        return result;
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    @Transactional
    public void add(Emp emp) {
        empMapper.insert(emp);
        if (emp.getExprList() != null && !emp.getExprList().isEmpty()) {
            emp.getExprList().forEach(expr -> expr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(emp.getExprList());
        }
    }

    @Override
    public Emp getById(Integer id) {
        Emp emp = empMapper.selectById(id);
        if (emp != null) {
            emp.setExprList(empExprMapper.selectByEmpId(id));
        }
        return emp;
    }

    @Override
    @Transactional
    public void update(Emp emp) {
        empMapper.update(emp);
        empExprMapper.deleteByEmpId(emp.getId());
        if (emp.getExprList() != null && !emp.getExprList().isEmpty()) {
            emp.getExprList().forEach(expr -> expr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(emp.getExprList());
        }
    }

    @Override
    public List<Emp> listAll() {
        return empMapper.listAll();
    }

    @Override
    public Emp login(String username, String password) {
        Emp emp = empMapper.selectByUsername(username);
        if (emp != null && emp.getPassword() != null && emp.getPassword().equals(password)) {
            return emp;
        }
        return null;
    }
} 