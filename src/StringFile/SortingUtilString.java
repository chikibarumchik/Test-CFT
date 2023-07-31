package StringFile;

import java.util.ArrayList;
import java.util.List;

public class SortingUtilString {
    public void mergeSort(List<String> list, String sortMode) {
        if (list.size() <= 1) {
            return;
        }

        int middle = list.size() / 2;


        List<String> left = new ArrayList<>(list.subList(0, middle));
        List<String> right = new ArrayList<>(list.subList(middle, list.size()));


        mergeSort(left, sortMode);
        mergeSort(right, sortMode);


        mergeSort(list, new ArrayList<>(left), new ArrayList<>(right), sortMode);
    }

    //    Сортировка переданног массива по возрастанию или по убыванию в зависимости от переданного аргумента
    private static void mergeSort(List<String> list, List<String> left, List<String> right,
                                     String sortMode) {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (sortMode.equals("-a")) {
                if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                    list.set(currentIndex, left.get(leftIndex));
                    leftIndex++;
                } else {
                    list.set(currentIndex, right.get(rightIndex));
                    rightIndex++;
                }
                currentIndex++;
            } else if (sortMode.equals("-d")) {
                if (left.get(leftIndex).compareTo(right.get(rightIndex)) >= 0) {
                    list.set(currentIndex, left.get(leftIndex));
                    leftIndex++;
                } else {
                    list.set(currentIndex, right.get(rightIndex));
                    rightIndex++;
                }
                currentIndex++;
            } else {
                System.out.println("Неизвестный параметр сортировки");
            }

        }

        addElementsInEnd(list, left, right, leftIndex, rightIndex, currentIndex);

    }


    private static void addElementsInEnd(List<String> list, List<String> left, List<String> right, int leftIndex, int rightIndex, int currentIndex) {
        while (leftIndex < left.size()) {
            list.set(currentIndex, left.get(leftIndex));
            leftIndex++;
            currentIndex++;
        }

        while (rightIndex < right.size()) {
            list.set(currentIndex, right.get(rightIndex));
            rightIndex++;
            currentIndex++;
        }
    }
}
