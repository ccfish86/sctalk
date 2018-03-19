package com.grpc.java.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.grpc.java.kernel.entity.IMUser;
import com.grpc.java.service.IUserService;
import com.grpc.java.utils.CommUtils;
import com.user.grpc.User;
import com.user.grpc.UserRequest;
import com.user.grpc.UserResponse;
import com.user.grpc.UserServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;

/**
 * Created by wx on 2017/11/9.
 */
@GrpcService(UserServiceGrpc.class)
public class UserServerImpl extends UserServiceGrpc.UserServiceImplBase {
    
    @Autowired
    private IUserService userService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void addUser(UserRequest request, StreamObserver<UserResponse> responseStreamObserver){

        String name=request.getName();
        IMUser user=new IMUser();

        IMUser existUser =this.userService.getUserByName(name);
        if(existUser !=null){
            logger.debug("内容已存在");
            UserResponse response = UserResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);
        }
        else{
            user.setName(name);
            user.setSex((byte)request.getSex());
            user.setNick(request.getNick());
            user.setPassword(request.getPassword());
            user.setSalt(request.getSalt());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setdepartid(request.getDepartid());
            user.setStatus((byte)2); // 正式 
            user.setCreated(CommUtils.getSystemSeconds());

            this.userService.addUser(user);
            logger.debug("添加成功");
            UserResponse response = UserResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }

    @Override
    public void modifyUser(UserRequest request, StreamObserver<UserResponse> responseStreamObserver){

        int id=request.getId();

        IMUser existUser =this.userService.getUserById(id);
        if(existUser !=null){
            existUser.setName(request.getName());
            existUser.setSex((byte)request.getSex());
            existUser.setNick(request.getNick());
            existUser.setPhone(request.getPhone());
            existUser.setEmail(request.getEmail());
            existUser.setdepartid(request.getDepartid());

            this.userService.updateUser(existUser);
            logger.debug("修改成功");
            UserResponse response = UserResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);

        }
        else{
            logger.debug("内容不存在");
            UserResponse response = UserResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }


    @Override
    public void listUser(UserRequest request, StreamObserver<UserResponse> responseStreamObserver){

        UserResponse.Builder builder = UserResponse.newBuilder();

        java.util.List<IMUser> data  =this.userService.getAllUser();

        if(data.size()>0){
            for(int i=0;i<data.size();i++){
                User.Builder bu=User.newBuilder();

                bu.setId(data.get(i).getId());
                bu.setName(data.get(i).getName());
                bu.setSex(data.get(i).getSex());
                bu.setNick(data.get(i).getNick());
                bu.setPassword(data.get(i).getPassword());
                bu.setSalt(data.get(i).getSalt());
                bu.setPhone(data.get(i).getPhone());
                bu.setEmail(data.get(i).getEmail());
                bu.setDepartid(data.get(i).getdepartid());

                User ab = bu.build();
                builder.addUser(ab);
            }
            UserResponse response = builder.setStatusId(1).build();

            responseStreamObserver.onNext(response);
        }else
        {
            UserResponse response = UserResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }


        responseStreamObserver.onCompleted();
    }


    @Override
    public void removeUser(UserRequest request,StreamObserver<UserResponse> responseStreamObserver){

        List<User> disId=request.getUserList();

        int status=0;
        int removeCount=0;
        for (User ids:disId) {
            IMUser exitId = this.userService.getUserById(ids.getId());

            if(exitId!=null){
                this.userService.deleteUser(exitId.getId());
                removeCount++;
            }else
            {
                logger.debug("数据库没有该数据-->id为:"+exitId.getId());
            }
        }

        if(removeCount==disId.size())
            status =1;

        UserResponse response = UserResponse.newBuilder()
                .setStatusId(status)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }


    @Override
    public void modifyPassword(UserRequest request, StreamObserver<UserResponse> responseStreamObserver){

        int id=request.getId();

        IMUser existUser =this.userService.getUserById(id);
        if(existUser !=null){
            existUser.setPassword(request.getPassword());
            existUser.setSalt(request.getSalt());
            this.userService.updatePassword(existUser);
            logger.debug("修改成功");
            UserResponse response = UserResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);

        }
        else{
            logger.debug("内容不存在");
            UserResponse response = UserResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }




}
