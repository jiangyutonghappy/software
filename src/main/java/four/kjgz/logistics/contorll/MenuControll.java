package four.kjgz.logistics.contorll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import four.kjgz.logistics.bean.Menu;
import four.kjgz.logistics.bean.RoleMenu;
import four.kjgz.logistics.mapper.MenuMapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuControll {
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 获取菜单列表
     * 登陆后才可以访问
     * 未登录访问的话返回未登录
     * @param num
     * @return
     * @throws JsonProcessingException
     */
//    @RequiresPermissions("select")
//    @PostMapping("/showMenu")
//    public String showMenu(@RequestParam("num") String num)throws JsonProcessingException {
//        Integer rid = num.charAt(0) - '0';
//        System.out.println(rid);
//        List<Menu> menuList = menuMapper.findByRid(rid);
//        Map<String,Object> map = new HashMap<>();
//        map.put("menus",menuList);
//        map.put("rid",rid);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String s = objectMapper.writeValueAsString(map);
//        return s;
//    }

    @PostMapping("/showAllMenu")
    public List<Map<String,Object>> showAllMenu()throws JsonProcessingException {
        List<RoleMenu> roleMenuList = menuMapper.findAll();
        List<String> menus1 = new ArrayList<>();
        List<String> menus2 = new ArrayList<>();
        List<String> menus3 = new ArrayList<>();
        for (RoleMenu roleMenu:roleMenuList){
            if (roleMenu.getRid() == 1){
                menus1.add(roleMenu.getName());
            }else if (roleMenu.getRid() == 2){
                menus2.add(roleMenu.getName());
            }else if (roleMenu.getRid() == 3){
                menus3.add(roleMenu.getName());
            }
        }
        Map<String,Object> map1 = new HashMap<>();
        Map<String,Object> map2 = new HashMap<>();
        Map<String,Object> map3 = new HashMap<>();
        map1.put("role","admin");
        map2.put("role","staff");
        map3.put("role","customer");
        map1.put("menus",menus1);
        map2.put("menus",menus2);
        map3.put("menus",menus3);

        List<Map<String,Object>> roleMenus = new ArrayList<>();
        roleMenus.add(map1);
        roleMenus.add(map2);
        roleMenus.add(map3);

        return roleMenus;
    }
}
