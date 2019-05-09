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
import javafx.scene.control.CheckBox;
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

    public static int arrayListCounter = 0;
public void loadGroupList(Combo combo){
                    boolean exist = false;
                    DataBase db = new DataBase();
                
                for(int i=0; i<db.getStudentsSize(); i++){
                    exist = false;
                String s = db.getStudentData(i, 2);
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
    public static void changeTableDialog(Shell shell) throws ParserConfigurationException{
        DataBase model = new DataBase();
        final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM);
        dialog.setLayout(new GridLayout(3, false));
        Composite InputUI = new Composite(dialog, SWT.BORDER);
        InputUI.setBounds(0, 0, 305, 30);
        Text textToTable = new Text(InputUI, SWT.BORDER);
        textToTable.setBounds(0, 0, 100, 20);
        InputUI.setLayout(new RowLayout(SWT.HORIZONTAL));
        Button inputToTable = new Button(InputUI, SWT.NONE);
        inputToTable.setText("Add to table");
        inputToTable.setBounds(100, 0, 100, 20);
        Combo ColumnMode = new Combo(InputUI, SWT.DROP_DOWN);
        Combo RowMode = new Combo(InputUI, SWT.DROP_DOWN);
        RowMode.setBounds(300, 0, 100, 20);
        ColumnMode.setBounds(200, 0, 100, 20);
        ColumnMode.add("ФИО");
        ColumnMode.add("Group");
        ColumnMode.add("Work");
        ColumnMode.select(0);
        RowMode.add(Integer.toString(model.getCounter()));
        RowMode.select(0);
        Button ok = new Button(dialog, SWT.PUSH);
        ok.setText("OK");
        ok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        ok.addSelectionListener(widgetSelectedAdapter(event -> {      
        }));
        inputToTable.addSelectionListener(widgetSelectedAdapter(event -> {
    
            String getInformation = textToTable.getText();
            arrayListCounter++;
        }
        ));
        //dialog.setDefaultButton (ok);
        dialog.pack();
        dialog.open();
    }

    public static void addToTableDialog(Shell shell) { 
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
           if (i==9)semLabel.setText("sem10");
           else semLabel.setText("sem "+Integer.toString(i+1));
                    }
        Composite UI = new Composite(dialog, SWT.BORDER);
        UI.setBounds(0, 150, 305, 30);
        //две колонки name и group с Text, остальное combobox. Потом это всё считать и в разослать по функциям на таблицу.
        UI.setLayout(new RowLayout(SWT.HORIZONTAL));
        Button inputToTable = new Button(UI, SWT.NONE);
        inputToTable.setText("Add to table");
        inputToTable.addSelectionListener(widgetSelectedAdapter(event -> {
                    GUI gui = new GUI();
                    WindowForm wf = new WindowForm();
                    if (studentName.getText().equals("")){ wf.Error(dialog,"Error 001","One of text fields are empty");return;}
            try {
                
                gui.addNewElement(studentName.getText(), studentGroup.getText(), comboList);
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
        | SWT.DIALOG_TRIM | SWT.ON_TOP);
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
        Text targetInput = new Text(findItemShell, SWT.BORDER);
        targetInput.setBounds(0, 300, 305, 30);
        Label comboInfo = new Label(findItemShell, SWT.NONE);
        comboInfo.setBounds(310,300,80,30);
        comboInfo.setText("List of groups: ");
        Label checkInfo = new Label(findItemShell, SWT.NONE);
        checkInfo.setBounds(310,330,155,30);
        checkInfo.setText("Use amount of work in search: ");
        Button useGroupInSearch = new Button(findItemShell,SWT.CHECK);
        useGroupInSearch.setBounds(470,329,17,17);
        Combo groupsList = new Combo(findItemShell, SWT.NONE);
        groupsList.setBounds(390, 300, 100, 30);
        Button findItem = new Button(findItemShell, SWT.NONE);
        findItem.setBounds(0, 330, 305, 30);
        findItem.setText("Find item");

        loadGroupList(groupsList);
        findItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
           boolean elementNotFound=false;
           table.removeAll();
           rows.clear();
           String target=groupsList.getText();
           ArrayList<Integer> indexes = new ArrayList();
         /*  for (int k=0; k<indexes.size(); k++)
               System.out.println("indexes["+k+"]: "+indexes.get(k));*/
           for (int i=0; i<db.getStudentsSize(); i++){
              if (db.getStudentData(i).get(1).equals(target)) { indexes.addAll(db.getIndexesOfRowsWithTarget(target,1));elementNotFound=false;break;} //that's name of student
              else {if(db.getStudentData(i).get(2).equals(target)){indexes.addAll(db.getIndexesOfRowsWithTarget(target,2));;elementNotFound=false;break;} //that's group of student
              else elementNotFound=true;
           }
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
    }

