// 2nd method : Threads by implementing Runnable interface.

class MyThread2 implements Runnable {
	public void run() {
		System.out.println(" This thread is running ... ");
	}
}

public class ThreadEx2 {
	public static void main(String[] args) {
		Thread t = new Thread(new MyThread2());
		t.start();
	}
}


