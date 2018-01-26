package com.manager.grpc;

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
    comments = "Source: manager.proto")
public final class ManagerServiceGrpc {

  private ManagerServiceGrpc() {}

  public static final String SERVICE_NAME = "com.manager.grpc.ManagerService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_LOGIN =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "login"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_ADD_MANAGER =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "addManager"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_MODIFY_PASSWORD =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "modifyPassword"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_REMOVE_MANAGER =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "removeManager"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_LIST_MANAGER =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "listManager"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_GET_INFO =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "getInfo"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_CHANGE_ROLE =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "changeRole"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager.grpc.ManagerRequest,
      com.manager.grpc.ManagerResponse> METHOD_MODIFY =
      io.grpc.MethodDescriptor.<com.manager.grpc.ManagerRequest, com.manager.grpc.ManagerResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager.grpc.ManagerService", "modify"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager.grpc.ManagerResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ManagerServiceStub newStub(io.grpc.Channel channel) {
    return new ManagerServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ManagerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ManagerServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ManagerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ManagerServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ManagerServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void login(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LOGIN, responseObserver);
    }

    /**
     */
    public void addManager(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_MANAGER, responseObserver);
    }

    /**
     */
    public void modifyPassword(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_PASSWORD, responseObserver);
    }

    /**
     */
    public void removeManager(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_MANAGER, responseObserver);
    }

    /**
     */
    public void listManager(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_MANAGER, responseObserver);
    }

    /**
     */
    public void getInfo(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_INFO, responseObserver);
    }

    /**
     */
    public void changeRole(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CHANGE_ROLE, responseObserver);
    }

    /**
     */
    public void modify(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LOGIN,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_LOGIN)))
          .addMethod(
            METHOD_ADD_MANAGER,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_ADD_MANAGER)))
          .addMethod(
            METHOD_MODIFY_PASSWORD,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_MODIFY_PASSWORD)))
          .addMethod(
            METHOD_REMOVE_MANAGER,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_REMOVE_MANAGER)))
          .addMethod(
            METHOD_LIST_MANAGER,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_LIST_MANAGER)))
          .addMethod(
            METHOD_GET_INFO,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_GET_INFO)))
          .addMethod(
            METHOD_CHANGE_ROLE,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_CHANGE_ROLE)))
          .addMethod(
            METHOD_MODIFY,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager.grpc.ManagerRequest,
                com.manager.grpc.ManagerResponse>(
                  this, METHODID_MODIFY)))
          .build();
    }
  }

  /**
   */
  public static final class ManagerServiceStub extends io.grpc.stub.AbstractStub<ManagerServiceStub> {
    private ManagerServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ManagerServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ManagerServiceStub(channel, callOptions);
    }

    /**
     */
    public void login(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LOGIN, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addManager(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_MANAGER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyPassword(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_PASSWORD, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeManager(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_MANAGER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listManager(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_MANAGER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getInfo(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_INFO, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void changeRole(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CHANGE_ROLE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modify(com.manager.grpc.ManagerRequest request,
        io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ManagerServiceBlockingStub extends io.grpc.stub.AbstractStub<ManagerServiceBlockingStub> {
    private ManagerServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ManagerServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ManagerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse login(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LOGIN, getCallOptions(), request);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse addManager(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_MANAGER, getCallOptions(), request);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse modifyPassword(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_PASSWORD, getCallOptions(), request);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse removeManager(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_MANAGER, getCallOptions(), request);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse listManager(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_MANAGER, getCallOptions(), request);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse getInfo(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_INFO, getCallOptions(), request);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse changeRole(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CHANGE_ROLE, getCallOptions(), request);
    }

    /**
     */
    public com.manager.grpc.ManagerResponse modify(com.manager.grpc.ManagerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ManagerServiceFutureStub extends io.grpc.stub.AbstractStub<ManagerServiceFutureStub> {
    private ManagerServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ManagerServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ManagerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> login(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LOGIN, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> addManager(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_MANAGER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> modifyPassword(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_PASSWORD, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> removeManager(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_MANAGER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> listManager(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_MANAGER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> getInfo(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_INFO, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> changeRole(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CHANGE_ROLE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager.grpc.ManagerResponse> modify(
        com.manager.grpc.ManagerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY, getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_ADD_MANAGER = 1;
  private static final int METHODID_MODIFY_PASSWORD = 2;
  private static final int METHODID_REMOVE_MANAGER = 3;
  private static final int METHODID_LIST_MANAGER = 4;
  private static final int METHODID_GET_INFO = 5;
  private static final int METHODID_CHANGE_ROLE = 6;
  private static final int METHODID_MODIFY = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ManagerServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ManagerServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
          break;
        case METHODID_ADD_MANAGER:
          serviceImpl.addManager((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
          break;
        case METHODID_MODIFY_PASSWORD:
          serviceImpl.modifyPassword((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
          break;
        case METHODID_REMOVE_MANAGER:
          serviceImpl.removeManager((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
          break;
        case METHODID_LIST_MANAGER:
          serviceImpl.listManager((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
          break;
        case METHODID_GET_INFO:
          serviceImpl.getInfo((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
          break;
        case METHODID_CHANGE_ROLE:
          serviceImpl.changeRole((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
          break;
        case METHODID_MODIFY:
          serviceImpl.modify((com.manager.grpc.ManagerRequest) request,
              (io.grpc.stub.StreamObserver<com.manager.grpc.ManagerResponse>) responseObserver);
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

  private static final class ManagerServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.manager.grpc.ManagerOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ManagerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ManagerServiceDescriptorSupplier())
              .addMethod(METHOD_LOGIN)
              .addMethod(METHOD_ADD_MANAGER)
              .addMethod(METHOD_MODIFY_PASSWORD)
              .addMethod(METHOD_REMOVE_MANAGER)
              .addMethod(METHOD_LIST_MANAGER)
              .addMethod(METHOD_GET_INFO)
              .addMethod(METHOD_CHANGE_ROLE)
              .addMethod(METHOD_MODIFY)
              .build();
        }
      }
    }
    return result;
  }
}
