package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {

        String ext = "text";
        String dir = "C:\\projects\\task_cft\\src\\main\\resources\\";
        String in1 = "C:\\projects\\task_cft\\src\\main\\resources\\in1.txt";
        String in2 = "C:\\projects\\task_cft\\src\\main\\resources\\in2.txt";
        String out = "C:\\projects\\task_cft\\src\\main\\resources\\out.txt";
        Integer countStr = 3;
        List<String> listTempPath = new ArrayList<>();

        fileDelete(dir, ext);
        readeFile(in2, countStr, ext, dir, listTempPath);
        readeFile(in1, countStr, ext, dir, listTempPath);
        sortedFile(dir, ext, listTempPath);
//        mergeFile(listTempPath, out);
        mergeFileNew(listTempPath, out);
        fileDelete(dir, ext);

    }

    //читаем файл, дробим на мелкие файлы
    private static void readeFile(String pathFile, int countStr, String ext, String dir, List<String> listTempPath) {
        List<String> list = new ArrayList<>();
//        List<String> listTempPath = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if (list.size() >= countStr) {
                    File newFile = File.createTempFile(ext, ".temp", new File(dir));
                    writerFile(list, newFile);
                    list.clear();
                    listTempPath.add(newFile.getAbsolutePath());
                }
                list.add(line);
            }
            if (list.size() > 1) {
                File newFile = File.createTempFile(ext, ".temp", new File(dir));
                writerFile(list, newFile);
                list.clear();
                listTempPath.add(newFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return listTempPath;
    }

    //читаем временные файлы и сортируем их
    private static void sortedFile(String dir, String ext, List<String> listTempPath) {
        List<String> listTempPathNew = new ArrayList<>();
        for (String fileName : listTempPath) {
            String line = "";
            List<Integer> listInteger = new ArrayList<>();
            List<String> listString = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while ((line = br.readLine()) != null) {
                    listInteger.add(Integer.valueOf(line).intValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int[] listInt = listInteger.stream().mapToInt(value -> value).toArray();
            int[] listIntOrder = mergeSort(listInt);
            listString = Arrays.stream(listIntOrder).mapToObj(int_ -> String.valueOf(int_)).collect(Collectors.toList());

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                bw.flush();
                for (String str : listString) {
                    bw.write(str);
                    bw.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //объединение файлов слиянием неправильно
    private static void mergeFile(List<String> listTempPath, String outPath) {
        //обработка
        //считаем первые два слоя строк из буферов в массив
        //отсортируем масив строк
        //возьмем первую половину строки из массива, запишем их в выходной файл и удалим из массива
        //считаем следующий слой строк и повторим тоже самое

        List<String> listStr = new ArrayList<>();
        List<FileReader> listReader = listReader(listTempPath);
        List<BufferedReader> listBuf = listBuf(listReader);
        List<BufferedReader> listBufDel = new ArrayList<>();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {
            bw.flush();

            //читаем первую строку из всех буферов
            for (BufferedReader br : listBuf) {
                String line_ = br.readLine();
                listStr.add(line_);
            }

            //в цикле перечитываем следующую строку по всем буферам, пока в листе остались буферы
            while (listBuf.size() > 0) {
                for (BufferedReader br : listBuf) {
                    String line = "";
                    if ((line = br.readLine()) != null) {
                        listStr.add(line);
                    } else {
                        br.close();
                        listBufDel.add(br);
                    }
                }

                //удаляем буфер из списка,если все прочитали из него
                for (BufferedReader brDel : listBufDel) {
                    listBuf.remove(brDel);
                }
                listBufDel.clear();

                //преобразуем, сортируем
                int[] arrIn = listStr.stream().mapToInt(s -> Integer.valueOf(s)).toArray();
                int[] arrInSort = mergeSort(arrIn);
                List<String> listOut = Arrays.stream(arrInSort).mapToObj(value -> String.valueOf(value)).collect(Collectors.toList());

                //записываем в выходной файл
                int endIndex = Math.min(listTempPath.size(), listOut.size());
                for (int index = 0; index < endIndex; index++) {
                    String str = listOut.get(index);
                    bw.write(str);
                    bw.newLine();
                    //удаляем из массива уже записанные строки
                    listStr.remove(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (FileReader fr : listReader) {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //объединение файлов слиянием правильно
    private static void mergeFileNew(List<String> listTempPath, String outPath) throws IOException {

        List<FileReader> listReader = listReader(listTempPath);
        List<BufferedReader> listBuf = listBuf(listReader);
        List<String> listStrFinish = new ArrayList<>();
        Map<BufferedReader, String> mapReader = new HashMap<>();
        int countBuf = listBuf.size();

        for (BufferedReader br : listBuf) {
            String line = br.readLine();
            mapReader.put(br, line);
        }
        String minStr = null;
        BufferedReader minBr = null;

        var mapEntry = mapReader.entrySet();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {
            while (countBuf > 0) {
                minStr = null;
                minBr = null;
                for (Map.Entry<BufferedReader, String> br : mapEntry) {
                    if (br.getValue() == null) {
                        continue;
                    }

                    String line = br.getValue();
                    if (minStr == null || (Integer.valueOf(line) < Integer.valueOf(minStr))) {
                        minStr = line;
                        minBr = br.getKey();
                    }
                }
                listStrFinish.add(minStr);
                bw.write(minStr);
                bw.newLine();
                for (Map.Entry<BufferedReader, String> br : mapEntry) {
                    if (br.getKey().equals(minBr)) {
                        br.setValue(minBr.readLine());
                        minStr = br.getValue();
                        if (minStr == null) countBuf--;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (FileReader fr : listReader) {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (BufferedReader br : listBuf) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //удалить файлы
    private static void fileDelete(String dir, String ext) {
        File file = new File(dir);
        String[] arrPath = file.list((file_, name) -> name.toLowerCase().startsWith(ext));
        List<String> listPath = Arrays.stream(arrPath).map(str -> dir + str).collect(Collectors.toList());
        for (String path : listPath) {
            try {
                Files.delete(Path.of(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //запись в файл
    private static void writerFile(List<String> arr, File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String str : arr) {
                bw.write(str);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //получим массив ридеров
    private static List<FileReader> listReader(List<String> listPath) {
        List<FileReader> readerList = new ArrayList<>();
        for (String str : listPath) {
            try {
                FileReader fr = new FileReader(str);
                readerList.add(fr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return readerList;
    }

    //получить массив буферов
    private static List<BufferedReader> listBuf(List<FileReader> listPath) {
        List<BufferedReader> list = new ArrayList<>();
        for (FileReader fr : listPath) {
            try {
                BufferedReader br = new BufferedReader(fr);
                list.add(br);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //алгоритм слияния
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

    private static void merge(int[] src1, int src1Start, int[] src2, int src2Start, int[] dest, int destStart, int size) {
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
