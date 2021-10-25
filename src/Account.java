/**
 * Create Account class for storing account information.
 * @author user Eun Su Seo
 * last modified 2021-10-14
 */
import java.util.HashMap;

class Account {
	/* Create the hashmap to store accounts information.
	 * String -> To store Name, Integer -> To store balance.
	 */
	HashMap <String, Integer> account = new HashMap<>();

	// At first, Server has 5 accounts info. (I make my own case!)
	Account() {
		account.put("KIM", 40);
		account.put("SEO", 100);
		account.put("NAM", 80);
		account.put("LEE", 40);
		account.put("SON", 20);
	}

	/* The check operation is supported. & Support synchronization for each object.
	 * This operation is to check whether the account of 'key' exists.
	 */
	public synchronized int check(String key) {
		// If that account exists,
		if (account.get(key) != null) { return 0; }
		// If Server doesn't have that account,
		else { return 1; }
	}

	/* The deposit operation is supported. & Support synchronization for each object.
	 * This operation is to add 'value' to the account of 'key'.
	 */
	public synchronized void deposit(String key, int value) {
		account.replace(key, account.get(key) + value);
	}

	/* The withdraw operation is supported. & Support synchronization for each object.
	 * This operation is to withdraw 'value' from the account of 'key'.
	 */
	public synchronized void withdraw(String key, int value) {
		account.replace(key, account.get(key) - value);
	}

	/* The transfer operation is supported. & Support synchronization for each object.
	 * This operation is to transfer 'value' from the account of 'key' to the account of 'receiver'.
	 * implement using withdraw and deposit!
	 */
	public synchronized void transfer(String key, int value, String receiver) {
		withdraw(key, value);
		deposit(receiver, value);
	}
}