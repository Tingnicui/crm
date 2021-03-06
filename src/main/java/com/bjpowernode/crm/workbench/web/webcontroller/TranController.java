package com.bjpowernode.crm.workbench.web.webcontroller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入交易控制器");
        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if ("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)){
            getHistoryListByTranId(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if ("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }

    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = tranService.getCharts();
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        Tran tran = new Tran();
        tran.setId(tranId);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag = tranService.changeStage(tran);

        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        tran.setPossibility(possibility);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("tran",tran);
        PrintJson.printJsonObj(response,map);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String tranId = request.getParameter("tranId");
        List<TranHistory> historyList = tranService.getHistoryListByTranId(tranId);

        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        for (TranHistory tranHistory:historyList){
            String stage = tranHistory.getStage();
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }
        PrintJson.printJsonObj(response,historyList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id = request.getParameter("id");
        Tran tran = tranService.detail(id);

        String stage = tran.getStage();
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        tran.setPossibility(possibility);
        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Tran tran = new Tran();
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag = tranService.save(tran,customerName);

        if (flag){
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }


    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> customerNameList = customerService.getCustomerName(name);
        PrintJson.printJsonObj(response,customerNameList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserlist();
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

    }
}
