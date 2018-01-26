package com.grpc.java.server;


import org.springframework.beans.factory.annotation.Autowired;

import com.grpc.java.service.Manager_RoleService;
import com.manager_role.grpc.MRRequest;
import com.manager_role.grpc.MRResponse;
import com.manager_role.grpc.MRServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;

/**
 * Created by wx on 2017/11/8.
 */

@GrpcService(MRServiceGrpc.class)
public class MRServerImpl extends MRServiceGrpc.MRServiceImplBase {
    
    @Autowired
    private Manager_RoleService manager_roleService;

    @Override
    public void addMR(MRRequest request, StreamObserver<MRResponse> responseStreamObserver){

    }

    @Override
    public void modifyMR(MRRequest request, StreamObserver<MRResponse> responseStreamObserver){

    }

    @Override
    public void listMR(MRRequest request, StreamObserver<MRResponse> responseStreamObserver){

    }

    @Override
    public void removeMR(MRRequest request,StreamObserver<MRResponse> responseStreamObserver){

    }


}
