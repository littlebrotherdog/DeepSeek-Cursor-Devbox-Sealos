package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.entity.Emp;
import com.example.hello.service.EmpService;
import com.example.hello.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private EmpService empService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> param) {
        String username = param.get("username");
        String password = param.get("password");
        Emp emp = empService.login(username, password);
        if (emp == null) {
            return Result.error("用户名或密码错误");
        }
        String token = jwtUtil.createToken(emp.getId(), emp.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("id", emp.getId());
        data.put("username", emp.getUsername());
        data.put("name", emp.getName());
        data.put("token", token);
        return Result.success(data);
    }
} 