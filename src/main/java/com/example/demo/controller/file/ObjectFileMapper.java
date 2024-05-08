package com.example.demo.controller.file;

import com.example.demo.controller.dto.UserResourcesDTO;
import com.example.demo.controller.exception.FileReadWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectFileMapper {
    private final static File file = new File("persistInfo.txt");
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectFileMapper.class);

    static {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ie) {
                LOGGER.error("IOException: ", ie);
            }
        }
    }

    public static List<UserResourcesDTO> readFile() throws FileReadWriteException{
        List<UserResourcesDTO> res = new ArrayList<>();

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            res = (List<UserResourcesDTO>) in.readObject();
        } catch (EOFException e) {
            LOGGER.warn("EOF Exception: ", e.getMessage());
        } catch (IOException i) {
            LOGGER.error("IO Exception: ", i);
            throw new FileReadWriteException("IO Exception");
        } catch (ClassNotFoundException c) {
            LOGGER.error("ClassNotFoundException: ", c);
            throw new FileReadWriteException("Person class not found");
        }

        return res;
    }

    public static void writeFile(List<UserResourcesDTO> objs) throws FileReadWriteException, IOException{
        List<UserResourcesDTO> existedObjs = readFile();
        objs.addAll(existedObjs);

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            // 将对象写入文件
            out.writeObject(objs);
            out.flush();
        } catch (IOException i) {
            LOGGER.error("IO Exception: ", i);
            throw new IOException(i.getCause());
        }
    }
}
