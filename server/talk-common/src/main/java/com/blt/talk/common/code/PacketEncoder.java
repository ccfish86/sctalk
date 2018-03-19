package com.blt.talk.common.code;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.constant.SysConstant;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 返回消息处理(IMProtoMessage to protobuf) 
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public final class PacketEncoder extends MessageToByteEncoder<IMProtoMessage<MessageLite>> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final IMProtoMessage<MessageLite> protoMessage, final ByteBuf out) throws Exception {

        try {
            logger.trace("Protobuf encode started.");
            
            // [HEADER] data
            IMHeader header = protoMessage.getHeader();
            
            byte[] bytes = protoMessage.getBody().toByteArray();
            int length = bytes.length;

            // Set the length of bytebuf
            header.setLength(SysConstant.PROTOCOL_HEADER_LENGTH + length);
            
            byte[] allbytes = header.encode().array();
            allbytes = Arrays.copyOf(allbytes, SysConstant.PROTOCOL_HEADER_LENGTH + length);
            
            for (int i = 0; i < length; i++) {
                allbytes[i + 16] = bytes[i];
            }
            
            out.writeBytes(allbytes);
            logger.trace("Sent protobuf: commandId={}", header.getCommandId());
        } catch (Exception e) {
            logger.error("编码异常", e);
        } finally {
            logger.trace("Protobuf encode finished.");
        }
    }
}
