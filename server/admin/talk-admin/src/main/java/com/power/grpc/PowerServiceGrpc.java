package com.power.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.5.0)",
    comments = "Source: power.proto")
public final class PowerServiceGrpc {

  private PowerServiceGrpc() {}

  public static final String SERVICE_NAME = "com.power.grpc.PowerService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.power.grpc.PowerRequest,
      com.power.grpc.PowerResponse> METHOD_LIST_POWER =
      io.grpc.MethodDescriptor.<com.power.grpc.PowerRequest, com.power.grpc.PowerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.power.grpc.PowerService", "listPower"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.power.grpc.PowerRequest,
      com.power.grpc.PowerResponse> METHOD_ADD_POWER =
      io.grpc.MethodDescriptor.<com.power.grpc.PowerRequest, com.power.grpc.PowerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.power.grpc.PowerService", "addPower"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.power.grpc.PowerRequest,
      com.power.grpc.PowerResponse> METHOD_REMOVE_POWER =
      io.grpc.MethodDescriptor.<com.power.grpc.PowerRequest, com.power.grpc.PowerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.power.grpc.PowerService", "removePower"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.power.grpc.PowerRequest,
      com.power.grpc.PowerResponse> METHOD_MODIFY_POWER =
      io.grpc.MethodDescriptor.<com.power.grpc.PowerRequest, com.power.grpc.PowerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.power.grpc.PowerService", "modifyPower"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.power.grpc.PowerRequest,
      com.power.grpc.PowerResponse> METHOD_GET_ROUTE =
      io.grpc.MethodDescriptor.<com.power.grpc.PowerRequest, com.power.grpc.PowerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.power.grpc.PowerService", "getRoute"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.power.grpc.PowerRequest,
      com.power.grpc.PowerResponse> METHOD_GET_POWER =
      io.grpc.MethodDescriptor.<com.power.grpc.PowerRequest, com.power.grpc.PowerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.power.grpc.PowerService", "getPower"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.power.grpc.PowerResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PowerServiceStub newStub(io.grpc.Channel channel) {
    return new PowerServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PowerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PowerServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PowerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PowerServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class PowerServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_POWER, responseObserver);
    }

    /**
     */
    public void addPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_POWER, responseObserver);
    }

    /**
     */
    public void removePower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_POWER, responseObserver);
    }

    /**
     */
    public void modifyPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_POWER, responseObserver);
    }

    /**
     */
    public void getRoute(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_ROUTE, responseObserver);
    }

    /**
     */
    public void getPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_POWER, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LIST_POWER,
            asyncUnaryCall(
              new MethodHandlers<
                com.power.grpc.PowerRequest,
                com.power.grpc.PowerResponse>(
                  this, METHODID_LIST_POWER)))
          .addMethod(
            METHOD_ADD_POWER,
            asyncUnaryCall(
              new MethodHandlers<
                com.power.grpc.PowerRequest,
                com.power.grpc.PowerResponse>(
                  this, METHODID_ADD_POWER)))
          .addMethod(
            METHOD_REMOVE_POWER,
            asyncUnaryCall(
              new MethodHandlers<
                com.power.grpc.PowerRequest,
                com.power.grpc.PowerResponse>(
                  this, METHODID_REMOVE_POWER)))
          .addMethod(
            METHOD_MODIFY_POWER,
            asyncUnaryCall(
              new MethodHandlers<
                com.power.grpc.PowerRequest,
                com.power.grpc.PowerResponse>(
                  this, METHODID_MODIFY_POWER)))
          .addMethod(
            METHOD_GET_ROUTE,
            asyncUnaryCall(
              new MethodHandlers<
                com.power.grpc.PowerRequest,
                com.power.grpc.PowerResponse>(
                  this, METHODID_GET_ROUTE)))
          .addMethod(
            METHOD_GET_POWER,
            asyncUnaryCall(
              new MethodHandlers<
                com.power.grpc.PowerRequest,
                com.power.grpc.PowerResponse>(
                  this, METHODID_GET_POWER)))
          .build();
    }
  }

  /**
   */
  public static final class PowerServiceStub extends io.grpc.stub.AbstractStub<PowerServiceStub> {
    private PowerServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PowerServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PowerServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PowerServiceStub(channel, callOptions);
    }

    /**
     */
    public void listPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_POWER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_POWER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removePower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_POWER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_POWER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRoute(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_ROUTE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPower(com.power.grpc.PowerRequest request,
        io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_POWER, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PowerServiceBlockingStub extends io.grpc.stub.AbstractStub<PowerServiceBlockingStub> {
    private PowerServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PowerServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PowerServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PowerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.power.grpc.PowerResponse listPower(com.power.grpc.PowerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_POWER, getCallOptions(), request);
    }

    /**
     */
    public com.power.grpc.PowerResponse addPower(com.power.grpc.PowerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_POWER, getCallOptions(), request);
    }

    /**
     */
    public com.power.grpc.PowerResponse removePower(com.power.grpc.PowerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_POWER, getCallOptions(), request);
    }

    /**
     */
    public com.power.grpc.PowerResponse modifyPower(com.power.grpc.PowerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_POWER, getCallOptions(), request);
    }

    /**
     */
    public com.power.grpc.PowerResponse getRoute(com.power.grpc.PowerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_ROUTE, getCallOptions(), request);
    }

    /**
     */
    public com.power.grpc.PowerResponse getPower(com.power.grpc.PowerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_POWER, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PowerServiceFutureStub extends io.grpc.stub.AbstractStub<PowerServiceFutureStub> {
    private PowerServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PowerServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PowerServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PowerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.power.grpc.PowerResponse> listPower(
        com.power.grpc.PowerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_POWER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.power.grpc.PowerResponse> addPower(
        com.power.grpc.PowerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_POWER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.power.grpc.PowerResponse> removePower(
        com.power.grpc.PowerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_POWER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.power.grpc.PowerResponse> modifyPower(
        com.power.grpc.PowerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_POWER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.power.grpc.PowerResponse> getRoute(
        com.power.grpc.PowerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_ROUTE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.power.grpc.PowerResponse> getPower(
        com.power.grpc.PowerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_POWER, getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_POWER = 0;
  private static final int METHODID_ADD_POWER = 1;
  private static final int METHODID_REMOVE_POWER = 2;
  private static final int METHODID_MODIFY_POWER = 3;
  private static final int METHODID_GET_ROUTE = 4;
  private static final int METHODID_GET_POWER = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PowerServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PowerServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_POWER:
          serviceImpl.listPower((com.power.grpc.PowerRequest) request,
              (io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse>) responseObserver);
          break;
        case METHODID_ADD_POWER:
          serviceImpl.addPower((com.power.grpc.PowerRequest) request,
              (io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse>) responseObserver);
          break;
        case METHODID_REMOVE_POWER:
          serviceImpl.removePower((com.power.grpc.PowerRequest) request,
              (io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse>) responseObserver);
          break;
        case METHODID_MODIFY_POWER:
          serviceImpl.modifyPower((com.power.grpc.PowerRequest) request,
              (io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse>) responseObserver);
          break;
        case METHODID_GET_ROUTE:
          serviceImpl.getRoute((com.power.grpc.PowerRequest) request,
              (io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse>) responseObserver);
          break;
        case METHODID_GET_POWER:
          serviceImpl.getPower((com.power.grpc.PowerRequest) request,
              (io.grpc.stub.StreamObserver<com.power.grpc.PowerResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class PowerServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.power.grpc.PowerOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PowerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PowerServiceDescriptorSupplier())
              .addMethod(METHOD_LIST_POWER)
              .addMethod(METHOD_ADD_POWER)
              .addMethod(METHOD_REMOVE_POWER)
              .addMethod(METHOD_MODIFY_POWER)
              .addMethod(METHOD_GET_ROUTE)
              .addMethod(METHOD_GET_POWER)
              .build();
        }
      }
    }
    return result;
  }
}
