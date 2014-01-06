package org.icechamps.common.protocol;

import org.icechamps.common.Transformable;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Represents a packet that is sent back and forth over the network.
 *
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public class RequestPacket implements Transformable, Serializable {
    /**
     * Represents a person packet
     */
    public static final int TYPE_PERSON = 1;

    /**
     * Represents a pet packet
     */
    public static final int TYPE_PET = 2;

    /**
     * The packet's id. This is supposed to be auto-incrementing per connection
     */
    private int id;

    /**
     * The type of the packet. This is one of the above types
     *
     * @see RequestPacket.TYPE_PERSON
     * @see RequestPacket.TYPE_PET
     */
    private int type;

    /**
     * The payload of the packet. This is supposed to be a json string
     */
    private String payload;

    /**
     * Default constructor
     */
    public RequestPacket() {
    }

    /**
     * Helper constructor that takes an incoming byte buffer and deserializes it into a packet
     *
     * @param buffer The incoming byte buffer
     */
    public RequestPacket(ByteBuffer buffer) {
        fromBuffer(buffer);
    }

    @Override
    public ByteBuffer toBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(12 + payload.length());
        buffer.putInt(id);
        buffer.putInt(type);
        buffer.putInt(payload.length());
        buffer.put(payload.getBytes(Charset.forName("utf-8")));

        return buffer;
    }

    @Override
    public void fromBuffer(ByteBuffer buffer) {
        id = buffer.getInt();
        type = buffer.getInt();

        int payloadLength = buffer.getInt();
        byte[] payloadBytes = new byte[payloadLength];
        buffer.get(payloadBytes);

        payload = new String(payloadBytes, Charset.forName("utf-8"));
    }

    @Override
    public String toString() {
        return "RequestPacket{" +
                "id=" + id +
                ", type=" + type +
                ", payload='" + payload + '\'' +
                "} " + super.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
