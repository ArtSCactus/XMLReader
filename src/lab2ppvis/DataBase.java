/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2ppvis;

import java.util.ArrayList;

/**
 *
 * @author Asus
 */
public class DataBase {

private ArrayList<ArrayList> students = new ArrayList();
private ArrayList<Integer> amountOfWork = new ArrayList();//TODO: unnecessary, can be deleted;

public void addStudentData(ArrayList incomingList){
students.add(incomingList); 
}
public void outputStudents(){
    System.out.println("--------DataBase--------");
for (int i=0; i<students.size(); i++)
    for (int j=0; j<students.get(i).size(); j++){
       if(j==0) System.out.print("group: "+students.get(i).get(j));
       if(j==1) System.out.println(" name: "+students.get(i).get(j));
       if(j>1 & j<11) System.out.println("sem: "+students.get(i).get(j));
       if (j==11) System.out.println("summary work: "+students.get(i).get(j));
    }
}
public void addToAmountMassive(int number){//TODO: unnecessary, can be deleted;
    amountOfWork.add(number);
}
public int getAmountOfWork(int index){//TODO: unnecessary, can be deleted;
    return amountOfWork.get(index);
}
public int getLengthOfAmountMassive(){//TODO: unnecessary, can be deleted;
    return amountOfWork.size();
}
public void outputMassive(){//TODO: unnecessary, can be deleted;
    for (int i=0; i<amountOfWork.size(); i++)
   System.out.println("Amount of work massive["+i+"]: "+getAmountOfWork(i));
}
private  int counter = 0;
private boolean debugMode=false;
private String fileName;
public  int getCounter(){
    return counter;
}
public void setCounter(int i){
    counter+=i;
}
}
