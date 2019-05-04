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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.swt.SWT;
import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Asus
 */
public class UserDialog {
public static ArrayList<Combo> comboList = new ArrayList();
    public static int arrayListCounter = 0;

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
        DataBase model = new DataBase();
        ArrayList<Label> labelList = new ArrayList();
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
            try {
                    GUI gui = new GUI();
                    WindowForm wf = new WindowForm();
                    if (studentName.getText().equals("")){ wf.Error(dialog,"Error 001","One of text fields are empty");return;}
                    DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
                    DocumentBuilder  db = dbf.newDocumentBuilder();
                    Document doc=db.parse("data/iblog.xml");
                    NodeList nodeList = doc.getElementsByTagName("students");
                    Node students=doc.getElementsByTagName("student").item(0);//nodeList.getLength());
                    gui.addNewElement(doc,students, studentName.getText(), studentGroup.getText());
                   // gui.addNewElement(doc, students, studentName.getText(), studentGroup.getText());
                    for (int i=0; i<comboList.size(); i++){
                        NamedNodeMap namedNodeMap = students.getAttributes();
                        Node nodeAttr = namedNodeMap.getNamedItem("sem"+Integer.toString(i));
                       // int idx = comboList.get(i).getSelectionIndex();
                        nodeAttr.setNodeValue(comboList.get(i).getText());
                    }
          
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(new File("data/iblog.xml"));
                transformer.transform(domSource, streamResult);
            }
            catch(IOException | ParserConfigurationException | DOMException | SAXException ei){} catch (TransformerException ex) {
                Logger.getLogger(UserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ));
        //dialog.setDefaultButton (ok);
        dialog.pack();
        dialog.open();
    }
}
