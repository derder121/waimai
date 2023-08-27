package com.kece.fanta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kece.fanta.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
