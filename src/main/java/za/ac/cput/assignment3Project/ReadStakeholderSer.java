/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assignment3Project;

/**
 *
 * @author Kholani Benelzane 218257465
 * 
 * This class contains a main method, please run the class separately to produce the customer and supplier text output files
 */
import java.io.IOException;
import java.text.ParseException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Date;
import java.time.Period;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.io.FileWriter;
import java.io.EOFException;
import java.util.ArrayList;

public class ReadStakeholderSer{

  private ObjectInputStream input;
  FileWriter output;
  FileWriter output2;
  private ArrayList<Stakeholder> stakeholders = new ArrayList<Stakeholder>();//arrayList for storing all the objects in the ser File
  ArrayList<Customer> customer = new ArrayList<Customer>();
  ArrayList<Supplier> supplier = new ArrayList<Supplier>();

  //open serialized file
  public void openSerFile(){

    try{
      input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
      System.out.println("File opened");
    }//end try
    catch(IOException ioException){
      System.err.println("Error opening file");
    }//end catch
  }//end of openSerFile method

  //read the serialized file
  public void readSerFile(){
    Stakeholder stakeholder;

    try{
      while(true){//loop until end of file
        stakeholder = (Stakeholder) input.readObject();
        stakeholders.add(stakeholder);//add objects to stakeholders ArrayList
      }//end while
    }//end of try
    catch(EOFException endOfFileException){
      return;
    }//end catch
    catch(ClassNotFoundException classNotFoundException){
      System.err.println("Object could not be created");
    }//end catch
    catch(IOException ioException){
      System.err.println("Error reading from file");
    }//end catch
  }//end of readSerFile method

  public void closeSerFile(){
    try{

      if(input != null){
        input.close();
      }//end if
    }//end try
    catch(IOException ioException){
      System.err.println("Error closing file");
    }//end of catch
  }//end of method closeSerFile

  public void createCustomerList(){

    //traverse the Stakeholder arrayList for Customer objects and store them in the customer arrayList  
    for(Stakeholder obj : stakeholders){

      if(obj instanceof Customer){
        Customer cus = (Customer) obj;//convert from Stakeholder object to customer
        customer.add(cus);
        customer.sort((obj1, obj2)->obj1.getStHolderId().compareTo(obj2.getStHolderId()));//sort customer arrayList
      }//end if
    }//end for
  }//end createCustomerList method

  public int customerAge(String dateOfBirth){
    int age = 0;

    try{

      LocalDate currentDate = LocalDate.now();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date date = simpleDateFormat.parse(dateOfBirth);//convert String to date
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);//use Calendar object to manipulate the passed date objects

      LocalDate birthDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH ) + 1,
                                        calendar.get(Calendar.DATE));

      //calculate the age of each customer
      Period difference = Period.between(birthDate, currentDate);
      age = difference.getYears();
    }//end try
    catch(ParseException e){
      System.err.println(e);
    }

    return age;
  }//end of customerAge method

  public String dateOfBirthFormat(String dateOfBirth){
    String birthDate = "";

    try{
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date date = simpleDateFormat.parse(dateOfBirth);//conver String to date

      SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("d MMM yyyy");
      birthDate = simpleDateFormat2.format(date);//format date in the desired String
    }//end of try
    catch(ParseException e){
      System.err.println(e);
    }
    return birthDate;
  }//end of dateOfBirthFormat method

  public void createCustomerFile(){

    try{
      int canRent = 0;
      int canRentCounter = 0;
      int cannotRentCounter = 0;

      output = new FileWriter("customerOutFile.txt");

      output.write(String.format("%s\n%-10s%-10s%-15s%-15s%-10s\n%s\n",
                         "========================== CUSTOMERS =======================",
                         "ID", "Name", "Surname", "Date of birth", "Age",
                         "============================================================"));

      for(Customer obj : customer){

        output.write(String.format("%-10s%-10s%-15s%-15s%s\n", obj.getStHolderId(), obj.getFirstName(),
                    obj.getSurName(), dateOfBirthFormat(obj.getDateOfBirth()), customerAge(obj.getDateOfBirth())));

        canRent = (obj.getCanRent()) ? 1 : 0;

        if(canRent == 1){
          canRentCounter += canRent;
        }//end if

        else if(canRent == 0){
          cannotRentCounter += 1;
        }//end else if

      }//end of for

      output.write(String.format("\n%-40s%s\n%-40s%s","Number of customers who can rent:",canRentCounter,
                         "Number of customers who cannot rent:",cannotRentCounter));

    }//end of try
    catch(IOException iOException){
      System.err.println("Error writing to file");
    }//end catch
  }//end of createCustomerFile method

  public void closeCustomerFile(){
    try{
      if(output != null){
        output.close();
      }//end if
    }//end of try
    catch(IOException ioException){
      System.err.println("Error closing file");
    }//end catch
  }//end of close closeCustomerFile method

  public void createSupplierList(){
    
    for(Stakeholder obj : stakeholders){
      if(obj instanceof Supplier){
        Supplier sup = (Supplier) obj;
        supplier.add(sup);
      }//end if
    }//end for

    supplier.sort((obj1, obj2)->obj1.getName().compareTo(obj2.getName()));
  }//end of createSupplierList method
  
  public void createSupplierFile(){

    try{
      output2 = new FileWriter("supplierOutFile.txt");
      output2.write(String.format("%s\n%-10s%-25s%-15s%-10s\n%s\n",
                    "========================== SUPPLIERS ===============================",
                    "ID", "Name", "Prod Type", "Description",
                    "===================================================================="));

    for(Supplier obj : supplier){
      output2.write(String.format("%-10s%-25s%-15s%-10s\n", obj.getStHolderId(), obj.getName(),
                  obj.getProductType(), obj.getProductDescription()));
    }//end for

    }//end try
    catch(IOException iOException){
      System.err.println("Error writing to file");
    }//end catch
  }//end of createSupplierFile method
  
  public void closeSupplierFile(){
      
      try{
          if(output2 != null){
              output2.close();
          }
      }//end try
      catch(IOException ioException){
          System.err.println("Error closing file");
      }//end catch
      
  }//end of closeSupplierFIle method
  
  public static void main(String args[]){
    ReadStakeholderSer read = new ReadStakeholderSer();
    read.openSerFile();
    read.readSerFile();
    read.createCustomerList();
    read.createCustomerFile();
    read.createSupplierList();
    read.createSupplierFile();
    read.closeCustomerFile();
    read.closeSupplierFile();
    read.closeSerFile();
  }//end of main method
}//end of ReadStakeholderSer class
