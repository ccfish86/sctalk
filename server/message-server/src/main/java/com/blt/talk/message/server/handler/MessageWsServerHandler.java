package com.blt.talk.message.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.message.server.manager.HandlerManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

/**
 * 用来处理websocket过来的消息
 * 
 * @author 袁贵
 * @version 1.3
 * @since 1.3
 */
public final class MessageWsServerHandler extends MessageServerHandler {

    private Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);

    private WebSocketServerHandshaker handshaker;

    public MessageWsServerHandler(HandlerManager handlerManager) {
        super(handlerManager);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {
            logger.debug("HttpRequest");
            // 完成 握手
            handleHttpRequest(ctx, (HttpRequest) msg);
        } else if (msg instanceof IMProtoMessage) {
            super.channelRead(ctx, msg);
        } else if (msg instanceof WebSocketFrame) {
            // websocketService.handleFrame(ctx, (WebSocketFrame) msg);
            // 这句应该走不到
            logger.debug("WebSocketFrame");
        } else {
            logger.debug("other:{}", msg);
        }
    }

    /**
     * 处理Http请求，完成WebSocket握手<br/>
     * 注意：WebSocket连接第一次请求使用的是Http
     * 
     * @param ctx
     * @param request
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest request)
            throws Exception {
        // 如果HTTP解码失败，返回HHTP异常
        if (!request.getDecoderResult().isSuccess()
                || (!"websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, request,
                    new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // 正常WebSocket的Http连接请求，构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://" + request.headers().get(HttpHeaders.Names.HOST), null, false);
        handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) {
            // 无法处理的websocket版本
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 向客户端发送websocket握手,完成握手
            logger.debug("向客户端发送websocket握手,完成握手");
            handshaker.handshake(ctx.channel(), request);
        }
    }

    /**
     * Http返回
     * 
     * @param ctx
     * @param request
     * @param response
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest request,
            DefaultHttpResponse response) {
        // 返回应答给客户端
        if (response.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if (!HttpHeaders.isKeepAlive(request) || response.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
