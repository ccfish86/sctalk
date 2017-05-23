package com.blt.talk.common.code;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.analysis.ProtobufParseMap;
import com.blt.talk.common.constant.SysConstant;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 接收消息处理(Protobuf to IMProtoMessage)
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public final class PacketDecoder extends ByteToMessageDecoder {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {

        try {

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

            MessageLite msg = ProtobufParseMap.getMessage(header.getServiceId(), header.getCommandId(), body);

            IMProtoMessage<MessageLite> protoMessage = new IMProtoMessage<>(header, msg);
            out.add(protoMessage);
            
            logger.debug("Received protobuf ：length={}, commandId={}", header.getLength(), header.getCommandId());
        } catch (Exception e) {
            logger.error(ctx.channel().remoteAddress() + ",decode failed.", e);
        } finally {
            logger.debug("Protobuf decode finished.");
        }
    }

}
