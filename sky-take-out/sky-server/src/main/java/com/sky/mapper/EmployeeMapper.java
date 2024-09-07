package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     */
    @Insert("insert into employee values (null,#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Employee employee);


    /**
     * 分页查询
     * @param dto
     * @return
     */
    Page<Employee> page(EmployeePageQueryDTO dto);


    /**
     * 根据id更新员工状态
     * @param employee
     */
    void update(Employee employee);


    /**
     * id查询
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee findById(Long id);

}
