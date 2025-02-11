package com.sri.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecordAlreadyExistException extends Exception {

	RecordAlreadyExistException(String msg){
				super(msg);
	}
	
}
