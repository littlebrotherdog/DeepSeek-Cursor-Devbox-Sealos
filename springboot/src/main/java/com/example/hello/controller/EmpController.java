package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.entity.Emp;
import com.example.hello.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping
    public Result<Map<String, Object>> pageList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) LocalDate begin,
            @RequestParam(required = false) LocalDate end,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(empService.pageList(name, gender, begin, end, page, pageSize));
    }

    @DeleteMapping
    public Result<Void> delete(@RequestParam String ids) {
        List<Integer> idList = Arrays.stream(ids.split(",")).map(Integer::parseInt).toList();
        empService.deleteByIds(idList);
        return Result.success();
    }

    @PostMapping
    public Result<Void> add(@RequestBody Emp emp) {
        empService.add(emp);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Emp> getById(@PathVariable Integer id) {
        return Result.success(empService.getById(id));
    }

    @PutMapping
    public Result<Void> update(@RequestBody Emp emp) {
        empService.update(emp);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Emp>> listAll() {
        return Result.success(empService.listAll());
    }
} 