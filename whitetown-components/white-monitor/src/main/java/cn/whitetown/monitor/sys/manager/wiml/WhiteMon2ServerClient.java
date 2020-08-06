package cn.whitetown.monitor.sys.manager.wiml;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 数据对外传输
 * @author taixian
 * @date 2020/08/04
 **/
public class WhiteMon2ServerClient implements MonitorInfoSaveManager {

    private Logger logger = MonConfConstants.logger;

    private static MonitorInfoSaveManager dataToServerClient = new WhiteMon2ServerClient();

    private Channel channel;
    private EventLoopGroup clientGroup;

    private WhiteMon2ServerClient(){
        int threadSize = 1;
        clientGroup = new NioEventLoopGroup(threadSize);
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("decoder",new StringDecoder());
                            socketChannel.pipeline().addLast("encoder",new StringEncoder());
                        }
                    });
            channel = bootstrap.connect(MonConfConstants.SERVER_HOST, MonConfConstants.SERVER_PORT).sync().channel();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static MonitorInfoSaveManager getInstance(){
        return dataToServerClient;
    }

    @Override
    public void init() throws IOException {

    }

    @Override
    public boolean save(WhiteMonitorParams monitorParams) {
        try {
            channel.writeAndFlush(monitorParams.toString() + MonConfConstants.LINE_SEPARATOR);
            return true;
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void destroy() {
        try {
            channel.closeFuture().sync();
        }catch (Exception e){
        }finally {
            clientGroup.shutdownGracefully();
        }
    }
}
