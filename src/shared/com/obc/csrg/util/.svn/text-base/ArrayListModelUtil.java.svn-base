package com.obc.csrg.util;

import java.lang.reflect.Method;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;

import com.obc.csrg.model.Model;

public class ArrayListModelUtil<E extends Model> extends ArrayList<E> implements List<E>, RandomAccess, Cloneable, Serializable {

	public ArrayListModelUtil() {
		super();
	}

	public <T extends Model> ArrayListModelUtil(Set<T> list, String methodName){
		super();
		addAllModelByMethod(list,methodName);
	}
	
	public <T extends Model> void addAllModelByMethod(Set<T> list, String methodName){
		try{
			Method method=null;
			for(T t : list){
				if(method==null) method=t.getClass().getMethod(methodName);
				this.add((E)method.invoke(t));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean add(E e){
		boolean result = false;
		if(!e.isObjectInList(this)) result = super.add(e);
		return result;
	}
	
}
