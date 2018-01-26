package com.manager_role.grpc;

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
    comments = "Source: manager_role.proto")
public final class MRServiceGrpc {

  private MRServiceGrpc() {}

  public static final String SERVICE_NAME = "com.manager_role.grpc.MRService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager_role.grpc.MRRequest,
      com.manager_role.grpc.MRResponse> METHOD_LIST_MR =
      io.grpc.MethodDescriptor.<com.manager_role.grpc.MRRequest, com.manager_role.grpc.MRResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager_role.grpc.MRService", "listMR"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager_role.grpc.MRRequest,
      com.manager_role.grpc.MRResponse> METHOD_ADD_MR =
      io.grpc.MethodDescriptor.<com.manager_role.grpc.MRRequest, com.manager_role.grpc.MRResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager_role.grpc.MRService", "addMR"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager_role.grpc.MRRequest,
      com.manager_role.grpc.MRResponse> METHOD_REMOVE_MR =
      io.grpc.MethodDescriptor.<com.manager_role.grpc.MRRequest, com.manager_role.grpc.MRResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager_role.grpc.MRService", "removeMR"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.manager_role.grpc.MRRequest,
      com.manager_role.grpc.MRResponse> METHOD_MODIFY_MR =
      io.grpc.MethodDescriptor.<com.manager_role.grpc.MRRequest, com.manager_role.grpc.MRResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.manager_role.grpc.MRService", "modifyMR"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.manager_role.grpc.MRResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MRServiceStub newStub(io.grpc.Channel channel) {
    return new MRServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MRServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MRServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MRServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MRServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class MRServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_MR, responseObserver);
    }

    /**
     */
    public void addMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_MR, responseObserver);
    }

    /**
     */
    public void removeMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_MR, responseObserver);
    }

    /**
     */
    public void modifyMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_MR, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LIST_MR,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager_role.grpc.MRRequest,
                com.manager_role.grpc.MRResponse>(
                  this, METHODID_LIST_MR)))
          .addMethod(
            METHOD_ADD_MR,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager_role.grpc.MRRequest,
                com.manager_role.grpc.MRResponse>(
                  this, METHODID_ADD_MR)))
          .addMethod(
            METHOD_REMOVE_MR,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager_role.grpc.MRRequest,
                com.manager_role.grpc.MRResponse>(
                  this, METHODID_REMOVE_MR)))
          .addMethod(
            METHOD_MODIFY_MR,
            asyncUnaryCall(
              new MethodHandlers<
                com.manager_role.grpc.MRRequest,
                com.manager_role.grpc.MRResponse>(
                  this, METHODID_MODIFY_MR)))
          .build();
    }
  }

  /**
   */
  public static final class MRServiceStub extends io.grpc.stub.AbstractStub<MRServiceStub> {
    private MRServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MRServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MRServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MRServiceStub(channel, callOptions);
    }

    /**
     */
    public void listMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_MR, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_MR, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_MR, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyMR(com.manager_role.grpc.MRRequest request,
        io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_MR, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MRServiceBlockingStub extends io.grpc.stub.AbstractStub<MRServiceBlockingStub> {
    private MRServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MRServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MRServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MRServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.manager_role.grpc.MRResponse listMR(com.manager_role.grpc.MRRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_MR, getCallOptions(), request);
    }

    /**
     */
    public com.manager_role.grpc.MRResponse addMR(com.manager_role.grpc.MRRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_MR, getCallOptions(), request);
    }

    /**
     */
    public com.manager_role.grpc.MRResponse removeMR(com.manager_role.grpc.MRRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_MR, getCallOptions(), request);
    }

    /**
     */
    public com.manager_role.grpc.MRResponse modifyMR(com.manager_role.grpc.MRRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_MR, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MRServiceFutureStub extends io.grpc.stub.AbstractStub<MRServiceFutureStub> {
    private MRServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MRServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MRServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MRServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager_role.grpc.MRResponse> listMR(
        com.manager_role.grpc.MRRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_MR, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager_role.grpc.MRResponse> addMR(
        com.manager_role.grpc.MRRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_MR, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager_role.grpc.MRResponse> removeMR(
        com.manager_role.grpc.MRRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_MR, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.manager_role.grpc.MRResponse> modifyMR(
        com.manager_role.grpc.MRRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_MR, getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_MR = 0;
  private static final int METHODID_ADD_MR = 1;
  private static final int METHODID_REMOVE_MR = 2;
  private static final int METHODID_MODIFY_MR = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MRServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MRServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_MR:
          serviceImpl.listMR((com.manager_role.grpc.MRRequest) request,
              (io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse>) responseObserver);
          break;
        case METHODID_ADD_MR:
          serviceImpl.addMR((com.manager_role.grpc.MRRequest) request,
              (io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse>) responseObserver);
          break;
        case METHODID_REMOVE_MR:
          serviceImpl.removeMR((com.manager_role.grpc.MRRequest) request,
              (io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse>) responseObserver);
          break;
        case METHODID_MODIFY_MR:
          serviceImpl.modifyMR((com.manager_role.grpc.MRRequest) request,
              (io.grpc.stub.StreamObserver<com.manager_role.grpc.MRResponse>) responseObserver);
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

  private static final class MRServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.manager_role.grpc.ManagerRole.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MRServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MRServiceDescriptorSupplier())
              .addMethod(METHOD_LIST_MR)
              .addMethod(METHOD_ADD_MR)
              .addMethod(METHOD_REMOVE_MR)
              .addMethod(METHOD_MODIFY_MR)
              .build();
        }
      }
    }
    return result;
  }
}
