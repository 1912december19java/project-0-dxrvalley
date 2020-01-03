package com.revature.Bank.repositories;

import java.util.Scanner;
import com.revature.Bank.repositories.MembersDaoPostgres;

public class Driver {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    MembersDaoPostgres acount = new MembersDaoPostgres();
    int userChoice;
    boolean quit = false;
    do {

      System.out.println("1. Sign up");

      System.out.println("2. Login");

      System.out.print("Your choice, 0 to quit: \n");

      userChoice = sc.nextInt();

      switch (userChoice) {

        case 1:
          acount.signup();

          break;

        case 2:
          acount.login();

          break;
        case 0:

          quit = true;

          break;

        default:

          System.out.println("Wrong choice.");

          break;

      }

      System.out.println();

    } while (!quit);

    System.out.println("Bye!");

  }

}
