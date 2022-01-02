package Developed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Simulation {

	public int Server;
    public int WorkingServer;
    public int AvoidCustom;
    public boolean isBreaked;
    public boolean isLoanBreaked;
	    
    public int LoanCusNum;
    public int LoanServer;
	
	
	public double CLK;
	public CustomList FEL;
	
	
	public double Before;
	public double SumQ;
	public double SumM;
	public double BBefore;
	public double Util;
	public double AQL;
	public int QOut;
	public int SysOut;
	public int ExtraQOut;
	public int ExtraSysOut;
	public int Arr;
	public int partNum;
	public double WaitingTimeinQue;
    public double WaitingTimeinSystem;
    public double MaxTimeinQue=0;
    public double MaxTimeinSystem=0;
    public double ExtraWaitingTimeinQue;
    public double ExtraWaitingTimeinSystem;
    public final int ArrayLength = 1000;
    List<Customer> Ques = new ArrayList<Customer>();
    List<Customer> LoanQues = new ArrayList<Customer>();
  
    
    double [] LoadList = new double[ArrayLength];
    double [] ArrList =  new double[ArrayLength];
    double [] UnloadList = new double[ArrayLength];
   
    
    double [] DelLoadList = new double[ArrayLength];
    double [] DelUnloadList = new double[ArrayLength];
	
	
	
	public Random R;
	
	public double getAverageQueueLength(){
		return AQL;
	}
	
	public double getUtilization(){
		return Util;
	}
	
	
	
	public Simulation(){
		
	}
	
	
	public void Run(double eosTime){
		CLK = 0.0;
		Execute_Initialize_routine(CLK);
		
		Customer nextCustom = new Customer();
		System.out.printf("%6s \t %s \t %6s \t %s \t %s \t %s %n","Customer Number","EventTime", "EventName","Q(t)", "B(t)", "FutureEventList");
		
		while(CLK<eosTime){
			
			nextCustom = Retrieve_Event();
			CLK = nextCustom.CurEveTime;
			
			if(CLK > 150 && isBreaked){
				if(Server>4){
					Execute_Break_event_routine(CLK);
				}
				
			}
			if(CLK > 180 && isLoanBreaked){
				if(LoanServer>2){
					Execute_LoanBreak_event_routine(CLK);
				}
				
			}
			
			
			
			switch(nextCustom.Status){
			case "Arrive": {Execute_Arrive_event_routine(nextCustom,CLK);break;}
			case "Load":   { Execute_Load_event_routine(nextCustom,CLK);break; }
            case "Unload": { Execute_Unload_event_routine(nextCustom,CLK); break; }
            case "Avoid": {Execute_Avoid_event_routine(nextCustom,CLK);break;}
            case "BreakEnd": {Execute_BreakEnd_event_routine(CLK);break;}
            case "FinalBreakEnd": {Execute_FinalBreakEnd_event_routine(CLK);break;}
            case "LoanBreakEnd":{Execute_LoanBreakEnd_event_routine(CLK);break;}
            case "LoanFinalBreakEnd":{Execute_LoanFinalBreakEnd_event_routine(CLK);break;}
            case "LoanArrive":{Execute_LoanArrive_event_routine(nextCustom,CLK);break;}
            case "LoanLoad":{Execute_LoanLoad_event_routine(nextCustom,CLK);break;}
            case "LoanUnload":{Execute_LoanUnload_event_routine(nextCustom,CLK);break;}
            case "DelArrive":{Execute_DelArrive_event_routine(nextCustom,CLK,nextCustom.restTime);break;}
            case "DelLoad":{Execute_DelLoad_event_routine(nextCustom,CLK);break;}
            case "DelUnload":{Execute_DelUnload_event_routine(nextCustom,CLK);break;}
			}
			
        
			if(nextCustom.Status == "LoanArrive"||nextCustom.Status == "LoanLoad"||nextCustom.Status == "LoanUnload"){
				System.out.printf("%06d \t\t\t %f \t %6s \t %d \t %d \t %s %n", nextCustom.CustomNum ,(Math.round(CLK*100)/100.0), nextCustom.Status,LoanQues.size(), LoanServer, FEL.toString());
			}else if(nextCustom.Status == "DelArrive"||nextCustom.Status == "DelLoad"||nextCustom.Status == "DelUnload"){
				System.out.printf("***%3d \t\t\t %f \t %6s \t %d \t %d \t %s %n", nextCustom.CustomNum ,(Math.round(CLK*100)/100.0), nextCustom.Status,LoanQues.size(), LoanServer, FEL.toString());
			}else{
			System.out.printf("%6d \t\t\t %f \t %6s \t %d \t %d \t %s %n", nextCustom.CustomNum ,(Math.round(CLK*100)/100.0), nextCustom.Status,Ques.size(), Server, FEL.toString());
			}
			
			
			 try {
				Thread.sleep(0);            //controlling program ouput time function
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		Execute_Statistics_routine(nextCustom,CLK);
		getAverageTime();
		
		
	}
public void Schedule_Event(int cusNum, String status, double evetime){
		
		FEL.AddEvent(cusNum,status,evetime);
	}

public void Schedule_Event(int cusNum, String status, double evetime,double restTime){
	
	FEL.AddEvent(cusNum,status,evetime,restTime);
}



public Customer Retrieve_Event()
{
    Customer nextEvent = null;
    nextEvent = FEL.NextCustomer();
    return nextEvent;
}

public void Cancel_Customer(int cusNum ,String status)
{
    FEL.RemoveCustomer(cusNum,status);
}

public void Execute_Initialize_routine(double Now) 
{
    //FEL , Q, M �ʱ�ȭ
    FEL = new CustomList();
    isBreaked = true;
    isLoanBreaked = true;
    
    Server = 7; //"���� ������"  Machine�� 1�� ����
    WorkingServer = 0;
    SysOut=0;
    QOut=0;
    AvoidCustom = 0;
    ExtraQOut = 0;
    ExtraSysOut = 0;
    
    //Initialize the state variables for collecting statistics
   
    Before = 0; 
    BBefore=0;
    SumM=0;
    SumQ = 0;
    partNum=1;
    ///////////////////////���� �� ���� ����
    LoanCusNum = 1;
    LoanServer = 3;
    

    //Initialize Random variate R
    R = new Random();

    //Schedule Arrive event 
    Schedule_Event(partNum,"Arrive", Now);
    Schedule_Event(LoanCusNum,"LoanArrive",Now+1);
    
    
  
}

public void Execute_Arrive_event_routine(Customer curCus, double Now) 
{        
	SumQ += Ques.size() * (Now - Before); 
    Before = Now;        
    Arr++;
    partNum++;
    double ta = Exp(1.63); // ���� ���� �� �ð� ������ ����
    Schedule_Event(partNum,"Arrive", Now + ta); // ���� �ùķ��̼� time + �ð� ���� 
    ArrList[partNum]= Now+ta;


    if (Server > 0){
        Schedule_Event(curCus.CustomNum,"Load", Now);
        LoadList[curCus.CustomNum]=Now;

    }
    
    if(Server==0){
    	 Ques.add(curCus);
    	 
 
    	 if(Ques.size()<=SumQ/Now){      //Que�� ���̿� ���� ����Ȯ���� �޸���
    	 
    	if(Math.random()<0.005) 
    		Schedule_Event(curCus.CustomNum,"Avoid",(Now+1+Math.random()));
    	
    	 }else{
    		 if(Math.random()<0.02)
    	    		Schedule_Event(curCus.CustomNum,"Avoid",(Now+1+Math.random()));
    	 }
    	
    }
   
    
    
   
    
}





public void Execute_Load_event_routine(Customer curCus, double Now) 
{
	SumQ += Ques.size() * (Now - Before); 
	SumM += WorkingServer*(Now - BBefore);
    BBefore = Now;
    Before = Now;
    QOut++;
    Server--;
    WorkingServer++;
    
    double ts = Exp(7);
    
    if(ts>20){                            //���� ���� ��
    	LoadList[curCus.CustomNum]=Now;
        
        Schedule_Event(curCus.CustomNum,"Unload",Now+20);	
        Schedule_Event(curCus.CustomNum,"DelArrive",Now+20, ts-20);
        Cancel_Customer(curCus.CustomNum , "Avoid"); // ���� ������ ���� �Ǿ 1�а� ��⿭���� ����ϴ� �߿� ���񽺰� ���� �Ǹ� �̺�Ʈ ����Ʈ���� �ش� ���� "Avoid"�̺�Ʈ�� ���;
        
    }else{
    
     
       LoadList[curCus.CustomNum]=Now;
       
   
       Schedule_Event(curCus.CustomNum,"Unload", Now + ts);
    
      Cancel_Customer(curCus.CustomNum , "Avoid");
   }
}

private void Execute_Unload_event_routine(Customer curCus, double Now) 
{
	SumM += WorkingServer*(Now - BBefore);
    BBefore = Now;
    Server++; //���� ������ ������ ������ �÷���
    WorkingServer--;
    
    SysOut++;
    UnloadList[curCus.CustomNum] = Now;

    if (Ques.size() >0){
    	int waitingCusNum = Ques.get(0).CustomNum;
    	Ques.remove(0);
        Schedule_Event(waitingCusNum,"Load", Now); //Load ��� ����ǥ�ÿ� ���Ҿ� ���� �ùķ��̼� Ÿ�� üũ // �� ��⿭�� ��� ��Ʈ�� �ִٸ�
    }
}

public void Execute_Avoid_event_routine(Customer curCus, double Now){
	AvoidCustom++;
	SumQ += Ques.size() * (Now - Before); 
	for(int i = 0 ; i<Ques.size();i++){
		if(Ques.get(i).CustomNum == curCus.CustomNum){
			Ques.remove(i);
		}
	}	
}
////////////////////////////////////////////////////////////////////////////Account Task

public void Execute_LoanArrive_event_routine(Customer curCus, double Now){
	
	
    LoanCusNum++;
    double ta = Exp(30); // ���� ���� �� �ð� ������ ����
    Schedule_Event(LoanCusNum,"LoanArrive", Now + ta); // ���� �ùķ��̼� time + �ð� ���� 

    if (LoanServer > 0){
        Schedule_Event(curCus.CustomNum,"LoanLoad", Now);
        

    }
    
    if(LoanServer==0){
    	 LoanQues.add(curCus);
	
    }
    

}

public void Execute_LoanLoad_event_routine(Customer curCus, double Now){

    LoanServer--;
    
    double ts = Exp(40);
    
    Schedule_Event(curCus.CustomNum,"LoanUnload", Now + ts);
    
	
}

public void Execute_LoanUnload_event_routine(Customer curCus, double Now){
	
    LoanServer++; //���� ������ ������ ������ �÷���
   

    if (LoanQues.size() >0){
    	int waitingLoanCusNum = LoanQues.get(0).CustomNum;
    	
    	if(LoanQues.get(0).isDelayed==false){
            Schedule_Event(waitingLoanCusNum,"LoanLoad", Now);
        	}else{
        		Schedule_Event(waitingLoanCusNum,"DelLoad",Now,LoanQues.get(0).restTime);
        	}
    	LoanQues.remove(0);
       
    }
}


////////////////////////////////////////////////////////////////////////////Counseling Task

public void Execute_DelArrive_event_routine(Customer curCus, double Now, double resttime){
	if (LoanServer > 0){
        Schedule_Event(curCus.CustomNum,"DelLoad", Now, resttime );
        

    }
    
    if(LoanServer==0){
    	 LoanQues.add(curCus);
	
    }
}

public void Execute_DelLoad_event_routine(Customer curCus, double Now){
	LoanServer--;
	ExtraQOut++;
	double ts = curCus.restTime;
	 DelLoadList[curCus.CustomNum]=Now;
	
	Schedule_Event(curCus.CustomNum,"DelUnload", Now+ts);
}

public void Execute_DelUnload_event_routine(Customer curCus, double Now){
	LoanServer++;
	ExtraSysOut++;
	DelUnloadList[curCus.CustomNum] = Now;
	if (LoanQues.size() >0){
    	int waitingLoanCusNum = LoanQues.get(0).CustomNum;
    	if(LoanQues.get(0).isDelayed==false){
        Schedule_Event(waitingLoanCusNum,"LoanLoad", Now);
    	}else{
    		Schedule_Event(waitingLoanCusNum,"DelLoad", Now ,LoanQues.get(0).restTime);
    	}
        LoanQues.remove(0);
    }
	
}

////////////////////////////////////////////////////////////////////////////Delayed Customer Task

public void Execute_Break_event_routine(double Now){
	Server--;
	Server--;
	Server--;
	Server--;
	isBreaked = false;
	System.out.println("============================�Ϲ� Server ������ Lunch Time Over ,�Ĺ��� ���ɽ���======================================");
	FEL.AddEvent(000, "BreakEnd", Now+60);
	FEL.AddEvent(000, "FinalBreakEnd", Now+120);
	
}

public void Execute_BreakEnd_event_routine(double Now){
	Server++;
	System.out.println("============================�Ϲ� Server ������ Lunch Time Over ,�Ĺ��� ���ɽ���======================================");
	
}

public void Execute_FinalBreakEnd_event_routine(double Now){
	Server++;
	Server++;
	Server++;
	System.out.println("============================�Ϲ� Server �Ĺ��� Lunch Time Over======================================");
	
}


public void Execute_LoanBreak_event_routine(double Now){
	LoanServer--;
	LoanServer--;
	isLoanBreaked = false;
	System.out.println("============================Loan Server ������ Lunch Time========================================");
	FEL.AddEvent(000, "LoanBreakEnd", Now+60);
	FEL.AddEvent(000, "LoanFianlBreakEnd", Now+120);
	
}

public void Execute_LoanBreakEnd_event_routine(double Now){
	LoanServer++;
	System.out.println("============================Loan Server ������ Lunch Time Over �Ĺ��� ���ɽ���======================================");
	
}

public void Execute_LoanFinalBreakEnd_event_routine(double Now){
	LoanServer++;
	System.out.println("============================Loan Server �Ĺ��� Lunch Time Over======================================");
}
////////////////////////////////////////////////////////////////////////////////////Break Time
public void Execute_Statistics_routine(Customer curCus ,double Now) {
    SumQ += Ques.size() * (Now - Before); 
    AQL = SumQ / Now;
    SumM += WorkingServer*(Now - BBefore);
    Util = SumM/Now;
    		
    
}

public void getAverageTime(){
	System.out.println("");
	System.out.println("My Statistics===========================================================================================");
	System.out.println("");
	for(int i = 1 ; i<LoadList.length;i++){
		if(LoadList[i]>=ArrList[i]){
		WaitingTimeinQue += (LoadList[i]- ArrList[i]);
		if(MaxTimeinQue<(LoadList[i]-ArrList[i]))
			MaxTimeinQue = (LoadList[i]-ArrList[i]);
		
		}
		
	}
	for(int i = 1; i<UnloadList.length;i++){
		if(UnloadList[i]>=ArrList[i]){
		WaitingTimeinSystem += (UnloadList[i]-ArrList[i]);
		if(MaxTimeinSystem<(UnloadList[i]-ArrList[i]))
			MaxTimeinSystem = (UnloadList[i]-ArrList[i]);
		
		}
	}
	
	for(int i =1; i<DelLoadList.length;i++){
		if(DelLoadList[i]>=UnloadList[i])
			ExtraWaitingTimeinQue += (DelLoadList[i]-UnloadList[i]);
	}
	
	for(int i = 1; i<DelUnloadList.length;i++){
		if(DelUnloadList[i]>=UnloadList[i])
			ExtraWaitingTimeinSystem += (DelUnloadList[i]-UnloadList[i]);
	}
	
	
	
	
	double AQL = (Math.round(getAverageQueueLength()*100))/100.0; //Average Queue Length ��� ť ���� , ��� ������� ����
	double Util = (Math.round(getUtilization()*100))/100.0;
	System.out.println("Arr: "+Arr+"\tQOut: "+QOut+"\tSysOut: "+SysOut+ "\tWaitingTimeinQue: "+ WaitingTimeinQue+ "\tWaitingTimeinSystem: "+WaitingTimeinSystem);
	System.out.println("AvoidCustomer: "+AvoidCustom);
	System.out.println("AverageWaitingTimes in Que : "+(WaitingTimeinQue/QOut)+"\tMaxWaitingTimeinQue: "+MaxTimeinQue);
	System.out.println("AverageWaitingTimes in System: "+(WaitingTimeinSystem/SysOut)+"\tMaxWaitingTimeinSystem: "+MaxTimeinSystem);
	System.out.println("Average Queue Length: "+AQL +"\tUtilization : "+Util+" Server" );
	System.out.println("Extra Que Out: "+ExtraQOut+" Extra System Out: "+ExtraSysOut);
	System.out.println("ExtraAverageWaitingTimes in Que: "+(ExtraWaitingTimeinQue/ExtraQOut)+"\tExtraAverageWaitingTimeinSystem: "+(ExtraWaitingTimeinSystem/ExtraSysOut));
	
	System.out.println("\n");
	System.out.println(isBreaked);
	System.out.println(isLoanBreaked);
	
	
	
	
	
}




public double Exp(double a) 
{
    if (a<=0) throw 
        new IllegalArgumentException("Negative value is not allowed");
    
    double u = R.nextDouble(); 
    return (-a * Math.log(u)); //0���� 1������ �ڿ��α� ���� ������ �����Ƿ� -a�� ����
}



/**public double Uni(double a, double b) 
{
    if (a>=b) throw new IllegalArgumentException("The range is not valid.");
    double u = R.nextDouble();
    return (a + (b - a) * u);
}        **/



	
	
	
}
