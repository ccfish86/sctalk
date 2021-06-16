/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.code;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.constant.SysConstant;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;

/**
 * 供WebSocketFrame使用的
 * <br>
 * 返回消息处理(IMProtoMessage to WebSocketFrame) 
 * 
 * @author 袁贵
 * @version 1.3
 * @since  1.3
 */
public class PacketWsFrameEncoder extends MessageToMessageEncoder<IMProtoMessage<MessageLite>> implements WebSocketFrameEncoder {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /* (non-Javadoc)
     * @see io.netty.handler.codec.MessageToMessageEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, IMProtoMessage<MessageLite> protoMessage,
            List<Object> out) throws Exception {

        try {
            logger.debug("Protobuf encode started.");
            
            // [HEADER] data
            IMHeader header = protoMessage.getHeader();
            
            byte[] bytes = protoMessage.getBody().toByteArray();
            int length = bytes.length;

            // Set the length of bytebuf
            header.setLength(SysConstant.PROTOCOL_HEADER_LENGTH + length);
            
            ByteBuf byteBuf = ctx.alloc().buffer(header.getLength());
            byte[] headerBytes = header.encode();
            
            byteBuf.writeBytes(headerBytes);
            byteBuf.writeBytes(bytes);
            
            // allbytes
            BinaryWebSocketFrame wsFrame = new BinaryWebSocketFrame(byteBuf);
            
            out.add(wsFrame);
            logger.debug("Sent protobuf: commandId={}", header.getCommandId());
        } catch (Exception e) {
            logger.error("编码异常", e);
        } finally {
            logger.debug("Protobuf encode finished.");
        }
    
    }

}
