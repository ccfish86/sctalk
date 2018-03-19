/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.code;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.analysis.ProtobufParseMap;
import com.blt.talk.common.constant.SysConstant;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;

/**
 * 供WebSocketFrame使用的
 * <br>
 * 接收消息处理(Protobuf to IMProtoMessage)
 * 
 * @author 袁贵
 * @version 1.3
 * @since  1.3
 */
public class PacketWsFrameDecoder extends MessageToMessageDecoder<WebSocketFrame>  implements WebSocketFrameDecoder {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /* (non-Javadoc)
     * @see io.netty.handler.codec.MessageToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
            throws Exception {
        try {
            final ByteBuf in = msg.content();
            logger.debug("Protobuf decode started.");
            in.markReaderIndex();
            if (in.readableBytes() < 4) {
                logger.info("Readable Bytes length less than 4 bytes, ignored");
                in.resetReaderIndex();
                return;
            }

            DataBuffer dataBuf = new DataBuffer(in);

            IMHeader header = new IMHeader();
            header.decode(dataBuf);

            if (header.getLength() < 0) {
                ctx.close();
                logger.error("message length less than 0, channel closed");
                return;
            }

            ByteBuf byteBuf = ctx.alloc().buffer(header.getLength() - SysConstant.PROTOCOL_HEADER_LENGTH);

            in.readBytes(byteBuf);
            byte[] body;
            if (byteBuf.hasArray()) {
                body = byteBuf.array();
            } else {
                body = new byte[byteBuf.capacity()];
                byteBuf.readBytes(body);
            }

            MessageLite content = ProtobufParseMap.getMessage(header.getServiceId(), header.getCommandId(), body);

            IMProtoMessage<MessageLite> protoMessage = new IMProtoMessage<>(header, content);
            out.add(protoMessage);
            
            logger.debug("Received protobuf : length={}, commandId={}", header.getLength(), header.getCommandId());
        } catch (Exception e) {
            logger.error(ctx.channel().remoteAddress() + ",decode failed.", e);
        } finally {
            logger.debug("Protobuf decode finished.");
        }
    
    }
    
}
