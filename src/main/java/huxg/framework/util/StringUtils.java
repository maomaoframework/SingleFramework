package huxg.framework.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.springframework.util.Assert;

public class StringUtils {
	/**
	 * 
	 * @param input
	 * @param separator
	 * @return
	 */
	public static String fromCamelCase(String input, char separator) {
		int length = input.length();
		StringBuilder result = new StringBuilder(length * 2);
		int resultLength = 0;
		boolean prevTranslated = false;
		for (int i = 0; i < length; i++) {
			char c = input.charAt(i);
			if (i == 0 && Character.isUpperCase(c)) {
				// 转换为横线
				result.append(c).append("_");
			} else if (i > 0 || c != separator) {// skip first starting
													// separator
				if (Character.isUpperCase(c)) {
					if (!prevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != separator) {
						result.append(separator);
						resultLength++;
					}
					c = Character.toLowerCase(c);
					prevTranslated = true;
				} else {
					prevTranslated = false;
				}
				result.append(c);
				resultLength++;
			}
		}
		return resultLength > 0 ? result.toString() : input;
	}

	/**
	 * 
	 * @param input
	 * @param firstCharUppercase
	 * @param separator
	 * @return
	 */
	public static String toCamelCase(String input, boolean firstCharUppercase, char separator) {
		int length = input.length();
		StringBuilder sb = new StringBuilder(length);
		boolean upperCase = firstCharUppercase;

		for (int i = 0; i < length; i++) {
			char ch = input.charAt(i);
			if (ch == separator) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(ch));
				upperCase = false;
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * @author
	 * 
	 * @param parentCode
	 * @param currentLevelMaxCode
	 *            当前层级下（parent.getLevel()+1）的最大code ----
	 *            如果没有父亲传入或者有父亲但是一个孩子也没有的时候请传入""
	 * @param defaultStart
	 * @return 新的code
	 */
	public static String getRuleCode(String parentCode, String currentLevelMaxCode, String defaultStart) {
		Assert.notNull(parentCode, "parentCode not null");
		Assert.notNull(currentLevelMaxCode, "currentLevelMaxCode not null");
		Assert.notNull(defaultStart, "defaultStart not null");
		int level = defaultStart.length();
		long intCode;
		if ("".equals(currentLevelMaxCode)) {
			intCode = 1;
		} else {
			Assert.state(currentLevelMaxCode.length() == defaultStart.length(), "currentLevelMaxCode.length()==defaultStart.length()");
			int len = currentLevelMaxCode.length();
			intCode = Long.valueOf(currentLevelMaxCode.substring(len - level, len)).longValue() + 1;
		}
		return parentCode + leftZero(String.valueOf(intCode), level);
	}

	public static String leftZero(String code, int level) {
		Assert.notNull(code, "code not null");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < level - code.length(); i++) {
			sb = sb.append("0");
		}
		sb = sb.append(code);
		return sb.toString();
	}

	/**
	 * 是否非空字符串
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isNotEmptyString(final String input) {
		return input != null && !"".equals(input.trim());
	}

	/**
	 * 把秒转换成 时:分:秒 格式的时长信息
	 * 
	 * @param seconds
	 * @return
	 */
	public static String getTimeDurationLabel(final Long seconds) {
		if (seconds == null || seconds.intValue() == 0) {
			return "00:00:00";
		} else {
			return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
		}
	}

	/**
	 * 把时长 时:分:秒转换成秒
	 */
	public static Integer durationToSeconds(final String duration) {
		if (StringUtils.isEmpty(duration)) {
			return 0;
		}
		Integer result = 0;
		String[] datas = duration.split(":");
		if (datas.length == 3) {
			result += Integer.valueOf(datas[0]) * 3600 + Integer.valueOf(datas[1]) * 60 + Integer.valueOf(datas[2]);
		} else if (datas.length == 2) {
			result += Integer.valueOf(datas[0]) * 60 + Integer.valueOf(datas[1]);
		} else if (datas.length == 1) {
			result += Integer.valueOf(datas[0]);
		}
		return result;
	}

	/**
	 * 去掉某个字符串里包含的最后某个字符串
	 * 
	 * @param srcStr
	 * @param toStrip
	 * @return
	 */
	public static String stripLastStr(final String srcStr, final String toStrip) {
		if (!isNotEmptyString(srcStr) || !isNotEmptyString(toStrip)) {
			return null;
		}
		if (srcStr.length() <= toStrip.length()) {
			return null;
		}

		if (!srcStr.endsWith(toStrip)) {
			return srcStr;
		} else {
			return srcStr.substring(0, srcStr.length() - toStrip.length());
		}
	}

	/**
	 * 
	 * 
	 * @param sInput
	 *            String
	 * @param sPattern
	 *            String
	 * 
	 * @return List
	 */
	public static List split(final String sInput, String sPattern) {
		if (isEmptyAfterTrim(sInput))
			return null;

		List arrayResult = new ArrayList();

		Perl5Util util = new Perl5Util();
		sPattern = sPattern.replace('#', ' ');
		sPattern = "#" + sPattern + "#";
		util.split(arrayResult, sPattern, sInput);

		if (arrayResult.size() == 0)
			arrayResult.add(sInput);

		return arrayResult;
	}

	/**
	 * 以模式分割成数组
	 * 
	 * @param sInput
	 * @param sPattern
	 * @return
	 */
	public static String[] split2Array(final String sInput, String sPattern) {
		if (isEmptyAfterTrim(sInput))
			return null;

		return sInput.split(sPattern);
	}

	/**
	 * 
	 * 
	 * @param sInput
	 *            String
	 * @param sPattern
	 *            String
	 * @param iIdx
	 *            int
	 * 
	 * @return String
	 */
	public static final String getElement(final String sInput, final String sPattern, final int iIdx) {
		List listElement = split(sInput, sPattern);

		if (listElement != null) {
			if (iIdx < listElement.size())
				return (String) listElement.get(iIdx);
		}

		return null;
	}

	/**
	 * 
	 * 
	 * @param sString
	 *            String �ַ�
	 * 
	 * @return boolean
	 */
	public static final boolean isEmpty(final String sString) {
		if (sString == null || sString.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * 
	 * @param sString
	 *            String
	 * 
	 * @return boolean
	 */
	public static final boolean isEmptyAfterTrim(final String sString) {
		if (sString == null || sString.length() == 0)
			return true;

		if (isEmpty(sString.trim()))
			return true;

		return false;
	}

	/**
	 * 
	 * 
	 * @param sString
	 *            String
	 * 
	 * @return String
	 * 
	 * @author
	 */
	public static String trim(final String sString) {
		String str = sString;
		if (isEmpty(str))
			return str;

		Perl5Util utilPerl = new Perl5Util();
		str = utilPerl.substitute("s#^\\s*##g", str);
		str = utilPerl.substitute("s#\\s*$##g", str);

		return str;
	}

	/**
	 * 
	 * 
	 * @param sString
	 *            String
	 * @param sSrc
	 *            String
	 * @param sDest
	 *            String
	 * 
	 * @return String
	 */
	public static String replace(final String sString, final String sSrc, final String sDest) {
		return replace(sString, sSrc, sDest, true);
	}

	/**
	 * 
	 * 
	 * @param sString
	 *            String
	 * @param sSrc
	 *            String
	 * @param sDest
	 *            String
	 * @param bCaseSensitive
	 *            boolean
	 * 
	 * @return String
	 * 
	 * @author
	 */
	public static final String replace(String sString, String sSrc, String sDest, final boolean bCaseSensitive) {
		if (isEmpty(sString))
			return sString;

		if (!bCaseSensitive) {
			sSrc = sSrc.toLowerCase();
			sDest = sDest.toLowerCase();
			sString = sString.toLowerCase();
		}

		Perl5Util utilPerl = new Perl5Util();
		sSrc = sSrc.replace('#', ' ');
		sDest = sDest.replace('#', ' ');
		sString = utilPerl.substitute("s#" + sSrc + "#" + sDest + "#g", sString);

		return sString;
	}

	/**
	 * 
	 * 
	 * @param sWord
	 *            String
	 * 
	 * @return boolean
	 */
	public static final boolean isReplaceReservedWord(final String sWord) {
		Vector vecResvWord = new Vector();

		vecResvWord.add("?");
		vecResvWord.add(".");
		vecResvWord.add("*");
		vecResvWord.add("[");
		vecResvWord.add("]");
		vecResvWord.add("^");
		vecResvWord.add("$");
		vecResvWord.add("\\");
		vecResvWord.add("(");
		vecResvWord.add(")");
		vecResvWord.add("|");

		if (vecResvWord.indexOf(sWord) >= 0)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * <p>
	 * 
	 * <pre>
	 * 
	 * String sString = &quot;par1=value1;par2=value2;par3=value3;&quot;;
	 * 
	 * String sValue = StringUtils.parseParValue(sString, &quot;par2&quot;, &quot;;&quot;);
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param sString
	 *            String
	 * @param sParName
	 *            String
	 * @param sSeperator
	 *            String
	 * 
	 * @return String
	 */
	public static final String parseParValue(final String sString, final String sParName, final String sSeperator) {
		if (isEmptyAfterTrim(sString))
			return null;

		if (isEmptyAfterTrim(sParName))
			return null;

		String sPattern = "/" + sParName + "=([\\w.]+)(" + sSeperator + "|$)/";

		Perl5Util utilPerl = new Perl5Util();

		if (utilPerl.match(sPattern, sString))
			return utilPerl.group(1);
		else
			return null;
	}

	/**
	 * 
	 * <p>
	 * 
	 * <pre>
	 * String sString = &quot;par1=value1;par2=value20;par2=value21;par3=value3;&quot;;
	 * 
	 * List listValue = StringUtils.parseParValues(sString, &quot;par2&quot;, &quot;;&quot;);
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param sString
	 *            String
	 * @param sParName
	 *            String
	 * @param sSeperator
	 *            String
	 * 
	 * @return String
	 */
	public static List parseParValues(final String sString, final String sParName, final String sSeperator) {
		if (isEmptyAfterTrim(sString))
			return null;

		if (isEmptyAfterTrim(sParName))
			return null;

		String sPattern = "/" + sParName + "=([\\w.]+)(" + sSeperator + "|$)/";

		Perl5Util utilPerl = new Perl5Util();
		PatternMatcherInput inputSrc = new PatternMatcherInput(sString);

		List listValue = new ArrayList();
		while (utilPerl.match(sPattern, inputSrc)) {
			listValue.add(utilPerl.group(1));
		}

		if (listValue.size() > 0)
			return listValue;
		else
			return null;
	}

	/**
	 * 
	 * <p>
	 * 
	 * <pre>
	 * String sString = &quot;par1=value1;par2=value2;par3=value3;&quot;;
	 * 
	 * Hashtable hashValue = StringUtils.parseParValues(sString, &quot;;&quot;);
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param sString
	 *            String
	 * @param sSeperator
	 *            String
	 * 
	 * @return Hashtable
	 */
	public static Hashtable parseParValues(final String sString, final String sSeperator) {
		if (isEmptyAfterTrim(sString))
			return null;

		Hashtable hashParValue = new Hashtable();

		// pari=value
		List listParValue = split(sString, sSeperator);

		if (listParValue != null) {
			// pari, value
			List listPar;
			String sValue;
			for (int i = 0; i < listParValue.size(); i++) {
				listPar = split((String) listParValue.get(i), "=");

				if (listPar != null && listPar.size() == 2) {
					sValue = (String) listPar.get(1);
					if (!isEmptyAfterTrim(sValue))
						hashParValue.put(listPar.get(0), listPar.get(1));
				}
			}
		}

		return hashParValue;
	}

	/**
	 * 
	 * 
	 * @param sString
	 *            String
	 * 
	 * @return String
	 */
	public static final String upperCaseTheFirstChar(final String sString) {
		if (isEmpty(sString))
			return null;

		if (sString.length() > 1)
			return sString.toUpperCase().charAt(0) + sString.substring(1);
		else
			return sString.toUpperCase();
	}

	public static final String lowerCaseTheFirstChar(final String str) {
		if (isEmpty(str))
			return null;

		if (str.length() > 1)
			return str.toLowerCase().charAt(0) + str.substring(1);
		else
			return str.toLowerCase();
	}

	/**
	 * <code>true</code>
	 * <p>
	 * <code>true</code>
	 * </p>
	 * <ul>
	 * <li>true
	 * <li>yes
	 * <li>1
	 * </ul>
	 * 
	 * @param sValue
	 *            String
	 * 
	 * @return boolean
	 */
	static public boolean equalsTrue(String sValue) {
		if (sValue != null) {
			sValue = sValue.toLowerCase();

			if (sValue.equals("true") || sValue.equals("true") || sValue.equals("yes"))
				return true;
		}

		return false;
	}

	/**
	 * 
	 * 
	 * @param sValue
	 *            String
	 * 
	 * @return String
	 */
	static public final String quote(final String sValue) {
		if (sValue == null)
			return sValue;
		else
			return "'" + sValue + "'";
	}

	/**
	 * 
	 * 
	 * @param source
	 * 
	 * @param length
	 * 
	 * @return
	 */
	public static String leftFillZero(String source, int length) {
		source = (null == source) ? "" : source.trim();
		int len = source.length();
		for (int i = 0; i < length - len; i++) {
			source = "0" + source;
		}
		return source;
	}

	/**
	 * 
	 * @param s
	 * @param params
	 * @return
	 */
	static public final String replace(String s, List params) {
		if (params == null)
			return s;
		return replace(s, params.toArray());
	}

	/**
	 * 
	 * @param s
	 * @param params
	 * @return
	 */
	static public final String replace(String s, Object[] args) {
		char[] c = s.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < c.length; i++) {
			if (c[i] != '$') {
				sb.append(c[i]);
			} else {
				String sParamIdx = "";
				int idx = 0;
				int j = i + 1;
				for (; j < c.length; j++) {
					if (c[j] == ' ')
						break;
					sParamIdx += c[j];
				}
				j--;

				try {
					idx = Integer.parseInt(sParamIdx);

					if (idx > 0) {
						sb.append(args[idx - 1].toString());
						i = j;
					} else
						sb.append(c[i]);
				} catch (Exception e) {
					sb.append(c[i]);
					continue;
				}
			}
		}
		return sb.toString();
	}

	public static final String substringBetween(String str, String pos1, String pos2) {
		int p1 = str.indexOf(pos1);
		String sub = str.substring(p1 + pos1.length());

		int p2 = sub.indexOf(pos2);
		sub = sub.substring(0, p2);
		return sub;
	}

	public static final String defaultIfEmpty(String str, String defaultStr) {
		return StringUtils.isEmptyAfterTrim(str) ? defaultStr : str;
	}

	/**
	 * 替换所有全角半角英文字符
	 * 
	 * @param str
	 * @param replacement
	 * @return
	 */
	public static String replaceSymbol(String str, String replacement) {
		String s = str.replaceAll("[,|;\\s*]", replacement);
		s = s.replaceAll("[\uFE30-\uFFA0|、]", replacement);
		return s;
	}

	/**
	 * 格式化字符串
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String format(String str, Map<String, Object> params) {
		if (StringUtils.isEmpty(str))
			return str;

		String p = str;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (str.contains("{{" + entry.getKey() + "}}")) {
				if (entry.getValue() instanceof String)
					p = p.replaceAll("\\{\\{" + entry.getKey() + "\\}\\}", (String) entry.getValue());
			}
		}
		return p;
	}

	public static String array2String(String[] p, String spliter) {
		if (p == null)
			return null;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < p.length; i++) {
			if (i == 0)
				sb.append(p[i]);
			else
				sb.append(spliter).append(p[i]);
		}
		return sb.toString();
	}

	/**
	 * 检验手机号码
	 * @param phone
	 * @return
	 */
	public static boolean isValidMobile(String phone) {
		Pattern p = Pattern.compile("^((17[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}
}
