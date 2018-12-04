import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class order implements Runnable
{
    public void run()
    {
        // implement queue having objects of stock class
        BufferedReader br = null;
        stock r = new stock();

        try {
            String actionString;
            br = new BufferedReader(new FileReader("input3.txt"));

            while ((actionString = br.readLine()) != null) {
                r.performAction(actionString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }   
    }

}
class clean implements Runnable
{
    public Thread thread;
    public clean(Thread t)
    {
        thread = t;
    }
    public void run()
    {
        while(true)
        {
            System.out.print("");
            if(Exchange.Head1!=null)
            {
                list p = Exchange.Head1;
                while(p!=null)
                {
                    long ctm = System.currentTimeMillis();
                    if(p.data.isExpired==true || p.data.satisfied==true || ctm-stock.start>=(p.data.t0 + p.data.texp)*1000)
                    {
                        long ct = System.currentTimeMillis();
                        float ct1 = (float)(ct-stock.start)/1000.0f;
                        String a = Float.toString(ct1) + " " + p.data.getString();
                        stock.order_out(a,"clean.txt");
                        Exchange.Head1 = Exchange.Head1.del(Exchange.Head1, p);
                    }
                    p = p.next;
                }
            }
            if(Exchange.Head2!=null)
            {
                list p = Exchange.Head2;
                while(p!=null)
                {   
                    long ctm = System.currentTimeMillis();
                    if(p.data.isExpired==true || p.data.satisfied==true || ctm-stock.start>=(p.data.t0 + p.data.texp)*1000)
                    {
                        long ct = System.currentTimeMillis();
                        float ct1 = (float)(ct-stock.start)/1000.0f;
                        String a = Float.toString(ct1) + " " + p.data.getString();
                        stock.order_out(a,"clean.txt");
                        Exchange.Head2 = Exchange.Head2.del(Exchange.Head2, p);
                    }
                    p = p.next;
                }
            }
            if(!thread.isAlive())
            {
                break;
            }
        }
    }
}
public class test implements Runnable{
//Thread wrapper class
    public void run()
    {
        order o1 = new order();
        Thread o = new Thread(o1);
        Exchange ex1 = new Exchange(o);
        Thread ex = new Thread(ex1);
        clean c1 = new clean(ex);
        Thread c = new Thread(c1);
        o.start();
        ex.start();
        c.start();
        try{
            o.join();
            ex.join();
            c.join();
        }catch(InterruptedException e){}
    }
}