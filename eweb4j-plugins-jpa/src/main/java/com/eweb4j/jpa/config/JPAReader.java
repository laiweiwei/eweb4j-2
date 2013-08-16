package com.eweb4j.jpa.config;

import java.lang.annotation.Annotation;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

public class JPAReader {

	public final static byte TableName = 0x1;
	public final static byte JoinColumnName = 0x2;
	public final static byte JoinColumnReferenceName = 0x3;
	public final static byte ColumnName = 0x4;
	
	public static String getValue(Annotation ann, byte type){
		if (TableName == type)
			return ((Table)ann).name();
		
		if (JoinColumnName == type)
			return ((JoinColumn)ann).name();
		
		if (JoinColumnReferenceName == type)
			return ((JoinColumn)ann).referencedColumnName();
		
		if (ColumnName == type)
			return ((Column)ann).name();
		
		return null;
	}
	
}
