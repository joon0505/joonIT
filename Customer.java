package Developed;



public class Customer {
	
	public int CustomNum;
    public boolean isDelayed =false;
	public double restTime ;
	public String Status;
	public double CurEveTime;
	public Customer(){
		
	}
	
	public boolean Equals(Object obj){
		
if(obj instanceof Customer){
			
			Customer target = (Customer)obj; //����ȯ�� �����ϴٸ� Event������ ĳ�����Ͽ� target������ ���� 
			if(target.Status==this.Status && target.CustomNum == this.CustomNum) //target�̺�Ʈ�� �̸��� �ð��� ���⼭ ���ǵ� event�� �̸�, �ð��� ���ٸ� true��ȯ
				return true;
			else
				return false;
		}
		else{
			return false;
		}
	}

}
