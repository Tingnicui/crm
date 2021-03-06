package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    Boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    Boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListById(String activityId);

    Boolean deleteRemarkById(String id);

    Boolean saveRemark(ActivityRemark ar);

    Boolean updateRemarkById(ActivityRemark activityRemark);

    List<Activity> getActivityListByClueId(String id);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
