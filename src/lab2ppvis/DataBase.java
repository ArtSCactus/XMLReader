/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.util.ArrayList;

/**
 *
 * @author ArtSCactus
 */
public class DataBase {
private  String fileName;
private  ArrayList<ArrayList> students = new ArrayList();

public void removeDataFromBase(){
    students.clear();
}
public ArrayList getDataBase(){
    return students;
}
public void addDataBase(ArrayList incommingList){
    students.addAll(incommingList);
}
public  void setFileName(String newFileName){
    fileName=newFileName;
}
public  String getFileName(){
    return fileName;
}
public void removeStudent(int indexOfStudent){
    students.remove(indexOfStudent);
}
public int getStudentsSize(){
   return students.size();
}
public int getStudentsSize(int indexOfElement){
   return students.get(indexOfElement).size();
}
public void addStudentData(ArrayList incomingList){
students.add(incomingList); 
}
public String getStudentData(int studentIndex, int dataIndex){
   return (String) students.get(studentIndex).get(dataIndex);
}
public ArrayList getStudentData(int studentIndex){
  return students.get(studentIndex);
}
public int getIndexOfRowWithTarget(String target){
        int indexOfRow=0;
        boolean existence = false;
      for (int i=0; i<students.size(); i++)
              if (students.get(i).get(1).equals(target)) {indexOfRow=i; existence=true;}
     
     if(existence==true) return indexOfRow;
     else return -1;
    }
public int getIndexOfRowWithTarget(String target, int columnIndex){
        int indexOfRow=0;
        boolean existence = false;
      for (int i=0; i<students.size(); i++)
              if (students.get(i).get(columnIndex).equals(target)) {indexOfRow=i; existence=true;}
     
     if(existence==true) return indexOfRow;
     else return -1;
    }
public ArrayList getIndexesOfRowsWithTarget(String target, int indexOfColumn){
        ArrayList<Integer> indexesOfRows = new ArrayList();
      for (int i=0; i<students.size(); i++)
              if (students.get(i).get(indexOfColumn).equals(target)) {indexesOfRows.add(i);}
     return indexesOfRows;
    }
public ArrayList getIndexesOfRowsWithTarget(String nameOrGroup, String downLimit, String upLimit, int firstColumn, int secondColumn){
        ArrayList<Integer> indexesOfRows = new ArrayList();
      for (int i=0; i<students.size(); i++)
              if (students.get(i).get(firstColumn).equals(nameOrGroup) && Integer.parseInt((String)students.get(i).get(13))>=Integer.parseInt(downLimit) &&  Integer.parseInt((String)students.get(i).get(13))<=Integer.parseInt(upLimit)) {indexesOfRows.add(i);}
     return indexesOfRows;
    }
public boolean isExist(String target, int indexOfColumn){
        boolean existence = false;
      for (int i=0; i<students.size(); i++)
              if (students.get(i).get(indexOfColumn).equals(target)) existence=true;
     if(existence==true) return true;
     else return false;
}
public void outputStudents(){
    System.out.println("--------DataBase--------");
       for (int i=0; i<students.size(); i++)
       for (int j=0; j<students.get(i).size(); j++){
       if(j==0) System.out.println("№: "+students.get(i).get(j));
       if(j==1) System.out.println("name: "+students.get(i).get(j));
       if (j==2) System.out.println("group: "+students.get(i).get(j));
       if(j>2 & j<13) System.out.println("sem["+(j)+"]: "+students.get(i).get(j));
       if (j==13) System.out.println("summary work: "+students.get(i).get(j));
    }
}
private  int counter = 0;
private boolean debugMode=false;

public  int getCounter(){
    return counter;
}
public void setCounter(int i){
    counter+=i;
}
}
