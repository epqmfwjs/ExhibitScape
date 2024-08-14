package com.ExhibitScape.app.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Community가 존재하지 않는 경우
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="entity not found")
public class DataNotException extends RuntimeException {
	public DataNotException(String msg) {
		super(msg);
	}
}
