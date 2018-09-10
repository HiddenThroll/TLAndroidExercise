package com.tanlong.exercise.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileBusiness {

    /**
     * 写内容到文件
     * 1.创建文件夹
     * 2.该文件是否存在, 已存在.删除重建;不存在,创建
     * 3.写文件
     * @param content
     */
    public boolean writeFile(String dirPath, String fileName, String content) {
        boolean writeSuccess;
        File dirFile = new File(dirPath);
        dirFile.mkdirs();
        File writeFile = new File(fileName);
        if (writeFile.exists()) {
            writeFile.delete();
        }
        FileOutputStream fileOutputStream = null;

        try {
            writeFile.createNewFile();
            fileOutputStream = new FileOutputStream(writeFile);
            fileOutputStream.write(content.getBytes("utf-8"));
            writeSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            writeSuccess = false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return writeSuccess;
    }
}
