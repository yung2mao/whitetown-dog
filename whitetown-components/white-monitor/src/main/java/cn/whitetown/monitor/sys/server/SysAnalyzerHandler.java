package cn.whitetown.monitor.sys.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;

/**
 * 接收数据分析处理
 * @author taixian
 * @date 2020/08/04
 **/
@ChannelHandler.Sharable
public class SysAnalyzerHandler extends SimpleChannelInboundHandler<String> {

    private ExecutorService executorService;

    private SysMonSaveHandler paramsSaveHandler;

    public SysAnalyzerHandler(ExecutorService executorService, SysMonSaveHandler paramsSaveHandler) {
        this.executorService = executorService;
        this.paramsSaveHandler = paramsSaveHandler;
    }
    /**
     * 数据解析处理器
     * @param ctx
     * @param data
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String data) throws Exception {
        System.out.println(data);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("data is read complete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
