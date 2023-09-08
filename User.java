package ATM;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {

    private String firstname;
    private String lastname;
    private String uuid;
    private byte pinHash[];
    private ArrayList<Account> accounts;

    /**
     * @param firstname
     * @param lastname
     * @param pin
     * @param theBank
     */
    public User(String firstname,String lastname,String pin,Bank theBank){

        //set user name
        this.firstname=firstname;
        this.lastname=lastname;

        //store the pins MDS hash, rather that the original value for
        //security purpose

        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            this.pinHash=md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchALgorithmException");

            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        }

        //get a new uuid for the user
        this.uuid=theBank.getNewUserUUID();

        //create empty list of accounts
        this.accounts=new ArrayList<Account>();

        //print log message
        System.out.printf("New user %s %s with ID %s created.\n",lastname,firstname,this.uuid);
    }

    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    public String getUUID() {
        return this.uuid;
    }

    /**
     * @param aPin
     * @return
     */
    public boolean validatePin(String aPin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchALgorithmException");

            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        }
        return false; 
    }

    public String getFirstName(){
        return this.firstname;
    }

    public void printAccountSummary() {
        System.out.printf("\n\n%s's accoutn summary is\n",this.firstname);
        for(int i=0;i<this.accounts.size();i++){
        System.out.printf("\n %d) %s",i+1,this.accounts.get(i).getSummaryLine());
    }
    System.out.println();
    }

    public int numAccounts() {
        return this.accounts.size();
    }

    public void printAccountSummary(int accIdx) {
        this.accounts.get(accIdx).printTransHistory();
    }

    public double getAcctBalance(int accIdx) {
        return this.accounts.get(accIdx).getBalance();
    }

    public String getAcctUUID(int acctIdx) {
        return this.accounts.get(acctIdx).getUUID();
    }

    public void addAcctTransaction(int accIdx, double amount, String memo) {
        this.accounts.get(accIdx).addTransaction(amount,memo);
    }

    public void printAcctTransHistroy(int accIdx) {
        this.accounts.get(accIdx).printTransHistory();
    }  
    
}
