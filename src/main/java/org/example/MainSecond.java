package org.example;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainSecond {
    public static void main(String[] args) {
        readWriteFile(6);
//        Map<String, ChannelArray> map = new HashMap<>();
//        String pathFile = "C:\\projects\\task_cft\\src\\main\\resources\\out.txt";
//        int sizeBuffer = 5;
//        Map<String, ChannelArray> mapRide = readFile(map, sizeBuffer);
//        writeOutFile(mapRide, pathFile, sizeBuffer);
    }

    private static Map<String, ChannelArray> readFile(Map<String, ChannelArray> map, int sizeBuffer) {

        String out = "C:\\projects\\task_cft\\src\\main\\resources\\out.txt";
        String in1 = "C:\\projects\\task_cft\\src\\main\\resources\\in1.txt";
        String in2 = "C:\\projects\\task_cft\\src\\main\\resources\\in2.txt";
        String in3 = "C:\\projects\\task_cft\\src\\main\\resources\\in3.txt";

        ChannelArray channelArray1 = new ChannelArray(in1, sizeBuffer);
        channelArray1.readPartData();
        channelArray1.incrementCurrentPositionFile();

        ChannelArray channelArray2 = new ChannelArray(in2, sizeBuffer);
        channelArray2.readPartData();
        channelArray2.incrementCurrentPositionFile();

        ChannelArray channelArray3 = new ChannelArray(in3, sizeBuffer);
        channelArray3.readPartData();
        channelArray3.incrementCurrentPositionFile();

        map.put(in1, channelArray1);
        map.put(in2, channelArray2);
        map.put(in3, channelArray3);
        return map;
    }

    private static void readWriteFile(int sizeBuffer) {
        String in = "C:\\projects\\task_cft\\src\\main\\resources\\in1.txt";
        String out = "C:\\projects\\task_cft\\src\\main\\resources\\out.txt";

        ChannelArray channelArray = new ChannelArray(in, sizeBuffer);
        try (FileOutputStream fOut = new FileOutputStream(out);
             FileChannel channel = fOut.getChannel()) {
            while (channelArray.getCurrentPositionFile() <= channelArray.getSizeFile()) {
                channelArray.readPartData();
                channelArray.incrementCurrentPositionFile();
                channelArray.getBuffer().flip();

                ByteBuffer byteBuffer = (ByteBuffer)channelArray.getBuffer();
                int[] intArr = channelArray.getIntBuffer().array();
                var rsl = delBreak(intArr);
                var rsl1 = addBreak(rsl);
                var byteArr = createByteArr(rsl1);
                byteBuffer.put(byteArr);
                byteBuffer.flip();
                channel.write(byteBuffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //удалить символы переноса строки в массиве
    private static int[] delBreak(int[] intArr) {
        var rsl = Arrays.stream(intArr).filter(i -> i != 10).filter(i -> i != 13).filter(i -> i != 0);
        return rsl.toArray();
    }

    //добавить символы переноса
    private static int[] addBreak(int[] intArr) {
        IntStream.Builder builder = IntStream.builder();
        Arrays.stream(intArr).forEach(value -> builder.add(value).add(13).add(10));
        var rsl = builder.build().toArray();
        return rsl;
    }

    //создать массив байтов
    private static byte[] createByteArr(int[] intArr) {
        byte[] arrByte = new byte[intArr.length];
        for (int index = 0; index < intArr.length; index++) {
            arrByte[index] = (byte) intArr[index];
        }
        return arrByte;
    }

    private static void writeOutFile(Map<String, ChannelArray> mapRide, String pathFile, int sizeBuffer) {
//        String someText = "Hello, Amigo!!!!!";
        int sizeArr = mapRide.size() * sizeBuffer;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (FileOutputStream fOut = new FileOutputStream(pathFile);
             FileChannel channel = fOut.getChannel()) {
//            ByteBuffer buffer = ByteBuffer.allocate(someText.getBytes().length);
//            buffer.put(someText.getBytes(StandardCharsets.UTF_8));
//            buffer.flip();
//            channel.write(buffer);

            //объединение буферов в один массив
            for (ChannelArray channelArray : mapRide.values()) {
                ByteBuffer buffer = (ByteBuffer) channelArray.getBuffer();
                byte[] temp = buffer.array();
                outputStream.write(temp);
            }
            byte[] rsl = outputStream.toByteArray();

            //преобразование массива байтов в массив чисел
            IntStream.Builder intStreamBilder = IntStream.builder();
            Stream.of(rsl).forEach(bytes -> {
                for (byte b : bytes) {
                    intStreamBilder.add((int) b);
                }
            });
            IntStream rsl5 = intStreamBilder.build();
            var rsl6 = rsl5.filter(i -> i != 10).filter(i -> i != 13);
            var arrInt = rsl6.toArray();

            //сортировка массива
            int[] rsl1 = mergeSort(arrInt);

            //добавить символ переноса строки
            IntStream.Builder intStreamBilder1 = IntStream.builder();
            Arrays.stream(rsl1).forEach(value -> {
                intStreamBilder1.add(value);
                intStreamBilder1.add(13);
                intStreamBilder1.add(10);

            });
            var rsl8 = intStreamBilder1.build().toArray();

            //преобразование массива чисел в массив байт
            byte[] arrByteFinal = new byte[rsl8.length];
            for (int index = 0; index < rsl8.length; index++) {
                arrByteFinal[index] = (byte) rsl8[index];
            }


            //запись в файл
            ByteBuffer byteBuffer = ByteBuffer.allocate(arrByteFinal.length);
            byteBuffer.put(arrByteFinal);
            byteBuffer.flip();
            channel.write(byteBuffer);

//            for (ChannelArray channelArray : mapRide.values()) {
//                ByteBuffer buffer = (ByteBuffer) channelArray.getBuffer();
//                buffer.flip();
//                channel.write(buffer);
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] mergeSort(int[] array) {
        int[] tmp;
        int[] currentSrc = array;
        int[] currentDest = new int[array.length];

        int size = 1;
        while (size < array.length) {
            for (int i = 0; i < array.length; i += 2 * size) {
                merge(currentSrc, i, currentSrc, i + size, currentDest, i, size);
            }

            tmp = currentSrc;
            currentSrc = currentDest;
            currentDest = tmp;

            size = size * 2;

            System.out.println(arrayToString(currentSrc));
        }
        return currentSrc;
    }

    private static void merge(int[] src1, int src1Start, int[] src2, int src2Start, int[] dest,int destStart, int size) {
        int index1 = src1Start;
        int index2 = src2Start;

        int src1End = Math.min(src1Start + size, src1.length);
        int src2End = Math.min(src2Start + size, src2.length);

        if (src1Start + size > src1.length) {
            for (int i = src1Start; i < src1End; i++) {
                dest[i] = src1[i];
            }
            return;
        }

        int iterationCount = src1End - src1Start + src2End - src2Start;

        for (int i = destStart; i < destStart + iterationCount; i++) {
            if (index1 < src1End && (index2 >= src2End || src1[index1] < src2[index2])) {
                dest[i] = src1[index1];
                index1++;
            } else {
                dest[i] = src2[index2];
                index2++;
            }
        }
    }

    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(array[i]);
        }
        sb.append("]");
        return sb.toString();
    }


}
