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
private ArrayList<Integer> amountOfWork = new ArrayList();
public void addToAmountMassive(int number){
    amountOfWork.add(number);
}
public int getAmountOfWork(int index){
    return amountOfWork.get(index);
}
public int getLengthOfAmountMassive(){
    return amountOfWork.size();
}
public void outputMassive(){
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
