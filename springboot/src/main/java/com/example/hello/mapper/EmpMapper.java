package com.example.hello.mapper;

import com.example.hello.entity.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {
    List<Emp> listAll();

    List<Emp> selectByCondition(@Param("name") String name,
                                @Param("gender") Integer gender,
                                @Param("begin") LocalDate begin,
                                @Param("end") LocalDate end);

    Emp selectById(Integer id);

    void insert(Emp emp);

    void update(Emp emp);

    void deleteByIds(@Param("ids") List<Integer> ids);

    Emp selectByUsername(@Param("username") String username);
} 