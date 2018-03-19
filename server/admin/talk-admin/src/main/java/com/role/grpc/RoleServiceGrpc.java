package com.role.grpc;

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
    comments = "Source: role.proto")
public final class RoleServiceGrpc {

  private RoleServiceGrpc() {}

  public static final String SERVICE_NAME = "com.role.grpc.RoleService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role.grpc.RoleRequest,
      com.role.grpc.RoleResponse> METHOD_LIST_ROLE =
      io.grpc.MethodDescriptor.<com.role.grpc.RoleRequest, com.role.grpc.RoleResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role.grpc.RoleService", "listRole"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role.grpc.RoleRequest,
      com.role.grpc.RoleResponse> METHOD_ADD_ROLE =
      io.grpc.MethodDescriptor.<com.role.grpc.RoleRequest, com.role.grpc.RoleResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role.grpc.RoleService", "addRole"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role.grpc.RoleRequest,
      com.role.grpc.RoleResponse> METHOD_REMOVE_ROLE =
      io.grpc.MethodDescriptor.<com.role.grpc.RoleRequest, com.role.grpc.RoleResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role.grpc.RoleService", "removeRole"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role.grpc.RoleRequest,
      com.role.grpc.RoleResponse> METHOD_MODIFY_ROLE =
      io.grpc.MethodDescriptor.<com.role.grpc.RoleRequest, com.role.grpc.RoleResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role.grpc.RoleService", "modifyRole"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role.grpc.RoleRequest,
      com.role.grpc.RoleResponse> METHOD_CHANGE_POWER =
      io.grpc.MethodDescriptor.<com.role.grpc.RoleRequest, com.role.grpc.RoleResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role.grpc.RoleService", "changePower"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.role.grpc.RoleRequest,
      com.role.grpc.RoleResponse> METHOD_GET_ROLE =
      io.grpc.MethodDescriptor.<com.role.grpc.RoleRequest, com.role.grpc.RoleResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.role.grpc.RoleService", "getRole"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.role.grpc.RoleResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RoleServiceStub newStub(io.grpc.Channel channel) {
    return new RoleServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RoleServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RoleServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RoleServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RoleServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class RoleServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_ROLE, responseObserver);
    }

    /**
     */
    public void addRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_ROLE, responseObserver);
    }

    /**
     */
    public void removeRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_ROLE, responseObserver);
    }

    /**
     */
    public void modifyRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_ROLE, responseObserver);
    }

    /**
     */
    public void changePower(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CHANGE_POWER, responseObserver);
    }

    /**
     */
    public void getRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_ROLE, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LIST_ROLE,
            asyncUnaryCall(
              new MethodHandlers<
                com.role.grpc.RoleRequest,
                com.role.grpc.RoleResponse>(
                  this, METHODID_LIST_ROLE)))
          .addMethod(
            METHOD_ADD_ROLE,
            asyncUnaryCall(
              new MethodHandlers<
                com.role.grpc.RoleRequest,
                com.role.grpc.RoleResponse>(
                  this, METHODID_ADD_ROLE)))
          .addMethod(
            METHOD_REMOVE_ROLE,
            asyncUnaryCall(
              new MethodHandlers<
                com.role.grpc.RoleRequest,
                com.role.grpc.RoleResponse>(
                  this, METHODID_REMOVE_ROLE)))
          .addMethod(
            METHOD_MODIFY_ROLE,
            asyncUnaryCall(
              new MethodHandlers<
                com.role.grpc.RoleRequest,
                com.role.grpc.RoleResponse>(
                  this, METHODID_MODIFY_ROLE)))
          .addMethod(
            METHOD_CHANGE_POWER,
            asyncUnaryCall(
              new MethodHandlers<
                com.role.grpc.RoleRequest,
                com.role.grpc.RoleResponse>(
                  this, METHODID_CHANGE_POWER)))
          .addMethod(
            METHOD_GET_ROLE,
            asyncUnaryCall(
              new MethodHandlers<
                com.role.grpc.RoleRequest,
                com.role.grpc.RoleResponse>(
                  this, METHODID_GET_ROLE)))
          .build();
    }
  }

  /**
   */
  public static final class RoleServiceStub extends io.grpc.stub.AbstractStub<RoleServiceStub> {
    private RoleServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RoleServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoleServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RoleServiceStub(channel, callOptions);
    }

    /**
     */
    public void listRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_ROLE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_ROLE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_ROLE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_ROLE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void changePower(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CHANGE_POWER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRole(com.role.grpc.RoleRequest request,
        io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_ROLE, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RoleServiceBlockingStub extends io.grpc.stub.AbstractStub<RoleServiceBlockingStub> {
    private RoleServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RoleServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoleServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RoleServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.role.grpc.RoleResponse listRole(com.role.grpc.RoleRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_ROLE, getCallOptions(), request);
    }

    /**
     */
    public com.role.grpc.RoleResponse addRole(com.role.grpc.RoleRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_ROLE, getCallOptions(), request);
    }

    /**
     */
    public com.role.grpc.RoleResponse removeRole(com.role.grpc.RoleRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_ROLE, getCallOptions(), request);
    }

    /**
     */
    public com.role.grpc.RoleResponse modifyRole(com.role.grpc.RoleRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_ROLE, getCallOptions(), request);
    }

    /**
     */
    public com.role.grpc.RoleResponse changePower(com.role.grpc.RoleRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CHANGE_POWER, getCallOptions(), request);
    }

    /**
     */
    public com.role.grpc.RoleResponse getRole(com.role.grpc.RoleRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_ROLE, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RoleServiceFutureStub extends io.grpc.stub.AbstractStub<RoleServiceFutureStub> {
    private RoleServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RoleServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoleServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RoleServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role.grpc.RoleResponse> listRole(
        com.role.grpc.RoleRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_ROLE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role.grpc.RoleResponse> addRole(
        com.role.grpc.RoleRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_ROLE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role.grpc.RoleResponse> removeRole(
        com.role.grpc.RoleRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_ROLE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role.grpc.RoleResponse> modifyRole(
        com.role.grpc.RoleRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_ROLE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role.grpc.RoleResponse> changePower(
        com.role.grpc.RoleRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CHANGE_POWER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.role.grpc.RoleResponse> getRole(
        com.role.grpc.RoleRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_ROLE, getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_ROLE = 0;
  private static final int METHODID_ADD_ROLE = 1;
  private static final int METHODID_REMOVE_ROLE = 2;
  private static final int METHODID_MODIFY_ROLE = 3;
  private static final int METHODID_CHANGE_POWER = 4;
  private static final int METHODID_GET_ROLE = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RoleServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RoleServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_ROLE:
          serviceImpl.listRole((com.role.grpc.RoleRequest) request,
              (io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse>) responseObserver);
          break;
        case METHODID_ADD_ROLE:
          serviceImpl.addRole((com.role.grpc.RoleRequest) request,
              (io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse>) responseObserver);
          break;
        case METHODID_REMOVE_ROLE:
          serviceImpl.removeRole((com.role.grpc.RoleRequest) request,
              (io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse>) responseObserver);
          break;
        case METHODID_MODIFY_ROLE:
          serviceImpl.modifyRole((com.role.grpc.RoleRequest) request,
              (io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse>) responseObserver);
          break;
        case METHODID_CHANGE_POWER:
          serviceImpl.changePower((com.role.grpc.RoleRequest) request,
              (io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse>) responseObserver);
          break;
        case METHODID_GET_ROLE:
          serviceImpl.getRole((com.role.grpc.RoleRequest) request,
              (io.grpc.stub.StreamObserver<com.role.grpc.RoleResponse>) responseObserver);
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

  private static final class RoleServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.role.grpc.RoleOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RoleServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RoleServiceDescriptorSupplier())
              .addMethod(METHOD_LIST_ROLE)
              .addMethod(METHOD_ADD_ROLE)
              .addMethod(METHOD_REMOVE_ROLE)
              .addMethod(METHOD_MODIFY_ROLE)
              .addMethod(METHOD_CHANGE_POWER)
              .addMethod(METHOD_GET_ROLE)
              .build();
        }
      }
    }
    return result;
  }
}
