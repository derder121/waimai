package com.kece.fanta.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kece.fanta.common.R;
import com.kece.fanta.entity.User;
import com.kece.fanta.service.UserService;
import com.kece.fanta.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送邮箱验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取邮箱账号
        String phone = user.getPhone();

        String subject = "瑞吉餐购登录验证码";

        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            String context = "欢迎使用瑞吉餐购，登录验证码为: " + code + ",五分钟内有效，请妥善保管!";
            log.info("code={}", code);

            // 真正地发送邮箱验证码
            userService.sendMsg(phone, subject, context);

            //  将随机生成的验证码保存到session中
            session.setAttribute(phone, code);

            // 验证码由保存到session 优化为 缓存到Redis中，并且设置验证码的有效时间为 5分钟
//            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("验证码发送成功，请及时查看!");
        }
        return R.error("验证码发送失败，请重新输入!");
    }

    /**
     * 用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        String attribute = session.getAttribute(phone).toString();
        if (attribute != null && attribute.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");

    }

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request) {
        //清理Session中保存的当地登录用户的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
