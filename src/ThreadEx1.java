// 1st method : Extending Thread class.

class MyThread1 extends Thread {
	public void run() {
		System.out.println(" This thread is running ... ");
	}
}

public class ThreadEx1 {
	public static void main(String[] args) {
		MyThread1 t = new MyThread1();
		t.start();
	}
}
