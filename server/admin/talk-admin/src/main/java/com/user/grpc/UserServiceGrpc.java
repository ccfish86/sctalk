package com.user.grpc;

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
    comments = "Source: user.proto")
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final String SERVICE_NAME = "com.user.grpc.UserService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.user.grpc.UserRequest,
      com.user.grpc.UserResponse> METHOD_LIST_USER =
      io.grpc.MethodDescriptor.<com.user.grpc.UserRequest, com.user.grpc.UserResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.user.grpc.UserService", "listUser"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.user.grpc.UserRequest,
      com.user.grpc.UserResponse> METHOD_ADD_USER =
      io.grpc.MethodDescriptor.<com.user.grpc.UserRequest, com.user.grpc.UserResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.user.grpc.UserService", "addUser"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.user.grpc.UserRequest,
      com.user.grpc.UserResponse> METHOD_MODIFY_PASSWORD =
      io.grpc.MethodDescriptor.<com.user.grpc.UserRequest, com.user.grpc.UserResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.user.grpc.UserService", "modifyPassword"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.user.grpc.UserRequest,
      com.user.grpc.UserResponse> METHOD_REMOVE_USER =
      io.grpc.MethodDescriptor.<com.user.grpc.UserRequest, com.user.grpc.UserResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.user.grpc.UserService", "removeUser"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.user.grpc.UserRequest,
      com.user.grpc.UserResponse> METHOD_MODIFY_USER =
      io.grpc.MethodDescriptor.<com.user.grpc.UserRequest, com.user.grpc.UserResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.user.grpc.UserService", "modifyUser"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.user.grpc.UserResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    return new UserServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new UserServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new UserServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class UserServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_USER, responseObserver);
    }

    /**
     */
    public void addUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_USER, responseObserver);
    }

    /**
     */
    public void modifyPassword(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_PASSWORD, responseObserver);
    }

    /**
     */
    public void removeUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_USER, responseObserver);
    }

    /**
     */
    public void modifyUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_USER, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LIST_USER,
            asyncUnaryCall(
              new MethodHandlers<
                com.user.grpc.UserRequest,
                com.user.grpc.UserResponse>(
                  this, METHODID_LIST_USER)))
          .addMethod(
            METHOD_ADD_USER,
            asyncUnaryCall(
              new MethodHandlers<
                com.user.grpc.UserRequest,
                com.user.grpc.UserResponse>(
                  this, METHODID_ADD_USER)))
          .addMethod(
            METHOD_MODIFY_PASSWORD,
            asyncUnaryCall(
              new MethodHandlers<
                com.user.grpc.UserRequest,
                com.user.grpc.UserResponse>(
                  this, METHODID_MODIFY_PASSWORD)))
          .addMethod(
            METHOD_REMOVE_USER,
            asyncUnaryCall(
              new MethodHandlers<
                com.user.grpc.UserRequest,
                com.user.grpc.UserResponse>(
                  this, METHODID_REMOVE_USER)))
          .addMethod(
            METHOD_MODIFY_USER,
            asyncUnaryCall(
              new MethodHandlers<
                com.user.grpc.UserRequest,
                com.user.grpc.UserResponse>(
                  this, METHODID_MODIFY_USER)))
          .build();
    }
  }

  /**
   */
  public static final class UserServiceStub extends io.grpc.stub.AbstractStub<UserServiceStub> {
    private UserServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     */
    public void listUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_USER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_USER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyPassword(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_PASSWORD, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_USER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyUser(com.user.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<com.user.grpc.UserResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_USER, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UserServiceBlockingStub extends io.grpc.stub.AbstractStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.user.grpc.UserResponse listUser(com.user.grpc.UserRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_USER, getCallOptions(), request);
    }

    /**
     */
    public com.user.grpc.UserResponse addUser(com.user.grpc.UserRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_USER, getCallOptions(), request);
    }

    /**
     */
    public com.user.grpc.UserResponse modifyPassword(com.user.grpc.UserRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_PASSWORD, getCallOptions(), request);
    }

    /**
     */
    public com.user.grpc.UserResponse removeUser(com.user.grpc.UserRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_USER, getCallOptions(), request);
    }

    /**
     */
    public com.user.grpc.UserResponse modifyUser(com.user.grpc.UserRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_USER, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UserServiceFutureStub extends io.grpc.stub.AbstractStub<UserServiceFutureStub> {
    private UserServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.user.grpc.UserResponse> listUser(
        com.user.grpc.UserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_USER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.user.grpc.UserResponse> addUser(
        com.user.grpc.UserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_USER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.user.grpc.UserResponse> modifyPassword(
        com.user.grpc.UserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_PASSWORD, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.user.grpc.UserResponse> removeUser(
        com.user.grpc.UserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_USER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.user.grpc.UserResponse> modifyUser(
        com.user.grpc.UserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_USER, getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_USER = 0;
  private static final int METHODID_ADD_USER = 1;
  private static final int METHODID_MODIFY_PASSWORD = 2;
  private static final int METHODID_REMOVE_USER = 3;
  private static final int METHODID_MODIFY_USER = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UserServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UserServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_USER:
          serviceImpl.listUser((com.user.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<com.user.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_ADD_USER:
          serviceImpl.addUser((com.user.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<com.user.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_MODIFY_PASSWORD:
          serviceImpl.modifyPassword((com.user.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<com.user.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_REMOVE_USER:
          serviceImpl.removeUser((com.user.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<com.user.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_MODIFY_USER:
          serviceImpl.modifyUser((com.user.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<com.user.grpc.UserResponse>) responseObserver);
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

  private static final class UserServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.user.grpc.UserOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceDescriptorSupplier())
              .addMethod(METHOD_LIST_USER)
              .addMethod(METHOD_ADD_USER)
              .addMethod(METHOD_MODIFY_PASSWORD)
              .addMethod(METHOD_REMOVE_USER)
              .addMethod(METHOD_MODIFY_USER)
              .build();
        }
      }
    }
    return result;
  }
}
