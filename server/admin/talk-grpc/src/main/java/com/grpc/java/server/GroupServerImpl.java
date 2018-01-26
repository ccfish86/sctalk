package com.grpc.java.server;

import org.springframework.beans.factory.annotation.Autowired;

import com.group.grpc.GroupRequest;
import com.group.grpc.GroupResponse;
import com.group.grpc.GroupServiceGrpc;
import com.grpc.java.service.IGroupService;

import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;

/**
 * Created by wx on 2017/11/9.
 */
@GrpcService(GroupServiceGrpc.class)
public class GroupServerImpl extends GroupServiceGrpc.GroupServiceImplBase {
    
    @Autowired
    private IGroupService groupService;

    @Override
    public void addGroup(GroupRequest request, StreamObserver<GroupResponse> responseStreamObserver){



        responseStreamObserver.onCompleted();
    }



}
