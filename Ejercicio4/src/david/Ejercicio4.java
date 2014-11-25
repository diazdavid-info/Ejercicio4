package david;

import java.util.Random;
import java.util.concurrent.Semaphore;

class MiHebra implements Runnable{
	int[] argNumber = new int[10];
	int suma = 0;
	int resta = 0;
	int positionArg = -1;
	volatile int flag = 1;
	Semaphore _semaphore = new Semaphore(1, true);
	public void run(){
		for (int i = 0; i < argNumber.length; i++) {
			String nameThread = Thread.currentThread().getName();
			switch (nameThread) {
			case "Hebra1":
				addNumber();
				break;
			case "Hebra2":
				suma();
				break;
			case "Hebra3":
				resta();
				break;
			}
		}
	}
	
	public void addNumber(){
		while(flag != 1);
		Random r = new Random();
		_semaphore.acquireUninterruptibly();
		argNumber[++positionArg] = r.nextInt(100);
		System.out.println("Numero añadido");
		flag = 2;
		_semaphore.release();
	}
	
	public void suma(){
		while(flag != 2);
		_semaphore.acquireUninterruptibly();
		suma += argNumber[positionArg];
		System.out.println("suma");
		flag = 3;
		_semaphore.release();
	}
	
	public void resta(){
		while(flag != 3);
		_semaphore.acquireUninterruptibly();
		resta -= argNumber[positionArg];
		System.out.println("resta");
		flag = 1;
		_semaphore.release();
	}
}

public class Ejercicio4 {

	public static void main(String[] args) throws InterruptedException {
		MiHebra hebra = new MiHebra();
		
		Thread t1,t2,t3;
		
		t1 = new Thread(hebra, "Hebra1");
		t2 = new Thread(hebra, "Hebra2");
		t3 = new Thread(hebra, "Hebra3");
		
		t1.start();
		t2.start();
		t3.start();
		
		t1.join();
		t2.join();
		t3.join();
		
		System.out.println("El resultado de la suma es: "+hebra.suma);
		System.out.println("El resultado de la resta es: "+hebra.resta);
	}

}
