package com.grpc.java.server;

import com.group.grpc.GroupRequest;
import com.group.grpc.GroupResponse;
import com.group.grpc.GroupServiceGrpc;
import com.grpc.java.service.IGroupService;
import io.grpc.stub.StreamObserver;

/**
 * Created by wx on 2017/11/9.
 */
public class GroupServerImpl extends GroupServiceGrpc.GroupServiceImplBase {
    private IGroupService groupService;

    public GroupServerImpl(BeanContainer service) {
        this.groupService= service.groupService;
    }

    @Override
    public void addGroup(GroupRequest request, StreamObserver<GroupResponse> responseStreamObserver){



        responseStreamObserver.onCompleted();
    }



}
