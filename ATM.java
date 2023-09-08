package ATM;

import java.util.Scanner;



public class ATM {
    public static void main(String[] args){
    
    Scanner sc=new Scanner(System.in);

    Bank theBank=new Bank("State Bank of India");
    User aUser =theBank.addUser("john","doe","1234");

    Account newAccount=new Account("checking",aUser,theBank);
    aUser.addAccount(newAccount);
    theBank.addAccount(newAccount);

    User curUser;
    while(true){
        curUser =ATM.mainMenuPrompt(theBank,sc);

        ATM.printUserMenu(curUser,sc);
    }
    
}

    public static User mainMenuPrompt(Bank theBank,Scanner sc){
        //init
        String userID;
        String pin;
        User outhUser;

        do{
            System.out.printf("\n\nWelcome to %s\n\n",theBank.getName());
            System.out.println("Enter user ID");
            userID =sc.nextLine();
            System.out.print("enter pin ");
            pin = sc.nextLine();

            outhUser=theBank.userLogin(userID, pin);
            if(outhUser==null){
                System.out.println("Incorrect user ID/pin combination"+"Please try again");
            }
        }
        while(outhUser==null);
        //continue looping until successful login
        return outhUser;
    }
    public static void printUserMenu(User theUser,Scanner sc){

        //print a summary of the user account
        theUser.printAccountSummary();
        int choice;
        
        do{
            System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println("1) Show account transaction history");
            System.out.println("2) Withdrawl");
            System.out.println("3) Deposit");
            System.out.println("4) Transfer");
            System.out.println("5) Quit");

            System.out.print("Enter your choice");
            choice=sc.nextInt();

            if(choice<1 || choice>5){
                System.out.println("invalid choice ");
            }

        }while(choice<1 || choice>5);

        switch(choice){
            case 1:ATM.showTransHistory(theUser,sc);
                   break;
            case 2:ATM.withdrawlFunds(theUser,sc);
                   break;
            case 3:ATM.depostFunds(theUser,sc);
                   break;  
            case 4:ATM.transferFunds(theUser,sc);
                   break;
            case 5: //gobble up the rest of previous input
                   sc.nextLine();
                   break;    
        }
        if(choice !=5){
            ATM.printUserMenu(theUser, sc);
        }
    }

    

    

    /**
     * @param theUser
     * @param sc
     */
    private static void transferFunds(User theUser, Scanner sc) {

        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"to transfer from: ",theUser.numAccounts());
            fromAcct=sc.nextInt()-1;
            if(fromAcct<0 || fromAcct >= theUser.numAccounts()){
                System.out.println("invalid account.please try again");
            }
        }while(fromAcct<0 || fromAcct >= theUser.numAccounts());

        acctBal=theUser.getAcctBalance(fromAcct);

         do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"to transfer to: ",theUser.numAccounts());
            toAcct=sc.nextInt()-1;
            if(toAcct<0 || toAcct >= theUser.numAccounts()){
                System.out.println("invalid account.please try again");
            }
        }while(toAcct<0 || toAcct >=theUser.numAccounts());

        //get the amount to transfer

        do{
            System.out.printf("enter the amount to transfer (max rs%.02f): rs",acctBal);
            amount=sc.nextDouble();
            if(amount <0){
                System.out.println("amoutn must be greater tha zero");
            }
            else if(amount>acctBal){
                System.out.printf("amount must not be greater than\n"+"balance of rs%0.2f",acctBal);
            }

        }while(amount <0 || amount > acctBal);

        theUser.addAcctTransaction(fromAcct,-1*amount,String.format("tranfer to account %s",theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct,amount,String.format("tranfer to account %s",theUser.getAcctUUID(toAcct)));

    }

    private static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;

        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"whose transaction you want to see:",theUser.numAccounts());
            theAcct=sc.nextInt()-1;
            if(theAcct<0 || theAcct >= theUser.numAccounts()){
                System.out.println("invalid account.please try again.");
            }
        }while(theAcct<0 || theAcct >= theUser.numAccounts());

        theUser.printAcctTransHistroy(theAcct);
    }


    private static void withdrawlFunds(User theUser,Scanner sc) {
        String memo;
        int fromAcct;
        double amount;
        double acctBal;

        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"to withdraw from: ",theUser.numAccounts());
            fromAcct=sc.nextInt()-1;
            if(fromAcct<0 || fromAcct >= theUser.numAccounts()){
                System.out.println("invalid account.please try again");
            }
        }while(fromAcct<0 || fromAcct >=theUser.numAccounts());

        acctBal=theUser.getAcctBalance(fromAcct);

        //get the amount to transfer

        do{
            System.out.printf("enter the amount to withdraw (max rs%.02f): rs",acctBal);
            amount=sc.nextDouble();
            if(amount <0){
                System.out.println("amoutn must be greater tha zero");
            }
            else if(amount>acctBal){
                System.out.printf("amount must not be greater than\n"+"balance of rs%.2f",acctBal);
            }

        }while(amount <0 || amount >acctBal);

        //gobble up the rest of previous input
        sc.nextLine();
        //get a memo
        System.out.println("Enter a memo");
        memo=sc.nextLine();

        //do the withdrawl
        theUser.addAcctTransaction(fromAcct,-1*amount,memo);
    }


    private static void depostFunds(User theUser, Scanner sc) {
        String memo;
        int toAcct;
        double amount;
        double acctBal;

        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"to deposit in ",theUser.numAccounts());
            toAcct=sc.nextInt()-1;
            if(toAcct<0 || toAcct >= theUser.numAccounts()){
                System.out.println("invalid account.please try again");
            }
        }while(toAcct<0 || toAcct >=theUser.numAccounts());

        acctBal=theUser.getAcctBalance(toAcct);

        //get the amount to transfer

        do{
            System.out.printf("enter the amount to transfer (max rs%.02f): rs",acctBal);
            amount=sc.nextDouble();
            if(amount <0){
                System.out.println("amoutn must be greater tha zero");
            }
            

        }while(amount <0 );

        //gobble up the rest of previous input
        sc.nextLine();
        //get a memo
        System.out.println("Enter a memo");
        memo=sc.nextLine();

        //do the withdrawl
        theUser.addAcctTransaction(toAcct,amount,memo);
    }
}
