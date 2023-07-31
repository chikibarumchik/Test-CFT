package StringFile;

import IntegerFile.SortingUtilInteger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeSortingString {

    private static final int BLOCK_SIZE = 300000;
    private static final SortingUtilString sortingUtilString = new SortingUtilString();
    private static long fileNumber = 0;
    private static Map<Long, String> tempFiles = new HashMap<>();
    private static final String TEMP_DIRECTORY = "temp_directory";

    private static boolean checkFileForSorting(String inputFile, String sortMode) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            List<String> block = new ArrayList<>(BLOCK_SIZE);
            List<String> sortBlock = new ArrayList<>(BLOCK_SIZE);
            List<Long> tempFilesNumbers = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.split(" ").length == 1) {
                    block.add(line);
                } else {
                    for (Long l: tempFilesNumbers) {
                        new File(tempFiles.get(l)).delete();
                        tempFiles.remove(l);
                    }
                    System.out.println("В файле передан неверный тип данных");
                    return false;
                }

                if (BLOCK_SIZE == block.size()) {
                    sortBlock.addAll(block);
                    sortingUtilString.mergeSort(sortBlock, sortMode);
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
                    saveBlockInFileString(block, TEMP_DIRECTORY + "/temp_" + fileNumber + ".txt");
                    block.clear();
                    sortBlock.clear();
                    fileNumber++;
                }
            }

            if (!block.isEmpty()) {
                sortBlock.addAll(block);
                sortingUtilString.mergeSort(sortBlock, sortMode);
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
                saveBlockInFileString(block, TEMP_DIRECTORY + "/temp_" + fileNumber + ".txt");
                block.clear();
                sortBlock.clear();
                fileNumber++;
            }
            return true;
        }
    }

    private static void saveBlockInFileString(List<String> block, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            for (String line: block) {
                writer.write(line);
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
                System.out.println(line);
                lines.set(i, line);
            }

            while (!readers.isEmpty()) {
                int index;
                if (sortMode.equals("-d")) {
                    index = findMaxIndex(lines);
                } else {
                    index = findMinIndex(lines);
                }
                String line = lines.get(index);
                writer.write(line);
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
        String minValue = lines.get(0);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line != null && (minValue == null || line.compareTo(minValue) < 0)) {
                minValue = line;
                minIndex = i;
            }
        }

        return minIndex;
    }

    private static int findMaxIndex(List<String> lines) {
        int maxIndex = 0;
        String maxValue = lines.get(0);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line != null && (maxValue == null || line.compareTo(maxValue) > 0)) {
                maxValue = line;
                maxIndex = i;
            }
        }

        return maxIndex;
    }


}
