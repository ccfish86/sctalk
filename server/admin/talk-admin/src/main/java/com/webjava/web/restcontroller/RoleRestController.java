package com.webjava.web.restcontroller;

/**
 * Created by wx on 2017/10/27.
 */


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.role.grpc.Role;
import com.role.grpc.RoleRequest;
import com.role.grpc.RoleResponse;
import com.role.grpc.RoleServiceGrpc;
import com.webjava.kernel.entity.IMManager;
import com.webjava.model.role_info;
import com.webjava.utils.HttpUtils;
import com.webjava.utils.ResponseInfo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
public class RoleRestController {

    private static final String HOST = "localhost";
    private static final int PORT = 50051;

    @RequestMapping(value="/role/modify", method= RequestMethod.POST)
    public void modifyRole(HttpServletRequest request, HttpServletResponse response){

        String strData=HttpUtils.getJsonBody(request);

        Gson gson = new Gson();
        role_info role=gson.fromJson(strData,role_info.class);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        RoleServiceGrpc.RoleServiceBlockingStub stub =
                RoleServiceGrpc.newBlockingStub(channel);

        // Create a request
        RoleRequest modifyRequest = RoleRequest.newBuilder()
                .setRoleId(role.getRoleId())
                .setRoleName(role.getRoleName())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        RoleResponse modifyResponse = stub.modifyRole(modifyRequest);

        if(modifyResponse.getStatusId()==0){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"修改成功!"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"修改失败!"));
        }

    }

    @RequestMapping(value = "/role/add",method = RequestMethod.POST)
    public void addRole(HttpServletRequest request, HttpServletResponse response){

        String  strJson= HttpUtils.getJsonBody(request);

        Gson gson=new Gson();

        role_info role=gson.fromJson(strJson,role_info.class);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        RoleServiceGrpc.RoleServiceBlockingStub stub =
                RoleServiceGrpc.newBlockingStub(channel);

        // Create a request
        RoleRequest addRoleRequest = RoleRequest.newBuilder()
                .setRoleName(role.getRoleName())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        RoleResponse adminResponse = stub.addRole(addRoleRequest);

        if(adminResponse.getStatusId()==0){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"添加成功"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"内容存在"));
        }


    }

    @RequestMapping(value = "/role/remove",method = RequestMethod.POST)
    public void removeRole(HttpServletRequest request,HttpServletResponse response ) {

        String strjson = HttpUtils.getJsonBody(request);
        List<Integer> list=new ArrayList<Integer>();
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        list = new Gson().fromJson(strjson, type);


        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        RoleServiceGrpc.RoleServiceBlockingStub stub =
                RoleServiceGrpc.newBlockingStub(channel);

        RoleRequest.Builder builder = RoleRequest.newBuilder();
        // Create a request
        for (int i: list) {
            Role.Builder bu = Role.newBuilder();
            bu.setRoleId(i);
            Role user =bu.build();
            builder.addRole(user);
        }

        RoleRequest removeRoleRequest = builder.build();

        // Send the request using the stub
        System.out.println("Client sending request");
        RoleResponse userResponse = stub.removeRole(removeRoleRequest);

        if (userResponse.getStatusId()==0) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "删除成功！"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "部分信息未找到或未删除！"));
        }

    }

    @RequestMapping(value = "/role/list",method = RequestMethod.GET)
    public void listRole(HttpServletRequest request, HttpServletResponse response) throws InvalidProtocolBufferException, InvalidProtocolBufferException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        RoleServiceGrpc.RoleServiceBlockingStub stub =
                RoleServiceGrpc.newBlockingStub(channel);

        // Create a request
        RoleRequest listRoleRequest = RoleRequest.newBuilder().build();

        // Send the request using the stub
        System.out.println("Client sending request");
        RoleResponse roleResponse = stub.listRole(listRoleRequest);


        if(roleResponse.getStatusId()==0){

            String data= JsonFormat.printer().includingDefaultValueFields().preservingProtoFieldNames().print(roleResponse);

            HttpUtils.setJsonBody(response,new ResponseInfo(0,"显示所有用户",data));
        }else
        {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }

    }

    @RequestMapping(value = "/role/changePower",method = RequestMethod.POST)
    public void changePower(HttpServletRequest request,HttpServletResponse response) {
        String strjson = HttpUtils.getJsonBody(request);
        Gson gson = new Gson();
        List<Integer> list=new ArrayList<Integer>();

        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        list = new Gson().fromJson(strjson, type);
        int id=list.get(list.size()-1);
        list.remove(list.size()-1);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        RoleServiceGrpc.RoleServiceBlockingStub stub =
                RoleServiceGrpc.newBlockingStub(channel);

        // Create a request
        RoleRequest changePowerRequest =RoleRequest.newBuilder()
                .setRoleId(id)
                .addAllId(list)
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        RoleResponse userResponse = stub.changePower(changePowerRequest);

        if (userResponse.getStatusId()==0) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "修改权限成功！"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "修改权限失败！"));
        }
    }

    @RequestMapping(value = "/role/getRole",method = RequestMethod.POST)
    public void getRole(HttpServletRequest request,HttpServletResponse response) throws InvalidProtocolBufferException {
        String strData = HttpUtils.getJsonBody(request);
        Gson gson=new Gson();
        IMManager manager=gson.fromJson(strData,IMManager.class);
        int id = manager.getId();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        RoleServiceGrpc.RoleServiceBlockingStub stub =
                RoleServiceGrpc.newBlockingStub(channel);

        RoleRequest setRequest =RoleRequest.newBuilder()
                .setManagerId(id)
                .build();


        RoleResponse getResponse =stub.getRole(setRequest);

        if(getResponse.getStatusId()==0){

            String data= JsonFormat.printer().preservingProtoFieldNames().print(getResponse);

            HttpUtils.setJsonBody(response,new ResponseInfo(0,"获取该用户角色",data));
        }else
        {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }


    }

}