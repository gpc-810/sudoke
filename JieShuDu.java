package com.example.javaTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by guo on 18-7-6.
 */

public class JieShuDu {

    public static class Cell {
        int x;
        int y;
        HashSet<Integer> values = new HashSet<>();

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            Cell c2 = (Cell) o;
            if (this.x == c2.x && this.y == c2.y) {
                if (isSetEqual(this.values, c2.values)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isSetEqual(Set set1, Set set2) {

            if (set1 == null && set2 == null) {
                return true; // Both are null
            }

            if (set1 == null || set2 == null || set1.size() != set2.size()) {
                return false;
            }

            Iterator ite1 = set1.iterator();
            Iterator ite2 = set2.iterator();

            boolean isFullEqual = true;

            while (ite2.hasNext() && ite1.hasNext()) {
                Object iteObj1 = ite1.next();
                Object iteObj2 = ite2.next();
                if (!set1.contains(iteObj2) || !set2.contains(iteObj1)) {
                    isFullEqual = false;
                }
            }

            return isFullEqual;
        }
    }

    static ArrayList<Cell> shudu = new ArrayList<>();

    public static void main(String[] args1) {
        Scanner scanner = new Scanner(System.in);
        init();


        boolean isNext = false;
        boolean isFinish;


        do {
            Cell mn;
            System.out.println("请输入行:");
            int h = scanner.nextInt();
            System.out.println("请输入列:");
            int l = scanner.nextInt();
            System.out.println("请输入值");
            int value = scanner.nextInt();
            mn = shudu.get((h - 1) * 9 + l - 1);
            mn.values.clear();
            mn.values.add(new Integer(value));
            System.out.println("是否继续输入");
            isNext = scanner.nextBoolean();

        } while (isNext);

        a(shudu);

        //        System.out.println("矩阵值:");
        //        //                nextValue(nowIndex, nowValues);
        //        //        nextValue(nowIndex, nowValues);
        //        //        nextValue(nowIndex, nowValues);
        //
        //        syso("矩阵值:", shudu);


    }

    private static void init() {
        for (int h = 1; h <= 9; ++h) {
            for (int l = 1; l <= 9; ++l) {
                Cell mn = new Cell();
                mn.x = h;
                mn.y = l;
                mn.values = new HashSet<>();
                for (int value = 1; value <= 9; ++value) {
                    mn.values.add(value);
                }
                shudu.add(mn);
            }
        }
    }


    private static boolean a(ArrayList<Cell> arrayList) {
        ArrayList<Cell> nextArray = intCopy(arrayList);

        for (Cell cell : nextArray) {
            if (!findComrityData(cell, nextArray)){
                return false;
            }
        }
        syso("过度解", nextArray);
        if (isMoreCellValue(nextArray)) {
            if (!nextArray.equals(arrayList)) {
                if(!a(nextArray)){
                    return false;
                }
            } else {
                nextA(nextArray);
            }
        } else {
            syso("最终解", nextArray);
        }
        return true;

    }

    private static void nextA(ArrayList<Cell> arrayList) {
        ArrayList<Cell> nextArray = intCopy(arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            Cell cell = arrayList.get(i);
            if (cell.values.size() > 1) {
                Iterator<Integer> iterator = cell.values.iterator();
                while (iterator.hasNext()) {
                    Integer v = new Integer(iterator.next());
                    Cell nextCell = nextArray.get(i);
                    nextCell.values.clear();
                    nextCell.values.add(v);
                    a(nextArray);
                }
            }

        }


    }

    private static void syso(String tag, ArrayList<Cell> arrayList) {
        System.out.println("------------------------------------------------------------------------");
        System.out.println(tag);
        for (int i = 0; i < arrayList.size(); i++) {
            Cell mn = arrayList.get(i);

            StringBuffer s = new StringBuffer();
            Iterator<Integer> iterator = mn.values.iterator();
            while (iterator.hasNext()) {
                s.append(iterator.next() + ",");
            }
            int size = s.toString().length();
            System.out.print(s);
            for (int sSize = 20; sSize > size; sSize--) {
                System.out.print(" ");
            }
            if ((i + 1) % 9 == 0) {
                System.out.println();
                if (((i + 1) / 9) % 3 == 0) {
                    System.out.println();
                }
            }
        }
    }

    private static ArrayList<Cell> intCopy(ArrayList<Cell> arrayList) {
        ArrayList<Cell> list = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); ++i) {
            Cell mn = new Cell();
            Cell next = arrayList.get(i);
            mn.x = next.x;
            mn.y = next.y;
            mn.values = new HashSet<>();
            Iterator<Integer> iterator = next.values.iterator();
            while (iterator.hasNext()) {
                mn.values.add(iterator.next());
            }
            list.add(mn);
        }
        return list;

    }


    private static boolean findComrityData(Cell cell, ArrayList<Cell> arrayList) {
        ArrayList<Cell> rowArray = new ArrayList<>();
        ArrayList<Cell> columnArray = new ArrayList<>();
        ArrayList<Cell> sudokuArray = new ArrayList<>();

        int row = cell.x;
        int column = cell.y;

        int g_row = (cell.x - 1) / 3;
        int g_column = (cell.y - 1) / 3;


        for (int i = 0; i < 9; i++) {
            Cell rowCell = arrayList.get((row - 1) * 9 + i);
            rowArray.add(rowCell);
            Cell columnCell = arrayList.get(i * 9 + column - 1);
            columnArray.add(columnCell);
            int g_x = i / 3;
            int g_y = i % 3;
            Cell sudokuCell = arrayList.get((g_row * 3 + g_x) * 9 + g_column * 3 + g_y);
            sudokuArray.add(sudokuCell);
        }

        if (!oneRowData(cell, rowArray)) {
            return false;
        }
        if (!oneColumnData(cell, columnArray)) {
            return false;
        }
        if (!oneSudokuData(cell, sudokuArray)) {
            return false;
        }
        return true;

    }

    private static boolean oneRowData(Cell cell, ArrayList<Cell> arrayList) {
        if (!deleteData(cell, arrayList)) {
            return false;
        }
        setData(cell, arrayList);
        return true;

    }

    private static boolean oneColumnData(Cell cell, ArrayList<Cell> arrayList) {
        if (!deleteData(cell, arrayList)) {
            return false;
        }
        setData(cell, arrayList);
        return true;
    }

    private static boolean oneSudokuData(Cell cell, ArrayList<Cell> arrayList) {
        if (!deleteData(cell, arrayList)) {
            return false;
        }
        setData(cell, arrayList);
        return true;
    }


    private static boolean deleteData(Cell cell, ArrayList<Cell> arrayList) {
        int[] valueArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (Cell cell1 : arrayList) {
            Iterator<Integer> iterator = cell1.values.iterator();
            while (iterator.hasNext()) {
                Integer v = iterator.next();
                valueArray[v]++;
            }
        }

        if (cell.values.size() == 1) {
            for (Cell cell1 : arrayList) {
                if (!cell1.equals(cell)) {
                    if (cell1.values.size() <= 0) {
                        return false;
                    }
                    cell1.values.remove(cell.values.iterator().next());
                }
            }
        }
        return true;
    }

    private static void setData(Cell cell, ArrayList<Cell> arrayList) {
        if (cell.values.size() <= 1) {
            return;
        }
        int[] valueArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (Cell cell1 : arrayList) {
            Iterator<Integer> iterator = cell1.values.iterator();
            while (iterator.hasNext()) {
                Integer v = iterator.next();
                valueArray[v]++;
            }
        }

        boolean isOneValue = false;
        for (int v : valueArray) {
            if (v == 1) {
                isOneValue = true;
                break;
            }
        }
        if (!isOneValue) {//没有单个数据
            return;
        }
        for (Cell cell1 : arrayList) {
            Iterator<Integer> iterator = cell1.values.iterator();
            while (iterator.hasNext()) {
                Integer v = iterator.next();
                if (valueArray[v] == 1) {//当前单元格是有唯一一个值
                    cell1.values.clear();
                    cell1.values.add(v);
                    break;
                }
            }
        }


    }

    private static boolean isMoreCellValue(ArrayList<Cell> arrayList) {
        for (Cell cell : arrayList) {
            if (cell.values.size() > 1) {
                return true;
            }

        }
        return false;

    }


}
