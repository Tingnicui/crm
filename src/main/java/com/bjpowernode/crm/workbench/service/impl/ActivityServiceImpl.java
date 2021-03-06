package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao =SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    public Boolean save(Activity activity) {
        Boolean flag = true;


        int count = activityDao.save(activity);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<Activity> pageList(Map<String, Object> map) {
//        获取total
        int total = activityDao.getTotalByCondition(map);
//        获取dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
//        封装
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
//        返回
        return vo;
    }

    public Boolean delete(String[] ids) {
        Boolean flag = true;
        int count1 = activityRemarkDao.getCountByAids(ids);
        int count2 = activityRemarkDao.deleteByAids(ids);
        if (count1 != count2){
            flag = false;
        }
        int count3 = activityDao.delete(ids);
        if (ids.length != count3){
            flag = false;
        }
        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
//        获取userList
        List<User> userList = userDao.getUserlist();
//        获取activity
        Activity activity = activityDao.getById(id);
//        返回map
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    public Boolean update(Activity activity) {
        Boolean flag = true;


        int count = activityDao.update(activity);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);

        return activity;
    }

    public List<ActivityRemark> getRemarkListById(String activityId) {
        List<ActivityRemark> activityRemarkList = activityRemarkDao.getListByAid(activityId);
        return activityRemarkList;
    }

    public Boolean deleteRemarkById(String id) {
        Boolean flag = true;

        int count = activityRemarkDao.deleteRemarkById(id);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public Boolean saveRemark(ActivityRemark ar) {
        Boolean flag = true;

        int count = activityRemarkDao.save(ar);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public Boolean updateRemarkById(ActivityRemark activityRemark) {
        Boolean flag = true;

        int count = activityRemarkDao.updateRemarkById(activityRemark);
        if (1 != count){
            flag = false;
        }
        return flag;
    }

    public List<Activity> getActivityListByClueId(String id) {
        List<Activity> list = activityDao.getActivityListByClueId(id);
        return list;
    }

    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> list = activityDao.getActivityListByNameAndNotByClueId(map);
        return list;
    }

    public List<Activity> getActivityListByName(String aname) {
        List<Activity> list = activityDao.getActivityListByName(aname);
        return list;
    }
}
