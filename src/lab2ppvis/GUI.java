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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Asus
 */
public class GUI {
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
           db.addToAmountMassive(buffer);
             TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("data/iblog.xml"));
            transformer.transform(domSource, streamResult);
   }

public static void printToTable(TableItem nameOfItem,int index, String information){
    nameOfItem.setText(index,information);
}
public void loadTableFromFile(boolean debugMode,String fileLocation,Table table){
 DataBase dataBase = new DataBase();
 File fXml=new File(fileLocation);   
        try
        {
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=dbf.newDocumentBuilder();
            Document doc=db.parse(fXml);
            
            doc.getDocumentElement().normalize();
            if (debugMode==true)System.out.println("Root element ["+doc.getDocumentElement().getNodeName()+"]");
            
            Node root = doc.getFirstChild();
            NodeList nodeList = doc.getElementsByTagName(doc.getDocumentElement().getChildNodes().item(1).getNodeName());
            NodeList nodeLst=doc.getElementsByTagName("sem");
            table.removeAll();
              for(int tmp = 0; tmp < nodeList.getLength(); tmp++)
            {
                ArrayList<String> studentData = new ArrayList();
                Node student = doc.getElementsByTagName("student").item(tmp);
                  TableItem item = new TableItem(table, SWT.BORDER);
                printToTable(item, 0, Integer.toString(tmp));
                printToTable(item,1,student.getAttributes().item(1).getNodeValue());
                printToTable(item,2,student.getAttributes().item(0).getNodeValue());
                studentData.add(student.getAttributes().item(0).getNodeValue());// group
                studentData.add(student.getAttributes().item(1).getNodeValue());// name
                System.out.println("student: "+tmp+" group: "+student.getAttributes().item(0).getNodeValue()+" name: "+student.getAttributes().item(1).getNodeValue());
                Node node = nodeList.item(tmp);
                int buffer=0;
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element)node;
                    for (int i=0; i<10; i++){
                        studentData.add(element.getElementsByTagName("sem").item(i).getChildNodes().item(0).getNodeValue());
                        printToTable(item, i+3,element.getElementsByTagName("sem").item(i).getChildNodes().item(0).getNodeValue());
                        buffer+=Integer.parseInt(element.getElementsByTagName("sem").item(i).getChildNodes().item(0).getNodeValue());
                    System.out.println("sem: " + element.getElementsByTagName("sem").item(i).getChildNodes().item(0).getNodeValue());}
                   // System.out.println("Language: " + element.getElementsByTagName("sem").item(0).getChildNodes().item(0).getNodeValue());
                }
                dataBase.addToAmountMassive(buffer);
                studentData.add(Integer.toString(buffer));
                dataBase.addStudentData(studentData);
                dataBase.outputStudents();
            }
           
            
                    if (debugMode==true)dataBase.outputMassive();
        }
        catch(IOException | ParserConfigurationException | DOMException | SAXException ei){}
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
        updateTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                 loadTableFromFile(true,"data/iblog.xml",table);
            }
        });
        addItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                   UserDialog.addToTableDialog(MainMenu);
            }
        });
        WindowForm.WindowOpen(display, MainMenu, 630, 500);
    }
}
