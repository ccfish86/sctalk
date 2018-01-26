package com.grpc.java.server;

import com.grpc.java.kernel.entity.*;
import com.grpc.java.service.*;
import com.power.grpc.Power;
import com.power.grpc.PowerRequest;
import com.power.grpc.PowerResponse;
import com.power.grpc.PowerServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wx on 2017/11/8.
 */

public class PowerServerImpl extends PowerServiceGrpc.PowerServiceImplBase {
    private PowerService powerService;
    private ManagerService managerService;
    private Role_PowerService role_powerService;
    private RoleService roleService;
    private Manager_RoleService manager_roleService;

    public PowerServerImpl(BeanContainer service) {
        this.powerService= service.powerService;
        this.manager_roleService=service.manager_roleService;
        this.managerService=service.managerService;
        this.role_powerService=service.role_powerService;
        this.roleService=service.roleService;
    }

    @Override
    public void addPower(PowerRequest request, StreamObserver<PowerResponse> responseStreamObserver){
        String name=request.getPowerName();
        String url=request.getPowerUrl();
        int parent_id=request.getParentId();

        int status=-1;

        power_info power=powerService.getName(name);
        if(power!=null){
            status=1;
        }else {
            power_info pi =new power_info();
            pi.setParentId(parent_id);
            pi.setPowerName(name);
            pi.setPowerUrl(url);
            powerService.add(pi);
            status=0;
        }

        PowerResponse response = PowerResponse.newBuilder()
                .setStatusId(status)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();    
    }

    @Override
    public void modifyPower(PowerRequest request, StreamObserver<PowerResponse> responseStreamObserver){
        int id=request.getPowerId();
        String name=request.getPowerName();
        String url=request.getPowerUrl();
        int parent_id=request.getParentId();
        int status=-1;

        power_info power=powerService.getId(id);
        if(power!=null){
            power.setPowerName(name);
            power.setParentId(parent_id);
            power.setPowerUrl(url);
            powerService.update(power);
            status=0;
        }
        else {
            status=1;
        }

        PowerResponse response=PowerResponse.newBuilder()
                .setStatusId(status)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void listPower(PowerRequest request, StreamObserver<PowerResponse> responseStreamObserver){
        List<power_info> data =powerService.getAll();
        PowerResponse.Builder builder=PowerResponse.newBuilder();
        int status=-1;

        if(data.size()>0){
            for(power_info ri :data) {
                Power.Builder bu = Power.newBuilder();
                bu.setPowerId(ri.getPowerId());
                bu.setPowerName(ri.getPowerName());
                bu.setPowerUrl(ri.getPowerUrl());
                bu.setParentId(ri.getParentId());
                builder.addPower(bu);
            }
            status=0;
        }
        else
        {
            status=1;
        }

        PowerResponse response=builder.setStatusId(status).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void removePower(PowerRequest request,StreamObserver<PowerResponse> responseStreamObserver){
        List<Power> ids=request.getPowerList();
        int status=-1;
        int conunt=0;

        for(Power id : ids){
            power_info power =powerService.getId(id.getPowerId());
            if(power!=null){
                powerService.delete(power.getPowerId());
                conunt++;
            }else {
                System.out.println("数据库无此id:" + power.getPowerId() + "对应的信息!");
            }
        }
        if(conunt==ids.size()){
            status=0;
        }else {
            status=1;
        }

        PowerResponse response=PowerResponse.newBuilder()
                .setStatusId(status)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getRoute(PowerRequest request,StreamObserver<PowerResponse> responseStreamObserver){
        String token=request.getToken();
        List<Integer> cc=new ArrayList<Integer>();
        List<Integer> dd=new ArrayList<Integer>();
        dd.add(0);
        int count=0;
        int status=-1;

        PowerResponse.Builder builder=PowerResponse.newBuilder();

        manager_info manager = managerService.getToken(token);
        if(manager!=null){
            List<manager_role_info> mr=manager_roleService.getAll();
            for(manager_role_info aa: mr){
                if(aa.getManagerId()==manager.getManagerId()){
                    List<role_power_info> rp=role_powerService.getAll();
                    for(role_power_info bb:rp){
                        if(aa.getRoleId()==bb.getRoleId()){
                            cc.add(bb.getPowerId());
                        }
                    }
                }
            }
        }else
        {
            status=1;
        }

        if(cc.size()>0){
            for(int zz : cc){
                for(int i=0; i<dd.size(); i++){ //去除重复权限id
                    if(dd.get(i)==zz){
                       count=1;
                    }
                }
                if(count==0){
                    dd.add(zz);
                }
                count=0;
            }
            dd.remove(0);
            for(int power_id :dd){
                power_info power=powerService.getId(power_id);
                if(power!=null){
                    Power.Builder bu=Power.newBuilder();
                    bu.setPowerId(power_id);
                    bu.setPowerUrl(power.getPowerUrl());
                    bu.setPowerName(power.getPowerName());
                    bu.setParentId(power.getParentId());
                    builder.addPower(bu);
                }
            }
            status=0;
        }

        PowerResponse response=builder.setStatusId(status).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getPower(PowerRequest request,StreamObserver<PowerResponse> responseStreamObserver){
        int id=request.getId();
        int count=0;
        int status=-1;
        List<Integer> cc=new ArrayList<Integer>();
        List<Integer> dd=new ArrayList<Integer>();
        dd.add(0);

        PowerResponse.Builder builder=PowerResponse.newBuilder();

        role_info role =roleService.getId(id);
        if(role!=null){
            List<role_power_info> rp=role_powerService.getAll();
            for(role_power_info aa:rp) {
                if (aa.getRoleId() == role.getRoleId()) {
                    cc.add(aa.getPowerId());
                }
            }
        }else {
            status=1;
        }

        if(cc.size()>0){
            for(int zz : cc){
                for(int i=0; i<dd.size(); i++){ //去除重复权限id
                    if(dd.get(i)==zz){
                        count=1;
                    }
                }
                if(count==0){
                    dd.add(zz);
                }
                count=0;
            }
            dd.remove(0);
            for(int power_id :dd){
                power_info power=powerService.getId(power_id);
                if(power!=null){
                    Power.Builder bu=Power.newBuilder();
                    bu.setPowerId(power_id);
                    bu.setPowerUrl(power.getPowerUrl());
                    bu.setPowerName(power.getPowerName());
                    bu.setParentId(power.getParentId());
                    builder.addPower(bu);
                }
            }
            status=0;
        }


        PowerResponse response=builder.setStatusId(status).build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

}
