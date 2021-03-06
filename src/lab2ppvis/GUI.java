/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author ArtSCactus
 */
public class GUI {

    public ArrayList<TableItem> rows = new ArrayList();

    public ArrayList getRows() {
        return rows;
    }

    public void addRow(TableItem item) {
        rows.add(item);
    }

    public void removeRow(int index) {
        rows.remove(index);
    }

    public TableItem getRow(int index) {
        return rows.get(index);
    }
    Controller controller = new Controller();
    private String bufferName = "";
    private static final String[][] FILTERS = {{"Таблица XML (*.xml)", "*.xml"},
    {"Файлы Excel (*.xlsx)", "*.xlsx"},
    {"Файлы Adobe (*.pdf)", "*.pdf"},
    {"Файлы Word (*.docx)", "*.docx"},
    {"Все файлы (*.*)", "*.*"}};

    private void setFilters(FileDialog dialog) {
        String[] names = new String[FILTERS.length];
        String[] exts = new String[FILTERS.length];
        for (int i = 0; i < FILTERS.length; i++) {
            names[i] = FILTERS[i][0];
            exts[i] = FILTERS[i][1];
        }
        dialog.setFilterNames(names);
        dialog.setFilterExtensions(exts);
    }
    public static int arrayListCounter = 0;

    public void chooseFileDialog(Shell parentShell) {
        FileDialog dlg = new FileDialog(parentShell, SWT.OPEN);
        setFilters(dlg);
        String fname = dlg.open();
        if (fname != null) {
            controller.setFileName(fname);
        }
    }

    public void saveFileDialog(Shell parentShell) {
        FileDialog dlg = new FileDialog(parentShell, SWT.SAVE);
        setFilters(dlg);
        String fname = dlg.open();
        if (fname != null) {
            bufferName = fname;
        }
        System.out.println("" + fname);
    }

    public void loadGroupList(Combo combo, int column) {
        boolean exist = false;

        for (int i = 0; i < controller.getStudentsSize(); i++) {
            exist = false;
            String s = controller.getStudentData(i, column);
            if (s == null || s.length() == 0) {
                return;
            }
            String[] items = combo.getItems();
            for (String item : items) {
                if (item.equals(s) == true) {
                    exist = true;
                    break;
                }
            }
            if (exist == false) {
                combo.add(s);
            }
        }
    }

    public void changeTableDialog(OutputByPage obp, Shell parentShell) throws ParserConfigurationException {
        Shell deleteItemShell = new Shell(parentShell, SWT.APPLICATION_MODAL
                | SWT.DIALOG_TRIM);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.grabExcessVerticalSpace = true;
        Label highLimit = new Label(deleteItemShell, SWT.NONE);
        highLimit.setBounds(0, 10, 80, 25);
        highLimit.setText("Hight limit: ");
        Label lowLimit = new Label(deleteItemShell, SWT.NONE);
        lowLimit.setBounds(0, 40, 80, 25);
        lowLimit.setText("Down limit: ");
        Text upLimit = new Text(deleteItemShell, SWT.BORDER);
        upLimit.setBounds(80, 10, 50, 25);
        Text downLimit = new Text(deleteItemShell, SWT.BORDER);
        downLimit.setBounds(80, 40, 50, 25);
        Label comboInfo = new Label(deleteItemShell, SWT.NONE);
        comboInfo.setBounds(150, 10, 80, 30);
        comboInfo.setText("List of groups: ");
        Label checkInfo = new Label(deleteItemShell, SWT.NONE);
        checkInfo.setBounds(150, 40, 165, 30);
        checkInfo.setText("Use amount of work in search: ");
        Button useAmountOfWorkInSearch = new Button(deleteItemShell, SWT.CHECK);
        useAmountOfWorkInSearch.setBounds(315, 39, 17, 17);
        Combo groupsList = new Combo(deleteItemShell, SWT.NONE);
        groupsList.setBounds(230, 10, 100, 30);
        Button deleteItem = new Button(deleteItemShell, SWT.NONE);
        deleteItem.setBounds(340, 35, 80, 30);
        deleteItem.setText("Delete items");
        Label textInfo = new Label(deleteItemShell, SWT.NONE);
        textInfo.setBounds(330, 10, 100, 30);
        textInfo.setText(" Student surname:");
        Text studentName = new Text(deleteItemShell, SWT.BORDER);
        studentName.setBounds(435, 10, 100, 25);

        loadGroupList(groupsList, 4);
        deleteItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                boolean elementNotFound = false;
                rows.clear();
                String targetGroup = groupsList.getText();
                String targetName = studentName.getText();
                ArrayList<Integer> indexes = new ArrayList();
                if (useAmountOfWorkInSearch.getSelection()) {
                    if (downLimit.getText().equals("") || upLimit.getText().equals("")) {
                        WindowForm.error(deleteItemShell, "Limits missed", "You did not inputed limit");
                        return;
                    }
                    for (int i = 0; i < controller.getStudentsSize(); i++) {
                        if (controller.getStudentData(i).get(2).equals(targetName)) {
                            indexes.addAll(controller.getIndexesOfRowsWithTarget(targetName, downLimit.getText(), upLimit.getText()/*targetInput.getText()*/, 2, 15));
                            elementNotFound = false;
                            break;
                        } //that's name of student
                        else {
                            if (controller.getStudentData(i).get(4).equals(targetGroup)) {
                                indexes.addAll(controller.getIndexesOfRowsWithTarget(targetGroup, downLimit.getText(), upLimit.getText()/*targetInput.getText()*/, 4, 15));;
                                elementNotFound = false;
                                break;
                            } //that's group of student
                            else {
                                elementNotFound = true;
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < controller.getStudentsSize(); i++) {
                        if (controller.getStudentData(i).get(2).equals(targetName)) {
                            indexes.addAll(controller.getIndexesOfRowsWithTarget(targetName, 2));
                            elementNotFound = false;
                            break;
                        } //that's name of student
                        else {
                            if (controller.getStudentData(i).get(4).equals(targetGroup)) {
                                indexes.addAll(controller.getIndexesOfRowsWithTarget(targetGroup, 4));;
                                elementNotFound = false;
                                break;
                            } //that's group of student
                            else {
                                elementNotFound = true;
                            }
                        }
                    }
                }
                if (elementNotFound == true) {
                    WindowForm.error(deleteItemShell, "Not exist", "I didn't found any name or group number like that. :(");
                    return;
                }
                if (indexes.isEmpty()) {
                    WindowForm.error(deleteItemShell, "Not exist", "I didn't found any name or group number like that. :(");
                    return;
                }
                for (Integer item : indexes) {
                    //  try{
                    int result = controller.getIndexOfRowWithTarget(Integer.toString(item), 0);
                    if (result != -1) {
                        controller.removeStudent(result);
                    }
                }
                try {
                    obp.loadNewDataBaseToOBP(controller.getDataBase());
                    obp.setDefaultPageSettings(controller.getStudentsSize(), obp.getAmountOfRowsOnPage());
                } catch (IndexOutOfBoundsException ex) {
                    return;
                }
                WindowForm.error(deleteItemShell, "Successfully deleted", "On your request was deleted " + indexes.size() + " elements");
            }
        });
        deleteItemShell.pack();
        deleteItemShell.open();
    }

    public void addToTableDialog(OutputByPage obp, Shell shell) {
        ArrayList<Label> labelList = new ArrayList();
        ArrayList<Combo> comboList = new ArrayList();
        String[] numbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Shell dialog = new Shell(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
        dialog.setLayout(new GridLayout(2, false));
        Composite namePart = new Composite(dialog, SWT.NONE);
        Label labelName = new Label(namePart, SWT.NONE);
        labelName.setText("Name:");
        labelName.setBounds(0, 0, 35, 50);
        Text studentName = new Text(namePart, SWT.BORDER);
        studentName.setBounds(50, 0, 50, 25);
        Composite groupPart = new Composite(dialog, SWT.NONE);
        Label labelGroup = new Label(groupPart, SWT.NONE);
        labelGroup.setText("Surname:");
        labelGroup.setBounds(0, 0, 65, 50);
        Text studentGroup = new Text(groupPart, SWT.BORDER);
        studentGroup.setBounds(65, 0, 50, 25);
        Composite surnamePart = new Composite(dialog, SWT.NONE);
        Label labelSurname = new Label(surnamePart, SWT.NONE);
        labelSurname.setText("Patronymic:");
        labelSurname.setBounds(0, 0, 65, 50);
        Text studentSurname = new Text(surnamePart, SWT.BORDER);
        studentSurname.setBounds(65, 0, 50, 25);
        Composite patronymicPart = new Composite(dialog, SWT.NONE);
        Label labelPatronymic = new Label(patronymicPart, SWT.NONE);
        labelPatronymic.setText("Group:");
        labelPatronymic.setBounds(0, 0, 35, 50);
        Text studentPatronymic = new Text(patronymicPart, SWT.BORDER);
        studentPatronymic.setBounds(50, 0, 50, 25);
        for (int i = 0; i < 10; i++) {
            Composite semUIPart = new Composite(dialog, SWT.NONE);
            Label semLabel = new Label(semUIPart, SWT.NONE);
            Combo semCombo = new Combo(semUIPart, SWT.DROP_DOWN);
            labelList.add(semLabel);
            comboList.add(semCombo);
            semLabel.setBounds(0, 0, 40, 50);
            semCombo.setBounds(45, 0, 40, 50);
            semCombo.setItems(numbers);
            semCombo.select(0);
            semLabel.setText("Term " + Integer.toString(i + 1));
        }
        Composite UI = new Composite(dialog, SWT.BORDER);
        UI.setBounds(0, 150, 305, 30);
        UI.setLayout(new RowLayout(SWT.HORIZONTAL));
        Button inputToTable = new Button(UI, SWT.NONE);
        inputToTable.setText("Add to table");
        inputToTable.addSelectionListener(widgetSelectedAdapter(event -> {
            if (studentName.getText().equals("")) {
                WindowForm.error(dialog, "Wrong info", "One of text fields are empty");
                return;
            }
            try {
                addNewElement(studentName.getText(), studentGroup.getText(), studentSurname.getText(), studentPatronymic.getText(), comboList, dialog);
                obp.loadNewDataBaseToOBP(controller.getDataBase());
                obp.setDefaultPageSettings(controller.getStudentsSize(), obp.getAmountOfRowsOnPage());
            } catch (TransformerException | IOException | SAXException | ParserConfigurationException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                WindowForm.error(shell, "Wrong data fromat", "Term value cannot be text.");
            } catch (IndexOutOfBoundsException ex) {
                return;
            }
        }
        ));
        dialog.pack();
        dialog.open();
    }

    public void loadFindItemDialog(Shell parentShell) {
        OutputByPage obp = new OutputByPage();
        Shell findItemShell = new Shell(parentShell, SWT.APPLICATION_MODAL
                | SWT.DIALOG_TRIM);
        obp.loadTableComponent(findItemShell);
        Label highLimit = new Label(findItemShell, SWT.NONE);
        highLimit.setBounds(0, 335, 80, 25);
        highLimit.setText("Hight limit: ");
        Label lowLimit = new Label(findItemShell, SWT.NONE);
        lowLimit.setBounds(0, 365, 80, 25);
        lowLimit.setText("Down limit: ");
        Text upLimit = new Text(findItemShell, SWT.BORDER);
        upLimit.setBounds(80, 335, 50, 25);
        Text downLimit = new Text(findItemShell, SWT.BORDER);
        downLimit.setBounds(80, 365, 50, 25);
        Label comboInfo = new Label(findItemShell, SWT.NONE);
        comboInfo.setBounds(150, 335, 80, 30);
        comboInfo.setText("List of groups: ");
        Label checkInfo = new Label(findItemShell, SWT.NONE);
        checkInfo.setBounds(150, 365, 165, 30);
        checkInfo.setText("Use amount of work in search: ");
        Button useAmountOfWorkInSearch = new Button(findItemShell, SWT.CHECK);
        useAmountOfWorkInSearch.setBounds(315, 365, 17, 17);
        Combo groupsList = new Combo(findItemShell, SWT.NONE);
        groupsList.setBounds(230, 335, 100, 30);
        Label textInfo = new Label(findItemShell, SWT.NONE);
        textInfo.setBounds(335, 335, 100, 30);
        textInfo.setText("Student surname:");
        Text studentName = new Text(findItemShell, SWT.BORDER);
        studentName.setBounds(435, 335, 100, 25);
        comboInfo.setBounds(150, 335, 80, 30);
        comboInfo.setText("List of groups: ");
        Button findItems = new Button(findItemShell, SWT.NONE);
        findItems.setBounds(305, 300, 305, 30);
        findItems.setText("Find items");
        loadGroupList(groupsList, 4);
        ArrayList<ArrayList<String>> foundedStudents = new ArrayList();
        findItems.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                foundedStudents.clear();
                boolean elementNotFound = false;
                String targetGroup = groupsList.getText();
                String targetName = studentName.getText();
                ArrayList<Integer> indexes = new ArrayList();
                if (useAmountOfWorkInSearch.getSelection()) {
                    if (downLimit.getText().equals("") || upLimit.getText().equals("")) {
                        WindowForm.error(findItemShell, "Limits missed", "You did not inputed limit");
                        return;
                    }
                    for (int i = 0; i < controller.getStudentsSize(); i++) {
                        if (!studentName.getText().equals("")) {
                            targetName = studentName.getText();
                            indexes.addAll(controller.getIndexesOfRowsWithTarget(targetName, downLimit.getText(), upLimit.getText(), 2, 15));
                            elementNotFound = false;
                            break;
                        } //that's name of student
                        else {
                            if (controller.getStudentData(i).get(4).equals(targetGroup)) {
                                indexes.addAll(controller.getIndexesOfRowsWithTarget(targetGroup, downLimit.getText(), upLimit.getText(), 4, 15));;
                                elementNotFound = false;
                                break;
                            } //that's group of student
                            else {
                                elementNotFound = true;
                            }
                        }
                    }
                } else {
                    //  targetName=.getText();
                    for (int i = 0; i < controller.getStudentsSize(); i++) {
                        if (controller.getStudentData(i).get(2).equals(targetName)) {
                            indexes.addAll(controller.getIndexesOfRowsWithTarget(targetName, 2));
                            elementNotFound = false;
                            break;
                        } //that's name of student
                        else {
                            if (controller.getStudentData(i).get(4).equals(targetGroup)) {
                                indexes.addAll(controller.getIndexesOfRowsWithTarget(targetGroup, 4));;
                                elementNotFound = false;
                                break;
                            } //that's group of student
                            else {
                                elementNotFound = true;
                            }
                        }
                    }
                }
                if (elementNotFound == true) {
                    WindowForm.error(findItemShell, "Not exist", "I didn't found any name or group number like that. :(");
                    return;
                }
                if (indexes.isEmpty()) {
                    WindowForm.error(findItemShell, "Not exist", "I didn't found any name or group number like that. :(");
                    return;
                }
                for (int i = 0; i < indexes.size(); i++) {
                    foundedStudents.add(controller.getStudentData(indexes.get(i)));
                }
                obp.loadNewDataBaseToOBP(foundedStudents);
                obp.setDefaultPageSettings(foundedStudents.size(), obp.getAmountOfRowsOnPage());
            }

        });
        findItemShell.pack();
        findItemShell.open();
    }

    public void createRootElement(String fileName, Shell shellForErrorWindow) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("students");
        doc.appendChild(root);
        File file = new File(fileName);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

    public void addNewElement(String studentName, String studentSurname, String studentPatronymic, String studentGroup, ArrayList<Combo> comboList, Shell shellForErrorWindow) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        int buffer = 0;
        ArrayList<String> toDataBase = new ArrayList();
        toDataBase.add(Integer.toString(controller.getStudentsSize()));
        toDataBase.add(studentName);
        toDataBase.add(studentSurname);
        toDataBase.add(studentPatronymic);
        toDataBase.add(studentGroup);

        for (int i = 0; i < 10; i++) {
            toDataBase.add(comboList.get(i).getText());
            buffer += Integer.parseInt(comboList.get(i).getText());
        }
        toDataBase.add(Integer.toString(buffer));
        controller.addStudentData(toDataBase);
    }

    public void newOrOldFile(Shell parentShell) throws SAXException, IOException, ParserConfigurationException {
        Shell newOrOldFileDialogShell = new Shell(parentShell, SWT.APPLICATION_MODAL
                | SWT.DIALOG_TRIM);
        newOrOldFileDialogShell.setText("How to save?");
        Label dialogInfo = new Label(newOrOldFileDialogShell, SWT.CENTER);
        dialogInfo.setBounds(60, 0, 250, 30);
        dialogInfo.setText("Save in a new file or in the current?");
        Button newFile = new Button(newOrOldFileDialogShell, SWT.NONE);
        newFile.setBounds(0, 50, 100, 30);
        newFile.setText("New file");
        Button oldFile = new Button(newOrOldFileDialogShell, SWT.NONE | SWT.CENTER);
        oldFile.setBounds(300, 50, 100, 30);
        oldFile.setText("Current file");
        newFile.addSelectionListener(widgetSelectedAdapter(event -> {
            try {
                saveFileDialog(newOrOldFileDialogShell);
                controller.createFileWithRootElement(bufferName, parentShell);
                controller.writeElement(controller.getDataBase(), bufferName, parentShell);
            } catch (IOException | SAXException | ParserConfigurationException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            newOrOldFileDialogShell.close();
        }
        ));
        oldFile.addSelectionListener(widgetSelectedAdapter(event -> {
            try {
                try {
                    controller.clearFile(controller.getFileName(), true);
                    controller.writeElement(controller.getDataBase(), controller.getFileName(), parentShell);
                } catch (IllegalArgumentException ex) {
                    WindowForm.error(parentShell, "No file name or path", "You did not loaded any file.");
                    return;
                }
            } catch (TransformerException | IOException | SAXException | ParserConfigurationException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            newOrOldFileDialogShell.close();
        }
        ));
        newOrOldFileDialogShell.pack();
        newOrOldFileDialogShell.open();
    }

    public static void printToTable(TableItem nameOfItem, int index, String information) {
        nameOfItem.setText(index, information);
    }

    public void printDataBaseToTable(Table table, int amountOfRows) {
        table.removeAll();
        rows.clear();
        for (int i = 0; i < amountOfRows; i++) {
            rows.add(new TableItem(table, SWT.BORDER));
            for (int j = 0; j < controller.getStudentsSize(i); j++) {
                printToTable(rows.get(i), j, controller.getStudentData(i, j));
            }
        }
    }

    public void printDataBaseToTable(Table table) {
        table.removeAll();
        rows.clear();
        for (int i = 0; i < controller.getStudentsSize(); i++) {
            rows.add(new TableItem(table, SWT.BORDER));
            for (int j = 0; j < controller.getStudentsSize(i); j++) {
                printToTable(rows.get(i), j, controller.getStudentData(i, j));
            }
        }
    }

    public void fromTableToDataBase() {
        int tempCounter = 0;
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<String> tempArray = new ArrayList();
            tempCounter = 0;
            for (int j = 0; j < 14; j++) {
                tempArray.add(rows.get(i).getText(j));
                if (j > 2 & j < 13) {
                    tempCounter += Integer.parseInt(rows.get(i).getText(j));
                }
            }
            tempArray.add(Integer.toString(tempCounter));
            controller.addStudentData(tempArray);
        }
    }

    public void loadTable() {
        OutputByPage obp = new OutputByPage();
        Display display = new Display();
        Shell mainMenu = new Shell(display, SWT.SHELL_TRIM);
        obp.loadTableComponent(mainMenu);
        Button addItem = new Button(mainMenu, SWT.NONE);
        addItem.setBounds(306, 300, 305, 30);
        addItem.setText("Add item");
        Button findItem = new Button(mainMenu, SWT.NONE);
        findItem.setBounds(0, 330, 305, 30);
        findItem.setText("Find item");
        Button loadTable = new Button(mainMenu, SWT.NONE);
        loadTable.setBounds(0, 360, 305, 30);
        loadTable.setText("Load table");
        Button deleteItem = new Button(mainMenu, SWT.NONE);
        deleteItem.setBounds(305, 330, 305, 30);
        deleteItem.setText("Delete item");
        Button saveTable = new Button(mainMenu, SWT.NONE);
        saveTable.setBounds(305, 360, 305, 30);
        saveTable.setText("Save table to file");
        saveTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    newOrOldFile(mainMenu);
                } catch (SAXException | IOException | ParserConfigurationException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        loadTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                controller.removeDataFromBase();
                chooseFileDialog(mainMenu);
                if (controller.getFileName() == null) {
                    return;
                }
                controller.loadTableFromFile(true, controller.getFileName());
                try {
                    obp.loadNewDataBaseToOBP(controller.getDataBase());
                    obp.setDefaultPageSettings(controller.getStudentsSize(), obp.getAmountOfRowsOnPage());
                    // pageInfo.setText("Page " + obp.getCurrentPage() + "|" + obp.getAmountOfPages());
                    //urrentStudentsAmount.setText("Current amount of students: " + controller.getStudentsSize());
                } catch (IndexOutOfBoundsException ex) {
                    //pageInfo.setText("Page " + obp.getCurrentPage() + "|" + obp.getAmountOfPages());
                    //  error.setText("You have reached the end of table");
                    return;
                }
            }
        });
        deleteItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    changeTableDialog(obp, mainMenu);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        addItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                addToTableDialog(obp, mainMenu);
            }
        });
        findItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                loadFindItemDialog(mainMenu);
            }
        });
        WindowForm.WindowOpen(display, mainMenu, 1270, 500);
    }
}
