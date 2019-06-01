/*
 * To change this license header, choose License Headers in Proindexect Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.util.ArrayList;
import static lab2ppvis.GUI.printToTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

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
    private Table table;
    private Label error;
    private Label pageInfo;
           private Label currentStudentsAmount;
           private Label currentAmountOfRowsOnPage;
           //private Label currentNumberOfRows;
                   




    GUI gui = new GUI();
    
        public void settingsDialog(Shell parentShell) {
        Shell settingsDialogShell = new Shell(parentShell, SWT.APPLICATION_MODAL
                | SWT.DIALOG_TRIM);
        Label textInfo = new Label(settingsDialogShell, SWT.NONE);
        textInfo.setBounds(15, 0, 185, 25);
        textInfo.setText("Input number of rows on the page: ");
        Text inputRowsOnPage = new Text(settingsDialogShell, SWT.BORDER);
        inputRowsOnPage.setBounds(30, 25, 150, 30);
        Button setNumberOfRowsOnPage = new Button(settingsDialogShell, SWT.NONE);
        setNumberOfRowsOnPage.setBounds(70, 60, 80, 30);
        setNumberOfRowsOnPage.setText("Apply");
        setNumberOfRowsOnPage.addSelectionListener(widgetSelectedAdapter(event -> {
            setDefaultPageSettings( gui.controller.getStudentsSize(), Integer.parseInt(inputRowsOnPage.getText()));
            pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
            currentStudentsAmount.setText("Current amount of students: " + gui.controller.getStudentsSize());
            currentAmountOfRowsOnPage.setText("Current amount of rows: " +numberOfRowsOnPage);
            settingsDialogShell.close();
        }
        ));
        settingsDialogShell.pack();
        settingsDialogShell.open();
    }
    public void loadTableComponent(Shell shell){
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.grabExcessVerticalSpace = true;
        table = new Table(shell, SWT.READ_ONLY);
        table.setLayoutData(gridData);
        table.setBounds(0, 0, 1300, 300);
        table.setLinesVisible(true);
        table.computeSize(gui.controller.getCounter(), gui.controller.getCounter());
        table.setHeaderVisible(true);
        TableColumn numberOfRow = new TableColumn(table, SWT.BORDER);
        numberOfRow.setText("№");
        numberOfRow.setWidth(90);
        TableColumn studentNSF = new TableColumn(table, SWT.BORDER);
        studentNSF.setText("ФИО студента");
        studentNSF.setWidth(170);
        table.setHeaderVisible(true);
        TableColumn group = new TableColumn(table, SWT.BORDER);
        group.setText("Группа");
        group.setWidth(100);
        table.setHeaderVisible(true);
        ArrayList<TableColumn> columns = new ArrayList();
        for (int i = 0; i < 10; i++) {
            TableColumn SocialWork = new TableColumn(table, SWT.BORDER);
            SocialWork.setText("Семестр " + (i + 1));
            SocialWork.setWidth(90);
            columns.add(SocialWork);
        }

        Button back = new Button(shell, SWT.NONE);
        back.setBounds(615, 300, 30, 30);
        back.setText("<");
        pageInfo = new Label(shell, SWT.NONE);
        pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
        pageInfo.setBounds(670, 300, 60, 30);
        Button forward = new Button(shell, SWT.NONE);
        forward.setBounds(740, 300, 30, 30);
        forward.setText(">");
        Button goToLastPage = new Button(shell, SWT.NONE);
        goToLastPage.setBounds(730, 335, 60, 30);
        goToLastPage.setText("Last page");
        Button goToFirstPage = new Button(shell, SWT.NONE);
        goToFirstPage.setBounds(615, 335, 60, 30);
        goToFirstPage.setText("First page");
        Button settings = new Button(shell, SWT.NONE);
        settings.setBounds(0, 300, 305, 30);
        settings.setText("Settings");
        error = new Label(shell, SWT.NONE);
        error.setBounds(620, 365, 200, 30);
        currentStudentsAmount = new Label(shell, SWT.NONE);
        currentStudentsAmount.setBounds(780, 300, 200, 20);
        currentAmountOfRowsOnPage = new Label(shell, SWT.NONE);
        currentAmountOfRowsOnPage.setBounds(780, 320, 200, 30);        
        currentStudentsAmount.setText("Current students amount: "+gui.controller.getStudentsSize());
        currentAmountOfRowsOnPage.setText("Current amount of rows: " +numberOfRowsOnPage);
      
        goToFirstPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                goToPage(table, 1);
                try {
                    pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
                } catch (IndexOutOfBoundsException ex) {
                    pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
                    error.setText("You have reached the end of table");
                }
            }
        });
        goToLastPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    goToPage(table, getAmountOfPages());
                    pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
                } catch (IndexOutOfBoundsException ex) {
                    pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
                    error.setText("You have reached the end of table");
                }
            }
        });

        settings.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
               settingsDialog(shell);
                pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
            }
        });
        back.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (getCurrentPage() == 1) {
                    error.setText("You have reached the end of table");
                    return;
                }
                error.setText("");
                try {
                    previousPage(table);
                } catch (IndexOutOfBoundsException ex) {
                    error.setText("You have reached the end of table");
                }
                pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
            }
        });
        forward.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (getCurrentPage()==getAmountOfPages()) {error.setText("You have reached the end of table"); return;}

                error.setText("");
                try {
                    nextPage(table);
                } catch (IndexOutOfBoundsException ex) {
                    error.setText("You have reached the end of table");
                }
                pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
            }
        });
    }

    public void printDataBaseToTable(Table table, int start, int end) {
        table.removeAll();
        gui.rows.clear();
        int rowsCounter = 0;
        String nameTranslator = "";
        int rowAmountSaverToPrint = 0;
        for (int studentNumber = start; studentNumber < end + 1; studentNumber++) {
                               rowAmountSaverToPrint = studentNumber;
            gui.rows.add(new TableItem(table, SWT.BORDER));
            try {
                for (int index = 0; index < gui.controller.getStudentsSize(studentNumber); index++) {
                    switch (index) {
                        case 0:
                            printToTable(gui.rows.get(rowsCounter), 0, gui.controller.getStudentData(studentNumber, index));
                            break;
                        case 1:
                            nameTranslator += gui.controller.getStudentData(studentNumber, index+2) + " ";
                            break;
                        case 2:
                            nameTranslator += gui.controller.getStudentData(studentNumber, index-1) + " ";
                            break;
                        case 3:
                            nameTranslator += gui.controller.getStudentData(studentNumber, index-1) + " ";
                            break;
                        case 4:
                            printToTable(gui.rows.get(rowsCounter), 1, nameTranslator);
                            nameTranslator = "";
                            printToTable(gui.rows.get(rowsCounter), index - 2, gui.controller.getStudentData(studentNumber, index));
                            break;
                        default:
                            printToTable(gui.rows.get(rowsCounter), index - 2, gui.controller.getStudentData(studentNumber, index));
                            break;
                    }
                }
            } catch (IndexOutOfBoundsException ex) {
               /*            // try{
                end = gui.controller.getStudentsSize();
                gui.rows.add(new TableItem(table, SWT.BORDER));
                for (int index = indexSaverInCaseOfException; index < gui.controller.getStudentsSize(i); index++) {
                    switch (index) {
                        case 0:
                            printToTable(gui.rows.get(rowsCounter), 0, gui.controller.getStudentData(i, index));
                            break;
                        case 1:
                            nameTranslator += gui.controller.getStudentData(i, index+2) + " ";
                            break;
                        case 2:
                            nameTranslator += gui.controller.getStudentData(i, index-1) + " ";
                            break;
                        case 3:
                            nameTranslator += gui.controller.getStudentData(i, index-1) + " ";
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
                }*/
               numberOfRowsOnPage=rowAmountSaverToPrint;
             currentAmountOfRowsOnPage.setText("Current amount of rows: " +numberOfRowsOnPage);

               return;
            }        


            rowsCounter++;
        }
    }

    public void setAmountOfPages(int amountOfStudents, int rowsOnPage) {
        int rowsCounter = 0;
        for (int index = 0; index < amountOfStudents; index++) {
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
        for (int counter = 0; counter < amountOfStudents; counter++) {
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

    public void setDefaultPageSettings(int amountOfStudents, int newNumberOfRowsOnPage) {
        //clearDataBase();
        //getDataBase(dataBase);
        amountOfPages = 0;
        setAmountOfPages(amountOfStudents, newNumberOfRowsOnPage);
        currentPageStart = 0;
        numberOfRowsOnPage = newNumberOfRowsOnPage;
        currentPageEnd = numberOfRowsOnPage - 1;
        currentPage = 1;
        table.removeAll();
        try{
                    printDataBaseToTable(table, currentPageStart, currentPageEnd);
                                    pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());
                    currentStudentsAmount.setText("Current amount of students: "+gui.controller.getStudentsSize());
        }catch(IndexOutOfBoundsException ex){
            numberOfRowsOnPage = gui.controller.getStudentsSize();
        currentPageEnd = numberOfRowsOnPage - 1;
        currentPage = 1;
        printDataBaseToTable (table, currentPageStart, gui.controller.getStudentsSize());
                            currentStudentsAmount.setText("Current amount of students: "+gui.controller.getStudentsSize());
                                                                pageInfo.setText("Page " + getCurrentPage() + "|" + getAmountOfPages());

}
    }
public void loadNewDataBaseToOBP(ArrayList newList){
            gui.controller.removeDataFromBase();
                    gui.controller.addDataBase(newList);
}
    public void getDataBase(ArrayList list) {
    }

    public void clearDataBase() {
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
