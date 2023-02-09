package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class MainFourth {
    public static void main(String[] args) {
        String in = "C:\\projects\\task_cft\\src\\main\\resources\\in1.txt";
        String in2 = "C:\\projects\\task_cft\\src\\main\\resources\\in2.txt";
        String out = "C:\\projects\\task_cft\\src\\main\\resources\\out.txt";
//        channelReader(in);
//        channelWrite(out);
        fileReaderWriter(in, in2, out);
    }

    private static void channelReader(String in) {
        int count;
        try (SeekableByteChannel fChan = Files.newByteChannel(Path.of(in))) {
            ByteBuffer mBuf = ByteBuffer.allocate(2);
            do {
                count = fChan.read(mBuf);
                if (count != -1) {
                    mBuf.rewind();
//                    System.out.println(mBuf);
                    for (int i = 0; i < count; i++) {
                        System.out.print((char) mBuf.get());
                    }
                }
            } while (count != -1);
        } catch (InvalidPathException e) {
            System.out.println("Ошибка в пути: " + e);
        } catch (IOException e) {
            System.out.println("Ошибка: " + e);
        }
    }

    private static void channelWrite(String out) {
        try (FileChannel fChan = (FileChannel)
                Files.newByteChannel(Path.of(out),
                        StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            ByteBuffer mBuf = ByteBuffer.allocate(8);
//            mBuf.put(arrByte);
            for (int i = 0; i < 8; i++) {
                mBuf.put((byte) ('A' + i));
            }
            mBuf.rewind();
            fChan.write(mBuf);
            System.out.println(mBuf);
        } catch (InvalidPathException e) {
            System.out.println("Ошибка в пути: " + e);
        } catch (IOException e) {
            System.out.println("Ошибка: " + e);
            System.exit(1);
        }
    }

    private static void fileReaderWriter(String in, String in2, String out) {
        int count;
        try (FileChannel fChan = (FileChannel)
                Files.newByteChannel(Path.of(out),
                        StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            fileReader(in, in2, fChan);
        } catch (InvalidPathException e) {
            System.out.println("Ошибка в пути: " + e);
        } catch (IOException e) {
            System.out.println("Ошибка: " + e);
            System.exit(1);
        }
    }

    private static void fileReader(String in, String in2, FileChannel fileChannel) {
        int count;
        int count2;
        try (SeekableByteChannel fChan = Files.newByteChannel(Path.of(in));
             SeekableByteChannel fChan2 = Files.newByteChannel(Path.of(in2))) {
            ByteBuffer mBuf = ByteBuffer.allocate(2);
            ByteBuffer mBuf2 = ByteBuffer.allocate(2);

            do {
                count = fChan.read(mBuf);
                count2 = fChan2.read(mBuf2);
                if ((count != -1) & (count2 != -1)) {
                    byte[] byteArr = mBuf.array();
                    byte[] byteArr2 = mBuf2.array();

                    mBuf.rewind();
                    mBuf2.rewind();

                    ByteBuffer mBufWreter = ByteBuffer.allocate(4);
                    mBufWreter.put(mBuf.array());
                    mBufWreter.put(mBuf2.array());

                    var rsl = mBufWreter.array();
                    var rsl1 = mergeSort(rsl);
                    mBufWreter.rewind();
                    fileChannel.write(mBufWreter);
                }
            } while ((count != -1) && (count2 != -1));



//            do {
//                count = fChan.read(mBuf);
//                if (count != -1) {
//                    mBuf.rewind();
//
//                    ByteBuffer mBufWreter = ByteBuffer.allocate(2);
//                    mBufWreter.put(mBuf.array());
//                    mBufWreter.rewind();
//                    fileChannel.write(mBufWreter);
//                }
//            } while (count != -1);

//            do {
//                count = fChan2.read(mBuf2);
//                if (count != -1) {
//                    mBuf2.rewind();
//
//                    ByteBuffer mBufWreter = ByteBuffer.allocate(2);
//                    mBufWreter.put(mBuf2.array());
//                    mBufWreter.rewind();
//                    fileChannel.write(mBufWreter);
//                }
//            } while (count != -1);

        } catch (InvalidPathException e) {
            System.out.println("Ошибка в пути: " + e);
        } catch (IOException e) {
            System.out.println("Ошибка: " + e);
        }
    }



    public static byte[] mergeSort(byte[] array) {
        byte[] tmp;
        byte[] currentSrc = array;
        byte[] currentDest = new byte[array.length];

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

    private static void merge(byte[] src1, int src1Start, byte[] src2, int src2Start, byte[] dest,
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

    private static String arrayToString(byte[] array) {
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
