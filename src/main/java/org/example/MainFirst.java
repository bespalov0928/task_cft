package org.example;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainFirst {

/*
   Допустим, у вас имеется n отсортированных файлов, которые надо слепить воедино.
1. Открывайте FileStream на финальный файл
2. Открывайте n FileStream'ов — по одному на каждый из мелких отсортированных файлов.
3. Считывайте по одной строчке из каждого файла.
4. Найдите строчку, которая "меньше" всех остальных.
5. Запишите эту строчку в финальный файл.
6. Найдите FileStream, из которого вы считали эту строчку
7. Считайте следующую строчку из этого же потока.
8. Если строчек больше нет, то закройте этот поток и удалите временный файл.
9. Если все файлы были закрыты и удалены, то работа завершена, закройте поток в финальный файл.
10. В противном случае goto 4.
*/

    //функция слияния
    private static void mergeFile(int currentPositionBuffer, Map<String, ChannelArray> map) {
        for (ChannelArray ca : map.values()) {
            ByteBuffer buffer = (ByteBuffer) ca.getBuffer();
        }

    }
    //функция запись в файл

    //функция вычитывает очередную порцию данных в буфер
    private static Map<String, ChannelArray> readFile() {
        Map<String, ChannelArray> map = new HashMap<>();
        int sizeBuffer = 5;
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

    private static void fileOut(Map<String, ChannelArray> map) {
        int maxLengthBuffer = 0;
        for (ChannelArray ca : map.values()) {
            if (ca.getSizeFile() > maxLengthBuffer) maxLengthBuffer = ca.getSizeFile();
        }

        for (int index = 0; index < maxLengthBuffer; index++) {

        }

//        merge(buffer1, buffer2, int startIndex, int endIndex)
        System.out.println(maxLengthBuffer);
//        System.out.println(rsl);
//        StringBuilder text1 = new StringBuilder();
//        StringBuilder text2 = new StringBuilder();
//        StringBuilder text3 = new StringBuilder();
//        FileChannel channel1 = null;
//        ByteBuffer byteBuffer1 = ByteBuffer.allocate(5);
//
//        try (FileInputStream fIn1 = new FileInputStream(in1)) {
//            channel1 = fIn1.getChannel();
//            channel1.read(byteBuffer1);
//            ChannelArray channelArray = new ChannelArray(channel1, byteBuffer1, 0);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////            channel1.read(byteBuffer1);
//        byteBuffer1.mark();
////        System.out.println("размер канала: " + channel1.size());
////            System.out.println((char) byteBuffer1.get(1));
////            System.out.println((char) byteBuffer1.get(2));
////            System.out.println("текущая позиция: " + byteBuffer1.position());
//        byteBuffer1.flip();
//        while (byteBuffer1.hasRemaining()) {
//            System.out.println(byteBuffer1.get());
//            System.out.println("текущая позиция: " + byteBuffer1.position());
//        }

//        System.out.println((char) byteBuffer1.get(1));
//        System.out.println(fIn1.);


////        1. Открывайте FileStream на финальный файл
//        try (FileOutputStream fout = new FileOutputStream(out)) {
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////      2. Открывайте n FileStream'ов — по одному на каждый из мелких отсортированных файлов.
//        try (FileInputStream fIn1 = new FileInputStream(in1)) {
//
////      получаем экземпляр класса FileChannel
//            channel1 = fIn1.getChannel();
//
////      создаем буфер необходимого размера на основании размера нашего канала
//            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
//            System.out.println("Емкость буффера: " + byteBuffer.capacity());
//
////      прочитанные данные будем помещать в StringBuilder
//            StringBuilder builder = new StringBuilder();
//
////      записываем данные из канала в буфер
//            channel1.read(byteBuffer);
//
////      переключаем буфер с режима записи на режим чтения
//            byteBuffer.flip();
//
////      в цикле записываем данные из буфера в StringBuilder
//            while (byteBuffer.hasRemaining()) {
//                builder.append((char) byteBuffer.get());
//            }
//
////      выводим содержимое StringBuilder в консоли
//            System.out.println(builder);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    private static void merge(int[] src1, int src1Start, int[] src2, int src2Start, int[] dest,
                              int destStart, int size) {
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


    public static int[] mergeSort(int[] sortArr) {
        int[] buffer1 = Arrays.copyOf(sortArr, sortArr.length);
        int[] buffer2 = new int[sortArr.length];
        int[] result = mergeSortInner(buffer1, buffer2, 0, sortArr.length);
        return result;
    }

    public static int[] mergeSortInner(int[] buffer1, int[] buffer2, int startIndex, int endIndex) {
        if (startIndex >= endIndex - 1) {
            return buffer1;
        }

        //уже отсортирован
        int middle = startIndex + (endIndex - startIndex) / 2;
        int[] sorted1 = mergeSortInner(buffer1, buffer2, startIndex, middle);
        int[] sorted2 = mergeSortInner(buffer1, buffer2, middle, endIndex);

        //слияние
        int index1 = startIndex;
        int index2 = middle;
        int destIndex = startIndex;
        int[] result = sorted1 == buffer1 ? buffer2 : buffer1;
        while (index1 < middle && index2 < endIndex) {
            result[destIndex++] = sorted1[index1] < sorted2[index2] ? sorted1[index1++] : sorted2[index2++];
        }
        while (index1 < middle) {
            result[destIndex++] = sorted1[index1++];
        }
        while (index2 < endIndex) {
            result[destIndex++] = sorted2[index2++];
        }
        return result;
    }

    public static void main(String args[]) {
        Map<String, ChannelArray> map = readFile();
        fileOut(map);

//        String in1 = "C:\\projects\\task_cft\\src\\main\\resources\\in1.txt";
//        String in2 = "C:\\projects\\task_cft\\src\\main\\resources\\in2.txt";
//        String in3 = "C:\\projects\\task_cft\\src\\main\\resources\\in3.txt";
//        int len = 3;
//        int arrSize = 15;
//        try (FileReader fr = new FileReader(in1)) {
//            char[] cbuf = new char[arrSize];
//            fr.read(cbuf, 0, len);
//            for (char ch : cbuf) {
//                System.out.print(ch);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println();
//        System.out.println();
//
//        try (FileInputStream f = new FileInputStream(in1)) {
//            int size = 0;
//            size = f.available();
////            System.out.println(size);
//            byte[] buffer = new byte[arrSize];
//            var x1 = f.read(buffer, 0, len);
//            for (Byte b : buffer) {
//                var rsl3 = b.intValue();
////                System.out.print((char) rsl3 + " : " + rsl3);
//                System.out.println(rsl3);
////                System.out.print((char) rsl3);
//            }
//            System.out.println();
////
//////            for (int i = 0; i < size; i++) {
//////                var x = f.read();
//////                System.out.print((char) x);
////////                System.out.print(System.lineSeparator());
//////            }
////
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        var rsl = System.getProperties();
//        System.out.println(java.class.path);

//        int[] sortArr = {12, 6, 4, 1, 15, 10};
//        int[] result = mergeSort(sortArr);
//        System.out.println(Arrays.toString(result));
    }

//    public static void main(String[] args) {
//        System.out.println("hello world");
//
//        //        String in1 = args[2];
//        String in1 = "C:\\projects\\task_cft\\src\\main\\resources\\in1.txt";
//        String in2 = "C:\\projects\\task_cft\\src\\main\\resources\\in2.txt";
//        String in3 = "C:\\projects\\task_cft\\src\\main\\resources\\in3.txt";
//        try (FileReader fr = new FileReader(in1)) {
//            int c;
//            while ((c = fr.read()) != -1) {
//                System.out.print((char) c);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}




