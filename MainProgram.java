package Developed;

public class MainProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Simulation sim = new Simulation();
		//long strt = System.currentTimeMillis();		
		try{
			sim.Run(420);   //End of Simulation Time �� ���ڷ� �����ϸ鼭 Run //���� �Ϸ� 7�ð� �ٹ� 420��
		}
		catch(Exception e){
			
			System.out.println(e.toString());
			System.out.println(e.getStackTrace());
			
		}
		
		
		
		
		
		
	}
	}


