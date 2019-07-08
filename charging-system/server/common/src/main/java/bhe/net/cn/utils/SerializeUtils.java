package bhe.net.cn.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SerializeUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(SerializeUtils.class);

    public static byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArray);
        outputStream.writeObject(obj);
        outputStream.flush();
        return byteArray.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objInput = new ObjectInputStream(inputStream);
        return objInput.readObject();
    }
}
