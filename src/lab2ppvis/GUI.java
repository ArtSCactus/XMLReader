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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Asus
 */
public class GUI {
    //private 

    public ArrayList<TableItem> rows = new ArrayList();
    //public ArrayList<Integer> pages = new ArrayList()
    private static int numberOfPages=0;
    private static int currentPageStart=0;
    private static int numberOfRowsOnPage = 10;
    private static int currentPageEnd = numberOfRowsOnPage;
    private static int currentPage = 0;
    Document documentTransfer;
    DataBase database = new DataBase();

    public Document getDocument() {
        return documentTransfer;
    }
    public static void setNumberOfRowsOnPage(int newNumberOfRowsOnPage) {
        numberOfRowsOnPage = newNumberOfRowsOnPage;
        currentPageStart = 0;
        currentPageEnd=newNumberOfRowsOnPage;
    }

    public static void printToTable(TableItem nameOfItem, int index, String information) {
        nameOfItem.setText(index, information);
    }

    public static void getAmountOfPages(int amountOfStudents, int rowsOnPage) {
        int rowsCounter = 0;
        for (int i = 0; i < amountOfStudents; i++) {
            if (rowsCounter == rowsOnPage - 1) {
                numberOfPages++;
                rowsCounter = 0;
            } else {
                rowsCounter++;
            }
        }
        if (rowsCounter > 0) {
            numberOfPages++;
        }
        System.out.println("Inputed number of rows: " + numberOfRowsOnPage);
        System.out.println("Amount of pages: " + numberOfPages);
        System.out.println("Amount of students: " + amountOfStudents);
    }

    public void printChangeHistoryToTable(Table table, ArrayList<TableItem> arrayOfRows) {
        //table.clearAll();
        DataBase db = new DataBase();
        table.removeAll();
        arrayOfRows.clear();
        for (int i = 0; i < db.getChangeHistorySize(); i++) {
            ArrayList<String> toTable = new ArrayList();
            toTable.addAll(db.getChangeHistory(i));
            arrayOfRows.add(new TableItem(table, SWT.BORDER));
            for (int j = 0; j < db.getChangeHistorySize(i); j++) {
                printToTable(arrayOfRows.get(i), j, toTable.get(j));
            }
        }
    }

    public void printDataBaseToTable(Table table, int start, int end) {
        table.removeAll();
        rows.clear();
        int rowsCounter = 0;
        
        for (int i = start; i < end + 1; i++) {
            rows.add(new TableItem(table, SWT.BORDER));
            try{
            for (int j = 0; j < database.getStudentsSize(i); j++) {
                printToTable(rows.get(rowsCounter), j, database.getStudentData(i, j));
            }
            } catch(IndexOutOfBoundsException ex){
                end =database.getStudentsSize();
             for (int h = start; h < end + 1; h++) { rows.add(new TableItem(table, SWT.BORDER));
            for (int j = 0; j < database.getStudentsSize(h); j++) {
                printToTable(rows.get(rowsCounter), j, database.getStudentData(i, j));
            }}
            }
            rowsCounter++;
        }
    }

    public void printDataBaseToTable(Table table, int amountOfRows) {
        //table.clearAll();
        //на for int i=start; i<stop; i++
        table.removeAll();
        rows.clear();
        for (int i = 0; i < amountOfRows; i++) {
            // TableItem row = new TableItem(table, SWT.BORDER);
            rows.add(new TableItem(table, SWT.BORDER));
            for (int j = 0; j < database.getStudentsSize(i); j++) {
                printToTable(rows.get(i), j, database.getStudentData(i, j));
            }
        }
    }

    public void printDataBaseToTable(Table table) {
        //table.clearAll();
        //на for int i=start; i<stop; i++
        table.removeAll();
        rows.clear();
        for (int i = 0; i < database.getStudentsSize(); i++) {
            // TableItem row = new TableItem(table, SWT.BORDER);
            rows.add(new TableItem(table, SWT.BORDER));
            for (int j = 0; j < database.getStudentsSize(i); j++) {
                printToTable(rows.get(i), j, database.getStudentData(i, j));
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
            DataBase.addStudentData(tempArray);
        }
    }

    /*  public void saveTableToFile(Document document, String fileNameOrPath,Shell shellForErrorWindow) throws TransformerException{
       if (fileNameOrPath==null) {WindowForm.Error(shellForErrorWindow, "No path", "You did not inputed any file name or path."); return;}
        File file = new File(fileNameOrPath);
Transformer transformer = TransformerFactory.newInstance().newTransformer();
transformer.setOutputProperty(OutputKeys.INDENT, "yes");
transformer.transform(new DOMSource(document), new StreamResult(file));
   }*/
    public void createFileWithRootElement(String fileName, Shell shellForErrorWindow) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("students");
        doc.appendChild(root);
        File file = new File(fileName);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(file));
        // saveTableToFile(doc,fileName, shellForErrorWindow);
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
        // saveTableToFile(doc,fileName, shellForErrorWindow);
    }

    public void writeElement(ArrayList<ArrayList<String>> dataBase, String fileName, Shell shellForErrorWindow) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(DataBase.getFileName());
        // Document doc = documentBuilderFactory.newDocumentBuilder().newDocument();
        // Element root = doc.createElement("students");
        //doc.appendChild(root);
        //File file = new File(fileName);
        //Корневой элемент
        Node root = document.getFirstChild();
        for (int i = 0; i < database.getStudentsSize(); i++) {
            // for (int j=0; j<database.getStudentsSize(i); j++){
            Element student = document.createElement("student");
            root.appendChild(student);
            student.setAttribute("name", dataBase.get(i).get(1));
            student.setAttribute("group", dataBase.get(i).get(2));
            for (int k = 0; k < 10; k++) {
                Element sem = document.createElement("sem");
                sem.appendChild(document.createTextNode(dataBase.get(i).get(k + 3)));
                sem.setAttribute("id", Integer.toString(k));
                student.appendChild(sem);
            }
        }
        //Теперь запишем контент в XML файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName));

        transformer.transform(domSource, streamResult);

        System.out.println("Файл сохранен!");
    }

    public void addNewElement(Table table, String studentName, String studentGroup, ArrayList<Combo> comboList, Shell shellForErrorWindow) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DataBase db = new DataBase();
        int buffer = 0;
        /*DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder  = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(DataBase.getFileName());*/

        ArrayList<String> toDataBase = new ArrayList();
        toDataBase.add(Integer.toString(db.getStudentsSize()));
        toDataBase.add(studentName);
        toDataBase.add(studentGroup);

        for (int i = 0; i < 10; i++) {
            toDataBase.add(comboList.get(i).getText());
            buffer += Integer.parseInt(comboList.get(i).getText());
        }
        toDataBase.add(Integer.toString(buffer));
        DataBase.addStudentData(toDataBase);
        TableItem item = new TableItem(table, SWT.BORDER);
        rows.add(item);
        for (int i = 0; i < 13; i++) {
            printToTable(item, i, toDataBase.get(i));
        }
    }

    public void deleteElement(int indexOfStudent, Shell parentShell) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DataBase db = new DataBase();
        // db.addToChangeHitory(db.getStudentData(indexOfStudent));
        db.removeStudent(indexOfStudent);
        //  saveTableToFile(document, "data/iblog.xml", parentShell);
    }

    public void clearFile(String fileName, boolean onlyContentOfStudents) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DataBase db = new DataBase();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileName);

        Node root = document.getElementsByTagName("students").item(0);
        // onlyContentOfStudents=true? document.removeChild(root) : root.removeChild(root.getFirstChild());
        if (onlyContentOfStudents == true) {
            NodeList nodelist = root.getChildNodes();
            for (int i = 0; i < nodelist.getLength(); i++) {
                root.removeChild(nodelist.item(i));
            }
        } else {
            document.removeChild(root);
        }
        // db.addToChangeHitory(db.getStudentData(indexOfStudent));
        // db.removeStudent(indexOfStudent);
        //  saveTableToFile(document, "data/iblog.xml", parentShell);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName));

        transformer.transform(domSource, streamResult);
    }

    public void loadTableFromFile(boolean debugMode, String fileLocation, Table table) {
        try {
            DataBase db = new DataBase();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //db.removeDataFromBase();
            DefaultHandler defaultHandler = new DefaultHandler() {
                DataBase db = new DataBase();
                boolean studentReadingStarted = false;
                boolean bStudent = false;
                boolean bSem = false;
                int tempAttrStorage = 0;
                int buffer = 0;
                int columnCounter = 0;
                int rowCounter = 0;
                ArrayList<String> newStudentData = new ArrayList();

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    System.out.println("Start element: " + qName);
                    if (qName.equalsIgnoreCase("student")) {
                        bStudent = true;
                        studentReadingStarted = true;
                        String bufferName = "";
                        String bufferGroup = "";

                        newStudentData.add(Integer.toString(rowCounter));
                        //  TableItem item = new TableItem(table, SWT.BORDER);
                        //   rows.add(item);
                        int length = attributes.getLength();
                        for (int i = 0; i < length; i++) {
                            String name = attributes.getQName(i);
                            System.out.println("Attr name:" + name);
                            String value = attributes.getValue(i);
                            if (i == 0) {
                                bufferGroup = attributes.getValue(i);
                            } else {
                                bufferName = attributes.getValue(i);
                            }
                            System.out.println("Attr value:" + value + " current i=" + i);
//newStudentData.add(value);
                            if (name.equals("group")) {
//printToTable(rows.get(rowCounter), 2, value);
//newStudentData.add(value);
                                columnCounter = i;
                            }
                            if (name.equals("name")) {
//printToTable(rows.get(rowCounter), 1, value);
//newStudentData.add(value);
                                columnCounter = i;
                            }
                        }
                        newStudentData.add(bufferName);
                        newStudentData.add(bufferGroup);
                    }
                    if (qName.equalsIgnoreCase("Sem")) {
                        bSem = true;
                        String name = attributes.getQName(0);
                        System.out.println("Attr name:" + name);
                        String value = attributes.getValue(0);
                        System.out.println("Attr value:" + value);
                        tempAttrStorage = Integer.parseInt(value);
                    }
                    // get the number of attributes in the list    
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    System.out.println("End element: " + qName);
                    if (qName.equals("student")) {
                        //printToTable(rows.get(rowCounter), 0, Integer.toString(rowCounter));
                        //newStudentData.add(Integer.toString(rowCounter));
                        ArrayList<String> buffer = new ArrayList();
                        int termWorkCounter = 0;
                        for (int i = 0; i < newStudentData.size(); i++) {
                            buffer.add(newStudentData.get(i));
                        }
                        for (int i = 0; i < 10; i++) {
                            termWorkCounter += Integer.parseInt(buffer.get(i + 3));
                        }
                        buffer.add(Integer.toString(termWorkCounter));
                        newStudentData.clear();
                        db.addStudentData(buffer);
                        rowCounter++;
                        columnCounter = 0;
                    }
                }

                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if (bStudent) {
                        System.out.println("Student: " + new String(ch, start, length));
                        bStudent = false;
                        //newStudentData.add(Integer.toString(buffer));
                        // buffer=0;
                    }
                    if (bSem) {
                        System.out.println("Sem value: " + new String(ch, start, length));
                        bSem = false;
                        newStudentData.add(new String(ch, start, length));
                        //  printToTable(rows.get(rowCounter), columnCounter+2,new String(ch, start, length));
                        //printToTable(rows.get(rowCounter), tempAttrStorage+3,new String(ch, start, length));
                        columnCounter++;
                    }
                }
            };
            saxParser.parse(DataBase.getFileName(), defaultHandler);
        } catch (IOException | ParserConfigurationException | SAXException | IllegalArgumentException ex) {
            WindowForm.Error(new Shell(), "File open error", "You did not choosed the file.");
            //System.out.println(ex.getLocalizedMessage());
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
        Button loadTable = new Button(MainMenu, SWT.NONE);
        loadTable.setBounds(0, 360, 305, 30);
        loadTable.setText("Load table");
        Button deleteItem = new Button(MainMenu, SWT.NONE);
        deleteItem.setBounds(305, 330, 305, 30);
        deleteItem.setText("Delete item");
        Button back = new Button(MainMenu, SWT.NONE);
        back.setBounds(615, 330, 30, 30);
        back.setText("<");
        Label pageInfo = new Label(MainMenu, SWT.NONE);
        pageInfo.setText("Page " + currentPage + "|" + numberOfPages);
        pageInfo.setBounds(650, 330, 50, 30);
        Button forward = new Button(MainMenu, SWT.NONE);
        forward.setBounds(700, 330, 30, 30);
        forward.setText(">");
        Button settings = new Button(MainMenu, SWT.NONE);
        settings.setBounds(615, 300, 100, 30);
        settings.setText("Settings");
         Label error = new Label(MainMenu, SWT.NONE);
         error.setBounds(600, 360, 200,30);
        // loadTableFromFile(true,db.getFileName(),table);
        //  printDataBaseToTable(table);
        //getAmountOfPages(db.getStudentsSize(), 10);
        /*  Button devMode = new Button (MainMenu, SWT.NONE);
       devMode.setBounds(330, 360, 100, 30);
       devMode.setText("Output DB");
        devMode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
               db.outputStudents();
            }
        });*/
        loadTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                UserDialog ud = new UserDialog();
                db.removeDataFromBase();
                ud.chooseFileDialog(MainMenu);
                loadTableFromFile(true, DataBase.getFileName(), table);
                getAmountOfPages(db.getStudentsSize(), numberOfRowsOnPage);
                printDataBaseToTable(table, 0, numberOfRowsOnPage);
                 pageInfo.setText("Page " + (currentPage+1) + "|" + numberOfPages);
            }
        });
        settings.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                UserDialog ud = new UserDialog();
                ud.settingsDialog(MainMenu);
                //loadTableFromFile(true,DataBase.getFileName(),table);
            }
        });
        back.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                                if (currentPage-1==-1) {error.setText("You have reached the end of file"); return;}
                error.setText("");
                currentPageStart -= numberOfRowsOnPage;
                currentPageEnd -= numberOfRowsOnPage;
                currentPage--;
                                    pageInfo.setText("Page " + (currentPage+1) + "|" + numberOfPages);
                                    

                // printDataBaseToTable(table,currentPageStart,currentPageEnd);
                try {
                    printDataBaseToTable(table, currentPageStart, currentPageEnd);
                    System.out.println("Current page start: " + currentPageStart);
                    System.out.println("Current page end: " + currentPageEnd);
                    System.out.println("Current page: " + currentPage);
                                        System.out.println("Number of rows on the page: " + numberOfRowsOnPage);

                    
                } catch (ArrayIndexOutOfBoundsException ex) {
                   WindowForm.Error(MainMenu, "Out of sizes", "An error was encountered. Outputting data has leaved borders massive.");
                }

            }
        });
        forward.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
            if (currentPage==numberOfPages-1){error.setText("You have reached the end of file"); return;}

                                error.setText("");
                currentPageStart += numberOfRowsOnPage;
                currentPageEnd += numberOfRowsOnPage;
                currentPage++;
                pageInfo.setText("Page " + (currentPage+1) + "|" + numberOfPages);
                try {
                    printDataBaseToTable(table, currentPageStart, currentPageEnd);
                    System.out.println("Current page start: " + currentPageStart);
                    System.out.println("Current page end: " + currentPageEnd);
                    System.out.println("Current page: " + currentPage);
                } catch (IndexOutOfBoundsException ex) {
                    WindowForm.Error(MainMenu, "Out of sizes", "An error was encountered. Outputting data has leaved borders massive.");                        
                }
            }
        });
        deleteItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                UserDialog ud = new UserDialog();
                try {
                    // ud.loadFindItemDialog(MainMenu);
                    // deleteElement(8,MainMenu);
                    ud.changeTableDialog(MainMenu);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                //  printDataBaseToTable(table);
                db.outputStudents();
            }
        });

        //  fromTableToDataBase();
        updateTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    //   UserDialog ud = new UserDialog();
                    //db.removeDataFromBase();
                    //  loadTableFromFile(true,DataBase.getFileName(),table);
                    table.removeAll();
                    db.outputStudents();
                    printDataBaseToTable(table);
                } catch (IndexOutOfBoundsException ex) {
                    WindowForm.Error(MainMenu, "Fatal error", "Possible data format in the file are wrong, or some data missed");
                }
            }
        });
        addItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                UserDialog.addToTableDialog(table, MainMenu);
                //printDataBaseToTable(table);
            }
        });
        findItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                UserDialog ud = new UserDialog();
                ud.loadFindItemDialog(MainMenu);
                //printDataBaseToTable(table);
                // loadTableFromFile(true,DataBase.getFileName(),table);
            }
        });
        WindowForm.WindowOpen(display, MainMenu, 1270, 500);
    }
    //public void 
}
