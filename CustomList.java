package Developed;
import java.util.ArrayList;
import java.util.List;


public class CustomList {

	public List<Customer> _Customs ;
	
public CustomList(){
		
		_Customs = new ArrayList<Customer>();
	}

public void Initialize(){
	_Customs.clear();
}



public void AddEvent(int cusNum, String status ,  double eventTime){        
 	
	Customer nextCustom = new Customer();
	nextCustom.CustomNum = cusNum;
	nextCustom.Status = status;
	nextCustom.CurEveTime = eventTime;
	
	
	if(_Customs.size()==0){
		
		_Customs.add(nextCustom);
	} 
	else{
		boolean isAdded = false;
		for(int i = 0; i< _Customs.size();i++){
			
			Customer c = _Customs.get(i);
			if(nextCustom.CurEveTime<=c.CurEveTime){
				_Customs.add(i, nextCustom);
				isAdded = true;
				break;
			}
		}
		if(!isAdded)
			_Customs.add(nextCustom);
	}
}

public void AddEvent(int cusNum, String status ,  double eventTime, double restTime){        
 	
	Customer nextCustom = new Customer();
	nextCustom.CustomNum = cusNum;
	nextCustom.Status = status;
	nextCustom.CurEveTime = eventTime;
	nextCustom.restTime = restTime;
	nextCustom.isDelayed = true;
	
	if(_Customs.size()==0){
		
		_Customs.add(nextCustom);
	} 
	else{
		boolean isAdded = false;
		for(int i = 0; i< _Customs.size();i++){
			
			Customer c = _Customs.get(i);
			if(nextCustom.CurEveTime<=c.CurEveTime){
				_Customs.add(i, nextCustom);
				isAdded = true;
				break;
			}
		}
		if(!isAdded)
			_Customs.add(nextCustom);
	}
}




public Customer NextCustomer(){
	
	Customer temp_Custom = null;
	if(_Customs.size()>0){
		temp_Custom = _Customs.get(0);
		_Customs.remove(0);
	}
	
	return temp_Custom;
}






public void RemoveCustomer(int cusNum, String status){
	
	Customer CancelEvent = null;
	for(int i = 0; i< _Customs.size();i++){
		Customer c = _Customs.get(i);
		if(c.Status == status && c.CustomNum == cusNum){
			CancelEvent = c;
			break;
		}
	}
	if(CancelEvent != null){
		_Customs.remove(CancelEvent);
	}
}

public String toString(){
	
	String fel = "";
	for (int i = 0; i < _Customs.size(); i++)
    {
        if (i != 0)
            fel += ", ";
        fel += "<" +_Customs.get(i).CustomNum+", "+ _Customs.get(i).Status + ", " +(Math.round(_Customs.get(i).CurEveTime*100)/100.0) + ">";
    }

    return fel;
	
}

public String ToString2()
{
    String fel = "";

    //FEL�� �ϳ��� member���� ������ �ִٸ� 1st FEL�� info�� String type���� ��ȯ�ؾ���
    int num;
    if (_Customs.size() < 2)
        num = _Customs.size();
    else
        num = 2;

    for (int i = 0; i < num; i++)
    {
        if (i != 0)
            fel += ", ";
        fel += "<" +_Customs.get(i).CustomNum+", "+ _Customs.get(i).Status + ", " + (Math.round(_Customs.get(i).CurEveTime*100)/100.0) + ">";
    }

    return fel;
}







}







