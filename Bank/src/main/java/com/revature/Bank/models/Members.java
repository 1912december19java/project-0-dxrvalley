package com.revature.Bank.models;

public class Members {
  private Integer id;
  private String email;
  private String password_;
  private String acount;
  private Float balance;
  
  public Members(Integer id, String email, String password_, String acount, Float balance) {
    super();
    this.id = id;
    this.email = email;
    this.password_ = password_;
    this.acount = acount;
    this.balance = balance;
  }
  
  public Members( Float balance) {
    super();
    this.balance = balance;
  }

  public Members() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword_() {
    return password_;
  }

  public void setPassword_(String password_) {
    this.password_ = password_;
  }

  public String getAcount() {
    return acount;
  }

  public void setAcount(String acount) {
    this.acount = acount;
  }

  public Float getBalance() {
    return balance;
  }

  public void setBalance(Float balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "Member_id: " + id +"\n" + "email: " + email +"\n" + "password: " + password_ +"\n" + "acount: "
        + acount +"\n" + "balance: " + balance;
  }
  
  
  
  
  
}
 
