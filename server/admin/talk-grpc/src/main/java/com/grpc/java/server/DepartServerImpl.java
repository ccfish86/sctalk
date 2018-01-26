package com.grpc.java.server;

import com.depart.grpc.Depart;
import com.depart.grpc.DepartRequest;
import com.depart.grpc.DepartResponse;
import com.depart.grpc.DepartServiceGrpc;
import com.grpc.java.kernel.entity.IMDepart;
import com.grpc.java.kernel.entity.IMUser;
import com.grpc.java.service.IDepartService;
import com.grpc.java.service.IUserService;
import io.grpc.stub.StreamObserver;

import java.util.List;

/**
 * Created by wx on 2017/11/9.
 */
public class DepartServerImpl extends DepartServiceGrpc.DepartServiceImplBase {
    private IDepartService departService;
    private IUserService userService;

    public DepartServerImpl(BeanContainer service) {
        this.departService= service.departService;
        this.userService=service.userService;
    }

    @Override
    public void addDepart(DepartRequest request, StreamObserver<DepartResponse> responseStreamObserver){

        int id=request.getId();
        String departName=request.getDepartname();
        int priority=request.getPriority();
        int parentId=request.getParentid();

        IMDepart depart=new IMDepart();

        IMDepart existDepart =this.departService.getDepartByName(departName);
        if(existDepart !=null){
            System.out.println("内容已存在");
            DepartResponse response = DepartResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);

        }
        else{
            depart.setDepartname(departName);
            depart.setPriority(priority);
            depart.setParentid(parentId);
            this.departService.addDepart(depart);
            System.out.println("添加成功");
            DepartResponse response = DepartResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }

    @Override
    public void modifyDepart(DepartRequest request, StreamObserver<DepartResponse> responseStreamObserver){

        int id=request.getId();
        String departName=request.getDepartname();
        int priority=request.getPriority();
        int parentId=request.getParentid();

        IMDepart existDepart =this.departService.getDepartById(id);
        if(existDepart !=null){
            existDepart.setDepartname(departName);
            existDepart.setPriority(priority);
            existDepart.setParentid(parentId);
            this.departService.updateDepart(existDepart);
            System.out.println("修改成功");
            DepartResponse response = DepartResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);

        }
        else{
            System.out.println("内容不存在");
            DepartResponse response = DepartResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }


    @Override
    public void listDepart(DepartRequest request, StreamObserver<DepartResponse> responseStreamObserver){

        DepartResponse.Builder builder = DepartResponse.newBuilder();

        java.util.List<IMDepart> data  =this.departService.getAllDepart();

        if(data.size()>0){
            for(int i=0;i<data.size();i++){
                Depart.Builder bu=Depart.newBuilder();

                bu.setId(data.get(i).getId());
                bu.setDepartname(data.get(i).getDepartname());
                bu.setPriority(data.get(i).getPriority());
                bu.setParentid(data.get(i).getParentid());

                Depart ab = bu.build();
                builder.addDepart(ab);
            }
            DepartResponse response = builder.setStatusId(1).build();

            responseStreamObserver.onNext(response);
        }else
        {
            DepartResponse response = DepartResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }


        responseStreamObserver.onCompleted();
    }


    @Override
    public void removeDepart(DepartRequest request,StreamObserver<DepartResponse> responseStreamObserver){

        List<Depart> disId=request.getDepartList();

        List<IMUser> allUsers=this.userService.getAllUser();

        List<IMDepart> allDepart=this.departService.getAllDepart();

        int status=1;
        int removeCount=0;
        for (Depart ids:disId) {
            IMDepart exitId = this.departService.getDepartById(ids.getId());

            if(exitId!=null){
                for(IMDepart depart:allDepart){
                    if(exitId.getId()==depart.getParentid()){
                        status=-1;
                        break;
                    }
                }
                if(status!=-1){
                    for(IMUser user:allUsers){
                        if (exitId.getId()==user.getdepartid()){
                            status=-1;
                            break;
                        }
                    }
                }
                if(status==-1){
                    break;
                }else if(status!=-1){
                    this.departService.deleteDepart(exitId.getId());
                    removeCount++;
                }

            }else
            {
                System.out.println("数据库没有该数据-->id为:"+exitId.getId());
            }
        }

        if(removeCount==disId.size())
            status =0;

        DepartResponse response = DepartResponse.newBuilder()
                .setStatusId(status)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }


}
