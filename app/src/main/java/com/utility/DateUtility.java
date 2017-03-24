package com.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtility {

	public static String convertDataToGivitDateFormatForMyAccount(String string_date){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
		outputFormat.setTimeZone(TimeZone.getDefault());
		Date date;
		try {
			formatter.setTimeZone(TimeZone.getDefault());
			date = formatter.parse(string_date);
			String outputDate = outputFormat.format(date); 
			return outputDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ""; 
	}
	
//	public static String DatetoStringconverter(Date date)
//	{
//		DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String output_date=outputFormat.format(new Date());
//		return output_date;
//	}

	public static String convertDataToGivitDateFormat(String string_date){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
		Date date;
		try {
			formatter.setTimeZone(TimeZone.getDefault());
			outputFormat.setTimeZone(TimeZone.getDefault());
			date = (Date)formatter.parse(string_date);
			String outputDate = outputFormat.format(date); 
			return outputDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ""; 
	}
	
	
	
	public static String convertGMTtoLocalFormat(String string_date){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
		DateFormat outputFormat = new SimpleDateFormat("MM.dd.yyyy");
		Date date;
		try {
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			outputFormat.setTimeZone(TimeZone.getDefault());
			date = (Date)formatter.parse(string_date);
			String outputDate = outputFormat.format(date); 
			return outputDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ""; 
	}
	
	private static Object convertGMTtoLocalTimeFormat(String string_date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
		DateFormat outputFormat = new SimpleDateFormat("hh:mm aa");
		Date date;
		try {
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			outputFormat.setTimeZone(TimeZone.getDefault());
			date = (Date)formatter.parse(string_date);
			String outputDate = outputFormat.format(date); 
			return outputDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ""; 
	}
	
	
	
	
	public static String getLocalDateTime(){
		Calendar cal = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
		 Date currentLocalTime = cal.getTime();
		 DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		 date.setTimeZone(TimeZone.getDefault()); 
		 String localTime = date.format(currentLocalTime);
		 return localTime;
	}
	
	
	public static String getCurrentDateTimeInGMT(){
		String localDateTime = "";
		 try {
			localDateTime = getLocalDateTime();
			DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			DateFormat outputdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
			
			date.setTimeZone(TimeZone.getDefault()); 
			outputdate.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			String localTime = outputdate.format(date.parse(localDateTime));
			return localTime;
		} catch (Exception e) {
			e.printStackTrace();
			return localDateTime;
		}
	}
	
	public static String convertDateTimeInGMT(String dateTime){
		//System.out.println("dateTime "+dateTime);
		//System.out.println("in Default timezone "+dateTime);
		 try {
			DateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm");  
			DateFormat outputdate = new SimpleDateFormat("MM/dd/yyyy HH:mm");   
			
			date.setTimeZone(TimeZone.getDefault()); 
			outputdate.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			String localTime = outputdate.format(date.parse(dateTime));
			//System.out.println("in GMT timezone "+localTime);
			return localTime;
		} catch (Exception e) {
			e.printStackTrace();
			return dateTime;
		}
	}
	
	public static String convertTimeInGMT(String dateTime){
		 try {
			DateFormat date = new SimpleDateFormat("hh:mm a");  
			DateFormat outputdate = new SimpleDateFormat("HH:mm");   
			
			//date.setTimeZone(TimeZone.getDefault()); 
			//outputdate.setTimeZone(TimeZone.getDefault());
			
			String localTime = outputdate.format(date.parse(dateTime));
			return localTime;
		} catch (Exception e) {
			e.printStackTrace();
			return dateTime;
		}
	}
	
	
	
	
	
	

	
	
	
	
	
//	public static boolean isAfterDate(String startdateposted , String endDateposted){
//		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//		Date d1 = null , d2 = null;
//
//		try {
//			d1 = formatter.parse(startdateposted);
//			
//			Calendar c=Calendar.getInstance();
//			c.setTime(d1);
//			
//			Calendar c1=Calendar.getInstance();
//			d2 = formatter.parse(endDateposted);
//			c1.setTime(d2);
//		
//			//System.out.println("---> d1.after(d2) "+d1.after(d2));
//			//System.out.println("---> d1.before(d2) "+d1.before(d2));
//			
//			return d1.before(d2);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public static boolean isCorrectDurationFormat(String startdateposted , String endDateposted , String startTimeposted , String endTimeposted){
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		formatter.setTimeZone(TimeZone.getDefault());
		Date d1 = null , d2 = null;
		try {
			d1 = formatter.parse(startdateposted);
			
			Calendar c=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
			c.setTime(d1);
			
			Calendar c1=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
			d2 = formatter.parse(endDateposted);
			c1.setTime(d2);
		    if(!d1.equals(d2)) {
		    	return d1.before(d2);
		    }else if(d1.equals(d2)){
		    	formatter = new SimpleDateFormat("hh:mm aa");
		    	formatter.setTimeZone(TimeZone.getDefault());
		    	d1 = formatter.parse(startTimeposted);
		    	
		    	d2 = formatter.parse(endTimeposted);
		    	return d1.before(d2);
		    }else{
		    	return false;
		    }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean isLarger(String currentDate,String startDateposted, String endDateposted ){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date presentDate = null, startDate = null, endDate = null;
		try {
			presentDate = formatter.parse(currentDate);

			startDate = formatter.parse(startDateposted);

			endDate = formatter.parse(endDateposted);

			if (presentDate.after(startDate) && presentDate.before(endDate)) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static String getEventStatus(String currentDate,String startDateposted, String endDateposted ){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date presentDate = null, startDate = null, endDate = null;
		try {
			presentDate = formatter.parse(currentDate);

			startDate = formatter.parse(startDateposted);

			endDate = formatter.parse(endDateposted);

			if (presentDate.after(startDate) && presentDate.before(endDate)) {
				return "Ongoing Event";
			}else if(presentDate.before(startDate)){
				return "Upcomming Event";
			}else if(presentDate.after(endDate)){
				return "Event Expired";
			} else
			   return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static boolean isLarger(String currentDate,String startDateposted){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date presentDate = null, startDate = null;
		try {
			presentDate = formatter.parse(currentDate);
			startDate = formatter.parse(startDateposted);
			if (presentDate.after(startDate)) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*public static boolean isLarger(String startdateposted , String endDateposted ){
		//System.out.println("feed post startdateposted "+startdateposted +"   endDateposted: "+endDateposted);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date d1 = null , d2 = null;
		try {
			d1 = formatter.parse(startdateposted);
			
			Calendar c=Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			c.setTime(d1);
			
			Calendar c1=Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			d2 = formatter.parse(endDateposted);
			c1.setTime(d2);
			if(d1.equals(d2))
				return false;
			else
				return d1.after(d2);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/
	
	
	
//	public static String convertDateToScocialNetworkingFormat(String startdateposted , String endDateposted){
//		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		formatter.setTimeZone(TimeZone.getDefault());
//		StringBuffer sBuffer = new StringBuffer("");
//		Date d1 = null , d2 = null;
//
//		try {
//			d1 = formatter.parse(startdateposted);
//			
//			Calendar c=Calendar.getInstance(TimeZone.getDefault());
//			c.setTime(d1);
//			
//			Calendar c1=Calendar.getInstance(TimeZone.getDefault());
//			d2 = formatter.parse(endDateposted);
//			c1.setTime(d2);
//			long diff = c1.getTimeInMillis()-c.getTimeInMillis();
//			long diffSeconds = diff / 1000 % 60;
//			long diffMinutes = diff / (60 * 1000) % 60;
//			long diffHours = diff / (60 * 60 * 1000) % 24;
//			long diffDays = diff / (24 * 60 * 60 * 1000);
//
//			if(diffDays>0)
//				sBuffer.append(diffDays+" days");
//			else if(diffHours>0 && diffDays<=0)
//				sBuffer.append(diffHours+" hours");
//			else if(diffMinutes>0 && diffHours<=0)
//				sBuffer.append(diffMinutes+" mins");
//			else if(diffSeconds>=0 && diffMinutes<=0)
//				sBuffer.append(diffSeconds+" secs");
//			
//			return sBuffer.toString().length() == 0?"0 secs":sBuffer.toString()+" ago";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//	}
	
	public static String convertDateToSociaVizeFormat(String startDateposted , String endDateposted){
		DateFormat formatter_GMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat formatter_DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter_DEFAULT.setTimeZone(TimeZone.getDefault());
		formatter_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		
		
		StringBuffer sBuffer = new StringBuffer("");
		Date d1 = null , d2 = null;

		try {
			//d1 = formatter.parse(startDateposted);
			
			d1 = formatter_GMT.parse(startDateposted);
			startDateposted = formatter_DEFAULT.format(d1);
			d1 = formatter_DEFAULT.parse(startDateposted);
			
			Calendar c=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
			c.setTime(d1);
			
			Calendar c1=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
			//d2 = formatter.parse(endDateposted);
			d2 = formatter_GMT.parse(endDateposted);
			endDateposted = formatter_DEFAULT.format(d2);
			d2 = formatter_DEFAULT.parse(endDateposted);
			
			
			c1.setTime(d2);
			long diff = c1.getTimeInMillis()-c.getTimeInMillis();
			
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if(diffDays < 3){
				if(diffDays == 1)
					sBuffer.append("Ending in Today");
				else if(diffDays > 1)
					sBuffer.append("Ending in "+diffDays+(diffDays > 1?" Days":" Day"));
				else if(diffDays == 0)
					sBuffer.append("Event finished");
			}else{
				//d1 = formatter.parse(startDateposted);
				sBuffer.append("Start - "+convertDataToGivitDateFormat(startDateposted));
			}
			
			
			
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
//	public static String convertDateToScocialNetworkingFormat(String dateposted){
//		if(dateposted == null || dateposted.length() == 0)
//			return "";
//		DateFormat formatter_GMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		DateFormat formatter_DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		formatter_DEFAULT.setTimeZone(TimeZone.getDefault());
//		formatter_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));
//		
//		StringBuffer sBuffer = new StringBuffer("");
//		Date d1 = null , d2 = null;
//
//		try {
//			//dateposted = DateUtility.convert
//			d1 = formatter_GMT.parse(dateposted);
//			d1 = formatter_DEFAULT.parse(formatter_DEFAULT.format(d1));
//			
//			Calendar c=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
//			c.setTime(d1);
//			
//			Calendar c1=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
//			d2 = formatter_DEFAULT.parse(getLocalDateTime());
//			c1.setTime(d2);
//			long diff = c1.getTimeInMillis() - c.getTimeInMillis();
//			
//			//System.out.println("--> c1 original time "+c1.toString()+"     local time"+c.toString());
//			System.out.println("d1 --> "+d1+"     d2--> "+d2);
//			System.out.println(dateposted+"     "+getLocalDateTime()+" diff  "+diff);
//			
//			long diffSeconds = diff / 1000 % 60;
//			long diffMinutes = diff / (60 * 1000) % 60;
//			long diffHours = diff / (60 * 60 * 1000) % 24;
//			long diffDays = diff / (24 * 60 * 60 * 1000);
//
//			System.out.println("diffSeconds "+diffSeconds+"   "+diffMinutes+"    "+diffHours+"    diffDays: "+diffDays);
//			
//			if(diffDays>0)
//				sBuffer.append(diffDays+(diffDays > 1?" Days":" Day"));
//			else if(diffHours>0 && diffDays<=0)
//				sBuffer.append(diffHours+(diffHours > 1?" Hours":" Hour"));
//			else if(diffMinutes>0 && diffHours<=0)
//				sBuffer.append(diffMinutes+(diffMinutes > 1?" Mins":" Min"));
//			else if(diffSeconds>=0 && diffMinutes<=0)
//				sBuffer.append(diffSeconds+(diffSeconds > 1?" Secs":" Secs"));
//			
//			
//			
//			
//			return sBuffer.toString().length() == 0?"0 Sec ago":sBuffer.toString()+" ago";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//	}
	
	
	public static String convertDateToScocialNetworkingFormat(String dateposted){
		if(dateposted == null || dateposted.length() == 0)
		return "";
		DateFormat formatter_GMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat formatter_DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter_DEFAULT.setTimeZone(TimeZone.getDefault());
		formatter_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));

		StringBuffer sBuffer = new StringBuffer("");
		Date d1 = null , d2 = null;

		try {
		//dateposted = DateUtility.convert
		d1 = formatter_GMT.parse(dateposted);
		d1 = formatter_DEFAULT.parse(formatter_DEFAULT.format(d1));

		Calendar c=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
		c.setTime(d1);

		Calendar c1=Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
		d2 = formatter_DEFAULT.parse(getLocalDateTime());
		c1.setTime(d2);
		long diff = c1.getTimeInMillis() - c.getTimeInMillis();

		//System.out.println("--> c1 original time "+c1.toString()+" local time"+c.toString());
		System.out.println("d1 --> "+d1+" d2--> "+d2);
		System.out.println(dateposted+" "+getLocalDateTime()+" diff "+diff);

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		System.out.println("diffSeconds "+diffSeconds+" "+diffMinutes+" "+diffHours+" diffDays: "+diffDays);
		if(diffDays == 1)
		sBuffer.append("Yesterday");
		else if(diffDays>1)
		sBuffer.append(convertGMTtoLocalFormat(dateposted));
		
//		else if(diffHours>0 && diffDays<=0)
//		sBuffer.append(diffHours+(diffHours > 1?" Hours":" Hour"));
//		else if(diffMinutes>0 && diffHours<=0)
//		sBuffer.append(diffMinutes+(diffMinutes > 1?" Mins":" Min"));
//		else if(diffSeconds>=0 && diffMinutes<=0)
//		sBuffer.append(diffSeconds+(diffSeconds > 1?" Secs":" Secs"));
		
		else{
			sBuffer.append(convertGMTtoLocalTimeFormat(dateposted));
		}

		//return sBuffer.toString().length() == 0?"0 Sec ago":sBuffer.toString()+" ago";
		return sBuffer.toString().length() == 0?"0 Sec ago":sBuffer.toString();
		
		} catch (Exception e) {
		e.printStackTrace();
		return "";
		}
		}



	

	public static boolean isSelectDateAfterCurrentDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		formatter.setTimeZone(TimeZone.getDefault());
		Date strDate = null;
		try {
			strDate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());

		Date current_date = null;
		String formattedDate = formatter.format(c.getTime());
		try {
			current_date = formatter.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return current_date.after(strDate);
	}
	
	
	public static boolean isAfterCurrentDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getDefault());
		Date strDate = null;
		try {
			strDate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
		//System.out.println("Current time => " + c.getTime());

		Date current_date = null;
		String formattedDate = formatter.format(c.getTime());
		try {
			current_date = formatter.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return current_date.after(strDate);
	}
	
	
	public static boolean isAfterTimeDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		formatter.setTimeZone(TimeZone.getDefault());
		Date strDate = null;
		try {
			strDate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
		//System.out.println("Current time => " + c.getTime());

		Date current_date = null;
		String formattedDate = formatter.format(c.getTime());
		try {
			current_date = formatter.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return current_date.after(strDate);
	}
	
//	public static boolean isAfterCurrentDate(String date) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		formatter.setTimeZone(TimeZone.getDefault());
//		Date strDate = null;
//		try {
//			strDate = formatter.parse(date);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
//		//System.out.println("Current time => " + c.getTime());
//
//		Date current_date = null;
//		String formattedDate = formatter.format(c.getTime());
//		try {
//			current_date = formatter.parse(formattedDate);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//  
//		return strDate.before(current_date);
//	}

}
