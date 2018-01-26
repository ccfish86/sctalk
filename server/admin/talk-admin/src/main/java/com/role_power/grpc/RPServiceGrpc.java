package com.role_power.grpc;

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
    comments = "Source: role_power.proto")
public final class RPServiceGrpc {

  private RPServiceGrpc() {}

  public static final String SERVICE_NAME = "com.role_power.grpc.RPService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role_power.grpc.RPRequest,
      com.role_power.grpc.RPResponse> METHOD_LIST_RP =
      io.grpc.MethodDescriptor.<com.role_power.grpc.RPRequest, com.role_power.grpc.RPResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role_power.grpc.RPService", "listRP"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role_power.grpc.RPRequest,
      com.role_power.grpc.RPResponse> METHOD_ADD_RP =
      io.grpc.MethodDescriptor.<com.role_power.grpc.RPRequest, com.role_power.grpc.RPResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role_power.grpc.RPService", "addRP"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role_power.grpc.RPRequest,
      com.role_power.grpc.RPResponse> METHOD_REMOVE_RP =
      io.grpc.MethodDescriptor.<com.role_power.grpc.RPRequest, com.role_power.grpc.RPResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role_power.grpc.RPService", "removeRP"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role_power.grpc.RPRequest,
      com.role_power.grpc.RPResponse> METHOD_MODIFY_RP =
      io.grpc.MethodDescriptor.<com.role_power.grpc.RPRequest, com.role_power.grpc.RPResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role_power.grpc.RPService", "modifyRP"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role_power.grpc.RPResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RPServiceStub newStub(io.grpc.Channel channel) {
    return new RPServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RPServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RPServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RPServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RPServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class RPServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_RP, responseObserver);
    }

    /**
     */
    public void addRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_RP, responseObserver);
    }

    /**
     */
    public void removeRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_RP, responseObserver);
    }

    /**
     */
    public void modifyRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_RP, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LIST_RP,
            asyncUnaryCall(
              new MethodHandlers<
                com.role_power.grpc.RPRequest,
                com.role_power.grpc.RPResponse>(
                  this, METHODID_LIST_RP)))
          .addMethod(
            METHOD_ADD_RP,
            asyncUnaryCall(
              new MethodHandlers<
                com.role_power.grpc.RPRequest,
                com.role_power.grpc.RPResponse>(
                  this, METHODID_ADD_RP)))
          .addMethod(
            METHOD_REMOVE_RP,
            asyncUnaryCall(
              new MethodHandlers<
                com.role_power.grpc.RPRequest,
                com.role_power.grpc.RPResponse>(
                  this, METHODID_REMOVE_RP)))
          .addMethod(
            METHOD_MODIFY_RP,
            asyncUnaryCall(
              new MethodHandlers<
                com.role_power.grpc.RPRequest,
                com.role_power.grpc.RPResponse>(
                  this, METHODID_MODIFY_RP)))
          .build();
    }
  }

  /**
   */
  public static final class RPServiceStub extends io.grpc.stub.AbstractStub<RPServiceStub> {
    private RPServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RPServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RPServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RPServiceStub(channel, callOptions);
    }

    /**
     */
    public void listRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_RP, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_RP, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_RP, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyRP(com.role_power.grpc.RPRequest request,
        io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_RP, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RPServiceBlockingStub extends io.grpc.stub.AbstractStub<RPServiceBlockingStub> {
    private RPServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RPServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RPServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RPServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.role_power.grpc.RPResponse listRP(com.role_power.grpc.RPRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_RP, getCallOptions(), request);
    }

    /**
     */
    public com.role_power.grpc.RPResponse addRP(com.role_power.grpc.RPRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_RP, getCallOptions(), request);
    }

    /**
     */
    public com.role_power.grpc.RPResponse removeRP(com.role_power.grpc.RPRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_RP, getCallOptions(), request);
    }

    /**
     */
    public com.role_power.grpc.RPResponse modifyRP(com.role_power.grpc.RPRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_RP, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RPServiceFutureStub extends io.grpc.stub.AbstractStub<RPServiceFutureStub> {
    private RPServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RPServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RPServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RPServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role_power.grpc.RPResponse> listRP(
        com.role_power.grpc.RPRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_RP, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role_power.grpc.RPResponse> addRP(
        com.role_power.grpc.RPRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_RP, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role_power.grpc.RPResponse> removeRP(
        com.role_power.grpc.RPRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_RP, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role_power.grpc.RPResponse> modifyRP(
        com.role_power.grpc.RPRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_RP, getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_RP = 0;
  private static final int METHODID_ADD_RP = 1;
  private static final int METHODID_REMOVE_RP = 2;
  private static final int METHODID_MODIFY_RP = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RPServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RPServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_RP:
          serviceImpl.listRP((com.role_power.grpc.RPRequest) request,
              (io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse>) responseObserver);
          break;
        case METHODID_ADD_RP:
          serviceImpl.addRP((com.role_power.grpc.RPRequest) request,
              (io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse>) responseObserver);
          break;
        case METHODID_REMOVE_RP:
          serviceImpl.removeRP((com.role_power.grpc.RPRequest) request,
              (io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse>) responseObserver);
          break;
        case METHODID_MODIFY_RP:
          serviceImpl.modifyRP((com.role_power.grpc.RPRequest) request,
              (io.grpc.stub.StreamObserver<com.role_power.grpc.RPResponse>) responseObserver);
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

  private static final class RPServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.role_power.grpc.RolePower.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RPServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RPServiceDescriptorSupplier())
              .addMethod(METHOD_LIST_RP)
              .addMethod(METHOD_ADD_RP)
              .addMethod(METHOD_REMOVE_RP)
              .addMethod(METHOD_MODIFY_RP)
              .build();
        }
      }
    }
    return result;
  }
}
