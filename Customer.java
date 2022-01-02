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
			
			Customer target = (Customer)obj; //형변환이 가능하다면 Event형으로 캐스팅하여 target변수에 저장 
			if(target.Status==this.Status && target.CustomNum == this.CustomNum) //target이벤트의 이름과 시간이 여기서 정의된 event의 이름, 시간과 같다면 true반환
				return true;
			else
				return false;
		}
		else{
			return false;
		}
	}

}
