package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

//
//    @Autowired
//    private HttpServletRequest Request;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //  后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());


        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    //添加员工
    @Override
    public void add(EmployeeDTO employeeDTO) {


        //1.手动创建出来一个Employee对象

        Employee employee = new Employee();

        //2.把dto里面的数据搬运到employee对象上

        BeanUtils.copyProperties(employeeDTO, employee); //拷贝同名属性


        //3.看看employee对象还缺什么属性没有值，缺少什么，我们就补充什么

        employee.setStatus(1);

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //创建用户和更新用户的人

//        String header = Request.getHeader("token");
//
//        Claims claims = JwtUtil.parseJWT("itcast", header);
//
//        Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employee.setPassword("123456");

        //4.调用dao完成工作

        employeeMapper.add(employee);
    }

    @Override
    public PageResult page(EmployeePageQueryDTO dto) {


        //1.使用分页插件设置查询第几页，每页多少条

        PageHelper.startPage(dto.getPage(),dto.getPageSize());

        //2.调用Mapper

        Page<Employee> page = employeeMapper.page(dto);




        //3.封装结果放回
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Override
    public void updateStatus(Integer status, Long id) {

        //1.组装对象
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        //2.调用mapper

        employeeMapper.update(employee);

    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Override
    public Employee findById(Long id) {

        Employee employee = employeeMapper.findById(id);

        //有可能id写错了
        if(employee  == null)
        {
            return null;
        }
        employee.setPassword("****");

        return employee;

    }

    /**
     * 更新员工
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {

        //构建一个Employee对象
        Employee employee = new Employee();
        //同名搬运
        BeanUtils.copyProperties(employeeDTO, employee);
        //剩余的搬运
        employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setUpdateTime(LocalDateTime.now());


        employeeMapper.update(employee);

    }

}
