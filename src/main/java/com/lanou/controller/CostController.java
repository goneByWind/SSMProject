package com.lanou.controller;

import com.github.pagehelper.PageInfo;
import com.lanou.bean.Cost;
import com.lanou.mapper.CostMapper;
import com.lanou.service.CostService;
import com.lanou.utils.AjaxResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dllo on 17/10/20.
 */
@Controller
@Scope("prototype")
public class CostController {

    @Resource
    private CostService costService;

    @RequestMapping(value = "/costpage_list")
    public String costPageList(){
        return "fee/fee_list";
    }

    // 把cost是否启用的判断方法抽出来
    private void costStartOrNot(List<Cost> costList){
        for (Cost cost : costList) {
            String status = cost.getStatus();
            if (status.equals("1")){
                cost.setStatus("开通");
            }else {
                cost.setStatus("暂停");
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/findAllCost")
    public AjaxResult findAllCost(){

        List<Cost> costList = costService.findAll();

        costStartOrNot(costList);

        return new AjaxResult(costList,0,"返回资费列表成功!");

    }


    @RequestMapping(value = "/costpage_add")
    public String costPageAdd(){
        return "fee/fee_add";
    }

    @ResponseBody
    @RequestMapping(value = "/addNewCost")
    public AjaxResult addNewCost(Cost cost){
        System.out.println(cost);
        cost.setCreatime(new Date());
        cost.setStatus("0");
        costService.addNew(cost);
        return new  AjaxResult("添加资费项目成功");
    }

    @RequestMapping(value = "/costpage_modi")
    public String costPageModify(){
        return "fee/fee_modi";
    }

    @ResponseBody
    @RequestMapping(value = "/modifyCostSaveId")
    public AjaxResult modifyCostSaveIdFromCostList(HttpServletRequest request,@RequestParam("costId") Integer id){

        request.getSession().setAttribute("costIdForModify",id);
        return new AjaxResult("设置点击修改所在tr的costId到session域!");
    }


    @ResponseBody
    @RequestMapping(value = "/modifyCostByIdPre")
    public AjaxResult modifyCostById(HttpServletRequest request){

        HttpSession session = request.getSession();
        Integer costId = (Integer) session.getAttribute("costIdForModify");
        Cost costById = costService.findCostById(costId);
        return new AjaxResult(costById,0,"根据id查找到的资费条目信息");
//        return "redirect:costpage_modi";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteCostById")
    public AjaxResult deleteCostById(@RequestParam("costId") Integer id){
        costService.deleteCostById(id);
        return new AjaxResult("删除资费项目成功!");
    }

    @ResponseBody
    @RequestMapping(value = "/updateCostById")
    public AjaxResult updateCostById(HttpServletRequest request,Cost cost){
        HttpSession session = request.getSession();
        Integer costId = (Integer) session.getAttribute("costIdForModify");
        cost.setCostId(costId);
        costService.updateCost(cost);
        return new AjaxResult("修改资费项目成功!");
    }

    // http://localhost:8080/costpageinfo?no=2&size=6
    @ResponseBody
    @RequestMapping(value = "/costpageinfo")
    public AjaxResult costPageInfo(@RequestParam("no") Integer pageNo,
                                   @RequestParam("size") Integer pageSize){
        PageInfo<Cost> costPageInfo = costService.costPageinfo(pageNo, pageSize);
        List<Cost> costList = costPageInfo.getList();
        costStartOrNot(costList);
        return new AjaxResult(costPageInfo,0,"将资费列表进行分类,当前返回第一页");
    }

    @ResponseBody
    @RequestMapping(value = "/sortCost")
    public AjaxResult sortCost(@RequestParam("sortType") Integer type,
                               @RequestParam("sortOrder") Integer order){
        System.out.print(type);
        System.out.println(" "+order);
        List<Cost> costListSorted = costService.sortCost(type, order);
        costStartOrNot(costListSorted);
        return new AjaxResult(costListSorted,0,"已经按"+type+order+"排序");
    }

    @ResponseBody
    @RequestMapping(value = "/sortCostWithPage")
    public AjaxResult sortCostWithPage(@RequestParam("sortType") Integer type,
                                       @RequestParam("sortOrder") Integer order,
                                       @RequestParam("no") Integer pageNo,
                                       @RequestParam("size") Integer pageSize){

        PageInfo<Cost> costPageInfo = costService.sortCostWithPage(type, order,pageNo,pageSize);
        List<Cost> costList = costPageInfo.getList();
        costStartOrNot(costList);

        return new AjaxResult(costPageInfo,0,"排序with分页");
    }

    @ResponseBody
    @RequestMapping(value = "/startCostById")
    public AjaxResult startCostById(@RequestParam("costId") Integer id){
        Cost costToStart = new Cost();
        costToStart.setCostId(id);
        costToStart.setStatus("1");
        costToStart.setStartime(new Date());
        costService.updateCost(costToStart);

        Cost costById = costService.findCostById(id);
        System.out.println(costById);


        return new AjaxResult("开通id为:"+id+"的资费项目");
    }

    @RequestMapping(value = "/costpage_detail")
    public String costPageDetail(){
        return "fee/fee_detail";
    }


    @ResponseBody
    @RequestMapping(value = "/getDetailById")
    public AjaxResult getDetailById(HttpServletRequest request){
        Integer costIdForModifyAndDesc = (Integer) request.getSession().getAttribute("costIdForModify");
        System.out.println(costIdForModifyAndDesc);
        Cost costForDesc = costService.findCostById(costIdForModifyAndDesc);
        return new AjaxResult(costForDesc,0,"返回点击的详细信息到desc页面");
    }

    @ResponseBody
    @RequestMapping(value = "/showCostNameOptions")
    public AjaxResult showCostNameOptions(){

        List<Cost> costList = costService.findAll();
        return new AjaxResult(costList,0,"返回资费列表");
    }

    @ResponseBody
    @RequestMapping(value = "/judageCostName")
    public AjaxResult judageCostName(@RequestParam("costName")String name){

        String regex = "^[a-zA-Z\\d\\_\\u2E80-\\u9FFF]{0,16}$";

        String rex = "^[a-zA-Z\\d\\_\\u2E80-\\u9FFF]{0,16}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(name);

        // 是否和数据库中已经存在的资费项目重名
        Cost costByName = costService.findCostByName(name);
        if (!m.find()){
            return new AjaxResult(0,"输入验证失败(不符合命名规则)");
        }
        if (!(costByName==null)){
            return new AjaxResult(1,"该资费名称已存在");
        }
        if (name==null||name.length()==0){
            return new AjaxResult(2,"资费名称不能为空");
        }
        return new AjaxResult(3,"验证成功!");
    }
    @ResponseBody
    @RequestMapping(value = "/judageBaseDuration")
    public boolean judageBaseDuration(@RequestParam("baseDuration")String baseDuration){
        String rex = "^[1-5]\\d{2}|^[1-9]\\d{0,1}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(baseDuration);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping(value = "/judageBaseCost")
    public boolean judageBaseCost(@RequestParam("baseCost")String baseCost){
        String rex = "^[1-9]\\d{0,4}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(baseCost);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping(value = "/judageUnitCost")
    public boolean judageUnitCost(@RequestParam("unitCost") String unitCost){
        String rex = "^[1-9]\\d{0,4}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(unitCost);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
    @ResponseBody
    @RequestMapping(value = "/judgeDesc")
    public boolean judgeDesc(@RequestParam("desc") String desc){
        System.out.println(desc);
        String rex = "^[a-zA-Z\\d\\_\\u2E80-\\u9FFF]{0,100}$";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(desc);
        if (m.find()){
            return true;
        }else {
            return false;
        }
    }
}
