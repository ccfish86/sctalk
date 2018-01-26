package com.webjava.web.restcontroller;

/**
 * Created by wx on 2017/10/27.
 */


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.power.grpc.Power;
import com.power.grpc.PowerRequest;
import com.power.grpc.PowerResponse;
import com.power.grpc.PowerServiceGrpc;
import com.webjava.model.power_info;
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
public class PowerRestController {

    private static final String HOST = "localhost";
    private static final int PORT = 50051;

    @RequestMapping(value="/power/modify", method= RequestMethod.POST)
    public void modifyPower(HttpServletRequest request, HttpServletResponse response){

        String strData=HttpUtils.getJsonBody(request);

        Gson gson = new Gson();
        power_info power=gson.fromJson(strData,power_info.class);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        PowerServiceGrpc.PowerServiceBlockingStub stub =
                PowerServiceGrpc.newBlockingStub(channel);

        // Create a request
        PowerRequest modifyRequest = PowerRequest.newBuilder()
                .setPowerId(power.getPowerId())
                .setPowerUrl(power.getPowerUrl())
                .setPowerName(power.getPowerName())
                .setParentId(power.getParentId())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        PowerResponse modifyResponse = stub.modifyPower(modifyRequest);

        if(modifyResponse.getStatusId()==0){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"修改成功!"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"修改失败!"));
        }

    }

    @RequestMapping(value = "/power/add",method = RequestMethod.POST)
    public void addPower(HttpServletRequest request, HttpServletResponse response){

        String  strJson= HttpUtils.getJsonBody(request);

        Gson gson=new Gson();

        power_info power=gson.fromJson(strJson,power_info.class);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        PowerServiceGrpc.PowerServiceBlockingStub stub =
                PowerServiceGrpc.newBlockingStub(channel);

        // Create a request
        PowerRequest addPowerRequest = PowerRequest.newBuilder()
                .setParentId(power.getParentId())
                .setPowerName(power.getPowerName())
                .setPowerUrl(power.getPowerUrl())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        PowerResponse adminResponse = stub.addPower(addPowerRequest);

        if(adminResponse.getStatusId()==0){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"添加成功"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"内容存在"));
        }


    }

    @RequestMapping(value = "/power/remove",method = RequestMethod.POST)
    public void removePower(HttpServletRequest request,HttpServletResponse response ) {

        String strjson = HttpUtils.getJsonBody(request);
        List<Integer> list=new ArrayList<Integer>();
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        list = new Gson().fromJson(strjson, type);


        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        PowerServiceGrpc.PowerServiceBlockingStub stub =
                PowerServiceGrpc.newBlockingStub(channel);

        PowerRequest.Builder builder = PowerRequest.newBuilder();
        // Create a request
        for (int i :list) {
            Power.Builder bu = Power.newBuilder();
            bu.setPowerId(i);
            Power user =bu.build();
            builder.addPower(user);
        }

        PowerRequest removePowerRequest = builder.build();

        // Send the request using the stub
        System.out.println("Client sending request");
        PowerResponse userResponse = stub.removePower(removePowerRequest);

        if (userResponse.getStatusId()==0) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "删除成功！"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "部分信息未找到或未删除！"));
        }

    }

    @RequestMapping(value = "/power/list",method = RequestMethod.GET)
    public void listPower(HttpServletRequest request, HttpServletResponse response) throws InvalidProtocolBufferException, InvalidProtocolBufferException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        PowerServiceGrpc.PowerServiceBlockingStub stub =
                PowerServiceGrpc.newBlockingStub(channel);

        // Create a request
        PowerRequest listPowerRequest = PowerRequest.newBuilder().build();

        // Send the request using the stub
        System.out.println("Client sending request");
        PowerResponse powerResponse = stub.listPower(listPowerRequest);


        if(powerResponse.getStatusId()==0){

            String data= JsonFormat.printer().includingDefaultValueFields().preservingProtoFieldNames().print(powerResponse);
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"显示所有用户",data));
        }else
        {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }

    }

    @RequestMapping(value = "/getRoute",method = RequestMethod.GET)
    public void getRoute(HttpServletRequest request,HttpServletResponse response) throws InvalidProtocolBufferException {
        String token = request.getParameter("token");
        System.out.println(token);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        PowerServiceGrpc.PowerServiceBlockingStub stub =
                PowerServiceGrpc.newBlockingStub(channel);

        PowerRequest setRequest =PowerRequest.newBuilder()
                .setToken(token)
                .build();


        PowerResponse getResponse =stub.getRoute(setRequest);

        if(getResponse.getStatusId()==0){

            String data= JsonFormat.printer().preservingProtoFieldNames().print(getResponse);

            HttpUtils.setJsonBody(response,new ResponseInfo(0,"获取路由信息",data));
        }else
        {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }


    }

    @RequestMapping(value = "/getPower",method = RequestMethod.POST)
    public void getPower(HttpServletRequest request,HttpServletResponse response) throws InvalidProtocolBufferException {
        String strData = HttpUtils.getJsonBody(request);
        Gson gson=new Gson();
        role_info role=gson.fromJson(strData,role_info.class);
        int id = role.getRoleId();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        PowerServiceGrpc.PowerServiceBlockingStub stub =
                PowerServiceGrpc.newBlockingStub(channel);

        PowerRequest setRequest =PowerRequest.newBuilder()
                .setId(id)
                .build();


        PowerResponse getResponse =stub.getPower(setRequest);

        if(getResponse.getStatusId()==0){

            String data= JsonFormat.printer().preservingProtoFieldNames().print(getResponse);

            HttpUtils.setJsonBody(response,new ResponseInfo(0,"获取该角色权限",data));
        }else
        {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }


    }

}