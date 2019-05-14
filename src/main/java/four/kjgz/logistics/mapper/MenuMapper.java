package four.kjgz.logistics.mapper;

import four.kjgz.logistics.bean.Menu;
import four.kjgz.logistics.bean.RoleMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper {
    @Select("select Menu.name from RoleMenu, Menu where RoleMenu.rid = #{rid} and RoleMenu.mid = Menu.id")
    public List<String> findByRid(Integer rid);

    @Select("select rid,name from RoleMenu, Menu where RoleMenu.mid = Menu.id")
    public List<RoleMenu> findAll();
}
