// The driver : 3rd Threads sharing the same object.

public class InternetBankingSystem {
	public static void main(String[] args) {
		Account accountObject = new Account();
		Thread t1 = new Thread();
		Thread t2 = new Thread();
		Thread t3 = new Thread();
		
		// 쓰레드 시작.
		t1.start();
		t2.start();
		t3.start();
	}
}

class Account {
	int balance;
	
	public synchronized void deposit() {
		// balance += deposit_amount;
	}
	public synchronized void withdraw() {
		// balance -= deposit_amount;
	}
	public synchronized void enquire() {
		// display balance.
	}
}

class MyThread implements Runnable {
	Account acc;
	
	// 생성자.
	public MyThread(Account s) {
		acc = s;
	}
	
	public void run() {
		acc.deposit();
	}
}

class YourThread implements Runnable {
	Account acc;
	
	// 생성자.
	YourThread(Account s) {
		acc = s;
	}
	
	public void run() {
		acc.withdraw();
	}
}

class HerThread implements Runnable {
	Account acc;
	
	HerThread(Account s) {
		acc = s;
	}
	
	public void run() {
		acc.enquire();
	}
}