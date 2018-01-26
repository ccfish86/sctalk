package com.webjava.web.restcontroller;
/**
 * Created by wx on 2017/10/27.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webjava.kernel.entity.IMGroup;
import com.webjava.kernel.service.IGroupService;
import com.webjava.utils.HttpUtils;
import com.webjava.utils.ResponseInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
public class GroupRestController {

    @Resource
    private IGroupService groupService;


    @RequestMapping(value = "/group/list",method = RequestMethod.GET)
    public void listGroup(HttpServletRequest request, HttpServletResponse response){
        List<IMGroup> groups=this.groupService.getAllGroup();
        if(groups.size()>0){
            Gson gson=new Gson();
            String data=gson.toJson(groups);
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"显示所有用户",data));
        }
        else
        {
            System.out.println("fail");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }
    }

    @RequestMapping(value = "/group/add",method = RequestMethod.POST)
    public void addGroup(HttpServletRequest request, HttpServletResponse response){

        String  strJson= HttpUtils.getJsonBody(request);

        Gson gson=new Gson();

        IMGroup group=gson.fromJson(strJson,IMGroup.class);

        System.out.println(group);
        IMGroup existgroup =this.groupService.getGroupByName(group.getName());
        if(existgroup !=null){
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"用户名已存在！"));
        }
        else{
            this.groupService.addGroup(group);
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"添加用户成功！"));
        }

    }

    @RequestMapping(value = "/group/remove",method = RequestMethod.POST)
    public void removeGroup(HttpServletRequest request,HttpServletResponse response ){

        String strjson = HttpUtils.getJsonBody(request);
        List<Integer> list=new ArrayList<Integer>();
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        list = new Gson().fromJson(strjson, type);

        for(int i : list){
            Integer id = i;
            IMGroup exitId =this.groupService.getGroupById(id);
            if(exitId!=null){
                this.groupService.deleteGroup(id);
                HttpUtils.setJsonBody(response,new ResponseInfo(0,"修改成功！"));
            }else
            {
                HttpUtils.setJsonBody(response,new ResponseInfo(1,"无此ID信息！"));
            }
        }
    }

    @RequestMapping(value = "/group/update",method = RequestMethod.POST)
    public void updateGroup (HttpServletRequest request,HttpServletResponse response){
        String strData =HttpUtils.getJsonBody(request);
        Gson gson=new Gson();
        IMGroup group = gson.fromJson(strData,IMGroup.class);
        IMGroup existgroup=this.groupService.getGroupById(group.getId());
        if(existgroup !=null){
            this.groupService.updateGroup(group);
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"更新用户信息成功！"));
        }
        else {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无此用户，用户信息更新失败"));
        }
    }

}