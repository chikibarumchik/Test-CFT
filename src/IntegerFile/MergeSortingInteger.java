package IntegerFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeSortingInteger {

    private static final int BLOCK_SIZE = 300000;
    private static final SortingUtilInteger sortingUtilInteger = new SortingUtilInteger();
    private static long fileNumber = 0;
    private static Map<Long, String> tempFiles = new HashMap<>();
    private static final String TEMP_DIRECTORY = "temp_directory";

//    Метод проверяет, отсортирован ли переданный файл в соответствии с переданым аргументов сортировки
    private static boolean checkFileForSorting(String inputFile, String sortMode) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            List<Long> block = new ArrayList<>(BLOCK_SIZE);
            List<Long> sortBlock = new ArrayList<>(BLOCK_SIZE);
            List<Long> tempFilesNumbers = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    long num = Long.parseLong(line);
                    block.add(num);
                } catch (Exception e) {
                    for (Long l: tempFilesNumbers) {
                        new File(tempFiles.get(l)).delete();
                        tempFiles.remove(l);
                    }
                    System.out.println("В файле передан неверный тип данных");
                    return false;
                }

                if (BLOCK_SIZE == block.size()) {
                    sortBlock.addAll(block);
                    sortingUtilInteger.mergeSort(sortBlock, sortMode);

                    if (!sortBlock.equals(block)) {
                        for (Long l: tempFilesNumbers) {
                            new File(tempFiles.get(l)).delete();
                            tempFiles.remove(l);
                        }
                        System.out.println("Передан не сортированный файл");
                        return false;
                    }
                    tempFilesNumbers.add(fileNumber);
                    tempFiles.put(fileNumber, TEMP_DIRECTORY + "/temp_" + fileNumber + ".txt");
                    saveBlockInFileInteger(block, TEMP_DIRECTORY + "/temp_" + fileNumber + ".txt");
                    block.clear();
                    sortBlock.clear();
                    fileNumber++;
                }
            }

            if (!block.isEmpty()) {
                sortBlock.addAll(block);
                sortingUtilInteger.mergeSort(sortBlock, sortMode);
                if (!sortBlock.equals(block)) {
                    for (Long l: tempFilesNumbers) {
                        new File(tempFiles.get(l)).delete();
                        tempFiles.remove(l);
                    }
                    System.out.println("Передан не сортированный файл");
                    return false;
                }
                tempFilesNumbers.add(fileNumber);
                tempFiles.put(fileNumber, TEMP_DIRECTORY + "/temp_" + fileNumber + ".txt");
                saveBlockInFileInteger(block, TEMP_DIRECTORY + "/temp_" + fileNumber + ".txt");
                block.clear();
                sortBlock.clear();
                fileNumber++;
            }
            return true;
        }
    }

    private static void saveBlockInFileInteger(List<Long> block, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            for (Long line: block) {
                writer.write(String.valueOf(line));
                writer.newLine();
            }
        }
    }

    public void mergeAllFiles(List<String> files, String sortMode) throws IOException {
        Path tempDirPath = Paths.get(TEMP_DIRECTORY);
        if (!Files.exists(tempDirPath)) {
            Files.createDirectory(tempDirPath); // Создаем директорию, если ее нет
        }
        for (String file : files) {
            checkFileForSorting(file, sortMode);
        }
    }



    public void mergeSortedBlocks (String outputFile, String sortMode) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            List<BufferedReader> readers = new ArrayList<>();
            List<String> lines = new ArrayList<>();

            for (Map.Entry<Long, String> entry: tempFiles.entrySet()) {
                readers.add(new BufferedReader(new FileReader(entry.getValue())));
                lines.add(null);
            }

            for (int i = 0; i < readers.size(); i++) {
                BufferedReader reader = readers.get(i);
                String line = reader.readLine();
                lines.set(i, line);
            }

            while (!readers.isEmpty()) {
                int index;
                if (sortMode.equals("-d")) {
                    index = findMaxIndex(lines);
                } else {
                    index = findMinIndex(lines);
                }
                String minLine = lines.get(index);
                writer.write(minLine);
                writer.newLine();

                BufferedReader reader = readers.get(index);
                String nextLine = reader.readLine();
                if (nextLine == null) {
                    readers.remove(index);
                    lines.remove(index);
                    reader.close();
                } else {
                    lines.set(index, nextLine);
                }
            }
        }

        for (Map.Entry<Long, String> entry: tempFiles.entrySet()) {
            new File(entry.getValue()).delete();
        }
    }

    private static int findMinIndex(List<String> lines) {
        int minIndex = 0;
        long minValue = Long.parseLong(lines.get(0));

        for (int i = 1; i < lines.size(); i++) {
            long value = Long.parseLong(lines.get(i));
            if (value < minValue) {
                minValue = value;
                minIndex = i;
            }
        }

        return minIndex;
    }

    private static int findMaxIndex(List<String> lines) { // Переименование метода
        int maxIndex = 0;
        long maxValue = Long.parseLong(lines.get(0));

        for (int i = 1; i < lines.size(); i++) {
            long value = Long.parseLong(lines.get(i));
            if (value > maxValue) { // Изменение условия здесь
                maxValue = value;
                maxIndex = i;
            }
        }

        return maxIndex;
    }



}
