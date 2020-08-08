package cn.whitetown.monitor.sys.server;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import com.alibaba.fastjson.JSON;
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


    public SysAnalyzerHandler(ExecutorService executorService) {
        this.executorService = executorService;
    }
    /**
     * 数据解析处理器
     * @param ctx
     * @param data
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String data) throws Exception {
        WhiteMonitorParams monitorParams = JSON.parseObject(data, WhiteMonitorParams.class);
        executorService.execute(new SysMonSaveHandler(monitorParams));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
