
import IntegerFile.MergeSortingInteger;
import StringFile.MergeSortingString;

import java.io.*;
import java.util.*;

public class Main {
    private static final MergeSortingString margeString = new MergeSortingString();
    private static final MergeSortingInteger mergeInteger = new MergeSortingInteger();

    public static void main(String[] args) throws IOException {

        String sortMode;
        String dataType;
        String outputFile;
        List<String> inputFiles = new ArrayList<>();
        if (args[0].equals("-d") || args[0].equals("-a")) {
            sortMode = args[0];
            dataType = args[1];
            outputFile = args[2];
            for (int i = 3; i < args.length; i++) {
                inputFiles.add(args[i]);
            }
        } else {
            sortMode = "-a";
            dataType = args[0];
            outputFile = args[1];
            for (int i = 2; i < args.length; i++) {
                inputFiles.add(args[i]);
            }
        }

//        Вызываем метода класса в зависимости от того, какой аргумент передан
        try {
            if (dataType.equals("-i")) {
                mergeInteger.mergeAllFiles(inputFiles, sortMode);
                mergeInteger.mergeSortedBlocks(outputFile, sortMode);
            } else if (dataType.equals("-s")) {
                margeString.mergeAllFiles(inputFiles, sortMode);
                margeString.mergeSortedBlocks(outputFile, sortMode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





    }
}