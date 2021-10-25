// 1st method : Extending Thread class.

class MyThreadA extends Thread {
	public void run() {
		for (;;) {
			System.out.println("hello world1");
		}
	}
}

class MyThreadB extends Thread {
	public void run() {
		for (;;) {
			System.out.println("hello world2");
		}
	}
}

public class Main1 {
	public static void main(String[] args) {
		MyThreadA t1 = new MyThreadA();
		MyThreadB t2 = new MyThreadB();
		
		// 두 개의 쓰레드를 동시에 실행.
		t1.start();
		t2.start();
	}
}
