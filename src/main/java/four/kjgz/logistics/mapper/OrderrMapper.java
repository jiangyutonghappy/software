package four.kjgz.logistics.mapper;

import four.kjgz.logistics.bean.orderr;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface OrderrMapper {
    @Select("select * from orderr where ordernum = #{num}")
    public orderr findByOrdernum(String num);

    @Options(useGeneratedKeys = true,keyProperty = "id")//是不是使用自动生成的组件 那个属性就是那个主键
    @Insert("insert into orderr(cid,sname,sphone,saddress,sdate,rname,rphone,raddress,goodstype,paytype,ordernum,status) values(#{cid},#{sname},#{sphone},#{saddress},#{sdate},#{rname},#{rphone},#{raddress},#{goodstype},#{paytype},#{ordernum},0)")
    public int insertOrderr(orderr orderr);

    @Delete("delete from orderr where id=#{id}")
    public int delOrderr(Integer id);

    @Update("update orderr set sname = #{sname},sphone = #{sphone},saddress = #{saddress},sdate = #{sdate},rname = #{rname},rphone = #{rphone},raddress = #{raddress} where id = #{id}")
    public int updateOrderr(orderr orderr);

    @Update("update orderr set status = #{status} where id = #{id}")
    public int updateOrderrStatus(Integer id, Integer status);

    @Select("select * from orderr where cid = #{cid}")
    public List<orderr> selectOrdersBySid(Integer cid);

    @Select("select * from orderr")
    public List<orderr> selectAll();

    @Select("select count(id) from orderr")
    public int getCount();
}
