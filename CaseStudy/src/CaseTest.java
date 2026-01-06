import java.util.Arrays;
import java.util.Scanner;

class Transaction {

    String type;
    double amount;

    Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }
    

    String getType() {
		return type;
	}


	void setType(String type) {
		this.type = type;
	}


	double getAmount() {
		return amount;
	}


	void setAmount(double amount) {
		this.amount = amount;
	}


	public String toString() {
        return type + " : " + amount;
    }
}
abstract class Account {

    int accNo;
    String name;
    double balance;
    String status;
    Transaction[] tArr;
    int tCount;


    static int accCounter = 1000;
    

   
     Account() {
        this.accNo = 0;
        this.name = "not given";
        this.balance = 0.0;
        this.status = "OPEN";
    }


    Account(String name, double balance) {
        this.accNo = ++accCounter;
        this.name = name;
        this.balance = balance;
        this.status = "OPEN";
        tArr = new Transaction[10];
        tCount = 0;
    }

    

    String getName() {
		return name;
	}


	void setName(String name) {
		this.name = name;
	}


	static int getAccCounter() {
		return accCounter;
	}


	static void setAccCounter(int accCounter) {
		Account.accCounter = accCounter;
	}


	Transaction[] gettArr() {
		return tArr;
	}


	void settArr(Transaction[] tArr) {
		this.tArr = tArr;
	}


	int gettCount() {
		return tCount;
	}


	void settCount(int tCount) {
		this.tCount = tCount;
	}


	void setAccNo(int accNo) {
		this.accNo = accNo;
	}


	void setBalance(double balance) {
		this.balance = balance;
	}


	boolean deposit(double amt) {
		if (status != "OPEN")
	        return false;
        balance = balance + amt;
        tArr[tCount++] = new Transaction("Deposit", amt);
        return true;
    }

	boolean withdraw(double amt) {
		if (status != "OPEN")
	        return false;
        balance = balance - amt;
        tArr[tCount++] = new Transaction("Withdraw", amt);
        return true;
    }

    int getAccNo() {
        return accNo;
    }

    double getBalance() {
        return balance;
    }
    boolean open() {
        if (status == "CLOSED") {
            status = "OPEN";
            return true;
        }
        return false;
    }

    boolean close() {
        if (status != "CLOSED") {
            status = "CLOSED";
            return true;
        }
        return false;
    }

    boolean freez() {
        if (status == "OPEN") {
            status = "FROZEN";
            return true;
        }
        return false;
    }


    

    abstract double calculateInterest();

    public String toString() {
        return "AccNo:" + accNo + " Name:" + name + " Balance:" + balance;
    }
}





// Current Account
class CurrentAccount extends Account {

    static double overdraftLimit = 10000;
    static double getOverdraftLimit() {
        return overdraftLimit;
    }

    CurrentAccount() {
        super();
    }

    CurrentAccount(String name, double balance) {
        super(name, balance);
    }

    

   void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    // override withdraw to allow overdraft
    boolean withdraw(double amt) {
        if (balance - amt >= -overdraftLimit) {
            balance = balance - amt;
            tArr[tCount++] = new Transaction("Withdraw", amt);
            return true;
        }
        return false;
    }

    double calculateInterest() {
        return 0;
    }

    public String toString() {
        return "CurrentAccount AccNo:" + accNo +
               " Name:" + name +
               " Balance:" + balance +
               " OverdraftLimit:" + overdraftLimit;
    }
}



//Controller
class BankController {

    BankDAO bd = new BankDAO();

    String addAccount(Account acc) {

        if (acc.status == "INVALID") {
            return "INVALID";
        }

        if (acc.status == "FROZEN") {
            return "FROZEN";
        }

        if (bd.addAccount(acc)) {
            return "SUCCESS";
        }

        return "FULL";
    }




    Account searchAccountByAccNo(int accNo) {
        Account acc = bd.searchAccount(accNo);
        if (acc != null) {
            return acc;
        }
        return null;
    }

    boolean deposit(int accNo, double amt) {
        if (bd.deposit(accNo, amt)) {
            return true;
        }
        return false;
    }

    boolean withdraw(int accNo, double amt) {
        if (bd.withdraw(accNo, amt)) {
            return true;
        }
        return false;
    }

    boolean closeAccount(int accNo) {
        if (bd.closeAccount(accNo)) {
            return true;
        }
        return false;
    }

    boolean freezeAccount(int accNo) {
        if (bd.freezeAccount(accNo)) {
            return true;
        }
        return false;
    }

    boolean deleteAccount(int accNo) {
        if (bd.deleteAccount(accNo)) {
            return true;
        }
        return false;
    }

    void displayAllAccounts() {
        Account[] arr = bd.getAllAccounts();
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}




class CaseTest {
	
	public static void main(String[] args) {
        BankController bc = new BankController();
        BankView bv = new BankView(bc);
        bv.showOptions();
    }
}