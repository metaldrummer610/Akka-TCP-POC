package org.icechamps.common.protocol;

import org.icechamps.common.Transformable;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * TODO: Describe this class!
 *
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public class ResponsePacket implements Transformable {
    private int id;
    private int type;
    private boolean success;
    private String failureReason;

    public ResponsePacket() {
    }

    public ResponsePacket(ByteBuffer buffer) {
        fromBuffer(buffer);
    }

    @Override
    public ByteBuffer toBuffer() {
        int failureSize = 0;
        if (!success) {
            failureSize = 4 + failureReason.length();
        }

        ByteBuffer buffer = ByteBuffer.allocate(12 + failureSize);

        buffer.putInt(id);
        buffer.putInt(type);
        buffer.putInt(success ? 1 : 0);

        if (!success) {
            buffer.putInt(failureSize);
            buffer.put(failureReason.getBytes(Charset.forName("utf-8")));
        }

        return (ByteBuffer) buffer.flip();
    }

    @Override
    public void fromBuffer(ByteBuffer buffer) {
        id = buffer.getInt();
        type = buffer.getInt();
        success = buffer.getInt() == 1;

        if (!success) {
            int failureLength = buffer.getInt();
            byte[] failure = new byte[failureLength];
            buffer.get(failure);

            failureReason = new String(failure, Charset.forName("utf-8"));
        }
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
