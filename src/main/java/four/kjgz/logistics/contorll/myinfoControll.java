package four.kjgz.logistics.contorll;

import com.alibaba.fastjson.JSONObject;
import four.kjgz.logistics.bean.*;
import four.kjgz.logistics.contorll.loginControll;
import four.kjgz.logistics.mapper.MenuMapper;
import four.kjgz.logistics.repository.AdministratorsReposity;
import four.kjgz.logistics.repository.CustomerReposity;
import four.kjgz.logistics.repository.StaffReposity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class myinfoControll {
    @Autowired
    loginInfo nowloginInfo;
    @Autowired
    AdministratorsReposity administratorsReposity;
    @Autowired
    CustomerReposity customerReposity;
    @Autowired
    StaffReposity staffReposity;
    @Autowired
    private MenuMapper menuMapper;
    Logger logger = LoggerFactory.getLogger(loginControll.class);

    public List<String> showMenu(String num) {
        Integer rid = num.charAt(0) - '0';
        //System.out.println(rid);
        List<String> menuList = menuMapper.findByRid(rid);
        return menuList;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String login(@RequestParam("token") String token)
    {
        Subject subject = SecurityUtils.getSubject();
        String[] myList = {token};
        JSONObject result = new JSONObject();
        result.put("roles", token);
        result.put("name", nowloginInfo.getMyusername());
        result.put("avatar", nowloginInfo.getMyimge());
        Object obj = subject.getPrincipal();
        System.out.println("没有登录：");
        System.out.println(nowloginInfo.getMyusernum());
        if(nowloginInfo.getMyusernum()==null)
        {
            return "没有登录";
        }
        String num=nowloginInfo.getMyusernum();

        char first = num.charAt(0);
        if (first=='0')//表示为超级管理员
        {
            result.put("introduction", "我是超级管理员");
        }
        else if (first=='1')//表示为管理员
        {
            result.put("introduction", "我是管理员");
        }
        else if(first=='2')//表示工作人员
        {
            result.put("introduction", "我是工作人员");
        }
        else{
            result.put("introduction", "我是顾客");
        }
        List<String> menus = showMenu(num);
        result.put("menus", menus);
        return result.toJSONString();
    }
}
