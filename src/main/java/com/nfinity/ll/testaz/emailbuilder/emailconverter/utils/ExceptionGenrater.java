package com.nfinity.ll.testaz.emailbuilder.emailconverter.utils;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.nfinity.ll.testaz.emailbuilder.emailconverter.dto.response.ResponseDto;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.exception.DublicateValueException;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.exception.GenralException;


public class ExceptionGenrater {

	public static final BiFunction<String, Integer, Supplier<GenralException>> genralException = (message,
			status) -> () -> new GenralException(DataMapper.object2Json.apply(new ResponseDto<>(message, status)));

	public static final BiFunction<String, Integer, Supplier<DublicateValueException>> dublicateException = (message,
			status) -> () -> new DublicateValueException(
					DataMapper.object2Json.apply(new ResponseDto<>(message, status)));

}
