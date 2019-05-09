/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Asus
 */
public class GUI {
   //private 
    public ArrayList<TableItem> rows=new ArrayList();
    DataBase database = new DataBase();
    public static void printToTable(TableItem nameOfItem,int index, String information){
    nameOfItem.setText(index,information);
}
 public void  fromDataBaseToTable(Table table){
    //table.clearAll();
    table.removeAll();
       rows.clear();
        for (int i=0; i<database.getStudentsSize(); i++){
           // TableItem row = new TableItem(table, SWT.BORDER);
        rows.add(new TableItem(table, SWT.BORDER));
        for (int j=0; j<database.getStudentsSize(i); j++)
            printToTable(rows.get(i),j,database.getStudentData(i, j));
            }
    }
    public void fromTableToDataBase(){
        int tempCounter=0;
        for (int i=0; i<rows.size(); i++){
       ArrayList<String> tempArray = new ArrayList();
       tempCounter=0;
        for (int j=0; j<14; j++){
           tempArray.add(rows.get(i).getText(j));
            if (j>2 & j<13) tempCounter+=Integer.parseInt(rows.get(i).getText(j));}
        tempArray.add(Integer.toString(tempCounter));
             DataBase.addStudentData(tempArray);
        }
    }
   
   public void addNewElement(String studentName, String studentGroup, ArrayList<Combo> comboList) throws TransformerException, IOException, SAXException, ParserConfigurationException{
       DataBase db = new DataBase();    
       int buffer = 0;
       DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("data/iblog.xml");

            Node root = document.getFirstChild();
   
           Element student = document.createElement("student");
           root.appendChild(student);
           student.setAttribute("name", studentName);
           student.setAttribute("group", studentGroup);
           
           for (int i=0; i<10; i++){
               Element sem = document.createElement("sem");
            sem.appendChild(document.createTextNode(comboList.get(i).getText()));
            sem.setAttribute("id", Integer.toString(i));
             student.appendChild(sem);
             buffer +=Integer.parseInt(comboList.get(i).getText());
           }
          // db.addToAmountMassive(buffer);
             TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("data/iblog.xml"));
            transformer.transform(domSource, streamResult);
   }
public void loadTableFromFile(boolean debugMode,String fileLocation,Table table){
 DataBase dataBase = new DataBase();  
   try
        {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
 
            DefaultHandler defaultHandler = new DefaultHandler()
            {   
                DataBase db = new DataBase();
                boolean studentReadingStarted =false;
                boolean bStudent = false;
                boolean bSem = false;
                int tempAttrStorage=0;
                int buffer=0;
                int columnCounter=0;
                int rowCounter= 0;
                ArrayList<String> newStudentData = new ArrayList();
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
                    System.out.println("Start element: " + qName);
                    if(qName.equalsIgnoreCase("student")){
                        bStudent = true;
                        studentReadingStarted = true;
                        String bufferName="";
                        String bufferGroup="";
                       
                        newStudentData.add(Integer.toString(rowCounter));
                       //  TableItem item = new TableItem(table, SWT.BORDER);
                     //   rows.add(item);
                                              int length = attributes.getLength();
  for (int i=0; i<length; i++) {
String name = attributes.getQName(i);
System.out.println("Attr name:" + name);
String value = attributes.getValue(i);
if (i==0)bufferGroup=attributes.getValue(i);
else bufferName=attributes.getValue(i);
System.out.println("Attr value:" + value+" current i="+i); 
//newStudentData.add(value);
if (name.equals("group")){
//printToTable(rows.get(rowCounter), 2, value);
//newStudentData.add(value);
columnCounter=i;
}
if (name.equals("name")){
//printToTable(rows.get(rowCounter), 1, value);
//newStudentData.add(value);
columnCounter=i;
}
  }
  newStudentData.add(bufferName);
newStudentData.add(bufferGroup);  
                    }
                    if(qName.equalsIgnoreCase("Sem")){
                        bSem = true;
                        String name = attributes.getQName(0);
System.out.println("Attr name:" + name);
String value = attributes.getValue(0);
System.out.println("Attr value:" + value); 
tempAttrStorage=Integer.parseInt(value);
                    }
                  // get the number of attributes in the list    
    }
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException{
                    System.out.println("End element: " + qName);
                    if (qName.equals("student")) {
                    //printToTable(rows.get(rowCounter), 0, Integer.toString(rowCounter));
                    //newStudentData.add(Integer.toString(rowCounter));
                    ArrayList<String> buffer = new ArrayList();
                                    int termWorkCounter=0;
                    for (int i=0; i<newStudentData.size(); i++)
                        buffer.add(newStudentData.get(i));
                    for (int i=0; i<10; i++)
                        termWorkCounter+=Integer.parseInt(buffer.get(i+3));
                    buffer.add(Integer.toString(termWorkCounter));
                     newStudentData.clear();
                    db.addStudentData(buffer);
                    rowCounter++; 
                    columnCounter=0;
                    }
                }
 
                @Override
                public void characters(char ch[], int start, int length) throws SAXException{
                    if(bStudent){
                        System.out.println("Student: " + new String(ch, start, length));
                        bStudent = false;
                        //newStudentData.add(Integer.toString(buffer));
                       // buffer=0;
                    }
                    if(bSem){
                        System.out.println("Sem value: " + new String(ch, start, length));
                        bSem = false;
                        newStudentData.add(new String(ch, start, length));
                      //  printToTable(rows.get(rowCounter), columnCounter+2,new String(ch, start, length));
                       //printToTable(rows.get(rowCounter), tempAttrStorage+3,new String(ch, start, length));
                        columnCounter++;
                    }
                }
            };
            saxParser.parse("data/iblog.xml", defaultHandler);
        }
        catch (IOException | ParserConfigurationException | SAXException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
}
    public void loadTable() {
        DataBase db = new DataBase();
        Display display = new Display();
       // Shell shell = new Shell(display, SWT.SHELL_TRIM);
     //   GridLayout gridLayout = new GridLayout();
        Shell MainMenu = new Shell(display, SWT.SHELL_TRIM);
        Table table = new Table(MainMenu, SWT.READ_ONLY);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.grabExcessVerticalSpace = true;
        table.setLayoutData(gridData);
        table.setBounds(0, 0, 1300, 300);
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
        ArrayList<TableColumn> columns = new ArrayList();// Columns massive
        for (int i = 0; i < 10; i++) {
            TableColumn SocialWork = new TableColumn(table, SWT.BORDER);
            SocialWork.setText("Семестр " + (i + 1));
            SocialWork.setWidth(90);
            columns.add(SocialWork);
        }
        Button updateTable = new Button(MainMenu, SWT.NONE);
        updateTable.setBounds(0, 300, 305, 30);
        updateTable.setText("Update table");

        Button addItem = new Button(MainMenu, SWT.NONE);
        addItem.setBounds(306, 300, 305, 30);
        addItem.setText("Add item");
        Button findItem = new Button(MainMenu, SWT.NONE);
        findItem.setBounds(0, 330, 305, 30);
        findItem.setText("Find item");
       loadTableFromFile(true,"data/iblog.xml",table);
                 findItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                UserDialog ud = new UserDialog();
                 ud.loadFindItemDialog(MainMenu);
                 //fromTableToDataBase();
                 fromDataBaseToTable(table);
                 db.outputStudents();
            }
        });

        //  fromTableToDataBase();
        updateTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                // loadTableFromFile(true,"data/iblog.xml",table);
                fromDataBaseToTable(table);
            }
        });
        addItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                   UserDialog.addToTableDialog(MainMenu);
            }
        });
        WindowForm.WindowOpen(display, MainMenu, 1270, 500);
    }
    //public void 
}
