package com.grpc.java.server;


import org.springframework.beans.factory.annotation.Autowired;

import com.grpc.java.service.Role_PowerService;
import com.role_power.grpc.RPRequest;
import com.role_power.grpc.RPResponse;
import com.role_power.grpc.RPServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;


/**
 * Created by wx on 2017/11/8.
 */

@GrpcService(RPServiceGrpc.class)
public class RPServerImpl extends RPServiceGrpc.RPServiceImplBase {

    @Autowired
    private Role_PowerService role_powerService;

    @Override
    public void addRP(RPRequest request, StreamObserver<RPResponse> responseStreamObserver){

    }

    @Override
    public void modifyRP(RPRequest request, StreamObserver<RPResponse> responseStreamObserver){

    }

    @Override
    public void listRP(RPRequest request, StreamObserver<RPResponse> responseStreamObserver){

    }

    @Override
    public void removeRP(RPRequest request,StreamObserver<RPResponse> responseStreamObserver){

    }


}
