package com.atguigu.admin.sort;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.FieldPosition;

public class MultiwayMergeSort {
    private static final int BUFFER_SIZE = 1024 * 1024; // 缓冲区大小 1MB

    public static void main(String[] args) throws IOException {
        File inputFile = new File("E:\\IdeaProjects\\databaseLab\\src\\main\\java\\com\\atguigu\\admin\\sort\\data.bin");
        File outputFile = new File("E:\\IdeaProjects\\databaseLab\\src\\main\\java\\com\\atguigu\\admin\\sort\\result.bin");
        sort(inputFile, outputFile,5);
    }
    public static void sort(File inputFile, File outputFile, int k) throws IOException {
        if (k <= 1) {
            throw new IllegalArgumentException("k must be greater than 1");
        }
        FileOutputStream outputFileStream = new FileOutputStream(outputFile, true);

        long startTime = System.currentTimeMillis();
        // 将输入文件均匀地划分成 k 个子文件
        File[] inputFiles = splitInputFile(inputFile, k);
        // 对每个子文件进行快速排序，并将排好序的子文件放入缓冲区
        FileInputStream[] inputStreams = new FileInputStream[k];
        byte[][] buffers = new byte[k][BUFFER_SIZE];
        int[] bufferSizes = new int[k];
        for (int i = 0; i < k; i++) {
            inputStreams[i] = new FileInputStream(inputFiles[i]);
            bufferSizes[i] = inputStreams[i].read(buffers[i]);
        }
        while (!allBuffersEmpty(bufferSizes)) {
            int minIndex = findMinIndex(buffers, bufferSizes);
            outputFileStream.write(buffers[minIndex], 0, bufferSizes[minIndex]);

            bufferSizes[minIndex] = inputStreams[minIndex].read(buffers[minIndex]);
        }
        // 关闭流和删除文件
        for (int i = 0; i < k; i++) {
            inputStreams[i].close();
            if (!inputFiles[i].delete()) {
                System.out.println("Warning: Failed to delete " + inputFiles[i]);
            }
        }
        outputFileStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Multiway merge sort takes " + (endTime - startTime) + " ms.");
    }

    private static File[] splitInputFile(File inputFile, int k) throws IOException {
        long fileSize = inputFile.length();
        long blockSize = fileSize / k;
        File[] inputFiles = new File[k];
        FileInputStream in = new FileInputStream(inputFile);
        for (int i = 0; i < k; i++) {
            inputFiles[i] = File.createTempFile("temp_", ".data", new File("E:\\IdeaProjects\\databaseLab\\src\\main\\java\\com\\atguigu\\admin\\sort\\"));
            FileOutputStream out = new FileOutputStream(inputFiles[i]);
            byte[] buffer = new byte[BUFFER_SIZE];
            long bytesWritten = 0;
            int bytesRead;
            while (bytesWritten < blockSize && (bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
                bytesWritten += bytesRead;
            }
            out.close();
        }
        in.close();
        return inputFiles;
    }

    private static boolean allBuffersEmpty(int[] bufferSizes) {
        for (int size : bufferSizes) {
            if (size > 0) {
                return false;
            }
        }
        return true;
    }

    private static int findMinIndex(byte[][] buffers, int[] bufferSizes) {
        int minIndex = -1;
        for (int i = 0; i < buffers.length; i++) {
            if (bufferSizes[i] > 0) {
                if (minIndex == -1 || compare(buffers[i], buffers[minIndex]) < 0) {
                    minIndex = i;
                }
            }
        }
        return minIndex;
    }

    private static int compare(byte[] a, byte[] b) {
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) {
                return a[i] - b[i];
            }
        }
        return a.length - b.length;
    }
}
