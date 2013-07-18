package gateway.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Staring���Ľ���
 */
public class LFixedLengthMsgDecoder extends FrameDecoder {
    public static final int LENGTH = 6;
    private static Logger logger = LoggerFactory.getLogger(LFixedLengthMsgDecoder.class);


    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() < LENGTH) {
            return null;
        }
        byte[] lengthBytes = new byte[LENGTH];
        buffer.getBytes(buffer.readerIndex(), lengthBytes);
        int dataLength = Integer.parseInt(new String(lengthBytes).trim());
        if (dataLength == 0) {
            throw new RuntimeException("�����ĳ��ȡ��ֶβ���Ϊ0");
        } else {
            logger.info("[�����ܳ���] " + dataLength);
        }
        if (buffer.readableBytes() < dataLength) {
            logger.info("[�ɽ��ձ��ĳ��Ȳ��㣬�ظ�������...]readableBytes():" + buffer.readableBytes());
            byte[] msgBytes = new byte[buffer.readableBytes()];
            buffer.readBytes(msgBytes);
            logger.info(new String(msgBytes));
            return null;
        }

        buffer.skipBytes(LENGTH);

        byte[] msgBytes = new byte[dataLength - LENGTH];
        buffer.readBytes(msgBytes);
        return msgBytes;
    }
}
