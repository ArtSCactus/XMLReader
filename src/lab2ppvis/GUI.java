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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Asus
 */
public class GUI {
   public void addNewElement(Document document,/*,NodeList nodeList,*/ Node nodeName, String studentName, String hisGroup) throws TransformerException{
           DataBase dataBase = new DataBase();
       Element newStudent = document.createElement("student");
     //  dataBase.setCounter(1);
            nodeName.appendChild(newStudent);
             NamedNodeMap attributes = nodeName.getAttributes();
             newStudent.setAttribute("aname", studentName);
                          newStudent.setAttribute("bgroup", hisGroup);
                          for (int i=0; i<10; i++)
                              newStudent.setAttribute("sem"+Integer.toString(i),"0");
                          //Запишем содержимое в xml файл
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
            
            NodeList nodeLst=doc.getElementsByTagName("student");
            if (debugMode==true)System.out.println("Students:");
            table.removeAll();
            for(int je=0;je<nodeLst.getLength();je++)
            {
                int buffer=0;
                Node fstNode=nodeLst.item(je);
                if(fstNode.getNodeType()==Node.ELEMENT_NODE)
                {
                   NamedNodeMap attributes = fstNode.getAttributes();
                   TableItem item = new TableItem(table, SWT.BORDER);

                // Вывод информации про все атрибуты
                for (int k = 0; k < attributes.getLength(); k++){
                    
                  if (debugMode==true)  System.out.println("Attribute name: " + attributes.item(k).getNodeName() + ", value: " + attributes.item(k).getNodeValue());   
                printToTable(item, k+1, attributes.item(k).getNodeValue());
                printToTable(item, 0, Integer.toString(je+1));
               if(k>=2) buffer+=Integer.parseInt(attributes.item(k).getNodeValue());      
                }
                }
                dataBase.addToAmountMassive(buffer);
            }
            
                    if (debugMode==true)dataBase.outputMassive();
        }
        catch(IOException | ParserConfigurationException | DOMException | SAXException ei){}
}
    public void loadTable() {
        DataBase db = new DataBase();
        Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        GridLayout gridLayout = new GridLayout();
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
                /*                try{
                DataBase model = new DataBase();
                
                
                DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
                
                //  File fXml=new File("data/book.xml");
                DocumentBuilder  db = dbf.newDocumentBuilder();
                Document doc=db.parse("data/iblog.xml");
                Node students=doc.getElementsByTagName("students").item(0);
                
                try {
                addNewElement(doc, students, "Вася пупкин", "11111");
                } catch (TransformerException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                }
                //doc.getdocumentElement().appendChild();
                catch(IOException | ParserConfigurationException | DOMException | SAXException ei){}
                loadTableFromFile(true,"data/iblog.xml",table);*/
                   UserDialog.addToTableDialog(MainMenu);
            }
        });
        WindowForm.WindowOpen(display, MainMenu, 630, 500);
    }
    //java.text 
    //java.time
}
