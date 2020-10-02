package it;

import com.jcg.maven.App;

public class ITDummyTest {

	public static void main(String[] args) {
		
		App it_myapp = new App();
		
		System.out.println("\nIntegration Testing");
		
		
		System.out.println("\nCall 1");
		it_myapp.method1();
		
		
		System.out.println("\nCall 2");
		it_myapp.method1();
		
		
		System.out.println("\nCall 3");
		it_myapp.method1(); 
		
		
	}

}
