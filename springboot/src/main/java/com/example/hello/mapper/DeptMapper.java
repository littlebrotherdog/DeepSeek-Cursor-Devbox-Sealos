package com.example.hello.mapper;

import com.example.hello.entity.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    
    @Select("SELECT * FROM dept")
    List<Dept> list();

    @Delete("DELETE FROM dept WHERE id = #{id}")
    void deleteById(Integer id);

    @Insert("INSERT INTO dept(name) VALUES(#{name})")
    void insert(Dept dept);

    @Select("SELECT * FROM dept WHERE id = #{id}")
    Dept getById(Integer id);

    @Update("UPDATE dept SET name = #{name} WHERE id = #{id}")
    void update(Dept dept);
} 