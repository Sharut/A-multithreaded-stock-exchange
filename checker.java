import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class checker
{
	public static void main ( String args [])
	{
		test obj = new test();
        Thread t = new Thread(obj, "test");
        t.start();
        try{
        	t.join();
        }catch(InterruptedException E){}
	}
}
