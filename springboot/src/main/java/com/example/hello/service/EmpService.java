package com.example.hello.service;

import com.example.hello.entity.Emp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EmpService {
    Map<String, Object> pageList(String name, Integer gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize);
    void deleteByIds(List<Integer> ids);
    void add(Emp emp);
    Emp getById(Integer id);
    void update(Emp emp);
    List<Emp> listAll();
    Emp login(String username, String password);
} 