/*
 * To change this license header, choose License Headers in Proindexect Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.util.ArrayList;
import static lab2ppvis.GUI.printToTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * @author ArtSCactus
 */
public class OutputByPage {

    private int amountOfPages = 0;
    private int currentPageStart = 0;
    private int numberOfRowsOnPage = 10;
    private int currentPageEnd = numberOfRowsOnPage;
    private int currentPage = 0;
    GUI gui = new GUI();

    public void printDataBaseToTable(Table table, int start, int end) {
        table.removeAll();
        gui.rows.clear();
        int rowsCounter = 0;
        String nameTranslator = "";
        int indexSaverInCaseOfException = 0;
        for (int i = start; i < end + 1; i++) {
            gui.rows.add(new TableItem(table, SWT.BORDER));
            try {
                for (int index = 0; index < gui.controller.getStudentsSize(i); index++) {
                    indexSaverInCaseOfException = index;
                    switch (index) {
                        case 0:
                            printToTable(gui.rows.get(rowsCounter), 0, gui.controller.getStudentData(i, index));
                            break;
                        case 1:
                            nameTranslator += gui.controller.getStudentData(i, index) + " ";
                            break;
                        case 2:
                            nameTranslator += gui.controller.getStudentData(i, index) + " ";
                            break;
                        case 3:
                            nameTranslator += gui.controller.getStudentData(i, index) + " ";
                            break;
                        case 4:
                            printToTable(gui.rows.get(rowsCounter), 1, nameTranslator);
                            nameTranslator = "";
                            printToTable(gui.rows.get(rowsCounter), index - 2, gui.controller.getStudentData(i, index));
                            break;
                        default:
                            printToTable(gui.rows.get(rowsCounter), index - 2, gui.controller.getStudentData(i, index));
                            break;
                    }
                }
            } catch (IndexOutOfBoundsException ex) {
                end = gui.controller.getStudentsSize();
                gui.rows.add(new TableItem(table, SWT.BORDER));
                for (int index = indexSaverInCaseOfException; index < gui.controller.getStudentsSize(i); index++) {
                    switch (index) {
                        case 0:
                            printToTable(gui.rows.get(rowsCounter), 0, gui.controller.getStudentData(i, index));
                            break;
                        case 1:
                            nameTranslator += gui.controller.getStudentData(i, index) + " ";
                            break;
                        case 2:
                            nameTranslator += gui.controller.getStudentData(i, index) + " ";
                            break;
                        case 3:
                            nameTranslator += gui.controller.getStudentData(i, index) + " ";
                            break;
                        case 4:
                            printToTable(gui.rows.get(rowsCounter), 1, nameTranslator);
                            nameTranslator = "";
                            printToTable(gui.rows.get(rowsCounter), index - 2, gui.controller.getStudentData(i, index));
                            break;
                        default:
                            printToTable(gui.rows.get(rowsCounter), index - 2, gui.controller.getStudentData(i, index));
                            break;
                    }
                }
            }
            rowsCounter++;
        }
    }

    public void setAmountOfPages(int amountOfStudents, int rowsOnPage) {
        int rowsCounter = 0;
        for (int i = 0; i < amountOfStudents; i++) {
            if (rowsCounter == rowsOnPage - 1) {
                amountOfPages++;
                rowsCounter = 0;
            } else {
                rowsCounter++;
            }
        }
        if (rowsCounter > 0) {
            amountOfPages++;
        }
        System.out.println("Inputed number of rows: " + numberOfRowsOnPage);
        System.out.println("Amount of pages: " + amountOfPages);
        System.out.println("Amount of students: " + amountOfStudents);
    }

    public int calculcateAmountOfPages(int amountOfStudents, int rowsOnPage) {
        int pageCounter = 0;
        int rowsCounter = 0;
        for (int i = 0; i < amountOfStudents; i++) {
            if (rowsCounter == rowsOnPage - 1) {
                pageCounter++;
                rowsCounter = 0;
            } else {
                rowsCounter++;
            }
        }
        if (rowsCounter > 0) {
            pageCounter++;
        }
        return pageCounter;
    }

    public int getAmountOfPages() {
        return amountOfPages;
    }

    public int getAmountOfRowsOnPage() {
        return numberOfRowsOnPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void newNumberOfRowsOnPage(int number) {
        numberOfRowsOnPage = number;
    }

    public void nextPage(Table table) {
        if (currentPage > amountOfPages - 1) {
            return;
        }
        currentPage++;
        currentPageEnd += numberOfRowsOnPage;
        currentPageStart += numberOfRowsOnPage;
        printDataBaseToTable(table, currentPageStart, currentPageEnd);
    }

    public void previousPage(Table table) {
        if (currentPage < 1) {
            return;
        }
        currentPage--;
        currentPageEnd -= numberOfRowsOnPage;
        currentPageStart -= numberOfRowsOnPage;
        printDataBaseToTable(table, currentPageStart, currentPageEnd);
    }

    public void rewriteInfo(Table table) {
        table.removeAll();
        printDataBaseToTable(table, currentPageStart, currentPageEnd);
    }

    public void setDefaultPageSettings(Table table, int amountOfStudents, int newNumberOfRowsOnPage, ArrayList dataBase) {
        clearDataBase();
        getDataBase(dataBase);
        amountOfPages = 0;
        setAmountOfPages(amountOfStudents, newNumberOfRowsOnPage);
        currentPageStart = 0;
        numberOfRowsOnPage = newNumberOfRowsOnPage;
        currentPageEnd = numberOfRowsOnPage - 1;
        currentPage = 1;
        table.removeAll();
        try{
        printDataBaseToTable(table, currentPageStart, currentPageEnd);
        }catch(IndexOutOfBoundsException ex){
            numberOfRowsOnPage = dataBase.size();
        currentPageEnd = numberOfRowsOnPage - 1;
        currentPage = 1;printDataBaseToTable (table, currentPageStart, dataBase.size());}
    }

    public void getDataBase(ArrayList list) {
        gui.controller.addDataBase(list);
    }

    public void clearDataBase() {
        gui.controller.removeDataFromBase();
    }

    public void goToPage(Table table, int targetPage) {
        if (targetPage > currentPage) {
            currentPageStart += numberOfRowsOnPage * (targetPage - currentPage);
            currentPageEnd += (numberOfRowsOnPage * (targetPage - currentPage));
            currentPage = targetPage;
            printDataBaseToTable(table, currentPageStart, currentPageEnd);
        } else {
            if (targetPage < currentPage) {

                currentPageStart -= numberOfRowsOnPage * (currentPage - targetPage);
                currentPageEnd -= (numberOfRowsOnPage * (currentPage - targetPage));
                currentPage = targetPage;
                printDataBaseToTable(table, currentPageStart, currentPageEnd);
            }

        }
    }
}
