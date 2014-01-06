package org.icechamps.common;

import java.nio.ByteBuffer;

/**
 * Denotes a class as being able to be turned into a {@link ByteBuffer} and back
 *
 * @param <T> The type of the class
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public interface Transformable<T> {
    /**
     * Turns the object into a ByteBuffer
     *
     * @return The buffer representation of this object
     */
    public ByteBuffer toBuffer();

    /**
     * Creates an object from the given ByteBuffer
     *
     * @param buffer The buffer used to create the new object
     * @return The new object
     */
    public T fromBuffer(ByteBuffer buffer);
}
