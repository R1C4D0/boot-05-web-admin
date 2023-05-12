package com.atguigu.admin.sort;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class SortingDemo {
    public static void main(String[] args) throws IOException {
        String filePath = "E:\\IdeaProjects\\databaseLab\\src\\main\\java\\com\\atguigu\\admin\\sort\\data.bin";
        int blockSize = 1000;
        int numBlocks = 10;

        // Step 1: 将数据按照 A 属性划分成若干个块，每个块可以容纳 blockSize 条记录
        List<String> blockPaths = new ArrayList<>();
        try (DataInputStream in = new DataInputStream(new FileInputStream(filePath))) {
            for (int i = 0; i < numBlocks; i++) {
                List<Record> block = new ArrayList<>();
                for (int j = 0; j < blockSize && in.available() > 0; j++) {
                    int A = in.readInt();
                    String B = in.readUTF();
                    block.add(new Record(A, B));
                }
                Collections.sort(block);
                String blockPath = "block" + i;
                saveBlock(block, blockPath);
                blockPaths.add(blockPath);
            }
        }

        // Step 2: 利用 K 路归并算法将所有块合并成一个有序序列
        String sortedPath = "sorted.bin";
        mergeBlocks(blockPaths, sortedPath);

        // Step 3: 读取有序序列并输出结果（可选）
        try (DataInputStream in = new DataInputStream(new FileInputStream(sortedPath))) {
            while (in.available() > 0) {
                int A = in.readInt();
                String B = in.readUTF();
                log.info("(" + A + ", " + B + ")");
            }
        }
    }

    public static void saveBlock(List<Record> block, String path) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(path))) {
            for (Record record : block) {
                out.writeInt(record.getA());
                out.writeUTF(record.getB());
            }
        }
    }

    public static void mergeBlocks(List<String> blockPaths, String outputPath) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputPath))) {
            PriorityQueue<RecordPair> pq = new PriorityQueue<>();
            List<DataInputStream> insList = new ArrayList<>(); // 所有块的输入流列表
            for (String blockPath : blockPaths) {
                DataInputStream in = new DataInputStream(new FileInputStream(blockPath));
                insList.add(in);
                if (in.available() > 0) {
                    int A = in.readInt();
                    String B = in.readUTF();
                    pq.offer(new RecordPair(A, B, in));
                }
            }

            while (!pq.isEmpty()) { // 进行 K 路归并，不断输出队首元素直到所有元素都被输出
                RecordPair curPair = pq.poll();
                out.writeInt(curPair.A);
                out.writeUTF(curPair.B);
                if (curPair.in.available() > 0) {
                    int A = curPair.in.readInt();
                    String B = curPair.in.readUTF();
                    pq.offer(new RecordPair(A, B, curPair.in));
                }
            }

            for (DataInputStream in : insList) {
                in.close();
            }
        }
    }

    static class Record implements Comparable<Record> {
        private int A;
        private String B;

        public Record(int A, String B) {
            this.A = A;
            this.B = B;
        }

        public int getA() {
            return A;
        }

        @Override
        public int compareTo(Record o) {
            return Integer.compare(this.A, o.A);
        }

        public String getB() {
            return B;
        }
    }

    static class RecordPair implements Comparable<RecordPair> {
        private int A;
        private String B;
        private DataInputStream in;

        public RecordPair(int A, String B, DataInputStream in) {
            this.A = A;
            this.B = B;
            this.in = in;
        }

        @Override
        public int compareTo(RecordPair o) {
            return Integer.compare(this.A, o.A);
        }
    }
}

