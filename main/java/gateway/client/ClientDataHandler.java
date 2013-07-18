package gateway.client;

import gateway.domain.LFixedLengthProtocol;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-24
 * Time: 下午8:24
 * To change this template use File | Settings | File Templates.
 */
public class ClientDataHandler extends SimpleChannelUpstreamHandler {
    private static Logger logger = LoggerFactory.getLogger(ClientDataHandler.class);
    private LFixedLengthProtocol toa;

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        byte[] dataBytes = (byte[]) e.getMessage();
        logger.info("[客户端接收]" + new String(dataBytes));
        toa = new LFixedLengthProtocol();
        toa.assembleFields(dataBytes);
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("客户端连接异常。", e.getCause());
        e.getChannel().close();
    }

    public LFixedLengthProtocol getToa() {
        return toa;
    }

    public void setToa(LFixedLengthProtocol toa) {
        this.toa = toa;
    }
}
