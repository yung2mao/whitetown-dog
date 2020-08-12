package cn.whitetown.dogbase.wache.buffer.wiml;

import cn.whitetown.dogbase.wache.buffer.DefaultBufferElement;

import java.util.Arrays;

/**
 * byte类型缓冲池元素
 * @author taixian
 * @date 2020/08/12
 **/
public class ByteBufferElement extends DefaultBufferElement<byte[]> {

    @Override
    public void close() {
        Arrays.fill(e, (byte) 0);
        super.close();
    }
}
