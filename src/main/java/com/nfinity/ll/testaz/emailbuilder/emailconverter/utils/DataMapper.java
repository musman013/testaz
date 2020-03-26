package com.nfinity.ll.testaz.emailbuilder.emailconverter.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.gson.Gson;

public class DataMapper {

	public static final Function<Object, String> object2Json = object -> new Gson().toJson(object);

	public static final BiFunction<String, Class<?>, Object> json2Object = (json, objectClass) -> new Gson()
			.fromJson(json, objectClass);

}
