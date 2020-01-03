package com.revature.Bank.repositories;

import org.apache.log4j.Logger;
import java.util.*;
import java.util.regex.Pattern;
import com.revature.Bank.models.Members;
import java.sql.*;

public class MembersDaoPostgres implements MembersDao {
  private static Connection conn;
  static Scanner sc = new Scanner(System.in);
  Integer inputId;
  Boolean valid;
  int MemberChoice;
  Float balance;
  Boolean quit = false;
  
  private static Logger log = Logger.getLogger(MembersDaoPostgres.class);
  static {
    try {
      conn = DriverManager.getConnection(System.getenv("connstring"), System.getenv("username"),
          System.getenv("password"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public boolean isValid(String email) {
    String emailRegex = "^[a-zA-Z]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@"
        + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,20}$";
    Pattern pat = Pattern.compile(emailRegex);
    if (email == null)
      return true;
    return pat.matcher(email).matches();
  }
  public boolean isValidPassword(String password) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]{2,15}$";
    Pattern pat = Pattern.compile(emailRegex);
    if (password == null)
      return true;
    return pat.matcher(password).matches();
  }
  @Override
  public void signup() {
    boolean exit = false;    
    String acountType;
    PreparedStatement stmt = null;
    Members acount= new Members();
    String emailInfo = null;
    String passwordInfo = null;

      do{
        System.out.println("\nWelcome to the Signup Screen\n");
        System.out.println("Provide Email"+ " Email must have @ and .com / Example:dv@gmail.com ");
        emailInfo = sc.nextLine();
      acount.setEmail(emailInfo);
   System.out.println("Provide Password"+ " Password Min:8 Max:20 ");
   passwordInfo = sc.nextLine();
   acount.setPassword_(passwordInfo);
    System.out.println("Press (S)Savings or (C)Checking");
    acountType = sc.nextLine();
    if (acountType.equalsIgnoreCase("S")) {
      acount.setAcount("Saving");
    } else {
      acount.setAcount("Checking");
    }
    System.out.println("Submit information... Press (Y) Yes or (N) No");
    String confirm =sc.nextLine();
    if (confirm.equalsIgnoreCase("y") & isValid(emailInfo) & isValidPassword(passwordInfo)) {
    
    try {
      stmt = conn.prepareStatement(
          "BEGIN; INSERT INTO bank_info(email,password_,acount) VALUES(?,?,?); COMMIT;");
      stmt.setString(1, acount.getEmail());
      stmt.setString(2, acount.getPassword_());
      stmt.setString(3, acount.getAcount());
      stmt.execute();
      stmt.close();
      log.info("Registered Data into the Database");
      }catch(SQLException e){
      log.error("Failed to connect to Database");
      }
    }
    exit=true;
      }while(!exit);
  }
  
  @Override
  public Members login() {
    ResultSet rs = null;
    Members acount = null;   
    String userEmail;
    String userPassword;
    Boolean info=false;
    Integer MemberChoice= null;


    do {
      System.out.println("\nWelcome to the Login Screen\n");
      System.out.println("Enter Email: ");
      userEmail = sc.nextLine();
      System.out.println("Enter Password");
      userPassword = sc.nextLine();
      PreparedStatement stmt = null;
      try {
        stmt = conn.prepareStatement("SELECT * FROM bank_info WHERE (email = ? AND password_ = ?)");
        stmt.setString(1, userEmail);
        stmt.setString(2, userPassword);
        stmt.execute();
        
        if (stmt.execute()) {
          rs = stmt.getResultSet();
        }
        while (rs.next()) {
          acount = new Members(rs.getInt("member_id"), rs.getString("email"), rs.getString("password_"),rs.getString("acount"),rs.getFloat("balance"));
        }
      }catch (SQLException e) {
        e.printStackTrace();
      }
      if (acount != null) {
        System.out.println("\n\n\n" + acount + "\n\n\n");
        info=true;
        }
    }while(!info);
      
      do {
        System.out.println("Welcome to the Acount screen" );
        System.out.println("Press (1) Deposit money\n");
        System.out.println("Press (2) Withdraw money\n");
        System.out.println("Press (3) Check Balance\n");
        System.out.println("Press (4) Log Out\n");

        MemberChoice = sc.nextInt();
        switch (MemberChoice) {
          
          case 1:
            float amount;
            Float balance = acount.getBalance();
            System.out.print("Amount to deposit: ");
            amount = sc.nextFloat();
            if (amount <= 0)
              System.out.println("Can't deposit nonpositive amount.");
            else {
              try {
                PreparedStatement stmt1 = conn.prepareStatement("BEGIN; UPDATE bank_info SET balance = balance + ? WHERE email = ?; COMMIT;");
                stmt1.setFloat(1, amount);
                stmt1.setObject(2, acount.getEmail());
                if (stmt1.execute()) {
                  rs = stmt1.getResultSet();
                }
                while (rs.next()) {
                  acount = new Members(rs.getInt("member_id"), rs.getString("email"), rs.getString("password_"),
                     rs.getString("acount"),rs.getFloat("balance"));
                }                
              } catch (SQLException e) {
                e.printStackTrace();
              }
              System.out.println("$" + amount + " has been Deposit.\n" );
              System.out.println("Press (b) Back to the Member Screen");
              String Choices = sc.nextLine();
              if(Choices.equalsIgnoreCase("b")) {
              }
              break;
            }  
        
          case 2:
      
            balance = acount.getBalance();
            System.out.print("Amount to withdraw: ");
             amount = sc.nextInt();
            if (amount <= 0 || amount > balance)
              System.out.println("Withdrawal can't be completed.");
            else {
              try {
                PreparedStatement stmt1 = conn.prepareStatement("BEGIN; UPDATE bank_info SET balance = balance - ? WHERE email = ?; COMMIT;");
                stmt1.setObject(1, amount);
                stmt1.setObject(2, acount.getEmail());
                if (stmt1.execute()) {
                  rs = stmt1.getResultSet();
                }
                while (rs.next()) {
                  acount = new Members(rs.getInt("member_id"), rs.getString("email"), rs.getString("password_"),
                     rs.getString("acount"),rs.getFloat("balance"));
                }
              } catch (SQLException e) {
                e.printStackTrace();
              }
              System.out.println("$" + amount + " has been withdrawn.");
              System.out.println("Press (b) Back to the Member Screen");
              String Choices = sc.nextLine();
              if(Choices.equalsIgnoreCase("b")) {
                break;
              }
            }
          
          
          case 3:
            
            balance = acount.getBalance();
            userEmail = acount.getEmail();
            userPassword = acount.getPassword_();
            
            try {
              PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM bank_info WHERE email = ? AND password_ = ?;");
              stmt1.setObject(1, userEmail);
              stmt1.setObject(2, userPassword);
              if (stmt1.execute()) {
                rs = stmt1.getResultSet();
              }
              while (rs.next()) {
                acount = new Members(rs.getInt("member_id"), rs.getString("email"), rs.getString("password_"),
                   rs.getString("acount"),rs.getFloat("balance"));
              }
              System.out.println("Your balance: $" + balance);
              System.out.println("Press (b) Back to the Member Screen");
              String Choices = sc.nextLine();
              if(Choices.equalsIgnoreCase("b")) {
                break;
              }
            } catch (SQLException e) {
              e.printStackTrace();
            }
          break;
        
          case 4:
          
            System.out.println("Login Out. Good Bye");
            quit = true;

      }
      }while(!quit);
    return acount;
    }
 
}


  



 