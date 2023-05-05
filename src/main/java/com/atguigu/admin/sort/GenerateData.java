package com.atguigu.admin.sort;

import java.io.*;
import java.util.Random;

public class GenerateData {
    public static void main(String[] args) throws IOException {
        String filePath = "E:\\IdeaProjects\\databaseLab\\src\\main\\java\\com\\atguigu\\admin\\sort\\data.bin";

        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("文件已经存在！");
        }
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            // 在这里写入数据到 data.bin 文件中
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        generateData(filePath, 10000);
    }
    public static void generateData(String filePath, int n) throws IOException {
        Random random = new Random();
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath))) {
            for (int i = 0; i < n; i++) {
                int A = random.nextInt(1000);
                String B = "record_" + i;
                Record record = new Record(A, B);
                writeRecord(out, record);
            }
        }
    }

    public static void writeRecord(DataOutputStream out, Record record) throws IOException {
        out.writeInt(record.getA()); // 写入整数属性 A
        out.writeUTF(record.getB()); // 写入字符串属性 B
    }

}

