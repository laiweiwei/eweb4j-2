package com.eweb4j.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

/**
 * 常用工具类
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-29 上午10:44:14
 */
public class CommonUtil {
	
	/**
	 * 检查字符串是否为空
	 * @date 2013-6-29 上午10:45:09
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * 获取文件名后缀
	 * @date 2013-6-29 上午10:45:30
	 * @param name
	 * @return
	 */
	public static String getFileExt(String name){
		return name.substring(name.lastIndexOf(".") + 1);
	}
	
	/**
	 * 将给定的字符串内容连接起来
	 * @date 2013-6-29 上午10:46:06
	 * @param objs
	 * @return
	 */
	public static String appendString(Object... objs) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs) {
			sb.append(obj);
		}
		return sb.toString();
	}
	
	/**
	 * 将类似 1,2,3,4,5,6 修复为: 1,2 3,4 5,6
	 * @date 2013-5-14 下午06:01:25
	 * @param _coords
	 * @return
	 */
	public static String resolveCoords(final String _coords) {
		String[] cs = _coords.split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cs.length ; i++) {
			//逢偶数就添加一个空格
			if ((i+1) % 2 == 0){
				if (sb.length() > 0)
					sb.append(" ");
				sb.append(cs[i-1]).append(",").append(cs[i]);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 获取给定多个坐标点的中心点
	 * @date 2013-5-24 下午03:09:26
	 * @param _coords 1,2 2,3 4,3
	 * @return x,y
	 */
	public static String getCenterCoord(final String _coords){
		if (_coords == null || _coords.trim().length() == 0)
			return null;
		
		try {
			String coords = _coords.trim();
			String[] pts = coords.split(" ");
			int nPts = pts.length;
			float x = 0; 
			float y = 0;
			float f;
			int j = nPts-1;
			String p1;
			String p2;
			
			for (int i = 0; i < nPts; j = i++) {
				p1 = pts[i].trim();
				if (p1.length() == 0)
					continue;
		      
				float p1_x = CommonUtil.toFloat(p1.split(",")[0]);
				float p1_y = CommonUtil.toFloat(p1.split(",")[1]);
		      
				p2 = pts[j].trim();
				if (p2.length() == 0)
					continue;
		      
				float p2_x = CommonUtil.toFloat(p2.split(",")[0]);
				float p2_y = CommonUtil.toFloat(p2.split(",")[1]);
		      
				f = p1_x * p2_y - p2_x * p1_y;
				x +=(p1_x + p2_x) * f;
				y +=(p1_y + p2_y) * f;
		   }
			
			f = CommonUtil.area(pts) * 6;
			
			return x/f + ","+ y/f;
		} catch (Throwable e) {
			return null;
		}
	}
	
	private static float area(String[] pts) {
		float area = 0;
		int nPts = pts.length;
		int j = nPts-1;
		String p1; 
		String p2;
		
		for (int i = 0; i < nPts; j = i++) {
			p1 = pts[i].trim();
			if (p1.length() == 0)
				continue;
				
			float p1_x = CommonUtil.toFloat(p1.split(",")[0]);
			float p1_y = CommonUtil.toFloat(p1.split(",")[1]);
			
			p2 = pts[j].trim();
			if (p2.length() == 0)
				continue;
			
			float p2_x = CommonUtil.toFloat(p2.split(",")[0]);
			float p2_y = CommonUtil.toFloat(p2.split(",")[1]);
			
			area += p1_x * p2_y;
			area -= p1_y * p2_x;
		}
		
		area/=2;
		return area;
	};
	
	/**
	 * 获取URL返回的字符串内容 
	 * @date 2013-6-29 上午10:47:08
	 * @param _url
	 * @return
	 */
	public static String fetchUrl(String _url) {
		return fetchUrl(_url, null);
	}
	
	/**
	 * 获取URL返回的字符串内容
	 * @date 2013-6-29 上午10:47:21
	 * @param _url
	 * @param charset
	 * @return
	 */
	public static String fetchUrl(String _url, String charset) {
		BufferedReader reader = null;
		try {
			URL url = new URL(_url);
			if (charset == null)
				reader = new BufferedReader(new InputStreamReader(url.openStream()));
			else
				reader = new BufferedReader(new InputStreamReader(url.openStream(), charset));
			
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null){
				sb.append(line);
			}
			
			return sb.toString();
		} catch (Throwable e){
			throw new RuntimeException(e);
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * 执行简单的计算表达式
	 * @date 2013-6-29 上午10:47:34
	 * @param op
	 * @param exp
	 * @return
	 */
	public static Number evalCalculateExp(String op, String exp){
		String _op = op;
		if ("*".equals(op)) _op = "\\*";
			
		String[] s = exp.split(_op);
		double left = toDouble(s[0].trim());
		double right = toDouble(s[1].trim());
		return "-".equals(op) ? left-right : ("+".equals(op) ? left+right : ("*".equals(op) ? left*right : ("/".equals(op) ? left/right : null)));
	}
	
	/**
	 * 从一个范围值里获取随机值 
	 * @date 2013-6-29 上午10:48:07
	 * @param min
	 * @param max
	 * @return
	 */
	public static Number random(double min, double max){
		return (min+(max-min)*Math.random());
	}
	
	/**
	 * 清除换行
	 * @date 2013-6-29 上午10:48:36
	 * @param str
	 * @return
	 */
	public static String cleanLF(String str) {
		return str.replace("\n", "");
	}
	
	/**
	 * 计算两个Double类型数值相加的结果
	 * @date 2013-6-29 上午10:48:54
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Double addDouble(Object d1, Object d2) {
		return toDouble(String.valueOf(d1))+toDouble(String.valueOf(d2));
	}
	
	/**
	 * 计算两个Float类型数值相加的结果
	 * @date 2013-6-29 上午10:49:23
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Float addFloat(Object d1, Object d2) {
		return toFloat(String.valueOf(d1))+toFloat(String.valueOf(d2));
	}
	
	/**
	 * 计算两个Integer类型数值相加的结果
	 * @date 2013-6-29 上午10:49:43
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Integer addInteger(Object d1, Object d2) {
		return toInt(String.valueOf(d1))+toInt(String.valueOf(d2));
	}
	
	/**
	 * 计算两个Long类型数值相加的结果
	 * @date 2013-6-29 上午10:49:52
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Long addLong(Object d1, Object d2) {
		return toLong(String.valueOf(d1))+toLong(String.valueOf(d2));
	}
	
	/**
	 * 返回给定对象的toString()结果
	 * @date 2013-6-29 上午10:50:03
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj){
		if (null == obj) return null;
		
		return String.valueOf(obj);
	}
	
	/**
	 * 将字符串转化为整数
	 * @date 2013-6-29 上午10:50:27
	 * @param str
	 * @return
	 */
	public static Integer toInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Throwable e){
			return null;
		}
	}
	
	/**
	 * 将字符串转化为长整数
	 * @date 2013-6-29 上午10:50:43
	 * @param str
	 * @return
	 */
	public static Long toLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (Throwable e){
			return null;
		}
	}
	
	/**
	 * 将字符串转化为浮点型
	 * @date 2013-6-29 上午10:50:57
	 * @param str
	 * @return
	 */
	public static Float toFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (Throwable e){
			return null;
		}
	}
	
	/**
	 * 将字符串转化为双精度浮点型
	 * @date 2013-6-29 上午10:51:09
	 * @param str
	 * @return
	 */
	public static Double toDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Throwable e){
			return null;
		}
	}
	
	/**
	 * 将字符串转化为布尔型
	 * @date 2013-6-29 上午10:51:28
	 * @param str
	 * @return
	 */
	public static Boolean toBoolean(String str) {
		try {
			return Boolean.parseBoolean(str);
		} catch (Throwable e){
			return null;
		}
	}
	
	/**
	 * 返回一个String对象 
	 * @date 2013-6-29 上午10:51:43
	 * @return
	 */
	public static String String(){
		return String("");
	}
	
	/**
	 * 将给定参数转化为String类型 
	 * @date 2013-6-29 上午10:52:04
	 * @param obj
	 * @return
	 */
	public static String String(Object obj){
		if (obj == null)
			return null;
		
		return String.valueOf(obj);
	}
	
	/**
	 * 将dom节点node输出为xml字符串内容
	 * @date 2013-6-29 上午10:52:34
	 * @param node
	 * @param keepHeader
	 * @return
	 * @throws Exception
	 */
	public static String toXml(Node node, boolean keepHeader) throws Exception{
		Transformer transformer;
    	DOMSource xmlSource = new DOMSource(node);
    	transformer = TransformerFactory.newInstance().newTransformer();
    	StringWriter writer = new StringWriter();
    	StreamResult outputTarget = new StreamResult(writer);
    	transformer.transform(xmlSource, outputTarget);
    	String str = writer.getBuffer().toString();
    	
    	if (!keepHeader)
    		return str.substring(str.indexOf("?>")+2);
    	else
    		return str;
	}
	
	/**
	 * 删除标签
	 * @date 2013-1-5 下午05:24:06
	 * @param html
	 * @param keepTags 保留的标签，如果不给定则删除所有标签
	 * @return
	 */
	public static String cleanOtherXmlTags(String html, String... keepTags) {
		return html.replaceAll(inverseXmlTagsRegex(keepTags), "");
	}
	
	/**
	 * 删除标签
	 * @date 2013-1-5 下午05:35:27
	 * @param html
	 * @param isRMCnt 是否删除标签内的所有内容 <p>This is p.<a href="#">This is a.</a></p>如果干掉a标签，就变成=><p>This is p.</p>
	 * @param delTags 需要删除的Tag，如果不给定则删除所有标签
	 * @return
	 */
	public static String cleanXmlTags(String html, boolean isRMCnt, String... delTags) {
		if (isRMCnt){
			for (String delTag : delTags){
				List<String> tag = findByRegex(html, xmlTagsRegex(delTag));
				if (tag == null || tag.isEmpty() || tag.size() != 2)
					continue;
				String regex = resolveRegex(tag.get(0)) + ".*" + resolveRegex(tag.get(1));
				html = html.replaceAll(regex, "");
			}
			return html;
		}
		
		return html.replaceAll(xmlTagsRegex(delTags), "");
	}
	
	/**
	 * 修复正则表达式中一些特殊的符号
	 * @date 2013-6-29 上午10:53:07
	 * @param regex
	 * @return
	 */
	public static String resolveRegex(String regex){
		List<String> cc = Arrays.asList("\\", "^", "$", "*", "+", "?", "{", "}", "(", ")", ".", "[", "]", "|");
		for (String c : cc) {
			regex = regex.replace(c, "\\"+c);
		}
		return regex;
	}
	
	/**
	 * 匹配除了给定标签意外其他标签的正则表达式
	 * @date 2013-1-7 下午03:45:29
	 * @param keepTags 如果不给定则匹配所有标签
	 * @return
	 */
	public static String inverseXmlTagsRegex(String... keepTags) {
		if (keepTags == null || keepTags.length == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		String fmt = "\\b%s\\b";
		StringBuilder sb = new StringBuilder();
		for (String kt : keepTags){
			if (kt == null || kt.trim().length() == 0)
				continue;
			
			if (sb.length() > 0)
				sb.append("|");
			sb.append(String.format(fmt, kt));
		}
		if (sb.length() == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		
		String pattern = "<[!/]?\\b(?!("+sb.toString()+"))+\\b\\s*[^>]*>";
		
		return pattern;
	}
	
	/**
	 * 匹配给定标签的正则表达式
	 * @date 2013-1-7 下午03:47:11
	 * @param tags 如果不给定则匹配所有标签
	 * @return
	 */
	public static String xmlTagsRegex(String... tags) {
		if (tags == null || tags.length == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		String fmt = "\\b%s\\b";
		StringBuilder sb = new StringBuilder();
		for (String kt : tags){
			if (kt == null || kt.trim().length() == 0)
				continue;
			
			if (sb.length() > 0)
				sb.append("|");
			sb.append(String.format(fmt, kt));
		}
		if (sb.length() == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		
		String pattern = "<[!/]?("+sb.toString()+")\\s*[^>]*>";
		
		return pattern;
	}
	
	/**
	 * 计算给定时间与系统当前时间的差值，返回结果用 1d 2h3m2s 这样表达 
	 * @date 2013-6-29 上午10:53:33
	 * @param start
	 * @return
	 */
	public static String calculateTime(long start){
		return calculateTime(start, System.currentTimeMillis(), "${d}d ${h}h${m}m${s}s");
	}
	
	/**
	 * 计算给定时间与系统当前时间的差值，返回结果用根据format来格式化显示
	 * @date 2013-6-29 上午10:54:28
	 * @param start
	 * @param format
	 * @return
	 */
	public static String calculateTime(long start, String format) {
		return calculateTime(start, System.currentTimeMillis(), format);
	}
	
	/**
	 * 计算给定开始、结束时间的差值，返回结果用 1d 2h3m2s 这样表达
	 * @date 2013-6-29 上午10:55:09
	 * @param start
	 * @param end
	 * @return
	 */
	public static String calculateTime(long start,long end){
		return calculateTime(start, end, "${d}d ${h}h${m}m${s}s");
	}
	
	/**
	 * 计算给定开始、结束时间的差值，返回结果根据format来格式化
	 * @date 2013-6-29 上午10:55:39
	 * @param start
	 * @param end
	 * @param format
	 * @return
	 */
	public static String calculateTime(long start, long end, String format){
		if (format == null)
			format = "${d}d ${h}h${m}m${s}s";
        long between = (end - start);// 得到两者的毫秒数
        long d = between / (24 * 60 * 60 * 1000);
        long h = (between / (60 * 60 * 1000) - d * 24);
        long m = ((between / (60 * 1000)) - d * 24 * 60 - h * 60);
        long s = (between / 1000 - d * 24 * 60 * 60 - h * 60 * 60 - m * 60);
        
		return format
				.replace("${d}", String.valueOf(d))
				.replace("${h}", String.valueOf(h))
				.replace("${m}", String.valueOf(m))
				.replace("${s}", String.valueOf(s));
	}
	
	/**
	 * 将给定的字符串时间表达式转成秒单位结果
	 * @date 2013-6-29 上午10:56:08
	 * @param strTime 比如 1d 1h1m1s (即一天一小时一分一秒)
	 * @return
	 */
	public static Float toSeconds(String strTime){
		Float time = 0F;
		for (String s : strTime.split(" ")){
			time += _toSeconds(s);
		}
		
		return time;
	}
	
	private static Float _toSeconds(String strTime){
		Float time = 0F;
		try {
			if (strTime.endsWith("s")){
				time = Float.parseFloat(strTime.replace("s", "")) * 1;
			}else if (strTime.endsWith("m")){
				time = Float.parseFloat(strTime.replace("m", "")) * 60;
			}else if (strTime.endsWith("h")){
				time = Float.parseFloat(strTime.replace("h", "")) * 60 * 60;
			}else if (strTime.endsWith("d")){
				time = Float.parseFloat(strTime.replace("d", "")) * 60 * 60 * 24;
			}else
				time = Float.parseFloat(strTime);
		} catch (Throwable e) {
			
		}
		
		return time;
	}
	
	/**
	 * 判断两个给定url是否属于同一个host
	 * @date 2013-6-29 上午10:58:05
	 * @param hostUrl
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static boolean isSameHost(String hostUrl, String url) throws Exception{
		URL siteURL = new URL(hostUrl);
		URL currURL = new URL(url);
		String siteHost = siteURL.getHost();
		String currHost = currURL.getHost();
		return siteHost.equals(currHost);
	}
	
	/**
	 * 正则匹配，只取第一个结果
	 * @date 2013-6-29 上午10:58:21
	 * @param input
	 * @param regex
	 * @return
	 */
	public static String findOneByRegex(String input, String regex){
		List<String> list = findByRegex(input, regex);
		if (list == null)
			return null;
		
		return list.get(0);
	}
	
	/**
	 * 正则匹配
	 * @date 2013-6-29 上午10:58:51
	 * @param input
	 * @param regex
	 * @return
	 */
	public static List<String> findByRegex(String input, String regex){
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(input);
		while(m.find()){
			result.add(m.group());
		}
		
		if (result.isEmpty()) return null;
		
		return result;
	}
	
	/**
	 * 将Long型转成byte数组
	 * @date 2013-6-29 上午10:59:06
	 * @param l
	 * @return
	 */
	public static byte[] long2ByteArray(long l) {
		byte[] array = new byte[8];
		int i, shift;
		for (i = 0, shift = 56; i < 8; i++, shift -= 8) {
			array[i] = (byte) (0xFF & (l >> shift));
		}
		return array;
	}

	/**
	 * 将int型转成byte数组
	 * @date 2013-6-29 上午10:59:33
	 * @param value
	 * @return
	 */
	public static byte[] int2ByteArray(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (3 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	/**
	 * 将int值放到数组中，给定位置
	 * @date 2013-6-29 上午10:59:51
	 * @param value
	 * @param buf
	 * @param offset
	 */
	public static void putIntInByteArray(int value, byte[] buf, int offset) {
		for (int i = 0; i < 4; i++) {
			int valueOffset = (3 - i) * 8;
			buf[offset + i] = (byte) ((value >>> valueOffset) & 0xFF);
		}
	}

	/**
	 * 将byte数组转成int
	 * @date 2013-6-29 上午11:00:25
	 * @param b
	 * @return
	 */
	public static int byteArray2Int(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * 将byte数组转成long
	 * @date 2013-6-29 上午11:00:45
	 * @param b
	 * @return
	 */
	public static long byteArray2Long(byte[] b) {
		int value = 0;
		for (int i = 0; i < 8; i++) {
			int shift = (8 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}
	
	/**
	 * 检查给定的ContentType是否是二进制媒体类型
	 * @date 2013-6-29 上午11:01:07
	 * @param contentType
	 * @return
	 */
	public static boolean hasBinaryContent(String contentType) {
		if (contentType != null) {
			String typeStr = contentType.toLowerCase();
			if (typeStr.contains("image") || typeStr.contains("audio")
					|| typeStr.contains("video")
					|| typeStr.contains("application")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查给定的ContentType是否是纯文本类型
	 * @date 2013-6-29 上午11:01:30
	 * @param contentType
	 * @return
	 */
	public static boolean hasPlainTextContent(String contentType) {
		if (contentType != null) {
			String typeStr = contentType.toLowerCase();
			if (typeStr.contains("text/plain")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将一段文本转成友好的SeoTitle
	 * @date 2013-6-29 上午11:02:01
	 * @param url
	 * @return
	 */
	public static String toFriendlySeoTitle(String url){
		return Normalizer.normalize(url.toLowerCase(), Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
		        .replaceAll("[^\\p{Alnum}]+", "-")
		        .replaceAll("[^a-zA-Z0-9]+$", "")
		        .replaceAll("^[^a-zA-Z0-9]+", "");
	}

	/**
	 * 获取当前时间
	 * @date 2013-6-29 上午11:02:26
	 * @return
	 */
	public static Long getNow(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 获取当前时间 
	 * @date 2013-6-29 上午11:02:43
	 * @param length 指定需要多少长度
	 * @return
	 */
	public static Long getNow(int length){
		return getTime(length, new Date());
	}
	
	/**
	 * 获取给定Date指定长度的UNIX时间戳
	 * @date 2013-6-29 上午11:03:08
	 * @param length
	 * @param date
	 * @return
	 */
	public static Long getTime(int length, Date date){
		String time = String.valueOf(date.getTime()).substring(0, length);
		return Long.parseLong(time);
	}
	
	/**
	 * 将给定字符串进行MD5加密
	 * @date 2013-6-29 上午11:03:33
	 * @param input
	 * @return
	 */
	public static String md5(final String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte[] output = md.digest();
			return bytesToHex(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return input;
	}

	/**
	 * 将byte数组内容转成十六进制
	 * @date 2013-6-29 上午11:03:50
	 * @param b
	 * @return
	 */
	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		
		return buf.toString();
	}

	/**
	 * 获取UUID
	 * @date 2013-6-29 上午11:04:08
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 修复时间格式
	 * @date 2013-6-29 上午11:04:34
	 * @param time
	 * @return
	 */
	public static String resolveTime(final String time) {
		String[] array = time.split(":");
		StringBuilder sb = new StringBuilder();
		for (String a : array) {
			if (sb.length() > 0)
				sb.append(":");

			if (a.length() == 1)
				a = new StringBuilder("0").append(a).toString();

			sb.append(a);
		}

		return sb.toString() + ":00";
	}

	/**
	 * 修复日期格式
	 * @date 2013-6-29 上午11:04:54
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date resolveDate(final String date) throws Exception {
		Date d = null;
		try {
			d = parse("yyyy-MM-dd", date);
		} catch (Throwable e1) {
			try {
				d = parse("yyyy-M-dd", date);
			} catch (Throwable e2) {
				try {
					d = parse("yyyy-MM-d", date);
				} catch (Throwable e3) {
					try {
						d = parse("yyyy-M-d", date);
					} catch (Throwable e4) {
						try {
							d = parse("MM/dd/yyyy", date);
						} catch (Throwable e5) {
							try {
								d = parse("MM/d/yyyy", date);
							} catch (Throwable e6) {
								try {
									d = parse("M/dd/yyyy", date);
								} catch (Throwable e7) {
									try {
										d = parse("M/d/yyyy", date);
									} catch (Throwable e8) {
										throw new Exception(e8);
									}
								}
							}
						}
					}
				}
			}
		}

		return d;
	}

	/**
	 * 检查时间格式
	 * @date 2013-6-29 上午11:05:28
	 * @param str
	 * @return
	 */
	public static boolean isValidTime(String str) {
		return str.matches("^\\d{2}:\\d{2}:\\d{2}$");
	}

	/**
	 * 检查日期格式
	 * @date 2013-6-29 上午11:05:46
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str) {
		return str != null ? str
				.matches("^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$") : false;
	}

	/**
	 * 检查日期+时间格式
	 * @date 2013-6-29 上午11:05:56
	 * @param source
	 * @return
	 */
	public static boolean isValidDateTime(String source){
		return isValidDateTime(source, Locale.getDefault());
	}
	
	public static boolean isValidDateTime(String source, Locale locale) {
		return isValidDateTime(source, "yyyy-MM-dd HH:mm:ss", locale);
	}

	public static boolean isValidDateTime(String source, String format){
		return isValidDateTime(source, format, Locale.getDefault());
	}
	
	public static boolean isValidDateTime(String source, String format, Locale locale) {
		try {
			Date date = parse(format, source, locale);
			return date != null;
		} catch (Throwable e) {
			return false;
		}
	}

	public static String percent(long a, long b) {
		double k = (double) a / b * 100;
		java.math.BigDecimal big = new java.math.BigDecimal(k);
		return big.setScale(2, java.math.BigDecimal.ROUND_HALF_UP)
				.doubleValue() + "%";
	}

	public static long[] changeSecondsToTime(long seconds) {
		long hour = seconds / 3600;
		long minute = (seconds - hour * 3600) / 60;
		long second = (seconds - hour * 3600 - minute * 60);

		return new long[] { hour, minute, second };
	}


	public static int getDayOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.get(Calendar.DAY_OF_YEAR);
	}

	public static int getLastDayOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	public static int getDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.get(Calendar.DAY_OF_MONTH);
	}

	public static int getLastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	// 判断日期为星期几,0为星期六,依此类推
	public static int getDayOfWeek(Date date) {
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return x;
	}

	public static int getLastDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.getActualMaximum(Calendar.DAY_OF_WEEK);
	}

	public static long difference(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		if (cal2.after(cal1)) {
			return cal2.getTimeInMillis() - cal1.getTimeInMillis();
		}

		return cal1.getTimeInMillis() - cal2.getTimeInMillis();
	}

	public static Date addSecond(Date source, int s) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.SECOND, s);

		return cal.getTime();
	}

	public static Date addMinute(Date source, int min) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.MINUTE, min);

		return cal.getTime();
	}

	public static Date addHour(Date source, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.HOUR_OF_DAY, hour);

		return cal.getTime();
	}

	public static Date addDay(Date source, int day){
		return addDate(source, day);
	}
	
	public static Date addDate(Date source, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.DAY_OF_MONTH, day);

		return cal.getTime();
	}

	public static Date addMonth(Date source, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.MONTH, month);

		return cal.getTime();
	}

	public static Date addYear(Date source, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.YEAR, year);

		return cal.getTime();
	}

	public static Date parse(String format, String source) {
		return parse(format, source, Locale.getDefault());
	}
	
	public static Locale getLocale(String lang){
		Locale[] locs = Locale.getAvailableLocales();
		for (Locale l : locs){
			if (lang.equals(l.getLanguage()))
				return l;
		}
		
		return Locale.ENGLISH;
	}
	
	public static Date parse(String format, String source, Locale locale) {
		int aaIndex = format.indexOf(" aa");
		if (aaIndex > -1){
			String apm = source.substring(aaIndex+1, aaIndex+1+2);
			format = format.replace(" aa", "");
			return parse(format, source.substring(0, aaIndex), apm, locale);
		}
		
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(format, locale);
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Date parse(String format, String source, String amOrPm){
		return parse(format, source, amOrPm, Locale.getDefault());
	}
	
	public static Date parse(String format, String source, String amOrPm, Locale locale) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(format, locale);
		try {
			Date date = sdf.parse(source);
			int HH = toInt(CommonUtil.formatTime("HH", date));
			if ("PM".equalsIgnoreCase(amOrPm)){
				if (HH <= 12)
					date = CommonUtil.addHour(date, 12);
			}else if ("AM".equalsIgnoreCase(amOrPm)){
				if (HH >= 12)
					date = CommonUtil.addHour(date, -12);
			}
			return date;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date parse(String source){
		return parse(source, Locale.getDefault());
	}
	
	public static Date parse(String source, Locale locale) {
		return parse("yyyy-MM-dd HH:mm:ss", source, locale);
	}

	public static String upperFirst(String s) {
		return s.replaceFirst(s.substring(0, 1), s.substring(0, 1)
				.toUpperCase());
	}

	public static Map<String, Object> map(String k, Object v) {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put(k, v);
		return map;
	}

	public static Map<String, Object> map(String[] keys, Object[] values) {
		Map<String, Object> map = new HashMap<String, Object>(keys.length);
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values[i]);
		}

		return map;
	}

	/**
	 * 按照给定的 by 分割字符串，然后转化成Long数组。
	 * 
	 * @param source
	 * @param by
	 * @return
	 */
	public static long[] splitToLong(String source, String by) {

		if (source == null || source.trim().length() == 0 || by == null
				|| by.trim().length() == 0)
			return null;

		String[] strs = source.split(by);
		long[] longs = new long[strs.length];
		for (int i = 0; i < strs.length; i++) {
			longs[i] = Long.parseLong(strs[i]);
		}

		return longs;
	}

	/**
	 * 按照给定的 by 分割字符串，然后转化成int数组。
	 * 
	 * @param source
	 * @param by
	 * @return
	 */
	public static int[] splitToInt(String source, String by) {

		if (source == null || by == null)
			return null;

		String[] strs = source.split(by);
		int[] ints = new int[strs.length];
		for (int i = 0; i < strs.length; i++)
			ints[i] = Integer.parseInt(strs[i]);

		return ints;
	}

	/**
	 * 格式化时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		return formatTime(null, date);
	}

	/**
	 * 格式化时间
	 * 
	 * @param format
	 *            格式，默认yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatTime(String format, Date date) {
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		String time = new java.text.SimpleDateFormat(format).format(date);
		return time;
	}
	
	public static Date newDate(){
		return new Date();
	}
	
	public static Date newDate(String pattern, String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}

	public static String formatStr(String format, Object... args) {
		return String.format(format, args);
	}

	public static boolean isValidEmail(String mail) {
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mail);

		return m.find();
	}
	
	/**
	 * 
	 * 1204,K => 1K
	 * @date 2012-12-16 上午10:13:01
	 * @param size
	 * @param format K,M,G,T
	 * @return
	 */
	public static String formatFileSize(long size, String format) {
		
		if (format.equals("K")){
			return size / 1024.0 + "K";
		}
		
		if (format.equals("M")){
			return size / 1024.0 / 1024.0 + "M";
		}
		
		if (format.equals("G")){
			return size / 1024.0 / 1024.0 / 1024.0 + "G";
		}
		
		if (format.equals("T")){
			return size / 1024.0 / 1024.0 / 1024.0 / 1024.0 + "T";
		}
		
		return size + "B";
	}
	
	public static long parseFileSize(String _size){
		if (_size.toUpperCase().endsWith("K")){
			long size = Long.parseLong(_size.toUpperCase().replace("K", ""));
			return size * 1024;
		}
		
		if (_size.toUpperCase().endsWith("M")){
			long size = Long.parseLong(_size.toUpperCase().replace("M", ""));
			return size * 1024 * 1024;
		}
		
		if (_size.toUpperCase().endsWith("G")){
			long size = Long.parseLong(_size.toUpperCase().replace("G", ""));
			return size * 1024 * 1024 * 1024;
		}
		
		return Long.parseLong(_size);
	}
	
	public static Date strToDate(String source, String pattern) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			date = format.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String dateToStr(Date source, String pattern) {
		String result = null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		result = format.format(source);
		return result;
	}

	/**
	 * 将字符串转换为数字
	 * 
	 * @param source
	 *            被转换的字符串
	 * @return int 型值
	 */
	public static int strToInt(String source) {
		int result = 0;
		try {
			result = Integer.parseInt(source);
		} catch (NumberFormatException e) {
			result = 0;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * @功能 将字符串首字母转为大写
	 * @param str
	 *            要转换的字符串
	 * @return String 型值
	 */
	public static String toUpCaseFirst(String str) {
		if (str == null || "".equals(str)) {
			return str;
		} else {
			char[] temp = str.toCharArray();
			temp[0] = str.toUpperCase().toCharArray()[0];
			str = String.valueOf(temp);
		}

		return str;
	}

	public static String toLowCaseFirst(String str) {
		if (str == null || "".equals(str)) {
			return str;
		} else {
			char[] temp = str.toCharArray();
			temp[0] = str.toLowerCase().toCharArray()[0];
			str = String.valueOf(temp);
		}

		return str;
	}

	/**
	 * 批量将英文字符串首字母转为大写
	 * 
	 * @param str
	 *            要转换的字符串数组
	 * @return 字符数组
	 */
	public static String[] toUpCaseFirst(String[] str) {
		if (str == null || str.length == 0) {
			return str;
		} else {
			String[] result = new String[str.length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = CommonUtil.toUpCaseFirst(str[i]);
			}

			return result;
		}
	}

	public static String[] toLowCaseFirst(String[] str) {
		if (str == null || str.length == 0) {
			return str;
		} else {
			String[] result = new String[str.length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = CommonUtil.toLowCaseFirst(str[i]);
			}

			return result;
		}
	}

	public static String hump2ohter(String param, String aother) {
		char other = aother.toCharArray()[0];
		Pattern p = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, other
					+ mc.group().toLowerCase());
			i++;
		}

		if (other == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}

		return builder.toString();
	}

	/**
	 * @功能 根据给定的regex正则表达式，验证给定的字符串input是否符合
	 * @param input
	 *            需要被验证的字符串
	 * @param regex
	 *            正则表达式
	 * @return boolean 型值
	 */
	public static boolean verifyWord(String input, String regex) {
		if (input == null) {
			input = "";
		}

		if (regex == null) {
			regex = "";
		}

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		boolean flag = m.matches();

		return flag;
	}

	/**
	 * @功能 转换字符串中属于HTML语言中的特殊字符
	 * @param source
	 *            为要转换的字符串
	 * @return String型值
	 */
	public static String changeHTML(String source) {
		String s0 = source.replace("\t\n", "<br />"); // 转换字符串中的回车换行
		String s1 = s0.replace("&", "&amp;"); // 转换字符串中的"&"符号
		String s2 = s1.replace(" ", "&nbsp;"); // 转换字符串中的空格
		String s3 = s2.replace("<", "&lt;"); // 转换字符串中的"<"符号
		String s4 = s3.replace(">", "&gt;"); // 转换字符串中的">"符号
		String s5 = s4.replace("\"", "&quot;"); // 转换字符串中的"\""符号
		String s6 = s5.replace("'", "&apos;"); // 转换字符串中的"'"符号
		return s6;
	}

	/**
	 * 将某些字符转为HTML标签。
	 * 
	 * @param source
	 * @return
	 */
	public static String toHTML(String source) {
		String s1 = source.replace("&amp;", "&"); // 转换字符串中的"&"符号
		String s2 = s1.replace("&nbsp;", " "); // 转换字符串中的空格
		String s3 = s2.replace("&lt;", "<"); // 转换字符串中的"<"符号
		String s4 = s3.replace("&gt;", ">"); // 转换字符串中的">"符号
		String s5 = s4.replace("<br />", "\t\n"); // 转换字符串中的回车换行
		String s6 = s5.replace("&quot;", "\""); // 转换字符串中的"\""符号
		String s7 = s6.replace("&apos;", "'"); // 转换字符串中的"'"符号

		return s7;
	}

	/**
	 * @功能 取得当前时间,给定格式
	 * @return
	 */
	public static String getNowTime(String format, Locale loc) {
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		if (loc == null)
			return new java.text.SimpleDateFormat(format).format(java.util.Calendar.getInstance().getTime());
		
		return new java.text.SimpleDateFormat(format, loc).format(java.util.Calendar.getInstance().getTime());
	}
	
	public static String getNowTime(String format){
		return getNowTime(format, null);
	}

	/**
	 * @功能 取得当前时间
	 * @return
	 */
	public static String getNowTime() {
		return getNowTime(null);
	}

	/**
	 * @功能 转换字符编码
	 * @param str
	 *            为要转换的字符串
	 * @return String 型值
	 */
	public static String toEncoding(String str, String encoding) {
		if (str == null) {
			str = "";
		}
		try {
			str = new String(str.getBytes("ISO-8859-1"), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 使一个数组的所有元素被一个“分隔符”串联起来组成一条字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String cutArrayBySepara(String[] source, String separator) {
		if (source == null || source.length == 0 || separator == null) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < source.length; ++i) {
			if (i == source.length - 1) {
				result.append(source[i]);
			} else {
				result.append(source[i]).append(separator);
			}
		}

		return result.toString();
	}

	public static boolean isNullOrEmpty(Object obj) {
		return obj == null || "".equals(obj.toString());
	}

	public static String join(Collection<?> s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator<?> iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	/**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param s
	 *            原文件名
	 * @return 重新编码后的文件名
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将utf-8编码的汉字转为中文
	 * 
	 * @param str
	 * @return
	 */
	public static String uriDecoding(String str) {
		String result = str;
		try {
			result = URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getExceptionString(Throwable e) {
		if (e == null)
			return "";
		
		StringWriter strWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(strWriter, true);
		e.printStackTrace(writer);
		StringBuffer sb = strWriter.getBuffer();
		String s = "cause by: \n\t" + sb.toString();
		
		return s.replace("Caused by:", "<font color='red'>Caused by:");
	}

	@Deprecated
	public static String getStack(StackTraceElement[] stes) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement ste : stes) {
			if (ste != null)
				sb.append("\n").append(ste.toString());
		}

		return sb.toString();
	}
	
	@Deprecated
	public static String resoveUrl(final String relativeUrl, final String baseUrl) {
		return resolveUrl(baseUrl, relativeUrl);
	}
	
	public static String resolveUrl(final String baseUrl, final String relativeUrl) {
		if (baseUrl == null) 
			return relativeUrl;
		
		if (relativeUrl == null || relativeUrl.trim().length() == 0) 
			return null;
		
//		if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://"))
//			return relativeUrl;
//			
		final Url url = resolveUrl(parseUrl(baseUrl.trim()), relativeUrl.trim());

		return url.toString();
	}

	private static Url parseUrl(final String spec) {
		final Url url = new Url();
		int startIndex = 0;
		int endIndex = spec.length();

		// Section 2.4.1: Parsing the Fragment Identifier
		//
		// If the parse string contains a crosshatch "#" character, then the
		// substring after the first (left-most) crosshatch "#" and up to the
		// end of the parse string is the <fragment> identifier. If the
		// crosshatch is the last character, or no crosshatch is present, then
		// the fragment identifier is empty. The matched substring, including
		// the crosshatch character, is removed from the parse string before
		// continuing.
		//
		// Note that the fragment identifier is not considered part of the URL.
		// However, since it is often attached to the URL, parsers must be able
		// to recognize and set aside fragment identifiers as part of the
		// process.
		final int crosshatchIndex = indexOf(spec, '#', startIndex, endIndex);

		if (crosshatchIndex >= 0) {
			url.fragment_ = spec.substring(crosshatchIndex + 1, endIndex);
			endIndex = crosshatchIndex;
		}
		// Section 2.4.2: Parsing the Scheme
		//
		// If the parse string contains a colon ":" after the first character
		// and before any characters not allowed as part of a scheme name (i.e.,
		// any not an alphanumeric, plus "+", period ".", or hyphen "-"), the
		// <scheme> of the URL is the substring of characters up to but not
		// including the first colon. These characters and the colon are then
		// removed from the parse string before continuing.
		final int colonIndex = indexOf(spec, ':', startIndex, endIndex);

		if (colonIndex > 0) {
			final String scheme = spec.substring(startIndex, colonIndex);
			if (isValidScheme(scheme)) {
				url.scheme_ = scheme;
				startIndex = colonIndex + 1;
			}
		}
		// Section 2.4.3: Parsing the Network Location/Login
		//
		// If the parse string begins with a double-slash "//", then the
		// substring of characters after the double-slash and up to, but not
		// including, the next slash "/" character is the network location/login
		// (<net_loc>) of the URL. If no trailing slash "/" is present, the
		// entire remaining parse string is assigned to <net_loc>. The double-
		// slash and <net_loc> are removed from the parse string before
		// continuing.
		//
		// Note: We also accept a question mark "?" or a semicolon ";" character
		// as
		// delimiters for the network location/login (<net_loc>) of the URL.
		final int locationStartIndex;
		int locationEndIndex;

		if (spec.startsWith("//", startIndex)) {
			locationStartIndex = startIndex + 2;
			locationEndIndex = indexOf(spec, '/', locationStartIndex, endIndex);
			if (locationEndIndex >= 0) {
				startIndex = locationEndIndex;
			}
		} else {
			locationStartIndex = -1;
			locationEndIndex = -1;
		}
		// Section 2.4.4: Parsing the Query Information
		//
		// If the parse string contains a question mark "?" character, then the
		// substring after the first (left-most) question mark "?" and up to the
		// end of the parse string is the <query> information. If the question
		// mark is the last character, or no question mark is present, then the
		// query information is empty. The matched substring, including the
		// question mark character, is removed from the parse string before
		// continuing.
		final int questionMarkIndex = indexOf(spec, '?', startIndex, endIndex);

		if (questionMarkIndex >= 0) {
			if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
				// The substring of characters after the double-slash and up to,
				// but not
				// including, the question mark "?" character is the network
				// location/login
				// (<net_loc>) of the URL.
				locationEndIndex = questionMarkIndex;
				startIndex = questionMarkIndex;
			}
			url.query_ = spec.substring(questionMarkIndex + 1, endIndex);
			endIndex = questionMarkIndex;
		}
		// Section 2.4.5: Parsing the Parameters
		//
		// If the parse string contains a semicolon ";" character, then the
		// substring after the first (left-most) semicolon ";" and up to the end
		// of the parse string is the parameters (<params>). If the semicolon
		// is the last character, or no semicolon is present, then <params> is
		// empty. The matched substring, including the semicolon character, is
		// removed from the parse string before continuing.
		final int semicolonIndex = indexOf(spec, ';', startIndex, endIndex);

		if (semicolonIndex >= 0) {
			if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
				// The substring of characters after the double-slash and up to,
				// but not
				// including, the semicolon ";" character is the network
				// location/login
				// (<net_loc>) of the URL.
				locationEndIndex = semicolonIndex;
				startIndex = semicolonIndex;
			}
			url.parameters_ = spec.substring(semicolonIndex + 1, endIndex);
			endIndex = semicolonIndex;
		}
		// Section 2.4.6: Parsing the Path
		//
		// After the above steps, all that is left of the parse string is the
		// URL <path> and the slash "/" that may precede it. Even though the
		// initial slash is not part of the URL path, the parser must remember
		// whether or not it was present so that later processes can
		// differentiate between relative and absolute paths. Often this is
		// done by simply storing the preceding slash along with the path.
		if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
			// The entire remaining parse string is assigned to the network
			// location/login (<net_loc>) of the URL.
			locationEndIndex = endIndex;
		} else if (startIndex < endIndex) {
			url.path_ = spec.substring(startIndex, endIndex);
		}
		// Set the network location/login (<net_loc>) of the URL.
		if ((locationStartIndex >= 0) && (locationEndIndex >= 0)) {
			url.location_ = spec.substring(locationStartIndex, locationEndIndex);
		}
		return url;
	}

	/*
	 * Returns true if specified string is a valid scheme name.
	 */
	private static boolean isValidScheme(final String scheme) {
		final int length = scheme.length();
		if (length < 1) {
			return false;
		}
		char c = scheme.charAt(0);
		if (!Character.isLetter(c)) {
			return false;
		}
		for (int i = 1; i < length; i++) {
			c = scheme.charAt(i);
			if (!Character.isLetterOrDigit(c) && (c != '.') && (c != '+') && (c != '-')) {
				return false;
			}
		}
		return true;
	}

	private static Url resolveUrl(final Url baseUrl, final String relativeUrl) {
		final Url url = parseUrl(relativeUrl);
		// Step 1: The base URL is established according to the rules of
		// Section 3. If the base URL is the empty string (unknown),
		// the embedded URL is interpreted as an absolute URL and
		// we are done.
		if (baseUrl == null) {
			return url;
		}
		// Step 2: Both the base and embedded URLs are parsed into their
		// component parts as described in Section 2.4.
		// a) If the embedded URL is entirely empty, it inherits the
		// entire base URL (i.e., is set equal to the base URL)
		// and we are done.
		if (relativeUrl.length() == 0) {
			return new Url(baseUrl);
		}
		// b) If the embedded URL starts with a scheme name, it is
		// interpreted as an absolute URL and we are done.
		if (url.scheme_ != null) {
			return url;
		}
		// c) Otherwise, the embedded URL inherits the scheme of
		// the base URL.
		url.scheme_ = baseUrl.scheme_;
		// Step 3: If the embedded URL's <net_loc> is non-empty, we skip to
		// Step 7. Otherwise, the embedded URL inherits the <net_loc>
		// (if any) of the base URL.
		if (url.location_ != null) {
			return url;
		}
		url.location_ = baseUrl.location_;
		// Step 4: If the embedded URL path is preceded by a slash "/", the
		// path is not relative and we skip to Step 7.
		if ((url.path_ != null) && url.path_.startsWith("/")) {
			url.path_ = removeLeadingSlashPoints(url.path_);
			return url;
		}
		// Step 5: If the embedded URL path is empty (and not preceded by a
		// slash), then the embedded URL inherits the base URL path,
		// and
		if (url.path_ == null) {
			url.path_ = baseUrl.path_;
			// a) if the embedded URL's <params> is non-empty, we skip to
			// step 7; otherwise, it inherits the <params> of the base
			// URL (if any) and
			if (url.parameters_ != null) {
				return url;
			}
			url.parameters_ = baseUrl.parameters_;
			// b) if the embedded URL's <query> is non-empty, we skip to
			// step 7; otherwise, it inherits the <query> of the base
			// URL (if any) and we skip to step 7.
			if (url.query_ != null) {
				return url;
			}
			url.query_ = baseUrl.query_;
			return url;
		}
		// Step 6: The last segment of the base URL's path (anything
		// following the rightmost slash "/", or the entire path if no
		// slash is present) is removed and the embedded URL's path is
		// appended in its place. The following operations are
		// then applied, in order, to the new path:
		final String basePath = baseUrl.path_;
		String path = new String();

		if (basePath != null) {
			final int lastSlashIndex = basePath.lastIndexOf('/');

			if (lastSlashIndex >= 0) {
				path = basePath.substring(0, lastSlashIndex + 1);
			}
		} else {
			path = "/";
		}
		path = path.concat(url.path_);
		// a) All occurrences of "./", where "." is a complete path
		// segment, are removed.
		int pathSegmentIndex;

		while ((pathSegmentIndex = path.indexOf("/./")) >= 0) {
			path = path.substring(0, pathSegmentIndex + 1).concat(path.substring(pathSegmentIndex + 3));
		}
		// b) If the path ends with "." as a complete path segment,
		// that "." is removed.
		if (path.endsWith("/.")) {
			path = path.substring(0, path.length() - 1);
		}
		// c) All occurrences of "<segment>/../", where <segment> is a
		// complete path segment not equal to "..", are removed.
		// Removal of these path segments is performed iteratively,
		// removing the leftmost matching pattern on each iteration,
		// until no matching pattern remains.
		while ((pathSegmentIndex = path.indexOf("/../")) > 0) {
			final String pathSegment = path.substring(0, pathSegmentIndex);
			final int slashIndex = pathSegment.lastIndexOf('/');

			if (slashIndex < 0) {
				continue;
			}
			if (!pathSegment.substring(slashIndex).equals("..")) {
				path = path.substring(0, slashIndex + 1).concat(path.substring(pathSegmentIndex + 4));
			}
		}
		// d) If the path ends with "<segment>/..", where <segment> is a
		// complete path segment not equal to "..", that
		// "<segment>/.." is removed.
		if (path.endsWith("/..")) {
			final String pathSegment = path.substring(0, path.length() - 3);
			final int slashIndex = pathSegment.lastIndexOf('/');

			if (slashIndex >= 0) {
				path = path.substring(0, slashIndex + 1);
			}
		}

		path = removeLeadingSlashPoints(path);

		url.path_ = path;
		// Step 7: The resulting URL components, including any inherited from
		// the base URL, are recombined to give the absolute form of
		// the embedded URL.
		return url;
	}

	private static String removeLeadingSlashPoints(String path) {
		while (path.startsWith("/..")) {
			path = path.substring(3);
		}

		return path;
	}

	/**
	 * Code copied from HtmlUnit
	 * (src/main/java/com/gargoylesoftware/htmlunit/TextUtil.java)
	 * (https://htmlunit.svn.sourceforge.net/svnroot/htmlunit/trunk/htmlunit -
	 * commit 5556)
	 */
	public static boolean startsWithIgnoreCase(final String stringToCheck, final String prefix) {

		if (prefix.length() == 0) {
			throw new IllegalArgumentException("Prefix may not be empty");
		}

		final int prefixLength = prefix.length();
		if (stringToCheck.length() < prefixLength) {
			return false;
		}
		return stringToCheck.substring(0, prefixLength).toLowerCase().equals(prefix.toLowerCase());
	}

	/**
	 * Code copied from HtmlUnit
	 * (src/main/java/com/gargoylesoftware/htmlunit/StringUtils.java)
	 * (https://htmlunit.svn.sourceforge.net/svnroot/htmlunit/trunk/htmlunit -
	 * commit 5556)
	 */
	public static int indexOf(final String s, final char searchChar, final int beginIndex, final int endIndex) {
		for (int i = beginIndex; i < endIndex; i++) {
			if (s.charAt(i) == searchChar) {
				return i;
			}
		}
		return -1;
	}

	private static class Url {

		private String scheme_;
		private String location_;
		private String path_;
		private String parameters_;
		private String query_;
		private String fragment_;

		public Url(final Url url) {
			scheme_ = url.scheme_;
			location_ = url.location_;
			path_ = url.path_;
			parameters_ = url.parameters_;
			query_ = url.query_;
			fragment_ = url.fragment_;
		}

		public Url() {
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();

			if (scheme_ != null) {
				sb.append(scheme_);
				sb.append(':');
			}
			if (location_ != null) {
				sb.append("//");
				sb.append(location_);
			}
			if (path_ != null) {
				sb.append(path_);
			}
			if (parameters_ != null) {
				sb.append(';');
				sb.append(parameters_);
			}
			if (query_ != null) {
				sb.append('?');
				sb.append(query_);
			}
			if (fragment_ != null) {
				sb.append('#');
				sb.append(fragment_);
			}
			return sb.toString();
		}
	}

}
