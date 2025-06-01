package com.example.hello.mapper;

import com.example.hello.entity.EmpExpr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EmpExprMapper {
    List<EmpExpr> selectByEmpId(Integer empId);

    void insertBatch(@Param("exprList") List<EmpExpr> exprList);

    void deleteByEmpId(Integer empId);

    void deleteByEmpIds(@Param("ids") List<Integer> ids);
} 