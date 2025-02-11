package com.sri.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecordNotFoundException extends Exception{

	RecordNotFoundException(String msg){
		super(msg);
	}
	
}
