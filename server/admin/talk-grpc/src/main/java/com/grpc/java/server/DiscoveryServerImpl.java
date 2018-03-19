package com.grpc.java.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.discovery.grpc.Discovery;
import com.discovery.grpc.DiscoveryRequest;
import com.discovery.grpc.DiscoveryResponse;
import com.discovery.grpc.DiscoveryServiceGrpc;
import com.grpc.java.kernel.entity.IMDiscovery;
import com.grpc.java.service.IDiscoveryService;

import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;


/**
 * Created by wx on 2017/11/9.
 */
@GrpcService(DiscoveryServiceGrpc.class)
public class DiscoveryServerImpl extends DiscoveryServiceGrpc.DiscoveryServiceImplBase {
    
    @Autowired
    private IDiscoveryService discoveryService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void addDiscovery(DiscoveryRequest request, StreamObserver<DiscoveryResponse> responseStreamObserver){

        int id=request.getId();
        String itemname=request.getItemname();
        String itemurl=request.getItemurl();
        int itempriority=request.getItempriority();

        IMDiscovery discovery=new IMDiscovery();

        IMDiscovery existDiscovery =this.discoveryService.getDiscoveryByName(itemname);
        if(existDiscovery !=null){
            logger.debug("内容已存在");
            DiscoveryResponse response = DiscoveryResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);

        }
        else{
            discovery.setItemname(itemname);
            discovery.setItemurl(itemurl);
            discovery.setItempriority(itempriority);
            this.discoveryService.addDiscovery(discovery);
            logger.debug("添加成功");
            DiscoveryResponse response = DiscoveryResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }

    @Override
    public void modifyDiscovery(DiscoveryRequest request, StreamObserver<DiscoveryResponse> responseStreamObserver){

        int id=request.getId();
        String itemname=request.getItemname();
        String itemurl=request.getItemurl();
        int itempriority=request.getItempriority();

        IMDiscovery existDiscovery =this.discoveryService.getDiscoveryById(id);
        if(existDiscovery !=null){
            existDiscovery.setItemname(itemname);
            existDiscovery.setItemurl(itemurl);
            existDiscovery.setItempriority(itempriority);
            this.discoveryService.updateDiscovery(existDiscovery);
            logger.debug("修改成功");
            DiscoveryResponse response = DiscoveryResponse.newBuilder()
                    .setStatusId(1)
                    .build();
            responseStreamObserver.onNext(response);

        }
        else{
            logger.debug("内容不存在");
            DiscoveryResponse response = DiscoveryResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }

        responseStreamObserver.onCompleted();
    }


    @Override
    public void listDiscovery(DiscoveryRequest request, StreamObserver<DiscoveryResponse> responseStreamObserver){

        DiscoveryResponse.Builder builder = DiscoveryResponse.newBuilder();

        java.util.List<IMDiscovery> data  =this.discoveryService.getAllDiscovery();

        if(data.size()>0){
            for(int i=0;i<data.size();i++){
                Discovery.Builder bu=Discovery.newBuilder();

                bu.setId(data.get(i).getId());
                bu.setItemname(data.get(i).getItemname());
                bu.setItemurl(data.get(i).getItemurl());
                bu.setItempriority(data.get(i).getItempriority());

                Discovery ab = bu.build();
                builder.addDiscovery(ab);
            }
            DiscoveryResponse response = builder.setStatusId(1).build();
            responseStreamObserver.onNext(response);
        }else
        {
            DiscoveryResponse response = DiscoveryResponse.newBuilder()
                    .setStatusId(0)
                    .build();
            responseStreamObserver.onNext(response);
        }


        responseStreamObserver.onCompleted();
    }


    @Override
    public void removeDiscovery(DiscoveryRequest request,StreamObserver<DiscoveryResponse> responseStreamObserver){

        List<Discovery> disId=request.getDiscoveryList();


        int status = 0;
        int removeCount=0;
        for (Discovery ids:disId) {
            IMDiscovery exitId = this.discoveryService.getDiscoveryById(ids.getId());
            if(exitId!=null){
                this.discoveryService.deleteDiscovery(exitId.getId());
                removeCount++;
            }else {
                logger.debug("数据库没有该数据-->id为:"+exitId.getId());
            }
        }
        if(removeCount==disId.size())
            status=1;

        DiscoveryResponse response = DiscoveryResponse.newBuilder()
                .setStatusId(status)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }


}
