/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.tasks;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.celstec.arlearn2.tasks.beans.GenericBean;

public class BeanDeserialiser {

	private HttpServletRequest req;

	public BeanDeserialiser(HttpServletRequest request) {
		this.req = request;
	}

	public GenericBean deserialize() {
		GenericBean returnObject = null;
		if (req.getParameter("type") == null)
			return returnObject;
		try {
			Class beanCls = Class.forName(req.getParameter("type"));
			returnObject = (GenericBean) beanCls.getConstructor().newInstance();
			Iterator<Field> fieldIt = getRelevantBeanProperties(beanCls).iterator();
			while (fieldIt.hasNext()) {
				processField(beanCls, fieldIt.next(), returnObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnObject;
	}

	private Object parseString (Field field, String value) {
		if (value == null) return value;
		try {
		if (field.getType().getName().equals("int") || field.getType().getName().equals("java.lang.Integer")) return new Integer(Integer.parseInt(value));
		if (field.getType().getName().equals("long") || field.getType().getName().equals("java.lang.Long")) return new Long(Long.parseLong(value));
		if (field.getType().getName().equals("double") || field.getType().getName().equals("java.lang.Double")) return new Double(Double.parseDouble(value));
		if (field.getType().getName().equals("boolean") || field.getType().getName().equals("java.lang.Boolean")) return new Boolean(Boolean.parseBoolean(value));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}
	private boolean processField(Class beanCls, Field field, Object returnObject) {
		Method m;
		try {
			m = beanCls.getDeclaredMethod(getBeanMethodName(field.getName()), field.getType());
			m.setAccessible(true);
			m.invoke(returnObject, parseString(field,req.getParameter( field.getName())));
			return true;
		} catch (NoSuchMethodException e) {
			if (!beanCls.getSuperclass().equals(Object.class)) {
				return processField(beanCls.getSuperclass(), field, returnObject);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected List<Field> getRelevantBeanProperties(Class beanCls) {
		Vector<Field> returnFields = new Vector<Field>();
		Field[] fields = beanCls.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return returnFields;
		}
		for (int i = 0; i < fields.length; i++) {
			checkField(fields[i], beanCls, returnFields);
		}
		Class superClass = beanCls.getSuperclass();
		if (!superClass.equals(Object.class)) {
			List<Field> subfields = getRelevantBeanProperties(superClass);
			returnFields.addAll(subfields);
		}

		return returnFields;
	}

	protected void checkField(Field f, Class beanCls, List<Field> returnFields) {
		Class type = f.getType();
		String typeName = type.getName();

		try {
			Method m = beanCls.getDeclaredMethod(getBeanMethodName(f.getName()), f.getType());
			// log("method will be added "+m.getName());
			if (!returnFields.contains(f))
				returnFields.add(f);
		} catch (NoSuchMethodException e) {
			// log("no such method");
		} catch (SecurityException e) {
		}

	}

	protected String getBeanMethodName(String nameOfField) {
		if (nameOfField == null || nameOfField == "")
			return "";
		String method_name = "set";
		method_name += nameOfField.substring(0, 1).toUpperCase();

		if (nameOfField.length() == 1)
			return method_name;

		method_name += nameOfField.substring(1);
		return method_name;
	}

}
