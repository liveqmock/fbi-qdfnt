package gateway.client;

import gateway.codec.LFixedLengthMsgDecoder;
import gateway.codec.LFixedLengthMsgEncoder;
import gateway.domain.LFixedLengthProtocol;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pub.platform.advance.utils.PropertyManager;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 同步Socket客户端
 */
@Component
@Scope("prototype")
public class SyncSocketClient {
    private static final String SERVER_IP = PropertyManager.getProperty("linking.server.ip");
    private static final int SERVER_PORT = PropertyManager.getIntProperty("linking.server.port");
    private static final long SERVER_TIMEOUT = PropertyManager.getIntProperty("linking.server.timeout");

    public LFixedLengthProtocol onRequest(LFixedLengthProtocol tia) {
        ClientBootstrap bootstrap = new ClientBootstrap(
                new OioClientSocketChannelFactory(Executors.newSingleThreadExecutor()));
        final ClientDataHandler clientDataHandler = new ClientDataHandler();
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new LFixedLengthMsgDecoder());
                pipeline.addLast("encoder", new LFixedLengthMsgEncoder());
                pipeline.addLast("handler", clientDataHandler);
                return pipeline;
            }
        });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
        future.getChannel().write(tia);
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        return clientDataHandler.getToa();
    }
}
