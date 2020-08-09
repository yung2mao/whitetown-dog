package cn.whitetown.monitor.sys.server;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.server.wiml.SysAnalyzerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.Logger;

/**
 * 数据接收与处理服务端
 * @author taixian
 * @date 2020/08/05
 **/
public class DefaultSysMonServer implements SysMonServer{

    private Logger logger = LogConstants.sysLogger;

    private int port = MonConfConstants.SERVER_PORT;

    private SysAnalyzerHandler analyzerHandler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public DefaultSysMonServer(SysAnalyzerHandler analyzerHandler){
        this.analyzerHandler = analyzerHandler;
    }

    @Override
    public void init() {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup(MonConfConstants.WORK_THREAD_SIZE);
    }

    @Override
    public void run(){
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("lineDecoder",new LineBasedFrameDecoder(4096));
                            socketChannel.pipeline().addLast("decoder",new StringDecoder());
                            socketChannel.pipeline().addLast("encoder",new StringEncoder());
                            socketChannel.pipeline().addLast(analyzerHandler);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("the data collect server is started");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void destroy() {
        try{
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }catch (Throwable e) {
            logger.error(e.getMessage());
        }
        logger.info("the monitor server is destroy");
    }
}
