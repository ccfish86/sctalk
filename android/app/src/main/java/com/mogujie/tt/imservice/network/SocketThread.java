package com.mogujie.tt.imservice.network;

import com.google.protobuf.GeneratedMessageLite;
import com.mogujie.tt.config.SysConstant;
import com.mogujie.tt.protobuf.base.DataBuffer;
import com.mogujie.tt.protobuf.base.Header;
import com.mogujie.tt.utils.Logger;

import java.net.InetSocketAddress;
import java.util.Arrays;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class SocketThread extends Thread {
	private Bootstrap clientBootstrap = null;
	private ChannelFuture channelFuture = null;
	private Channel channel = null;
	private String strHost = null;
	private int nPort = 0;
	private static Logger logger = Logger.getLogger(SocketThread.class);

	public SocketThread(String strHost, int nPort, ChannelInboundHandler handler) {
		this.strHost = strHost;
		this.nPort = nPort;
		init(handler);
	}

	@Override
	public void run() {
		doConnect();
	}

	private void init(final ChannelInboundHandler handler) {

		NioEventLoopGroup group = new NioEventLoopGroup();

		// only one IO thread
		clientBootstrap = new Bootstrap();

		clientBootstrap.group(group);

		clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		clientBootstrap.option(ChannelOption.TCP_NODELAY, true);
		clientBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

		// 指定channel类型
		clientBootstrap.channel(NioSocketChannel.class);

		clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {

			public void initChannel(SocketChannel channel) throws Exception
			{
				ChannelPipeline pipeline = channel.pipeline();
				// 接收的数据包解码
				pipeline.addLast("decoder", new LengthFieldBasedFrameDecoder(400 * 1024, 0, 4, -4, 0));
				// 发送的数据包编码
				pipeline.addLast("encoder", new ProtobufEncoder());
				// 具体的业务处理，这个handler只负责接收数据，并传递给dispatcher
				pipeline.addLast("handler", handler);
			}

		});

		ChannelFutureListener channelFutureListener = new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				// logger.debug(Config.TAG, "isDone:" + f.isDone() + " isSuccess:" + f.isSuccess() +
				// " cause" + f.cause() + " isCancelled" + f.isCancelled());
				if (f.isSuccess()) {
					logger.d("连接服务器成功");
				} else {
					logger.d("连接服务器失败");

					f.channel().eventLoop().shutdownGracefully();

//					// 3秒后重新连接
//					f.channel().eventLoop().schedule(new Runnable() {
//						@Override
//						public void run() {
//							doConnect();
//						}
//					}, 10, TimeUnit.SECONDS);
				}
			}
		};
	}

	public boolean doConnect() {
		try {
			if ((null == channel || (null != channel && !channel.isOpen()))
					&& null != this.strHost && this.nPort > 0) {
				logger.i("do connect-> %s:%d", strHost, nPort);
				// Start the connection attempt.
				channelFuture = clientBootstrap.connect(new InetSocketAddress(
					strHost, nPort));
				// Wait until the connection attempt succeeds or fails.
				channel = channelFuture.channel();
			}
			return true;

		} catch (Exception e) {
			logger.e("do connect failed. e: %s", e.getLocalizedMessage());
			return false;
		}
	}

	public Channel getChannel() {
		return channel;
	}

	public void close() {
		if (null == channelFuture)
			return;
		if (null != channelFuture.channel()) {
			channelFuture.channel().close();
		}
        channelFuture.cancel(true);
		channelFuture = null;
		channel = null;
	}


    // todo check
    @Deprecated
    public boolean isClose(){
        if(channelFuture != null && channelFuture.channel() != null){
            return !channelFuture.channel().isOpen();
        }
        return true;
    }

    /**
     * @param requset
     * @param header
     * @return
     */
    public boolean sendRequest(GeneratedMessageLite requset,Header header){
        int bodySize = requset.getSerializedSize();

		if (null != channel) {
			byte[] bytes = requset.toByteArray();
			byte[] hbytes = header.encode().array();

			ByteBuf byteBuf = channel.alloc().buffer(SysConstant.PROTOCOL_HEADER_LENGTH + bodySize);

			byteBuf.writeBytes(hbytes);
			byteBuf.writeBytes(bytes);

            /**底层的状态要提前判断，netty抛出的异常上层catch不到*/
            boolean isW = channel.isWritable();
            boolean isC  = channel.isOpen();
            if(!(isW && isC)){
                throw  new RuntimeException("#sendRequest#channel is close!");
            }
			//channel.writeAndFlush(buffer.getOrignalBuffer());
			channel.writeAndFlush(byteBuf);
            logger.d("packet#send ok");
            return true;
        } else {
            logger.e("packet#send failed");
            return false;
        }
    }

}
