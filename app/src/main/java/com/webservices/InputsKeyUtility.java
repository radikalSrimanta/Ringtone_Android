package com.webservices;

public class InputsKeyUtility {
	
	public static String[] generateInputsKeyArray(String[] keys){
		String[] inputs_keys = new String[keys.length];
		for(int i = 0 ;i<keys.length ; i++){
			inputs_keys[i] = "inputs[" + keys[i] + "]";
		}
		return inputs_keys;
	}
	
	public static String[] generateInputsKeyArray(String[] keys,String key_name){
		String[] inputs_keys = new String[keys.length];
		for(int i = 0 ;i<keys.length ; i++){
			inputs_keys[i] = key_name+ "[" + keys[i] + "]";
		}
		return inputs_keys;
	}
	
	
	public static String[] combineTowArray(String[] one,String[] two){
		 String[] combine= new String[one.length + two.length];  
		 System.arraycopy(one, 0, combine, 0, one.length); 
		 System.arraycopy(two, 0, combine, one.length, two.length);
		 return combine;
	}
	

}
