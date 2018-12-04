public class Exchange implements Runnable{
	//match orders
	public static int profit = 0;
	public static list Head1 = null;// for sell
    public Thread thread;
    public static list Head2 = null;// for buy
    public Exchange(Thread t)
    {
        this.thread =t ;
    }
    public void run()
    {
        // create linked lists of buy and sell
        int k=0;
        while(true)
        {
            if(stock.Head!=null)
            {
            while(stock.Head!=null)
            {
                list p = new list(stock.Head.data);
                try
                {
                    if(stock.Head.data.type.equals("sell"))
                    {
                        if(Head2!=null)
                        p.traversal(Head2,p);
                        else
                        {
                            long currentTime = System.currentTimeMillis();
                            if(currentTime - stock.start> (stock.Head.data.t0 + stock.Head.data.texp)*1000)
                            {
                                stock.Head = que.del(stock.Head);
                                continue;
                            }
                        }
                    }
                    else
                    {
                        if(Head1!=null)
                        p.traversal(Head1,p);
                        else
                        {
                            long currentTime = System.currentTimeMillis();
                            if(currentTime - stock.start> (stock.Head.data.t0 + stock.Head.data.texp)*1000)
                            {
                                stock.Head = que.del(stock.Head);
                                continue;
                            }
                        }
                    }
                }catch(Exception E)
                {
                    stock.Head = que.del(stock.Head);
                    continue;
                }
                if(p.data.satisfied==true)
                {
                    //output to the file as transaction;
                    //actually output will be handled by traversal function from time to time for every activity happens
                    //dont add to the linked list;
                    stock.Head = que.del(stock.Head);
                    continue;
                }
                if(((String)stock.Head.data.type).equals("sell"))
                {
                    try
                    {
                        Head1 = Head1.add(p, Head1);
                        long ct = System.currentTimeMillis();
                        float ct1 =(float) (ct-stock.start)/1000.0f;
                        String a = "S "+Float.toString(ct1)+" 0 "+p.data.getString();
                        stock.order_out(a,"exchange.txt");
                        stock.Head = que.del(stock.Head);
                    }catch(NullPointerException E)
                    {
                        Head1 = p;
                        long ct = System.currentTimeMillis();
                        float ct1 =(float) (ct-stock.start)/1000.0f;
                        String a = "S "+Float.toString(ct1)+" 0 "+p.data.getString();
                        stock.order_out(a,"exchange.txt");
                        stock.Head = que.del(stock.Head);
                    }
                }
                else if(stock.Head.data.type.equals("buy"))
                {
                    try{
                        Head2 = Head2.add(p, Head2);
                        long ct = System.currentTimeMillis();
                        float ct1 =(float) (ct-stock.start)/1000.0f;
                        String a = "P "+Float.toString(ct1)+" 0 "+p.data.getString();
                        stock.order_out(a,"exchange.txt");
                        stock.Head = que.del(stock.Head);
                    }catch(NullPointerException E)
                    {
                        Head2 = p;
                        long ct = System.currentTimeMillis();
                        float ct1 =(float) (ct-stock.start)/1000.0f;
                        String a = "P "+Float.toString(ct1)+" 0 "+p.data.getString();
                        stock.order_out(a,"exchange.txt");
                        stock.Head = que.del(stock.Head);
                    }
                }
            }
            }
            if(!thread.isAlive())
            {
                if(Head1==null && Head2==null)
                {
                    break;
                }
            }
        }
        String a = Integer.toString(Exchange.profit);
        stock.order_out(a,"exchange.txt");
    }
}