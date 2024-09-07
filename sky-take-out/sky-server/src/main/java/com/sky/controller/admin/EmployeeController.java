package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Put;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Api(tags = "员工控制器")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value = "员工登入")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "退出登入")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    //新增员工
    @ApiOperation(value = "添加员工")
    @PostMapping
    public Result add(@RequestBody EmployeeDTO employeeDTO){

        employeeService.add(employeeDTO);

        return Result.success();

    }

    /**
     * 分页查询
     * @return
     */
    @ApiOperation("员工分页")
    @GetMapping("/page")
    public Result page(EmployeePageQueryDTO dto){
        PageResult page = employeeService.page(dto);

        return Result.success(page);

    }


    /**
     * 启用和禁用员工状态码
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用和禁用员工状态码")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status,Long id ){

        employeeService.updateStatus(status,id);

        return Result.success();
    }


    /**
     * 根据id查询数据
     * @param id
     * @return
     */

    @ApiOperation("根据id查询数据")
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){

        Employee employee = employeeService.findById(id);

        return Result.success(employee);
    }


    @ApiOperation("更新员工信息")
    @PutMapping
    public  Result update(@RequestBody EmployeeDTO employeeDTO){

        employeeService.update(employeeDTO);

        return Result.success();

    }


}
