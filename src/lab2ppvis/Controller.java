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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author ArtSCactus
 */
public class Controller {

    DataBase dataBase = new DataBase();

    public void removeDataFromBase() {
        dataBase.removeDataFromBase();
    }

    public ArrayList getDataBase() {
        return dataBase.getDataBase();
    }

    public void addDataBase(ArrayList incommingList) {
        dataBase.addDataBase(incommingList);
    }

    public void setFileName(String newFileName) {
        dataBase.setFileName(newFileName);
    }

    public String getFileName() {
        return dataBase.getFileName();
    }

    public void removeStudent(int indexOfStudent) {
        dataBase.removeStudent(indexOfStudent);
    }

    public int getStudentsSize() {
        return dataBase.getStudentsSize();
    }

    public int getStudentsSize(int indexOfElement) {
        return dataBase.getStudentsSize(indexOfElement);
    }

    public void addStudentData(ArrayList incomingList) {
        dataBase.addStudentData(incomingList);
    }

    public String getStudentData(int studentIndex, int dataIndex) {
        return dataBase.getStudentData(studentIndex, dataIndex);
    }

    public ArrayList getStudentData(int studentIndex) {
        return dataBase.getStudentData(studentIndex);
    }

    public int getIndexOfRowWithTarget(String target) {
        return dataBase.getIndexOfRowWithTarget(target);
    }

    public int getIndexOfRowWithTarget(String target, int columnIndex) {
        return dataBase.getIndexOfRowWithTarget(target, columnIndex);
    }

    public ArrayList getIndexesOfRowsWithTarget(String target, int indexOfColumn) {
        return dataBase.getIndexesOfRowsWithTarget(target, indexOfColumn);
    }

    public ArrayList getIndexesOfRowsWithTarget(String nameOrGroup, String downLimit, String upLimit, int firstColumn, int secondColumn) {
        return dataBase.getIndexesOfRowsWithTarget(nameOrGroup, downLimit, upLimit, firstColumn, secondColumn);
    }

    public boolean isExist(String target, int indexOfColumn) {
        return dataBase.isExist(target, indexOfColumn);
    }
    private int counter = 0;
    private boolean debugMode = false;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int i) {
        counter += i;
    }

    public void writeElement(ArrayList<ArrayList<String>> students, String fileName, Shell shellForErrorWindow) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileName);
        Node root = document.getFirstChild();
        for (int i = 0; i < students.size(); i++) {
            Element student = document.createElement("student");
            root.appendChild(student);
            student.setAttribute("name", students.get(i).get(1));
            student.setAttribute("surname", students.get(i).get(2));
            student.setAttribute("patronymic", students.get(i).get(3));
            student.setAttribute("group", students.get(i).get(4));

            for (int k = 0; k < 10; k++) {
                Element sem = document.createElement("sem");
                sem.appendChild(document.createTextNode(students.get(i).get(k + 5)));
                sem.setAttribute("id", Integer.toString(k));
                student.appendChild(sem);
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName));

        transformer.transform(domSource, streamResult);

        System.out.println("Файл сохранен!");
    }

    public void clearFile(String fileName, boolean onlyContentOfStudents) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileName);

        Node root = document.getElementsByTagName("students").item(0);
        if (onlyContentOfStudents == true) {
            NodeList nodelist = root.getChildNodes();
            for (int i = 0; i < nodelist.getLength(); i++) {
                root.removeChild(nodelist.item(i));
            }
        } else {
            document.removeChild(root);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName));

        transformer.transform(domSource, streamResult);
    }

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
    }

    public void loadTableFromFile(boolean debugMode, String fileLocation) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DefaultHandler defaultHandler = new DefaultHandler() {
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
                        String bufferName = "";
                        String bufferGroup = "";
                        String bufferSurname = "";
                        String bufferPatronymic = "";

                        newStudentData.add(Integer.toString(rowCounter));
                        int length = attributes.getLength();
                        for (int i = 0; i < length; i++) {
                            String name = attributes.getQName(i);
                            System.out.println("Attr name:" + name);
                            String value = attributes.getValue(i);
                            switch (i) {
                                case (0):
                                    bufferGroup = attributes.getValue(i);
                                    break;
                                case (1):
                                    bufferName = attributes.getValue(i);
                                    break;
                                case (2):
                                    bufferSurname = attributes.getValue(i);
                                    break;
                                case (3):
                                    bufferPatronymic = attributes.getValue(i);
                                    break;
                            }
                            System.out.println("Attr value:" + value + " current i=" + i);
                            if (name.equals("group")) {
                                columnCounter = i;
                            }
                            if (name.equals("name")) {
                                columnCounter = i;
                            }
                        }
                        newStudentData.add(bufferName);
                        newStudentData.add(bufferSurname);
                        newStudentData.add(bufferPatronymic);
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
                        ArrayList<String> buffer = new ArrayList();
                        int termWorkCounter = 0;
                        for (int i = 0; i < newStudentData.size(); i++) {
                            buffer.add(newStudentData.get(i));
                        }
                        for (int i = 0; i < 10; i++) {
                            termWorkCounter += Integer.parseInt(buffer.get(i + 5));
                        }
                        buffer.add(Integer.toString(termWorkCounter));
                        newStudentData.clear();
                        dataBase.addStudentData(buffer);
                        rowCounter++;
                        columnCounter = 0;
                    }
                }

                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if (bStudent) {
                        System.out.println("Student: " + new String(ch, start, length));
                        bStudent = false;
                    }
                    if (bSem) {
                        System.out.println("Sem value: " + new String(ch, start, length));
                        bSem = false;
                        newStudentData.add(new String(ch, start, length));
                        columnCounter++;
                    }
                }
            };
            saxParser.parse(dataBase.getFileName(), defaultHandler);
        } catch (IOException | ParserConfigurationException | SAXException | IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
