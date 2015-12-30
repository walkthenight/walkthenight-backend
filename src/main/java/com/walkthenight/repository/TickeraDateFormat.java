package com.walkthenight.repository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TickeraDateFormat {

	public static String toString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
		return df.format(date);
	}

}
