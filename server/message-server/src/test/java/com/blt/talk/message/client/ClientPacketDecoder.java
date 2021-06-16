/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.analysis.ProtobufParseMap;
import com.blt.talk.common.constant.SysConstant;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClientPacketDecoder  extends ByteToMessageDecoder {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {

        try {

            logger.trace("Protobuf decode started.");
            in.markReaderIndex();
            if (in.readableBytes() < 4) {
                logger.debug("Readable Bytes length less than 4 bytes, ignored");
                in.resetReaderIndex();
                return;
            }

            ByteBuf dataBuf = ctx.alloc().buffer(SysConstant.PROTOCOL_HEADER_LENGTH);
            in.readBytes(dataBuf);
            
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

            ClientMessage<ByteString> protoMessage = new ClientMessage<>(header, ByteString.copyFrom(body));
            out.add(protoMessage);
            
            logger.trace("Received protobuf : length={}, commandId={}", header.getLength(), header.getCommandId());
        } catch (Exception e) {
            logger.error(ctx.channel().remoteAddress() + ",decode failed.", e);
        } finally {
            logger.trace("Protobuf decode finished.");
        }
    }

}
