package org.icechamps.common.protocol;

import org.junit.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * TODO: Describe this class!
 *
 * @author Robert.Diaz
 * @since 1.0, 01/07/2014
 */
public class ResponsePacketTest {
    @Test
    public void testToBuffer() throws Exception {
        ResponsePacket packet = new ResponsePacket();
        packet.setId(1);
        packet.setType(RequestPacket.TYPE_PERSON);
        packet.setSuccess(true);

        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(1);
        buffer.putInt(RequestPacket.TYPE_PERSON);
        buffer.putInt(1);

        System.out.println(Arrays.toString(packet.toBuffer().array()));
        System.out.println(Arrays.toString(buffer.array()));

        Assert.that(buffer.equals(packet.toBuffer()), "Buffers aren't equal!!!");
    }

    @Test
    public void testFromBuffer() throws Exception {

    }
}
