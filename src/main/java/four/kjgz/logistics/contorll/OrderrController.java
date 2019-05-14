package four.kjgz.logistics.contorll;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import four.kjgz.logistics.bean.*;
import four.kjgz.logistics.mapper.OrderInfMapper;
import four.kjgz.logistics.mapper.OrderrMapper;
import four.kjgz.logistics.repository.OrderrReposity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderrController {
    @Autowired
    private OrderrMapper orderrMapper;
    @Autowired
    private OrderInfMapper orderInfMapper;
    Logger logger = LoggerFactory.getLogger(OrderrController.class);
    @Autowired
     OrderrReposity orderrReposity;
    @Value("${IdWorker.workedId}")
    private Integer workId;

    @Value("${IdWorker.datacenterId}")
    private Integer datacenterId;

    /**
     * 根据订单号查询订单信息
     * 登录+拥有select权限
     * @param num
     * @return
     */
    @MyLog(value = "通过编号查找订单")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/selectOrderByNum", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String selectOrderByNum(@RequestParam("num") String num){
        orderr orderr = orderrMapper.findByOrdernum(num);
        if(orderr==null)
        {
            logger.error("通过编号查找订单失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "通过编号查找订单失败");
            return result.toJSONString();
        }
        else
        {
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "通过名字查找配送点成功");
            result.put("ourdata", orderr);
            return  result.toJSONString();
        }
    }

    /**
     * 根据订单号查询订单当前的物流位置
     * 登录+select
     * @param num
     * @return
     */
    @MyLog(value = "通过编号查找订单位置")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/selectOrderLocationByNum", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String selectOrderLocationByNum(@RequestParam("num") String num){

        if(orderInfMapper.findOrderLocationByOrdernum(num).size()==0)
        {
            logger.error("通过编号查找订单位置失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "通过编号查找订单位置失败");
            return result.toJSONString();
        }
        else
        {
            List<OrderInf> locations = orderInfMapper.findOrderLocationByOrdernum(num);
            OrderInf location = locations.get(0);
            JSONObject result2 = new JSONObject();
            result2.put("location",location);
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "通过编号查找订单位置成功");
            result.put("ourdata", result2);
            return result.toJSONString();
        }

    }


    /**
     * 添加订单
     * 登录+add
     * @param orderr
     * @return
     */
    @MyLog(value = "添加订单")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String addOrder(orderr orderr){
        //生成订单号
        IdWorker idWorker = new IdWorker(workId,datacenterId);
        String ordernum = String.valueOf(idWorker.nextId());

        orderr.setOrdernum(ordernum);
        orderr.setStatus(0);

        int num = orderrMapper.insertOrderr(orderr);
        if (num == 1){
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "添加订单成功");
            result.put("ourdata", orderr);
            return result.toJSONString();
        }else{
            logger.error("添加订单失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "添加订单失败");
            return result.toJSONString();
        }
    }


    /**
     * 删除订单
     * 登录+delete
     * @param id
     * @return
     */
    @MyLog(value = "通过的id删除订单")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/delOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String delOrder(@RequestParam("num") String num){
        if(orderrReposity.findByOrdernum(num).size()==0){
            logger.error("通过的id删除订单失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "通过的id删除订单失败");
            return result.toJSONString();
        }else{
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "通过的id删除订单成功");
            result.put("ourdata", orderrReposity.deleteByOrdernum(num).get(0));
            return result.toJSONString();
        }
    }

    /**
     * 更新订单信息
     * staff或者admin+登录+update
     * @param orderr
     * @return
     */
    @MyLog(value = "更新订单")  //这里添加了AOP的自定义注解
    @PostMapping("/updateOrder")
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateOrder(orderr orderr){
        int i = orderrMapper.updateOrderr(orderr);
        System.out.println("返回值是："+i);
        if (i == 1){
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "更新订单成功");
            result.put("ourdata", "true");
            return result.toJSONString();
        }else {
            logger.error("更新订单失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "更新订单失败");
            return result.toJSONString();

        }
    }



    /**
     * 添加订单的最新位置
     * staff或者admin+登录+add
     * @param orderInf
     * @return
     */
    @MyLog(value = "通过编号更新订单的位置或状态")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/updateOrderLocationByNum", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateOrderLocationAndStateByNum(@RequestParam("ordernum") String ordernum,@RequestParam("raddress") String raddress,@RequestParam("status") Integer status){
        orderr orderr = orderrReposity.findByOrdernum(ordernum).get(0);
        if (orderr == null){
            //订单不存在
            logger.error("通过编号更新订单的位置或状态失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "通过编号更新订单的位置或状态失败");
            return result.toJSONString();
        }else {
            four.kjgz.logistics.bean.orderr orderr1 =orderr;
            orderr1.setRaddress(raddress);
            orderr1.setStatus(status);
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "更新订单成功");
            result.put("ourdata", orderrReposity.save(orderr1));
            return result.toJSONString();
        }
    }



    /**
     * 根据寄件人的id查询该人的订单
     * 登录+select
     * @param cid
     * @return
     */
    @MyLog(value = "通过寄件人的id查找订单")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/selectOrdersByCid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String selectOrdersByCid(@RequestParam("cid") Integer cid){
        List<orderr> orders = orderrMapper.selectOrdersBySid(cid);
        if (orders.size() == 0){
            logger.error("通过寄件人的id查找订单失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "通过寄件人的id查找订单失败");
            return result.toJSONString();
        }else {
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "通过寄件人的id查找订单成功");
            result.put("ourdata", "true");
            return result.toJSONString();
        }
    }

    /**
     * 显示所有订单
     * staff或者admin+登录+select
     * @param pageNum
     * @param pageSize
     * @return
     */
    @MyLog(value = "查找所有的订单")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String selectAll(@RequestParam(defaultValue = "1",value = "page") Integer pageNum, @RequestParam("limit") Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<orderr> orders = orderrMapper.selectAll();
        if(orders.size()==0)
        {
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "无订单");
            return result.toJSONString();
        }
        else
        {
            PageInfo<orderr> pageInfo = new PageInfo<orderr>(orders);
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "查找所有的订单成功");
            result.put("ourdata",pageInfo);
            return result.toJSONString();
        }

    }

    @MyLog(value = "查找所有物流信息")  //这里添加了AOP的自定义注解
    @PostMapping("/selectAllLogisticsInfo")
    public PageInfo<OrderInf> selectAllLogisticsInfo(@RequestParam(defaultValue = "1",value = "page") Integer pageNum,@RequestParam(defaultValue = "5",value = "limit") Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<OrderInf> orderInfos = orderInfMapper.selectAll();
        if (orderInfos.size() == 0){
            return null;
        }else {
            PageInfo<OrderInf> pageInfo = new PageInfo<OrderInf>(orderInfos);
            return pageInfo;
        }
    }


    /**
     * 根据员工的id查询由该员工配送的订单
     * staff或者admin+登录+select
     * @param sid
     * @return
     */
    @MyLog(value = "通过快递人员的id查找订单")  //这里添加了AOP的自定义注解
    @RequestMapping(value = "/selectOrderBySid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String selectOrderBySid(@RequestParam("sid") Integer sid){
        List<OrderInf> orderInfList = orderInfMapper.findOrderBySid(sid);
        if (orderInfList != null){
            JSONObject result = new JSONObject();
            result.put("sts", "1");
            result.put("msg", "通过快递人员的id查找订单成功");
            result.put("ourdata",orderInfList);
            return result.toJSONString();
        }else{
            logger.error("通过快递人员的id查找订单失败");
            JSONObject result = new JSONObject();
            result.put("sts", "0");
            result.put("msg", "通过快递人员的id查找订单失败");
            return result.toJSONString();
        }
    }
}
