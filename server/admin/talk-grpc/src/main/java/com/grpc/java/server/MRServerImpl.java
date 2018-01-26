package com.grpc.java.server;


import com.grpc.java.service.Manager_RoleService;
import com.manager_role.grpc.MRRequest;
import com.manager_role.grpc.MRResponse;
import com.manager_role.grpc.MRServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * Created by wx on 2017/11/8.
 */

public class MRServerImpl extends MRServiceGrpc.MRServiceImplBase {
    private Manager_RoleService manager_roleService;

    public MRServerImpl(BeanContainer service) {
        this.manager_roleService= service.manager_roleService;
    }

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
