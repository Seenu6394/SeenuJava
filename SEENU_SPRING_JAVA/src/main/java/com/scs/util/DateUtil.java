package com.scs.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {
	
	private static final Logger logger = Logger.getLogger(DateUtil.class);
	
	protected static final String[] months= new DateFormatSymbols().getMonths();
	
	public static Date getTodayDate() {
		Date today = Calendar.getInstance().getTime();

		return today;
	}

	public static String getMonthName(String month)
	{
		return new DateFormatSymbols().getMonths()[Integer.parseInt(month) - 1];
	}
	/**
	 * MM/YYYY
	 * @param monthStr
	 * @return
	 */
	public static String getFirstOfMonth(String monthStr){
		SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
		Date date;
		try {
			date = format.parse(monthStr);
		} catch (ParseException e) {
			date = getTodayDate();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		SimpleDateFormat format1 = new SimpleDateFormat(ApiConstants.DATE_FORMAT);
		
		return format1.format(c.getTime());
		
	}
	
	
	public static String getLastDayOfMonth(String monthStr)
	{
		SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
		Date date;
		try {
			date = format.parse(monthStr);
		} catch (ParseException e) {
			date = getTodayDate();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		SimpleDateFormat format1 = new SimpleDateFormat(ApiConstants.DATE_FORMAT);		
		return format1.format(c.getTime());
				
	}
	
	public static String getCurrentDateTime() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ssZZZ");
		Date today = Calendar.getInstance().getTime();
		String loginTime = df.format(today);

		return loginTime;
	}

	public static String getCurrentDateTime(String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date today = Calendar.getInstance().getTime();
		String loginTime = df.format(today);

		return loginTime;
	}
	
	public static String getTimestamp() {
		DateFormat df = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		Date today = Calendar.getInstance().getTime();
		String loginTime = df.format(today);

		return loginTime;
	}
	
	public static String getCurrentDate() {
		DateFormat df = new SimpleDateFormat("YYYY/MM/dd");
		Date today = Calendar.getInstance().getTime();
		String loginTime = df.format(today);

		return loginTime;
	}
	
	public static String getCurrentDate(String format) {
		DateFormat df;
		if(Utility.checkNullEmpty(format)){
			df = new SimpleDateFormat(format);
		}else{
			df = new SimpleDateFormat("YYYY/MM/dd");
		}
		 
		Date today = Calendar.getInstance().getTime();
		String loginTime = df.format(today);

		return loginTime;
	}
	
	public static String getFormattedDate(String currentFormat, String format, String dateString) {

		logger.debug("DATE FOR FORMAT " + dateString + " FORMAT " + format + " CURRENT FORMAT " + currentFormat);
		if (Utility.checkNullEmpty(dateString)) {

			DateFormat dfcurrent = new SimpleDateFormat(currentFormat);
			DateFormat dfnew = new SimpleDateFormat(format);

			String result = null;
			try {
				Date d1 = dfcurrent.parse(dateString);
				logger.debug(dfcurrent.format(d1));
				result = dfnew.format(d1);

			} catch (ParseException e) {
				logger.error(Utility.getExceptionMessage(e));
			}

			logger.debug("FORMATTED DATE IS " + result);
			return result;
		} else {
			logger.debug("DATESTRING IS NULL ");
			return "";
		}
	}
	 
	
	public static long getDaysBetween(String start, String end) {
		long diff = 0l;
		try {
			if (Utility.checkNullEmpty(start) && Utility.checkNullEmpty(end)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ApiConstants.DATE_FORMAT);
				Date dateStart = simpleDateFormat.parse(start);
				Date dateEnd = simpleDateFormat.parse(end);
				logger.debug("DAY COUNT START " + start + " : END " + end);
				diff = Math.round((dateStart.getTime() - dateEnd.getTime()) / (double) 86400000);
				logger.debug("diff " + diff );
				return diff;
			} else {
				return -1;
			}

		} catch (Exception e) {
			logger.error(Utility.getExceptionMessage(e));
		}
		return -1;
	}
	
	public static String cleanDate(String dateString) {

		if (Utility.checkNullEmpty(dateString)) {
			StringBuffer sb = new StringBuffer();
			sb.append(dateString.substring(0, 10));
			sb.append(" ");
			sb.append(dateString.substring(11, 19));
			return sb.toString();
		} else {
			return "";
		}

	}
	
	public static String formattedDate(String dateText, String outFormat, String inFormat) throws ParseException {
		SimpleDateFormat inFormatSdf = new SimpleDateFormat(inFormat);
		SimpleDateFormat outFormatSdf = new SimpleDateFormat(outFormat);
		
		return outFormatSdf.format(inFormatSdf.parse(dateText));
	}
	
	public static long yearsInBetween(String startDate, String endDate, String format) {

		try {
			LocalDate date1;
			LocalDate date2;
			String outFormatStr = "yyyy-MM-dd";

			if (!Utility.checkNullEmpty(format)) {
				format = ApiConstants.DATE_FORMAT;
			}
			SimpleDateFormat inFormat = new SimpleDateFormat(format);
			SimpleDateFormat outFormat = new SimpleDateFormat(outFormatStr);

			if (Utility.checkNullEmpty(endDate)) {
				date2 = LocalDate.parse(outFormat.format(inFormat.parse(endDate)));
			} else {
				date2 = LocalDate.now();
			}

			date1 = LocalDate.parse(outFormat.format(inFormat.parse(startDate)));
			Period p = Period.between(date1, date2);

			return p.getYears();
		} catch (ParseException e) {
			logger.debug("INVALID DATE FORMAT : " + e);
		}
		return -1;
	}
	
	public static boolean isDateInFuture(String date, String format){
		try {
			
		if (!Utility.checkNullEmpty(format)) {
			format = ApiConstants.DATE_FORMAT;
		}
		String outFormatStr = "yyyy-MM-dd";
		
		SimpleDateFormat inFormat = new SimpleDateFormat(format);
		SimpleDateFormat outFormat = new SimpleDateFormat(outFormatStr);

		LocalDate date1 = LocalDate.parse(outFormat.format(inFormat.parse(date)));
		LocalDate date2 = LocalDate.now();
		
		Period p = Period.between(date2, date1);
	
		return p.getDays()>=0;
		
		} catch (ParseException e) {
			logger.debug("INVALID DATE FORMAT : " + e);
		}
		return false;
	}
	
	public static boolean checkExpiry(String date, String format) {
		SimpleDateFormat myFormat = new SimpleDateFormat(format);

		try {
			Date currentDate = myFormat.parse(getCurrentDate(format));
			Date expiryDate = myFormat.parse(date);
			long diff = expiryDate.getTime() - currentDate.getTime();
			if (diff >= 0) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			logger.debug("INVALID DATE FORMAT : " + e);
		}
		return false;
	}
	
	public static String convertTimeTo12HrFormat(String _24HourTime){
		try {
			SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
			SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
			Date _24HourDt = _24HourSDF.parse(_24HourTime);
			logger.debug(_12HourSDF.format(_24HourDt));
			return _12HourSDF.format(_24HourDt);
		} catch (Exception e) {
			logger.debug("INVALID TIME FORMAT : " + e);
		}
		return "";
	}
	


}
