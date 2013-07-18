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
 * Time: ����8:24
 * To change this template use File | Settings | File Templates.
 */
public class ClientDataHandler extends SimpleChannelUpstreamHandler {
    private static Logger logger = LoggerFactory.getLogger(ClientDataHandler.class);
    private LFixedLengthProtocol toa;

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        byte[] dataBytes = (byte[]) e.getMessage();
        logger.info("[�ͻ��˽���]" + new String(dataBytes));
        toa = new LFixedLengthProtocol();
        toa.assembleFields(dataBytes);
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("�ͻ��������쳣��", e.getCause());
        e.getChannel().close();
    }

    public LFixedLengthProtocol getToa() {
        return toa;
    }

    public void setToa(LFixedLengthProtocol toa) {
        this.toa = toa;
    }
}
