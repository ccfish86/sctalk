package com.grpc.java.server;

import com.grpc.java.kernel.entity.manager_info;
import com.grpc.java.kernel.entity.manager_role_info;
import com.grpc.java.service.ManagerService;
import com.grpc.java.service.Manager_RoleService;
import com.grpc.java.utils.EncryptHelper;
import com.manager.grpc.Manager;
import com.manager.grpc.ManagerRequest;
import com.manager.grpc.ManagerResponse;
import com.manager.grpc.ManagerServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wx on 2017/11/8.
 */


public class ManagerServerImpl extends ManagerServiceGrpc.ManagerServiceImplBase {
    private ManagerService managerService;
    private Manager_RoleService manager_roleService;

    public ManagerServerImpl(BeanContainer service) {
        this.managerService= service.managerService;
        this.manager_roleService=service.manager_roleService;
    }


    @Override
    public void login(ManagerRequest request, StreamObserver<ManagerResponse> responseStreamObserver){
        System.out.println("Received request: " + request);

        String username = request.getUsername();
        String password = request.getPassword();

        password = EncryptHelper.encodeByMD5(password);
        manager_info manager = this.managerService.getName(username);
        if (manager != null && manager.getPassword().equals(password))
        {
            System.out.print("okok");
            Manager.Builder aa=Manager.newBuilder();
            aa.setId(manager.getManagerId());
            aa.setUsername(manager.getUsername());
            aa.setAvatar(manager.getAvatar());
            aa.setIntroduction(manager.getIntroduction());
            aa.setToken(manager.getToken());
            ManagerResponse.Builder builder=ManagerResponse.newBuilder();
            builder.addManager(aa);
            ManagerResponse response =builder
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }
        else
        {
            System.out.print("fail");
            ManagerResponse response = ManagerResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }

    @Override
    public void getInfo(ManagerRequest request,StreamObserver<ManagerResponse> responseStreamObserver){

        String token =request.getToken();

        manager_info manager=this.managerService.getToken(token);
        if(manager!=null){
            Manager.Builder aa=Manager.newBuilder();
            aa.setId(manager.getManagerId());
            aa.setPassword(manager.getPassword());
            aa.setUsername(manager.getUsername());
            aa.setAvatar(manager.getAvatar());
            aa.setIntroduction(manager.getIntroduction());
            aa.setToken(manager.getToken());
            ManagerResponse.Builder builder=ManagerResponse.newBuilder();
            builder.addManager(aa);
            ManagerResponse response =builder.setStatusId(0).build();
            responseStreamObserver.onNext(response);
        }
        else {
            ManagerResponse response = ManagerResponse.newBuilder().setStatusId(1).build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }

    @Override
    public void listManager(ManagerRequest request,StreamObserver<ManagerResponse> responseStreamObserver){
        List<manager_info> manager = managerService.getAll();
        int status = -1;
        ManagerResponse.Builder builder=ManagerResponse.newBuilder();

        if(manager.size()>0){
            for(int i=0; i<manager.size(); i++){
                Manager.Builder bu=Manager.newBuilder();
                bu.setId(manager.get(i).getManagerId());
                bu.setUsername(manager.get(i).getUsername());
                bu.setIntroduction(manager.get(i).getIntroduction());
                builder.addManager(bu);
            }
            status =0;
        }
        else {
            status =1;
        }

        ManagerResponse response=builder.setStatusId(status).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void modifyPassword(ManagerRequest request, StreamObserver<ManagerResponse> responseStreamObserver){

        System.out.println("Received request: " + request);
        System.out.println(request);
        int id = request.getId();
        String pwd = request.getPassword();
        pwd=EncryptHelper.encodeByMD5(pwd);
        manager_info user = this.managerService.getId(id);
        if(user!=null){
            user.setPassword(pwd);
            this.managerService.updatePassword(user);
            System.out.print("okok");
            ManagerResponse response = ManagerResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);

        }else{
            System.out.print("fail");
            ManagerResponse response = ManagerResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }

    @Override
    public void modify(ManagerRequest request, StreamObserver<ManagerResponse> responseStreamObserver){
        int status = -1;
        int id =request.getId();
        manager_info manager = managerService.getId(id);

        if(manager!=null){
            manager.setUsername(request.getUsername());
            manager.setIntroduction(request.getIntroduction());
            managerService.update(manager);
            status=0;
        }else {
            status= 1;
        }

        ManagerResponse response = ManagerResponse.newBuilder()
                    .setStatusId(status)
                    .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void addManager(ManagerRequest request, StreamObserver<ManagerResponse> responseStreamObserver){

        String name=request.getUsername();
        manager_info user=new manager_info();

        manager_info existUser =this.managerService.getName(name);
        if(existUser !=null){
            System.out.println("内容已存在");
            ManagerResponse response = ManagerResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);
        }
        else{
            user.setUsername(name);
            user.setToken(name);
            user.setPassword(request.getPassword());
            user.setIntroduction(request.getIntroduction());
            user.setAvatar(request.getAvatar());
            this.managerService.add(user);
            System.out.println("添加成功");
            ManagerResponse response = ManagerResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }

    @Override
    public void removeManager(ManagerRequest request,StreamObserver<ManagerResponse> responseStreamObserver) {
        List<Manager> disId=request.getManagerList();
        List<manager_role_info> list_manager=new ArrayList<manager_role_info>();

        int status=1;
        int removeCount=0;
        for (Manager ids:disId) {
            manager_info exitId = this.managerService.getId(ids.getId());
            if(exitId!=null){
                list_manager=manager_roleService.getAll();
                for(manager_role_info mr : list_manager){
                    if(mr.getManagerId()==exitId.getManagerId()){
                        manager_roleService.delete(mr.getId());
                    }
                }
                this.managerService.delete(exitId.getManagerId());
                removeCount++;
            }else
            {
                System.out.println("数据库没有该数据-->id为:"+exitId.getManagerId());
            }
        }

        if(removeCount==disId.size())
        {
            status =0;
        }
        ManagerResponse response = ManagerResponse.newBuilder()
                .setStatusId(status)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void changeRole(ManagerRequest request,StreamObserver<ManagerResponse> responseStreamObserver){
        int id=request.getId();
        List<Integer> list=new ArrayList<Integer>();
        List<manager_role_info> list_role =new ArrayList<manager_role_info>();
        list = request.getRoleIdList();
        int status= -1;
        int count=0;

        manager_info manager =managerService.getId(id);
        if(manager!=null) {
            list_role = manager_roleService.getAll();
            for (manager_role_info mr : list_role) {
                if(mr.getManagerId()==manager.getManagerId()){
                    manager_roleService.delete(mr.getId());
                }
            }

            for(int i: list){
                manager_role_info mr=new manager_role_info();
                mr.setManagerId(id);
                mr.setRoleId(i);
                manager_roleService.add(mr);
                count++;
            }
        }else {
            status = 1;
        }

        if(count==list.size()){
            status = 0;
        }

        ManagerResponse response=ManagerResponse.newBuilder()
                .setStatusId(status)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

}
