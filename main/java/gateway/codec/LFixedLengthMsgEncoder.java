package gateway.codec;

import gateway.domain.LFixedLengthProtocol;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-5-31
 * Time: ÏÂÎç5:48
 * To change this template use File | Settings | File Templates.
 */
public class LFixedLengthMsgEncoder extends OneToOneEncoder {
    private static Logger logger = LoggerFactory.getLogger(LFixedLengthMsgEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext channelHandlerContext, Channel channel, Object o) throws Exception {
        LFixedLengthProtocol msg = (LFixedLengthProtocol)o;
        byte[] rtnBytes = msg.toByteArray();
        logger.info(new String(rtnBytes));
        return ChannelBuffers.wrappedBuffer(rtnBytes);
    }
}
