package Developed;

public class MainProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Simulation sim = new Simulation();
		//long strt = System.currentTimeMillis();		
		try{
			sim.Run(420);   //End of Simulation Time 을 인자로 전달하면서 Run //평일 하루 7시간 근무 420분
		}
		catch(Exception e){
			
			System.out.println(e.toString());
			System.out.println(e.getStackTrace());
			
		}
		
		
		
		
		
		
	}
	}


