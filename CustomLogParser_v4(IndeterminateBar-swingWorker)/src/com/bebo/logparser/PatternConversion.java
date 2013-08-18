package com.bebo.logparser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class PatternConversion {

	private TreeMap<String, String> tempMap = new TreeMap<>();
	private TreeMap<Integer, TreeMap<String, String>> patternMap = new TreeMap<>();

	public String getReqDateFormat(String conversionPattern) {

		String[] temp = conversionPattern.split("%");
		// System.out.println("aray length : " + temp.length);
		String dateType = "";
		String reqDateFormat = "";
		List<Character> charList = new ArrayList();
		
		for (int i = 0; i < temp.length - 1; i++) {

			String s = temp[i];

			// replacing anything other then alphabets with empty string
			s = s.replaceAll("[^a-zA-Z]", "");

			if (s.length() > 0) {
				if (!charList.contains(s.charAt(0)))
					charList.add(s.charAt(0));
			}

			if ( !( s.length() > 0 && s.charAt(0) == 'd') )
				continue;

			if (s.length() > 1 && !(s.charAt(1) == '{'))
				dateType = "ISO8601";

			else if (s.length() >= 2)
				dateType = s.substring(2, s.indexOf("}"));

//			System.out.println("datetype is :: " + dateType);

			refineDateFormat(dateType);
		}

		// sorted set of int to get hightest length pattern
		SortedSet<Integer> intKeySet = (SortedSet<Integer>) patternMap.keySet();

		String pattern = "";
		String orignalName = "";
		int patternLength = 0;
		int limitLengthTo = 0;

		if (patternMap.size() > 0) {
			System.out.println("mpa is " + patternMap.toString());

			// map >> [ 20, [pattern , PatternName ] ]
			patternLength = patternMap.firstKey();

			pattern = patternMap.get(intKeySet.last()).firstKey();

			reqDateFormat = pattern;

			orignalName = patternMap.get(intKeySet.last()).get(pattern);
			
			System.out.println("orignalName :: "+orignalName);

			String diffString = conversionPattern.substring(0,
					conversionPattern.indexOf(orignalName));
			
			System.out.println("diffString :: "+diffString);
			
			if (diffString.contains(".")) {
				diffString = diffString.replaceAll("[^.\\d]", "");
				System.out.println("that is " + diffString);
				limitLengthTo = Integer.valueOf(diffString.substring(diffString
						.indexOf(".") + 1));

				System.out.println("patternLength :: "+patternLength+" - limitLengthTo :: "+limitLengthTo);
System.out.println("reqDateFormat before cutting :: "+reqDateFormat);
				if (patternLength > limitLengthTo) {

					int diff = patternLength - limitLengthTo;

					reqDateFormat = pattern.substring(diff, patternLength);
				}
			} 
		}
		else
			System.out.println("map is empty no suitable date format is found");

		return reqDateFormat;
	}

	private void refineDateFormat(String orignal) {
		System.out.println("orignal is :: " + orignal);

		// clearing tempMap on each iteration, avoiding creation of new map
		// object
		if (tempMap.size() > 0)
			tempMap.clear();

		int length = 0;
		String t1 = "";

		if (orignal.equals("ISO8601")) {
			t1 = "yyyy-MM-dd HH:mm:ss,SSS";
		} else if (orignal.equals("ISO8601_BASIC")) {
			t1 = "yyyyMMdd HHmmss,SSS";
		} else if (orignal.equals("ABSOLUTE")) {
			t1 = "HH:mm:ss,SSS";
		} else if (orignal.equals("DATE")) {
			t1 = "dd MMM yyyy HH:mm:ss,SSS";
		} else if (orignal.equals("COMPACT")) {
			t1 = "yyyyMMddHHmmssSSS";
		}

		if(t1.length()>0)
		length = t1.length();
		
		// if pattern given is customized then find length of pattern according
		// to alphabets. ex. yyyy-MM-dd HH:mm:ss,SSS --> [y , M , d , H , m , s
		// , S]
		else {
			char c[] = orignal.replaceAll("[^a-zA-Z]", "").toCharArray();
			Set s = new TreeSet();
			for (int i = 0; i < c.length; i++) {
				char d = c[i];
				s.add(d);
			}
			System.out.println("proper date format is :: " + s.toString());
			length = s.size();
			t1=orignal;
		}

		// yyyy-MM-dd HH:mm:ss,SSS , ISO8601
		tempMap.put(t1, orignal);

		patternMap.put(length, tempMap);
	}

	public String getDatePattern(String reqDateFormat) {
		DateFormat df = new SimpleDateFormat(reqDateFormat);

		String datePattern = df.format(new Date()).replaceAll("[0-9]", "\\\\d");

		System.out.println("date pattern 1st conversion :: "+datePattern);
		datePattern = datePattern.replaceAll("[a-ce-zA-CE-Z]", "[a-zA-Z]");
		
		System.out.println("date pattern 2st conversion :: "+datePattern);
		
		return datePattern;
	}

}
