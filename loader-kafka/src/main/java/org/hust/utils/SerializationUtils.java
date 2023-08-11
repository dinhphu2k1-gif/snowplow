package org.hust.utils;

import java.io.*;

public class SerializationUtils {
    public static byte[] serialize(Object object) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {

            objectOutputStream.writeObject(object);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object deserialize(byte[] byteArray) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
