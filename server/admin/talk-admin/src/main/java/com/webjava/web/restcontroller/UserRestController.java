package com.webjava.web.restcontroller;

/**
 * Created by wx on 2017/10/27.
 */


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.user.grpc.User;
import com.user.grpc.UserRequest;
import com.user.grpc.UserResponse;
import com.user.grpc.UserServiceGrpc;
import com.webjava.kernel.entity.IMUser;
import com.webjava.utils.EncryptHelper;
import com.webjava.utils.HttpUtils;
import com.webjava.utils.ResponseInfo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



@RestController
@RequestMapping("/users")
public class UserRestController {


    private static final String HOST = "localhost";
    private static final int PORT = 50051;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/user/list",method = RequestMethod.GET)
    public void listUser(HttpServletRequest request, HttpServletResponse response) throws InvalidProtocolBufferException, InvalidProtocolBufferException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);

        // Create a request
        UserRequest listUserRequest = UserRequest.newBuilder().build();

        // Send the request using the stub
        System.out.println("Client sending request");
        UserResponse userResponse = stub.listUser(listUserRequest);


        if(userResponse.getStatusId()==1){

            String data= JsonFormat.printer().includingDefaultValueFields().preservingProtoFieldNames().print(userResponse);

            HttpUtils.setJsonBody(response,new ResponseInfo(0,"显示所有用户",data));
        }else
        {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }

    }

    @RequestMapping(value = "/user/add",method = RequestMethod.POST)
    public void addUser(HttpServletRequest request, HttpServletResponse response){

        String saltStr = Integer.toString(new Random().nextInt(10000));
        String  strJson= HttpUtils.getJsonBody(request);

        Gson gson=new Gson();

        IMUser user=gson.fromJson(strJson,IMUser.class);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);
        
        String password = passwordEncoder.encode(user.getPassword());;
        
        // Create a request
        UserRequest addUserRequest = UserRequest.newBuilder()
                .setName(user.getName())
                .setSex(user.getSex())
                .setNick(user.getNick())
                .setPhone(user.getPhone())
                // .setPassword(EncryptHelper.encodeByMD5(user.getPassword()+saltStr))
                .setPassword(password)
                .setSalt(saltStr)
                .setEmail(user.getEmail())
                .setDepartid(user.getdepartid())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        UserResponse userResponse = stub.addUser(addUserRequest);

        if(userResponse.getStatusId()==0){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"添加成功"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"内容存在"));
        }


    }

    @RequestMapping(value = "/user/remove",method = RequestMethod.POST)
    public void removeUser(HttpServletRequest request,HttpServletResponse response ) {

        String strjson = HttpUtils.getJsonBody(request);
        List<Integer> list=new ArrayList<Integer>();
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        list = new Gson().fromJson(strjson, type);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);

        UserRequest.Builder builder = UserRequest.newBuilder();
        // Create a request
        for(int i : list){
            User.Builder bu = User.newBuilder();
            bu.setId(i);
            User user = bu.build();
            builder.addUser(user);
        }

        UserRequest removeUserRequest = builder.build();

        // Send the request using the stub
        System.out.println("Client sending request");
        UserResponse userResponse = stub.removeUser(removeUserRequest);

        if (userResponse.getStatusId()==1) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "删除成功！"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "部分信息未找到或未删除！"));
        }

    }

    @RequestMapping(value = "/user/update",method = RequestMethod.POST)
    public void updateUser (HttpServletRequest request,HttpServletResponse response){
        String strData =HttpUtils.getJsonBody(request);
        Gson gson=new Gson();
        IMUser user = gson.fromJson(strData,IMUser.class);


        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);

        // Create a request
        UserRequest modifyUserRequest = UserRequest.newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setSex(user.getSex())
                .setNick(user.getNick())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setDepartid(user.getdepartid())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        UserResponse userResponse = stub.modifyUser(modifyUserRequest);

        if(userResponse.getStatusId()==1){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"修改成功"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"内容不存在"));
        }


    }


    @RequestMapping(value = "/user/updatePassword",method = RequestMethod.POST)
    public void updatePassword (HttpServletRequest request,HttpServletResponse response){

        String saltStr = Integer.toString(new Random().nextInt(10000));
        String strData =HttpUtils.getJsonBody(request);
        Gson gson=new Gson();


        IMUser user = gson.fromJson(strData,IMUser.class);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);

        // Create a request
        UserRequest modifyUserRequest = UserRequest.newBuilder()
                .setId(user.getId())
                .setPassword(EncryptHelper.encodeByMD5(user.getPassword()+saltStr))
                .setSalt(saltStr)
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        UserResponse userResponse = stub.modifyPassword(modifyUserRequest);

        if(userResponse.getStatusId()==1){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"修改成功"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1 ,"内容不存在"));
        }

    }

}