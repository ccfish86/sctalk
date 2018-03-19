package com.webjava.web.restcontroller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wx on 2017/10/27.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.manager.grpc.Manager;
import com.manager.grpc.ManagerRequest;
import com.manager.grpc.ManagerResponse;
import com.manager.grpc.ManagerServiceGrpc;
import com.webjava.kernel.entity.IMManager;
import com.webjava.model.CheckLogin;
import com.webjava.utils.HttpUtils;
import com.webjava.utils.ResponseInfo;

import io.grpc.ManagedChannel;
import net.ccfish.talk.admin.service.IUserService;
import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;

@RestController
@RequestMapping("/users")
public class ManagerRestController {

    @GrpcClient("talk-grpc")
    private ManagedChannel channel;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String dataStr = HttpUtils.getJsonBody(request);
        Gson gson = new Gson();
        CheckLogin checkLogin = gson.fromJson(dataStr, CheckLogin.class);

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        // Create a request
        ManagerRequest loginRequest =
                ManagerRequest.newBuilder().setUsername(checkLogin.getUsername())
                        .setPassword(checkLogin.getPassword()).build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse managerResponse = stub.login(loginRequest);

        // login for jwt
        userService.login(checkLogin.getUsername(), checkLogin.getPassword());

        if (managerResponse.getStatusId() == 0) {
            String data = JsonFormat.printer().preservingProtoFieldNames()
                    .print(managerResponse.getManager(0));
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "登录成功", data));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "用户名或密码错误"));
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpUtils.setJsonBody(response, new ResponseInfo(0, "success"));
    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public void getInfo(HttpServletRequest request, HttpServletResponse response, Principal user)
            throws InvalidProtocolBufferException {
        String token = request.getParameter("token");

        System.out.println(user.getName());

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        // Create a request
        ManagerRequest getRequest = ManagerRequest.newBuilder().setToken(token).build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse getResponse = stub.getInfo(getRequest);


        if (getResponse.getStatusId() == 0) {
            String data = JsonFormat.printer().preservingProtoFieldNames()
                    .print(getResponse.getManager(0));
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "获取成功", data));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "获取失败"));
        }

    }

    @RequestMapping(value = "/manager/updatePassword", method = RequestMethod.POST)
    public void modifyPassword(HttpServletRequest request, HttpServletResponse response) {

        String strData = HttpUtils.getJsonBody(request);
        System.out.println(strData);
        Gson gson = new Gson();
        IMManager admin = gson.fromJson(strData, IMManager.class);

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        // Create a request
        ManagerRequest modifyRequest = ManagerRequest.newBuilder().setId(admin.getId())
                .setPassword(admin.getPassword()).build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse modifyResponse = stub.modifyPassword(modifyRequest);

        if (modifyResponse.getStatusId() == 1) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "修改成功!"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "修改失败!"));
        }

    }

    @RequestMapping(value = "/manager/add", method = RequestMethod.POST)
    public void addManager(HttpServletRequest request, HttpServletResponse response) {

        String strJson = HttpUtils.getJsonBody(request);

        Gson gson = new Gson();

        IMManager manager = gson.fromJson(strJson, IMManager.class);

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        // Create a request
        ManagerRequest addManagerRequest = ManagerRequest.newBuilder()
                .setUsername(manager.getUsername()).setPassword(manager.getPassword())
                .setIntroduction(manager.getIntroduction()).setAvatar(manager.getAvatar()).build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse adminResponse = stub.addManager(addManagerRequest);

        if (adminResponse.getStatusId() == 0) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "添加成功"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "内容存在"));
        }


    }

    @RequestMapping(value = "/manager/modify", method = RequestMethod.POST)
    public void modify(HttpServletRequest request, HttpServletResponse response) {
        String strData = HttpUtils.getJsonBody(request);
        Gson gson = new Gson();
        IMManager manager = gson.fromJson(strData, IMManager.class);

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        // Create a request
        ManagerRequest modifyRequest = ManagerRequest.newBuilder().setId(manager.getId())
                .setUsername(manager.getUsername()).setIntroduction(manager.getIntroduction())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse managerResponse = stub.modify(modifyRequest);

        if (managerResponse.getStatusId() == 0) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "修改成功"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "内容不存在"));
        }


    }


    @RequestMapping(value = "/manager/list", method = RequestMethod.GET)
    public void listManager(HttpServletRequest request, HttpServletResponse response)
            throws InvalidProtocolBufferException {

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        // Create a request
        ManagerRequest listManagerRequest = ManagerRequest.newBuilder().build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse managerResponse = stub.listManager(listManagerRequest);


        if (managerResponse.getStatusId() == 0) {

            String data = JsonFormat.printer().preservingProtoFieldNames().print(managerResponse);

            HttpUtils.setJsonBody(response, new ResponseInfo(0, "显示所有", data));
        } else {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "无内容"));
        }
    }

    @RequestMapping(value = "/manager/remove", method = RequestMethod.POST)
    public void removeManager(HttpServletRequest request, HttpServletResponse response) {
        String strjson = HttpUtils.getJsonBody(request);
        List<Integer> list = new ArrayList<Integer>();
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        list = new Gson().fromJson(strjson, type);

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        ManagerRequest.Builder builder = ManagerRequest.newBuilder();
        // Create a request
        for (int i : list) {
            Manager.Builder bu = Manager.newBuilder();
            bu.setId(i);
            Manager manager = bu.build();
            builder.addManager(manager);
        }

        ManagerRequest removeManagerRequest = builder.build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse managerResponse = stub.removeManager(removeManagerRequest);

        if (managerResponse.getStatusId() == 0) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "删除成功！"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "部分信息未找到或未删除！"));
        }
    }

    @RequestMapping(value = "/manager/changeRole", method = RequestMethod.POST)
    public void changeRole(HttpServletRequest request, HttpServletResponse response) {
        String strjson = HttpUtils.getJsonBody(request);
        List<Integer> list = new ArrayList<Integer>();

        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        list = new Gson().fromJson(strjson, type);
        int id = list.get(list.size() - 1);
        list.remove(list.size() - 1);

        // Create a blocking stub with the channel
        ManagerServiceGrpc.ManagerServiceBlockingStub stub =
                ManagerServiceGrpc.newBlockingStub(channel);

        // Create a request
        ManagerRequest changeRoleRequest =
                ManagerRequest.newBuilder().setId(id).addAllRoleId(list).build();

        // Send the request using the stub
        System.out.println("Client sending request");
        ManagerResponse userResponse = stub.changeRole(changeRoleRequest);

        if (userResponse.getStatusId() == 0) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "修改权限成功！"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "修改权限失败！"));
        }
    }
}
