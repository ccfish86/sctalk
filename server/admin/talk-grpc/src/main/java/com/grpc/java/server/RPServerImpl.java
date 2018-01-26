package com.grpc.java.server;


import com.grpc.java.service.Role_PowerService;
import com.role_power.grpc.RPRequest;
import com.role_power.grpc.RPResponse;
import com.role_power.grpc.RPServiceGrpc;
import io.grpc.stub.StreamObserver;


/**
 * Created by wx on 2017/11/8.
 */

public class RPServerImpl extends RPServiceGrpc.RPServiceImplBase {
    private Role_PowerService role_powerService;

    public RPServerImpl(BeanContainer service) {
        this.role_powerService= service.role_powerService;
    }

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
