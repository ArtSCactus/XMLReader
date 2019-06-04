/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.util.ArrayList;

/**
 *
 * @author ArtSCactus
 */
public class DataBase {

    private String fileName;
    private ArrayList<ArrayList> students = new ArrayList();

    /**
     * Removes all data from data base
     */
    public void removeDataFromBase() {
        students.clear();
    }

    /**
     * Returns all data base in one massiv
     *
     * @return students (ArrayList)
     */
    public ArrayList getDataBase() {
        return students;
    }

    /**
     * Adding to data base new student in one massive (incommingList).
     *
     * @param incommingList
     */
    public void addDataBase(ArrayList incommingList) {
        students.addAll(incommingList);
    }

    /**
     * Changes current file name.
     *
     * @param newFileName
     */
    public void setFileName(String newFileName) {
        fileName = newFileName;
    }

    /**
     * Gets name of current file name
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Removes student (massive of student data) by his inde
     *
     * @param indexOfStudent
     */
    public void removeStudent(int indexOfStudent) {
        students.remove(indexOfStudent);
    }

    /**
     * Returns numbers of students (number of massives) in data base
     * @return 
     */
    public int getStudentsSize() {
        return students.size();
    }

    /**
     * Returns amount elements in student data massive by its number
     * (indexOfElement)
     *
     * @param indexOfElement
     * @return students.get(indexOfElement).size()
     */
    public int getStudentsSize(int indexOfElement) {
        return students.get(indexOfElement).size();
    }
/**Adds massive to main massive (students
     * @param incomingList)*/
    public void addStudentData(ArrayList incomingList) {
        students.add(incomingList);
    }
/**Get one cell from students massive in adress (studentIndex, dataIndex
     * @param studentIndex)
     * @param dataIndex
     * @return */
    public String getStudentData(int studentIndex, int dataIndex) {
        return (String) students.get(studentIndex).get(dataIndex);
    }
/**Returns all student data in a massive by its number
     * @param studentIndex
     * @return */
    public ArrayList getStudentData(int studentIndex) {
        return students.get(studentIndex);
    }
/**Returns index in massive of student, that consist target. Returns -1 in case if student wasn't foun
     * @param target
     * @return */
    public int getIndexOfRowWithTarget(String target) {
        int indexOfRow = 0;
        boolean existence = false;
        for (int studentNumber = 0; studentNumber < students.size(); studentNumber++) {
            for (int column = 0; column < 16; column++) {
                if (students.get(studentNumber).get(column).equals(target)) {
                    indexOfRow = studentNumber;
                    existence = true;
                }
            }
        }
        if (existence == true) {
            return indexOfRow;
        } else {
            return -1;
        }
    }
/**Returns index in students massive of student, that consist target in certain collumn
     * @param target
     * @param columnIndex
     * @return */
    public int getIndexOfRowWithTarget(String target, int columnIndex) {
        int indexOfRow = 0;
        boolean existence = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).get(columnIndex).equals(target)) {
                existence = true;
                return i;
            }
        }
        if (existence == true) {
            return indexOfRow;
        }
        return -1;
    }
/**Returns massive of students index in students massive with search by target in certain column
     * @param target
     * @param indexOfColumn
     * @return */
    public ArrayList getIndexesOfRowsWithTarget(String target, int indexOfColumn) {
        ArrayList<Integer> indexesOfRows = new ArrayList();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).get(indexOfColumn).equals(target)) {
                String num = (String) students.get(i).get(0);//ошибка была в несовпадении области поиска 
                indexesOfRows.add(Integer.valueOf(num));
            }
        }
        return indexesOfRows;
    }
/**Returns massive of students index in students massive with search by name or group in a certain column
 * with up and down limits of amount of work
 * @param nameOrGroup
 * @param downLimit
 * @param upLimit
 * @param firstColumn
 * @param secondColumn
 * @return 
 */ 
    public ArrayList getIndexesOfRowsWithTarget(String nameOrGroup, String downLimit, String upLimit, int firstColumn, int secondColumn) {
        ArrayList<Integer> indexesOfRows = new ArrayList();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).get(firstColumn).equals(nameOrGroup) & Integer.parseInt((String) students.get(i).get(secondColumn)) >= Integer.parseInt(downLimit) & Integer.parseInt((String) students.get(i).get(secondColumn)) <= Integer.parseInt(upLimit)) {
                indexesOfRows.add(i);
            }
        }
        return indexesOfRows;
    }
/**Returns true if exists such element, that consist target in a certain column
 * @param target
 * @param indexOfColumn
 * @return 
 */
    public boolean isExist(String target, int indexOfColumn) {
        boolean existence = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).get(indexOfColumn).equals(target)) {
                existence = true;
            }
        }
        if (existence == true) {
            return true;
        } else {
            return false;
        }
    }
    private int counter = 0;
/**Returns variable counter
 * 
 * @return 
 */
    public int getCounter() {
        return counter;
    }
/**Summing to variable counter i
 * 
 * @param i 
 */
    public void setCounter(int i) {
        counter += i;
    }
}
