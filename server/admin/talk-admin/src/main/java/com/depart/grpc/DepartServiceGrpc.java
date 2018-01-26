package com.depart.grpc;

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
    comments = "Source: depart.proto")
public final class DepartServiceGrpc {

  private DepartServiceGrpc() {}

  public static final String SERVICE_NAME = "com.depart.grpc.DepartService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.depart.grpc.DepartRequest,
      com.depart.grpc.DepartResponse> METHOD_LIST_DEPART =
      io.grpc.MethodDescriptor.<com.depart.grpc.DepartRequest, com.depart.grpc.DepartResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.depart.grpc.DepartService", "listDepart"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.depart.grpc.DepartRequest,
      com.depart.grpc.DepartResponse> METHOD_ADD_DEPART =
      io.grpc.MethodDescriptor.<com.depart.grpc.DepartRequest, com.depart.grpc.DepartResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.depart.grpc.DepartService", "addDepart"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.depart.grpc.DepartRequest,
      com.depart.grpc.DepartResponse> METHOD_REMOVE_DEPART =
      io.grpc.MethodDescriptor.<com.depart.grpc.DepartRequest, com.depart.grpc.DepartResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.depart.grpc.DepartService", "removeDepart"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.depart.grpc.DepartRequest,
      com.depart.grpc.DepartResponse> METHOD_MODIFY_DEPART =
      io.grpc.MethodDescriptor.<com.depart.grpc.DepartRequest, com.depart.grpc.DepartResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.depart.grpc.DepartService", "modifyDepart"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.depart.grpc.DepartResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DepartServiceStub newStub(io.grpc.Channel channel) {
    return new DepartServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DepartServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DepartServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DepartServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DepartServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class DepartServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_DEPART, responseObserver);
    }

    /**
     */
    public void addDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_DEPART, responseObserver);
    }

    /**
     */
    public void removeDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_DEPART, responseObserver);
    }

    /**
     */
    public void modifyDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_DEPART, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LIST_DEPART,
            asyncUnaryCall(
              new MethodHandlers<
                com.depart.grpc.DepartRequest,
                com.depart.grpc.DepartResponse>(
                  this, METHODID_LIST_DEPART)))
          .addMethod(
            METHOD_ADD_DEPART,
            asyncUnaryCall(
              new MethodHandlers<
                com.depart.grpc.DepartRequest,
                com.depart.grpc.DepartResponse>(
                  this, METHODID_ADD_DEPART)))
          .addMethod(
            METHOD_REMOVE_DEPART,
            asyncUnaryCall(
              new MethodHandlers<
                com.depart.grpc.DepartRequest,
                com.depart.grpc.DepartResponse>(
                  this, METHODID_REMOVE_DEPART)))
          .addMethod(
            METHOD_MODIFY_DEPART,
            asyncUnaryCall(
              new MethodHandlers<
                com.depart.grpc.DepartRequest,
                com.depart.grpc.DepartResponse>(
                  this, METHODID_MODIFY_DEPART)))
          .build();
    }
  }

  /**
   */
  public static final class DepartServiceStub extends io.grpc.stub.AbstractStub<DepartServiceStub> {
    private DepartServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DepartServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DepartServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DepartServiceStub(channel, callOptions);
    }

    /**
     */
    public void listDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_DEPART, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_DEPART, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_DEPART, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyDepart(com.depart.grpc.DepartRequest request,
        io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_DEPART, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DepartServiceBlockingStub extends io.grpc.stub.AbstractStub<DepartServiceBlockingStub> {
    private DepartServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DepartServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DepartServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DepartServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.depart.grpc.DepartResponse listDepart(com.depart.grpc.DepartRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_DEPART, getCallOptions(), request);
    }

    /**
     */
    public com.depart.grpc.DepartResponse addDepart(com.depart.grpc.DepartRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_DEPART, getCallOptions(), request);
    }

    /**
     */
    public com.depart.grpc.DepartResponse removeDepart(com.depart.grpc.DepartRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_DEPART, getCallOptions(), request);
    }

    /**
     */
    public com.depart.grpc.DepartResponse modifyDepart(com.depart.grpc.DepartRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_DEPART, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DepartServiceFutureStub extends io.grpc.stub.AbstractStub<DepartServiceFutureStub> {
    private DepartServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DepartServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DepartServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DepartServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.depart.grpc.DepartResponse> listDepart(
        com.depart.grpc.DepartRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_DEPART, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.depart.grpc.DepartResponse> addDepart(
        com.depart.grpc.DepartRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_DEPART, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.depart.grpc.DepartResponse> removeDepart(
        com.depart.grpc.DepartRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_DEPART, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.depart.grpc.DepartResponse> modifyDepart(
        com.depart.grpc.DepartRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_DEPART, getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_DEPART = 0;
  private static final int METHODID_ADD_DEPART = 1;
  private static final int METHODID_REMOVE_DEPART = 2;
  private static final int METHODID_MODIFY_DEPART = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DepartServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DepartServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_DEPART:
          serviceImpl.listDepart((com.depart.grpc.DepartRequest) request,
              (io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse>) responseObserver);
          break;
        case METHODID_ADD_DEPART:
          serviceImpl.addDepart((com.depart.grpc.DepartRequest) request,
              (io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse>) responseObserver);
          break;
        case METHODID_REMOVE_DEPART:
          serviceImpl.removeDepart((com.depart.grpc.DepartRequest) request,
              (io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse>) responseObserver);
          break;
        case METHODID_MODIFY_DEPART:
          serviceImpl.modifyDepart((com.depart.grpc.DepartRequest) request,
              (io.grpc.stub.StreamObserver<com.depart.grpc.DepartResponse>) responseObserver);
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

  private static final class DepartServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.depart.grpc.DepartOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DepartServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DepartServiceDescriptorSupplier())
              .addMethod(METHOD_LIST_DEPART)
              .addMethod(METHOD_ADD_DEPART)
              .addMethod(METHOD_REMOVE_DEPART)
              .addMethod(METHOD_MODIFY_DEPART)
              .build();
        }
      }
    }
    return result;
  }
}
