/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Asus
 */
public class UserDialog {
    DataBase db = new DataBase();
    private String bufferName="";
    private static final String[][] FILTERS = {{"Таблица XML (*.xml)" , "*.xml"},
                                           {"Файлы Excel (*.xlsx)", "*.xlsx"},
                                           {"Файлы Adobe (*.pdf)" , "*.pdf" },
                                           {"Файлы Word (*.docx)" , "*.docx"},
                                           {"Все файлы (*.*)"     , "*.*"}};
 private void setFilters(FileDialog dialog)
{
    String[] names = new String[FILTERS.length];
    String[] exts  = new String[FILTERS.length];
    for (int i = 0; i < FILTERS.length; i++) {
        names[i] = FILTERS[i][0];
        exts [i] = FILTERS[i][1];
    }
    // Определение фильтра диалога FileDialog
    dialog.setFilterNames(names);
    dialog.setFilterExtensions(exts);
}
    public static int arrayListCounter = 0;
    public void chooseFileDialog(Shell parentShell){
        FileDialog dlg = new FileDialog(parentShell, SWT.OPEN);
setFilters(dlg);
String fname = dlg.open();
if (fname != null)
    //System.out.println ("" + fname);
    DataBase.setFileName(fname);
    }
    public void saveFileDialog(Shell parentShell){
         // Диалоговое окно сохранения файла
FileDialog dlg = new FileDialog(parentShell, SWT.SAVE);
setFilters(dlg);
String fname = dlg.open();
if (fname != null)
    bufferName=fname;
    System.out.println ("" + fname);
    }
public void loadGroupList(Combo combo, int column){
                    boolean exist = false;
                    DataBase db = new DataBase();
                
                for(int i=0; i<db.getStudentsSize(); i++){
                    exist = false;
                String s = db.getStudentData(i, column);
                if (s == null || s.length() == 0) {
                    return;
                }
               String[] items = combo.getItems();
                for (String item : items) {
                    if (item.equals(s) == true) {
                       // WindowForm.Error(shell,"Error 001", "Such element already exists");
                        exist = true; break;
                    }
                }
                if (exist == false) {
                    combo.add(s);
                }
                }
}
    public void changeTableDialog(Shell parentShell) throws ParserConfigurationException{
        DataBase db = new DataBase();
        GUI gui = new GUI();
 Shell deleteItemShell = new Shell(parentShell, SWT.APPLICATION_MODAL
        | SWT.DIALOG_TRIM | SWT.ON_TOP);
        Table table = new Table(deleteItemShell, SWT.READ_ONLY);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.grabExcessVerticalSpace = true;
        table.setLayoutData(gridData);
        table.setBounds(0, 0, 600, 300);
        table.setLinesVisible(true);
        table.computeSize(db.getCounter(), db.getCounter());
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
        ArrayList<TableItem> rows = new ArrayList();
        for (int i = 0; i < 10; i++) {
            TableColumn SocialWork = new TableColumn(table, SWT.BORDER);
            SocialWork.setText("Семестр " + (i + 1));
            SocialWork.setWidth(90);
            columns.add(SocialWork);
        }
          Label highLimit = new Label(deleteItemShell, SWT.NONE);
        highLimit.setBounds(0,300,130,25);
        highLimit.setText("Input number of student: ");
        Text indexInput = new Text(deleteItemShell, SWT.BORDER);
        indexInput.setBounds(135, 300, 70, 25);
        Button deleteItem = new Button(deleteItemShell, SWT.NONE);
        deleteItem.setBounds(206, 300, 80, 30);
        deleteItem.setText("Delete item");
        gui.printChangeHistoryToTable(table, rows);
         deleteItem.addSelectionListener(widgetSelectedAdapter(event -> {
           
            try {
                int target = Integer.parseInt(indexInput.getText());
                if (Integer.parseInt(indexInput.getText())<0) WindowForm.Error(deleteItemShell, "Error", "Impossible value of student index");
                //проверка на тип данных
                                    TableItem row = new TableItem(table, SWT.BORDER);
                                                        rows.add(row);
                                                        db.addToChangeHitory(db.getStudentData(target));
                                                        gui.printChangeHistoryToTable(table, rows);
                for (int i=0; i<14; i++){
//                GUI.printToTable(row,i,db.getStudentData(target, i));
                }
                gui.deleteElement(Integer.parseInt(indexInput.getText()), deleteItemShell);
                //gui.fromDataBaseToTable(table);
               //gui.printChangeHistory(table, rows);
            } catch (TransformerException | IOException | SAXException | ParserConfigurationException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ));
        deleteItemShell.pack();
        deleteItemShell.open();
    }

    public static void addToTableDialog(Table table,Shell shell) { 
        //DataBase model = new DataBase();
        ArrayList<Label> labelList = new ArrayList();
        ArrayList<Combo> comboList = new ArrayList();
        String[] numbers= new String[] {"0","1","2","3","4","5","6","7","8","9"};//TODO: не забудь потом проверить на конвертацию.     
         Shell dialog = new Shell(shell, SWT.APPLICATION_MODAL
        | SWT.DIALOG_TRIM | SWT.ON_TOP);
        dialog.setLayout(new GridLayout(2,false));
        Composite namePart = new Composite(dialog, SWT.NONE);
        Label labelName = new Label(namePart, SWT.NONE);
        labelName.setText("Name:");
        labelName.setBounds(0,0,35,50);
        Text studentName = new Text(namePart, SWT.BORDER);
        studentName.setBounds(40,0,50,25);
        Composite groupPart = new Composite(dialog, SWT.NONE);
        Label labelGroup = new Label(groupPart, SWT.NONE);
        labelGroup.setText("Group:");
        labelGroup.setBounds(0,0,35,50);
        Text studentGroup = new Text(groupPart, SWT.BORDER);
        studentGroup.setBounds(40,0,50,25);
        for (int i=0; i<10; i++){
            Composite semUIPart = new Composite(dialog, SWT.NONE);
            Label semLabel = new Label(semUIPart, SWT.NONE);
            Combo semCombo = new Combo(semUIPart,SWT.DROP_DOWN);
            labelList.add(semLabel);
            comboList.add(semCombo);
            semLabel.setBounds(0,0,30,50);
            semCombo.setBounds(35,0,40,50);
            semCombo.setItems(numbers);
            semCombo.select(0);
           if (i==9)semLabel.setText("Term 10");
           else semLabel.setText("Term "+Integer.toString(i+1));
                    }
        Composite UI = new Composite(dialog, SWT.BORDER);
        UI.setBounds(0, 150, 305, 30);
        //две колонки name и group с Text, остальное combobox. Потом это всё считать и в разослать по функциям на таблицу.
        UI.setLayout(new RowLayout(SWT.HORIZONTAL));
        Button inputToTable = new Button(UI, SWT.NONE);
        inputToTable.setText("Add to table");
        inputToTable.addSelectionListener(widgetSelectedAdapter(event -> {
                    GUI gui = new GUI();
                    if (studentName.getText().equals("")){ WindowForm.Error(dialog,"Wrong info","One of text fields are empty");return;}
            try {         
                gui.addNewElement(table,studentName.getText(), studentGroup.getText(), comboList, dialog);
            } catch (TransformerException | IOException | SAXException | ParserConfigurationException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ));
        dialog.pack();
        dialog.open();
    }
    public void loadFindItemDialog(Shell parentShell){
            DataBase db = new DataBase();
        //Display display = new Display();
       // Shell shell = new Shell(display, SWT.SHELL_TRIM);
     //   GridLayout gridLayout = new GridLayout();
        Shell findItemShell = new Shell(parentShell, SWT.APPLICATION_MODAL
        | SWT.DIALOG_TRIM);
        Table table = new Table(findItemShell, SWT.READ_ONLY);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.grabExcessVerticalSpace = true;
        table.setLayoutData(gridData);
        table.setBounds(0, 0, 600, 300);
        table.setLinesVisible(true);
        table.computeSize(db.getCounter(), db.getCounter());
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
        ArrayList<TableItem> rows = new ArrayList();
        for (int i = 0; i < 10; i++) {
            TableColumn SocialWork = new TableColumn(table, SWT.BORDER);
            SocialWork.setText("Семестр " + (i + 1));
            SocialWork.setWidth(90);
            columns.add(SocialWork);
        }
          Label highLimit = new Label(findItemShell, SWT.NONE);
        highLimit.setBounds(0,300,80,25);
        highLimit.setText("Hight limit: ");
         Label lowLimit = new Label(findItemShell, SWT.NONE);
        lowLimit.setBounds(0,330,80,25);
        lowLimit.setText("Down limit: ");
        Text upLimit = new Text(findItemShell, SWT.BORDER);
        upLimit.setBounds(80, 300, 50, 25);
        Text downLimit = new Text(findItemShell, SWT.BORDER);
        downLimit.setBounds(80, 330, 50, 25);
        Label comboInfo = new Label(findItemShell, SWT.NONE);
        comboInfo.setBounds(150,300,80,30);
        comboInfo.setText("List of groups: ");
        Label checkInfo = new Label(findItemShell, SWT.NONE);
        checkInfo.setBounds(150,330,165,30);
        checkInfo.setText("Use amount of work in search: ");
        Button useAmountOfWorkInSearch = new Button(findItemShell,SWT.CHECK);
        useAmountOfWorkInSearch.setBounds(315,329,17,17);
        Combo groupsList = new Combo(findItemShell, SWT.NONE);
        groupsList.setBounds(230, 300, 100, 30);
        Button findItem = new Button(findItemShell, SWT.NONE);
        findItem.setBounds(340, 300, 80, 30);
        findItem.setText("Find items");

        loadGroupList(groupsList, 2);
        findItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
           boolean elementNotFound=false;
           table.removeAll();
           rows.clear();
           String target=groupsList.getText();
           ArrayList<Integer> indexes = new ArrayList();
         if (useAmountOfWorkInSearch.getSelection()){ 
             if (downLimit.getText().equals("") || upLimit.getText().equals("")){ WindowForm.Error(findItemShell,"Limits missed", "You did not inputed limit");return;}
             for (int i=0; i<db.getStudentsSize(); i++){
              if (db.getStudentData(i).get(1).equals(target)) { indexes.addAll(db.getIndexesOfRowsWithTarget(target,downLimit.getText(),upLimit.getText()/*targetInput.getText()*/,1,13));elementNotFound=false;break;} //that's name of student
              else {if(db.getStudentData(i).get(2).equals(target)){indexes.addAll(db.getIndexesOfRowsWithTarget(target,downLimit.getText(),upLimit.getText()/*targetInput.getText()*/,2,13));;elementNotFound=false;break;} //that's group of student
              else elementNotFound=true;
           }}}
         else {
           for (int i=0; i<db.getStudentsSize(); i++){
              if (db.getStudentData(i).get(1).equals(target)) { indexes.addAll(db.getIndexesOfRowsWithTarget(target,1));elementNotFound=false;break;} //that's name of student
              else {if(db.getStudentData(i).get(2).equals(target)){indexes.addAll(db.getIndexesOfRowsWithTarget(target,2));;elementNotFound=false;break;} //that's group of student
              else elementNotFound=true;
           }}
           }
       if (elementNotFound==true){WindowForm.Error(findItemShell, "Not exist", "I didn't found any name or group number like that. :("); return;}
           if (indexes.isEmpty()) {WindowForm.Error(findItemShell, "Not exist", "I didn't found any name or group number like that. :("); return;}
           for (int i=0; i<indexes.size(); i++){
                       TableItem item = new TableItem(table, SWT.BORDER);
                                  rows.add(item);
          GUI.printToTable(rows.get(i),0,db.getStudentData(indexes.get(i), 0));
            GUI.printToTable(rows.get(i),1,db.getStudentData(indexes.get(i), 1));
              GUI.printToTable(rows.get(i),2,db.getStudentData(indexes.get(i), 2));
           for(int j=0; j<10; j++)
               GUI.printToTable(rows.get(i),j+3,db.getStudentData(indexes.get(i),j+3));
           }
          // db.outputStudents();
                }       
        });
        findItemShell.pack();
        findItemShell.open();
    }
    public void settingsDialog(Shell parentShell){
        Shell settingsDialogShell = new Shell (parentShell,SWT.APPLICATION_MODAL
        | SWT.DIALOG_TRIM);
        Button openFile = new Button(settingsDialogShell, SWT.NONE);
        openFile.setBounds(0,0,80,30);
        openFile.setText("Open file");
        openFile.addSelectionListener(widgetSelectedAdapter(event -> {
                GUI gui = new GUI();
                try {
                chooseFileDialog(settingsDialogShell);
            
                //gui.loadFile();
            } catch (IllegalArgumentException ex) {
                //Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        ));
        Button saveFile = new Button(settingsDialogShell, SWT.NONE);
        saveFile.setBounds(0,35,80,30);
        saveFile.setText("Save table");
        saveFile.addSelectionListener(widgetSelectedAdapter(event -> {
                GUI gui = new GUI();
            try {
                newOrOldFile(settingsDialogShell,bufferName);
            } catch (SAXException | IOException | ParserConfigurationException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ));
        Label textInfo = new Label(settingsDialogShell, SWT.NONE);
        textInfo.setBounds(90,0,185,25);
        textInfo.setText("Input number of rows on the page: ");
        Text inputRowsOnPage = new Text(settingsDialogShell, SWT.BORDER);
        inputRowsOnPage.setBounds(85,25,150,30);
        Button setNumberOfRowsOnPage = new Button(settingsDialogShell, SWT.NONE);
         setNumberOfRowsOnPage.setBounds(130,60,80,30);
       setNumberOfRowsOnPage.setText("Apply");
        setNumberOfRowsOnPage.addSelectionListener(widgetSelectedAdapter(event -> {
               GUI gui = new GUI();
               //int numberOfRowsOnPage=Integer.parseInt(inputRowsOnPage.getText());
               gui.setNumberOfRowsOnPage(Integer.parseInt(inputRowsOnPage.getText()));
               gui.getAmountOfPages(db.getStudentsSize(),Integer.parseInt(inputRowsOnPage.getText()));
               settingsDialogShell.close();
        }
        ));
       settingsDialogShell.pack();
              settingsDialogShell.open();

    }
    public void newOrOldFile(Shell parentShell,String fileName) throws SAXException, IOException, ParserConfigurationException{
     /*   DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder  = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);*/
        Shell newOrOldFileDialogShell = new Shell(parentShell,SWT.APPLICATION_MODAL
        | SWT.DIALOG_TRIM);
        newOrOldFileDialogShell.setText("How to save?");
        Label dialogInfo = new Label(newOrOldFileDialogShell,SWT.CENTER);
        dialogInfo.setBounds(0,0,100,30);
        dialogInfo.setText("Save in a new file or in the current?");
        Button newFile = new Button(newOrOldFileDialogShell, SWT.NONE);
        newFile.setBounds(0,100,150,30);
        newFile.setText("New file");
        Button oldFile = new Button(newOrOldFileDialogShell, SWT.NONE | SWT.CENTER);
        oldFile.setBounds(300,100,100,30);
        oldFile.setText("Current file");
        newFile.addSelectionListener(widgetSelectedAdapter(event -> {
               GUI gui = new GUI();
            try {
                saveFileDialog(newOrOldFileDialogShell);
                gui.createFileWithRootElement(bufferName, parentShell);
                //for (int i=0; i<db.getStudentsSize(); i++)
                 //   gui.writeElement(db.getDataBase(), bufferName, parentShell);
              // gui.saveTableToFile( bufferName, parentShell);
            }catch (TransformerException ex) {
               return;
            }
            catch (IOException | SAXException | ParserConfigurationException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            newOrOldFileDialogShell.close();
        }
        ));
         oldFile.addSelectionListener(widgetSelectedAdapter(event -> {
               GUI gui = new GUI();
            try {
               gui.clearFile(DataBase.getFileName(), true);
                    // gui.createFileWithRootElement(DataBase.getFileName(), parentShell);
              //  for (int i=0; i<db.getStudentsSize(); i++)
                    gui.writeElement(db.getDataBase(), DataBase.getFileName(), parentShell);
            } catch (TransformerException | IOException | SAXException | ParserConfigurationException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            newOrOldFileDialogShell.close();
        }
        ));
        newOrOldFileDialogShell.pack();
        newOrOldFileDialogShell.open();
    }
    }

