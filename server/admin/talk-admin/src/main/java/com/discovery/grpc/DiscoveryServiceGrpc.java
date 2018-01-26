package com.discovery.grpc;

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
    comments = "Source: Discovery.proto")
public final class DiscoveryServiceGrpc {

  private DiscoveryServiceGrpc() {}

  public static final String SERVICE_NAME = "com.discovery.grpc.DiscoveryService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.discovery.grpc.DiscoveryRequest,
      com.discovery.grpc.DiscoveryResponse> METHOD_LIST_DISCOVERY =
      io.grpc.MethodDescriptor.<com.discovery.grpc.DiscoveryRequest, com.discovery.grpc.DiscoveryResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.discovery.grpc.DiscoveryService", "listDiscovery"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.discovery.grpc.DiscoveryRequest,
      com.discovery.grpc.DiscoveryResponse> METHOD_ADD_DISCOVERY =
      io.grpc.MethodDescriptor.<com.discovery.grpc.DiscoveryRequest, com.discovery.grpc.DiscoveryResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.discovery.grpc.DiscoveryService", "addDiscovery"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.discovery.grpc.DiscoveryRequest,
      com.discovery.grpc.DiscoveryResponse> METHOD_REMOVE_DISCOVERY =
      io.grpc.MethodDescriptor.<com.discovery.grpc.DiscoveryRequest, com.discovery.grpc.DiscoveryResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.discovery.grpc.DiscoveryService", "removeDiscovery"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.discovery.grpc.DiscoveryRequest,
      com.discovery.grpc.DiscoveryResponse> METHOD_MODIFY_DISCOVERY =
      io.grpc.MethodDescriptor.<com.discovery.grpc.DiscoveryRequest, com.discovery.grpc.DiscoveryResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.discovery.grpc.DiscoveryService", "modifyDiscovery"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.discovery.grpc.DiscoveryResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DiscoveryServiceStub newStub(io.grpc.Channel channel) {
    return new DiscoveryServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DiscoveryServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DiscoveryServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DiscoveryServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DiscoveryServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class DiscoveryServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_DISCOVERY, responseObserver);
    }

    /**
     */
    public void addDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_DISCOVERY, responseObserver);
    }

    /**
     */
    public void removeDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REMOVE_DISCOVERY, responseObserver);
    }

    /**
     */
    public void modifyDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY_DISCOVERY, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LIST_DISCOVERY,
            asyncUnaryCall(
              new MethodHandlers<
                com.discovery.grpc.DiscoveryRequest,
                com.discovery.grpc.DiscoveryResponse>(
                  this, METHODID_LIST_DISCOVERY)))
          .addMethod(
            METHOD_ADD_DISCOVERY,
            asyncUnaryCall(
              new MethodHandlers<
                com.discovery.grpc.DiscoveryRequest,
                com.discovery.grpc.DiscoveryResponse>(
                  this, METHODID_ADD_DISCOVERY)))
          .addMethod(
            METHOD_REMOVE_DISCOVERY,
            asyncUnaryCall(
              new MethodHandlers<
                com.discovery.grpc.DiscoveryRequest,
                com.discovery.grpc.DiscoveryResponse>(
                  this, METHODID_REMOVE_DISCOVERY)))
          .addMethod(
            METHOD_MODIFY_DISCOVERY,
            asyncUnaryCall(
              new MethodHandlers<
                com.discovery.grpc.DiscoveryRequest,
                com.discovery.grpc.DiscoveryResponse>(
                  this, METHODID_MODIFY_DISCOVERY)))
          .build();
    }
  }

  /**
   */
  public static final class DiscoveryServiceStub extends io.grpc.stub.AbstractStub<DiscoveryServiceStub> {
    private DiscoveryServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DiscoveryServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DiscoveryServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DiscoveryServiceStub(channel, callOptions);
    }

    /**
     */
    public void listDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_DISCOVERY, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_DISCOVERY, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REMOVE_DISCOVERY, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modifyDiscovery(com.discovery.grpc.DiscoveryRequest request,
        io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY_DISCOVERY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DiscoveryServiceBlockingStub extends io.grpc.stub.AbstractStub<DiscoveryServiceBlockingStub> {
    private DiscoveryServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DiscoveryServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DiscoveryServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DiscoveryServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.discovery.grpc.DiscoveryResponse listDiscovery(com.discovery.grpc.DiscoveryRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_DISCOVERY, getCallOptions(), request);
    }

    /**
     */
    public com.discovery.grpc.DiscoveryResponse addDiscovery(com.discovery.grpc.DiscoveryRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_DISCOVERY, getCallOptions(), request);
    }

    /**
     */
    public com.discovery.grpc.DiscoveryResponse removeDiscovery(com.discovery.grpc.DiscoveryRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REMOVE_DISCOVERY, getCallOptions(), request);
    }

    /**
     */
    public com.discovery.grpc.DiscoveryResponse modifyDiscovery(com.discovery.grpc.DiscoveryRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY_DISCOVERY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DiscoveryServiceFutureStub extends io.grpc.stub.AbstractStub<DiscoveryServiceFutureStub> {
    private DiscoveryServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DiscoveryServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DiscoveryServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DiscoveryServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.discovery.grpc.DiscoveryResponse> listDiscovery(
        com.discovery.grpc.DiscoveryRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_DISCOVERY, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.discovery.grpc.DiscoveryResponse> addDiscovery(
        com.discovery.grpc.DiscoveryRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_DISCOVERY, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.discovery.grpc.DiscoveryResponse> removeDiscovery(
        com.discovery.grpc.DiscoveryRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REMOVE_DISCOVERY, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.discovery.grpc.DiscoveryResponse> modifyDiscovery(
        com.discovery.grpc.DiscoveryRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY_DISCOVERY, getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_DISCOVERY = 0;
  private static final int METHODID_ADD_DISCOVERY = 1;
  private static final int METHODID_REMOVE_DISCOVERY = 2;
  private static final int METHODID_MODIFY_DISCOVERY = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DiscoveryServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DiscoveryServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_DISCOVERY:
          serviceImpl.listDiscovery((com.discovery.grpc.DiscoveryRequest) request,
              (io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse>) responseObserver);
          break;
        case METHODID_ADD_DISCOVERY:
          serviceImpl.addDiscovery((com.discovery.grpc.DiscoveryRequest) request,
              (io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse>) responseObserver);
          break;
        case METHODID_REMOVE_DISCOVERY:
          serviceImpl.removeDiscovery((com.discovery.grpc.DiscoveryRequest) request,
              (io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse>) responseObserver);
          break;
        case METHODID_MODIFY_DISCOVERY:
          serviceImpl.modifyDiscovery((com.discovery.grpc.DiscoveryRequest) request,
              (io.grpc.stub.StreamObserver<com.discovery.grpc.DiscoveryResponse>) responseObserver);
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

  private static final class DiscoveryServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.discovery.grpc.DiscoveryOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DiscoveryServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DiscoveryServiceDescriptorSupplier())
              .addMethod(METHOD_LIST_DISCOVERY)
              .addMethod(METHOD_ADD_DISCOVERY)
              .addMethod(METHOD_REMOVE_DISCOVERY)
              .addMethod(METHOD_MODIFY_DISCOVERY)
              .build();
        }
      }
    }
    return result;
  }
}
