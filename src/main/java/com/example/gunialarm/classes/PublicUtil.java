package com.example.gunialarm.classes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.ui.ModelMap;

public class PublicUtil {
	public static double toFixed(double val, int num){
    	BigDecimal fix = new BigDecimal(String.valueOf(val)).setScale(num, RoundingMode.HALF_DOWN);
    	return fix.doubleValue();
    }

    public static String getClientIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	    }

	    return ip;
	}
	
	public static boolean isNull(String str){
		return str == null || str.isEmpty() || str.equals("null") || str.equals("NaN") || str.equals("undefined");
	}

	public static boolean isValidEmail(String email) { 
		boolean err = false; 
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"; 
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(email); 
		if(m.matches()) { err = true; } 
		return err;
	}
	
	public static boolean isValidId(String id) { 
		if (id.length() > 10) {
            return false;
        }
		return true;
	}

	public static boolean isValidPassword(String pw) { 
		if (pw.length() < 8 || pw.length() > 32) {
            return false;
        }

        String pattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]+$";
        return pw.matches(pattern);
	}
	
	public static String getTempPassword(int length) {
		int index = 0;
		char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z' };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			index = (int) (charArr.length * Math.random());
			sb.append(charArr[index]);
		}
		return sb.toString();
	}

	public static boolean isValidPhone(String str) {
	    return Pattern.matches("^\\d{2,3}\\d{3,4}\\d{4}$", str);
	}

	public static String getTempNumber(int length) {
		int index = 0;
		char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			index = (int) (charArr.length * Math.random());
			sb.append(charArr[index]);
		}
		return sb.toString();
	}

	//Map<String, Object> 를 JSONObject로 변환
    public static JSONObject convertMapToJson(Map<String, Object> map) {
    
        JSONObject json = new JSONObject();
        String key = "";
        Object value = null;
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            key = entry.getKey();
            value = ""+entry.getValue();
            json.put(key, value);
        }
        return json;
    }
    //List<Map<String, Object>> 를 JsonArray 변환
    public static JSONArray convertMapListToJsonArray(List<Map<String, Object>> list) {
    
        JSONArray array = new JSONArray();
        for(Map<String, Object> data : list){
            array.add(convertMapToJson(data));
        }
        return array;
    }

	public static void setDate(HttpServletRequest request) {
		String sdate = request.getParameter("sdate");
		String edate = request.getParameter("edate");

		try {
			LocalDateTime today = LocalDateTime.now();
			if (isNull(sdate) && isNull(edate)) {
				sdate = "";
				edate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
			else if (!isNull(sdate) && isNull(edate)) {
				edate = sdate;
			}
			else if (!isNull(sdate) && !isNull(edate)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date sdateDt = sdf.parse(sdate);
				Date edateDt = sdf.parse(edate);
				
				if (sdateDt.after(edateDt)) {
					String temp = sdate;
					sdate = edate;
					edate = temp;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		System.out.println("11 sdate : "+sdate+" / edate : "+edate);
		request.setAttribute("sdate", sdate);
		request.setAttribute("edate", edate);
	}

	public static String dateFormat(LocalDateTime dateTime, boolean enter, boolean date, boolean time, boolean sec){
		if(isNull(""+dateTime)){
			return "-";
		}

		String datePattern = "yyyy-MM-dd";
		String timiPattern = "HH:mm";
		if(sec){
			timiPattern += ":ss";
		}

		if(date && time && enter){
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timiPattern);

			return dateTime.format(dateFormatter)+"<br>"+dateTime.format(timeFormatter);
		}
		else{
			String pattern = "";
			if(date){
				pattern += datePattern;
			}
			if(time){
				if(pattern.length()>0){
					pattern += " ";
				}
				pattern += timiPattern;
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			return dateTime.format(formatter);
		}
	}

	public static boolean checkUserSession(String userIdx, HttpSession session){
		if(userIdx == null || userIdx.isEmpty()) return false;
		return userIdx.equals(""+session.getAttribute("userIdx"));
	}

	public static JSONObject parseJsonObject(String str){
		JSONObject obj = new JSONObject();
		try{
			obj = (JSONObject) new JSONParser().parse(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
}