package com.webjava.web.restcontroller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wx on 2017/10/27.
 */

import com.discovery.grpc.Discovery;
import com.discovery.grpc.DiscoveryRequest;
import com.discovery.grpc.DiscoveryResponse;
import com.discovery.grpc.DiscoveryServiceGrpc;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.webjava.kernel.entity.IMDiscovery;
import com.webjava.utils.HttpUtils;
import com.webjava.utils.ResponseInfo;

import io.grpc.ManagedChannel;
import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;


@RestController
@RequestMapping("/users")
public class DiscoveryRestController {

    @GrpcClient("talk-grpc")
    private ManagedChannel channel;

    @RequestMapping(value = "/discovery/list",method = RequestMethod.GET)
    public void listDiscovery(HttpServletRequest request, HttpServletResponse response) throws InvalidProtocolBufferException {

        // Create a blocking stub with the channel
        DiscoveryServiceGrpc.DiscoveryServiceBlockingStub stub =
                DiscoveryServiceGrpc.newBlockingStub(channel);

        // Create a request
        DiscoveryRequest listDiscoveryRequest = DiscoveryRequest.newBuilder().build();

        // Send the request using the stub
        System.out.println("Client sending request");
        DiscoveryResponse discoveryResponse = stub.listDiscovery(listDiscoveryRequest);


         if(discoveryResponse.getStatusId()==1){

            String data= JsonFormat.printer().preservingProtoFieldNames().print(discoveryResponse);

            HttpUtils.setJsonBody(response,new ResponseInfo(0,"显示所有用户",data));
        }else
        {
            System.out.println("nothing");
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"无内容"));
        }

    }

    @RequestMapping(value = "/discovery/add",method = RequestMethod.POST)
    public void addDiscovery(HttpServletRequest request, HttpServletResponse response){

        String  strJson= HttpUtils.getJsonBody(request);

        Gson gson=new Gson();

        IMDiscovery discovery=gson.fromJson(strJson,IMDiscovery.class);

        // Create a blocking stub with the channel
        DiscoveryServiceGrpc.DiscoveryServiceBlockingStub stub =
                DiscoveryServiceGrpc.newBlockingStub(channel);

        // Create a request
        DiscoveryRequest addDiscoveryRequest = DiscoveryRequest.newBuilder()
                .setItemname(discovery.getItemname())
                .setItemurl(discovery.getItemurl())
                .setItempriority(discovery.getItempriority())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        DiscoveryResponse discoveryResponse = stub.addDiscovery(addDiscoveryRequest);

        if(discoveryResponse.getStatusId()==0){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"添加成功"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"内容存在"));
        }


    }

    @RequestMapping(value = "/discovery/remove",method = RequestMethod.POST)
    public void removeDiscovery(HttpServletRequest request,HttpServletResponse response ) {

        String strjson = HttpUtils.getJsonBody(request);
        List<Integer> list=new ArrayList<Integer>();
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        list = new Gson().fromJson(strjson, type);

        // Create a blocking stub with the channel
        DiscoveryServiceGrpc.DiscoveryServiceBlockingStub stub =
                DiscoveryServiceGrpc.newBlockingStub(channel);

        DiscoveryRequest.Builder builder = DiscoveryRequest.newBuilder();
        // Create a request
        for (int i : list) {
            Discovery.Builder bu = Discovery.newBuilder();
            bu.setId(i);
            Discovery discovery =bu.build();
            builder.addDiscovery(discovery);
          }

        DiscoveryRequest removeDiscoveryRequest = builder.build();

        // Send the request using the stub
        System.out.println("Client sending request");
        DiscoveryResponse discoveryResponse = stub.removeDiscovery(removeDiscoveryRequest);

        if (discoveryResponse.getStatusId()==1) {
            HttpUtils.setJsonBody(response, new ResponseInfo(0, "修改成功！"));
        } else {
            HttpUtils.setJsonBody(response, new ResponseInfo(1, "无此ID信息！"));
        }

    }

    @RequestMapping(value = "/discovery/update",method = RequestMethod.POST)
    public void updateDiscovery (HttpServletRequest request,HttpServletResponse response){
        String strData =HttpUtils.getJsonBody(request);
        Gson gson=new Gson();
        IMDiscovery discovery = gson.fromJson(strData,IMDiscovery.class);

        // Create a blocking stub with the channel
        DiscoveryServiceGrpc.DiscoveryServiceBlockingStub stub =
                DiscoveryServiceGrpc.newBlockingStub(channel);

        // Create a request
        DiscoveryRequest modifyDiscoveryRequest = DiscoveryRequest.newBuilder()
                .setId(discovery.getId())
                .setItemname(discovery.getItemname())
                .setItemurl(discovery.getItemurl())
                .setItempriority(discovery.getItempriority())
                .build();

        // Send the request using the stub
        System.out.println("Client sending request");
        DiscoveryResponse discoveryResponse = stub.modifyDiscovery(modifyDiscoveryRequest);

        if(discoveryResponse.getStatusId()==1){
            HttpUtils.setJsonBody(response,new ResponseInfo(0,"修改成功"));
        }else
        {
            HttpUtils.setJsonBody(response,new ResponseInfo(1,"内容不存在"));
        }


    }

}