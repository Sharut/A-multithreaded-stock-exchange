import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class stock implements Cloneable{
	//Perform I/O operation
    public int t0 ;
    public String name ;
    public int texp ;
    public String type ;
    public int qty ;
    public String stock_name ;
    public int price;
    public boolean partial ;
    public boolean faulty ;
    public boolean satisfied;
    public boolean isExpired;
    public static long start;
    public static que Head = null;
    
    public stock()
    {
        t0 = 0;
        name  = "";
        texp  = 0;
        type = "" ;
        qty = 0 ;
        stock_name  = "";
        price = 0;
        partial =false ;
        faulty =false;
        satisfied = false;
        isExpired = false;
        start = System.currentTimeMillis();
    }
    
    public String getString()
    {
        String s = String.format("%d %s %d %s %d %s %d %b",this.t0,this.name,this.texp,this.type,this.qty,this.stock_name,this.price,this.partial);
        return s;
    }
    
    public static void order_out(String a, String filename)
    {
        try {
            FileOutputStream f = new FileOutputStream(filename,true);
            PrintStream p = new PrintStream(f);
            p.println(a);
            f.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(stock.class.getName()).log(Level.SEVERE, null, ex);
        }catch(IOException E){}
        
    }
    
    public void performAction(String actionString){
        this.t0 = 0;
        this.name = "";
        this.texp = 0;
        this.type = "";
        this.qty = 0;
        this.stock_name = "";
        this.price = 0;
        this.partial = false;
        this.faulty = false;
        //System.out.println(actionString);
	    String[] tokens = actionString.split("\\s+");
	    try
	    {
	        if(tokens.length!=8)
                {
                    Exception myException = new Exception();
                    throw myException;
                }
                this.t0 = Integer.parseInt(tokens[0]);
                this.name = tokens[1];
                this.texp = Integer.parseInt(tokens[2]);
                this.type = tokens[3].toLowerCase();
                this.qty = Integer.parseInt(tokens[4]);
                this.stock_name = tokens[5];
                this.price = Integer.parseInt(tokens[6]);
                switch (tokens[7]) {
                    case "F":
                        this.partial = false;
                        break;
                    case "T":
                        this.partial = true;
                        break;
                    default:
                        Exception myException = new Exception();
                        throw myException;
                }
                if(t0<0||texp<0||qty<0||price<0||(!type.equals("sell") && !type.equals("buy")))
                {
                    Exception myException = new Exception();
                    throw myException;
                }
	    }catch(Exception e){
                //e.printStackTrace();
                String actionString1 = "EXCEPTION " + actionString;
                order_out(actionString1,"order.txt");
                this.faulty  = true;
            }
            long end = System.currentTimeMillis();
            if(this.faulty==false)
            {
                try{
                    //this.getInfo();
                    stock a = (stock) this.clone();
                    que p = new que(a);
                    if((end-start) < a.t0*1000)
                    {
                        Thread.sleep((a.t0*1000)-(end-start));
                        try{
                        Head = Head.add(p,Head);
                        }catch(NullPointerException e){Head = p;}
                        long t = System.currentTimeMillis();
                        String actionString2 = Float.toString((float) ((t-start)/1000.0f)) + " " + actionString;
                        order_out(actionString2,"order.txt");
                    }
                    else if(end-start>=a.t0*1000 && end-start<=(a.t0 + a.texp)*1000)
                    {
                        try{
                        Head = Head.add(p,Head);
                        }catch(NullPointerException e){Head = p;}
                        long t = System.currentTimeMillis();
                        String actionString2 = Float.toString((float) ((t-start)/1000.0f)) + " " + actionString;
                        order_out(actionString2,"order.txt");
                    }
                }
                catch(CloneNotSupportedException | InterruptedException ex){Logger.getLogger(stock.class.getName()).log(Level.SEVERE, null, ex);}
            }
       }    
}

class que implements Cloneable
{
    public stock data;
    public que next;
    public static int size=0;
    
    public que(stock a)
    {
        data = a;
        next = null;
    }
    
    public synchronized que add(que p, que Head)
    {
        que k = Head;
        while(k.next!=null)
        {
            k = k.next;
        }
        k.next = p;
        size++;
        return Head;
    }
    
    public synchronized static que del(que Head)
    {
        if(Head!=null)
        {
        Head = Head.next;
        size--;
        }
        return Head;
    }
    public synchronized void getQue(que Head)
    {
        que q = Head;
        while(q!=null)
        {
           System.out.println(q.data.getString());
            q = q.next;
        }
    }
}

class list
{
    public stock data;
    public list next;
    private static int len = 0;
    
    public list(stock a)
    {
        data = a;
        next = null;
    }
    
    public synchronized list add(list p, list Head)
    {
        list k = Head;
        while(k.next!=null)
        {
            k = k.next;
        }
        k.next = p;
        len++;
        return Head;
    }
    
    public synchronized list del(list Head, int index)
    {
        if(index==0)
        {
            if(len!=0)
            {
                Head = Head.next;
                len--;
            }
        }
        else if(index==len-1)
        {
            list q = Head;
            for(int i=0;i<len-2;i++)
            {
                q = q.next;
            }
            q.next = null;
            len--;
        }
        else
        {
            list q = Head;
            for(int i=0;i<index-1;i++)
            {
                q = q.next;
            }
            q.next = (q.next).next;
            len--;
        }
        return Head;
    }

    public synchronized list del(list Head, list target)
    {
        if(target==Head)
        {
            if(Head!=null)
            {
                Head = Head.next;
                len--;
            }
        }
        else
        {
            list l = Head;
            while(l.next!=target)
            {
                l = l.next;
            }
            if(l.next.next==null)
            {
                l.next = null;
                len--;
            }
            else
            {
                l.next = l.next.next;
                len--;
            }
        }
        return Head;
    }

    public synchronized void getList(list Head)
    {
        list q = Head;
        while(q!=null)
        {
            System.out.println(q.data.getString());
            q = q.next;
        }
    }

    public synchronized void traversal(list Head, list node) throws Exception
    {
        long ctime = System.currentTimeMillis();
        if(ctime - stock.start> (node.data.t0 + node.data.texp)*1000)
        {
            node.data.isExpired = true;
            Exception myException = new Exception();
            throw myException;
        }
        int r = node.data.qty;
        int profit = 0;
        while(r>0)
        {
            list node_max = null;
            list p = Head;
            while(p!=null)
            {
                //try to find the maximum suitable match
                long cTime = System.currentTimeMillis();
                if(cTime - stock.start> (p.data.t0 + p.data.texp)*1000)
                {
                    p.data.isExpired = true;
                }
                if(cTime-stock.start>(node.data.t0 + node.data.texp)*1000)
                {
                    node.data.isExpired = true;
                }
                if(p.data.stock_name.equals(node.data.stock_name) && p.data.isExpired==false && node.data.isExpired==false && node.data.satisfied==false && p.data.satisfied==false)
                {
                    if(node.data.type.equals("sell"))
                    {
                        if(p.data.price>=node.data.price)
                        {
                            if(node.data.partial==true)
                            {
                                if(p.data.partial==true)
                                {
                                    if(p.data.qty<node.data.qty)
                                    {
                                        int pft = (p.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else if(p.data.qty==node.data.qty)
                                    {
                                        int pft = (p.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else
                                    {
                                        int pft = (node.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                                else if(p.data.partial==false)
                                {
                                    if(p.data.qty<node.data.qty)
                                    {
                                        int pft= (p.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else if(p.data.qty==node.data.qty)
                                    {
                                        int pft = (p.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                            }
                            else if (node.data.partial == false)
                            {
                                if(p.data.partial==true)
                                {
                                    if(p.data.qty>node.data.qty)
                                    {
                                        int pft= (node.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else if(p.data.qty==node.data.qty)
                                    {
                                        int pft= (node.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                                else if(p.data.partial == false)
                                {
                                    if(p.data.qty==node.data.qty)
                                    {
                                        int pft= (node.data.qty)*(p.data.price-node.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(node.data.type.equals("buy"))
                    {
                        if(p.data.price<=node.data.price)
                        {
                            if(node.data.partial==true)
                            {
                                if(p.data.partial==true)
                                {
                                    if(p.data.qty<node.data.qty)
                                    {
                                        int pft= (p.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else if(p.data.qty==node.data.qty)
                                    {
                                        int pft= (p.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else
                                    {
                                        int pft= (node.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                                else if(p.data.partial==false)
                                {
                                    if(p.data.qty<node.data.qty)
                                    {
                                        int pft= (p.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else if(p.data.qty==node.data.qty)
                                    {
                                        int pft= (p.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                            }
                            else if(node.data.partial==false)
                            {
                                if(p.data.partial==true)
                                {
                                    if(p.data.qty>node.data.qty)
                                    {
                                        int pft= (node.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                    else if(p.data.qty==node.data.qty)
                                    {
                                        int pft= (p.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                                else if(p.data.partial==false)
                                {
                                    if(p.data.qty==node.data.qty)
                                    {
                                        int pft= (p.data.qty)*(node.data.price-p.data.price);
                                        if(pft>=profit)
                                        {
                                            profit = pft;
                                            node_max = p;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                  p = p.next;
            }
            if(node_max==null)
            {
                break;
            }
            else
            {
                    //automatically node_max is from buy
                //System.out.println(node.data.getString());
                //System.out.println(node_max.data.getString());
                //System.out.println(profit);
                    if(node.data.partial==true)
                    {
                        if(node_max.data.partial==true)
                        {
                            if(node.data.qty<node_max.data.qty)
                            {
                                Exchange.profit += profit;
                                long ct = System.currentTimeMillis();
                                float ct1 = (float)(ct-stock.start)/1000.0f;
                                String a = "T " + Float.toString(ct1)+ " " + Integer.toString(node.data.qty) + " " + node.data.getString();
                                stock.order_out(a,"exchange.txt"); 
                                node_max.data.qty -= node.data.qty;
                                node.data.qty = 0;
                                node.data.satisfied = true;
                                r = 0;
                            }
                            else if(node.data.qty==node_max.data.qty)
                            {
                                Exchange.profit += profit;
                                long ct = System.currentTimeMillis();
                                float ct1 = (float)(ct-stock.start)/1000.0f;
                                String a = "T " + Float.toString(ct1)+ " " + Integer.toString(node_max.data.qty) + " " + node.data.getString();
                                stock.order_out(a,"exchange.txt");
                                node_max.data.qty -= node.data.qty;
                                node.data.qty = 0;
                                node.data.satisfied = true;
                                node_max.data.satisfied = true;
                                r = 0;
                            }
                            else
                            {
                                Exchange.profit += profit;
                                long ct = System.currentTimeMillis();
                                float ct1 = (float)(ct-stock.start)/1000.0f;
                                String a = "T " + Float.toString(ct1)+ " " + Integer.toString(node_max.data.qty) + " " + node.data.getString();
                                stock.order_out(a,"exchange.txt");
                                node.data.qty -= node_max.data.qty;
                                node_max.data.qty = 0;
                                node_max.data.satisfied = true;
                                r = node.data.qty;
                            }
                        }
                        else
                        {
                            if(node_max.data.qty<node.data.qty)
                                {
                                    Exchange.profit += profit;
                                    long ct = System.currentTimeMillis();
                                    float ct1 = (float)(ct-stock.start)/1000.0f;
                                    String a = "T " + Float.toString(ct1)+ " " + Integer.toString(node_max.data.qty) + " " + node.data.getString();
                                    stock.order_out(a,"exchange.txt");
                                    node.data.qty -= node_max.data.qty;
                                    node_max.data.qty = 0;
                                    node_max.data.satisfied = true;
                                    r = node.data.qty;
                                }
                                else if(node_max.data.qty==node.data.qty)
                                {
                                    Exchange.profit += profit;
                                    long ct = System.currentTimeMillis();
                                    float ct1 = (float)(ct-stock.start)/1000.0f;
                                    String a = "T " + Float.toString(ct1)+ " " + Integer.toString(node_max.data.qty) + " " + node.data.getString();
                                    stock.order_out(a,"exchange.txt");
                                    node.data.qty = 0;
                                    node_max.data.qty = 0;
                                    node_max.data.satisfied = true;
                                    node.data.satisfied = true;
                                    r = 0;
                                }
                        }
                    }
                    else
                    {
                        if(node_max.data.partial==true)
                        {
                            if(node_max.data.qty>node.data.qty)
                                {
                                    Exchange.profit += profit;
                                    long ct = System.currentTimeMillis();
                                    float ct1 = (float)(ct-stock.start)/1000.0f;
                                    String a = "T " + Float.toString(ct1)+ " " + Integer.toString(node.data.qty) + " " + node.data.getString();
                                    stock.order_out(a,"exchange.txt");
                                    node_max.data.qty -= node.data.qty;
                                    node.data.qty = 0;
                                    node.data.satisfied = true;
                                    r = 0;
                                }
                                else if(node_max.data.qty==node.data.qty)
                                {
                                    Exchange.profit += profit;
                                    long ct = System.currentTimeMillis();
                                    float ct1 = (float)(ct-stock.start)/1000.0f;
                                    String a = "T " + Float.toString(ct1)+ " " + Integer.toString(p.data.qty) + " " + node.data.getString();
                                    stock.order_out(a,"exchange.txt");
                                    node.data.qty = 0;
                                    node_max.data.qty = 0;
                                    node_max.data.satisfied = true;
                                    node.data.satisfied = true;
                                    r = 0;
                                }
                        }
                        else
                        {
                            if(node_max.data.qty==node.data.qty)
                            {
                                Exchange.profit += profit;
                                long ct = System.currentTimeMillis();
                                float ct1 = (float)(ct-stock.start)/1000.0f;
                                String a = "T " + Float.toString(ct1)+ " " + Integer.toString(node.data.qty) + " " + node.data.getString();
                                stock.order_out(a,"exchange.txt");
                                node.data.qty = 0;
                                node_max.data.qty = 0;
                                node_max.data.satisfied = true;
                                node.data.satisfied = true;
                                r = 0;
                            }
                        }
                    }
            }
        } 
    }
}