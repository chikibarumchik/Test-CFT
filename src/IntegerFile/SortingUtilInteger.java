package IntegerFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortingUtilInteger {
    public void mergeSort(List<Long> list, String sortMode) {
        if (list.size() <= 1) {
            return;
        }

        int middle = list.size() / 2;


        List<Long> left = new ArrayList<>(list.subList(0, middle));
        List<Long> right = new ArrayList<>(list.subList(middle, list.size()));


        mergeSort(left, sortMode);
        mergeSort(right, sortMode);


        mergeSort(list, new ArrayList<>(left), new ArrayList<>(right), sortMode);
    }

//    Сортировка переданног массива по возрастанию или по убыванию в зависимости от переданного аргумента
    private static void mergeSort(List<Long> list, List<Long> left, List<Long> right,
                                     String sortMode) {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (sortMode.equals("-a")) {
                if (left.get(leftIndex) <= right.get(rightIndex)) {
                    list.set(currentIndex, left.get(leftIndex));
                    leftIndex++;
                } else {
                    list.set(currentIndex, right.get(rightIndex));
                    rightIndex++;
                }
                currentIndex++;
            } else if (sortMode.equals("-d")) {
                if (left.get(leftIndex) >= right.get(rightIndex)) {
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

    private static void addElementsInEnd(List<Long> list, List<Long> left, List<Long> right, int leftIndex, int rightIndex, int currentIndex) {
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
