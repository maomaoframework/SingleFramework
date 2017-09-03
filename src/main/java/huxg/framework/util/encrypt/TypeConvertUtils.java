package huxg.framework.util.encrypt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * 
 * </p>
 *
 * @author
 * @version 1.0
 */
public class TypeConvertUtils
{
  private static Log m_log = LogFactory.getLog(TypeConvertUtils.class);

  /**
   *
   *
   * @param sDateTime String [yyyy-MM-dd HH:mm:ss | yyyy-MM-dd HH:mm]
   * @param bWithSecond boolean true[yyyy-MM-dd HH:mm:ss] false true[yyyy-MM-dd HH:mm]
   *
   * @return Date Date
   */
  public static Date convertString2Time(String sDateTime, boolean bWithSecond)
  {
    if( sDateTime == null || sDateTime.length() == 0 )
      return null;

    SimpleDateFormat formatDateTime;

    if( bWithSecond )
      formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    else
      formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    try
    {
      return formatDateTime.parse(sDateTime);
    }
    catch( Exception e )
    {
      m_log.error(e.getMessage());
      return null;
    }
  }

  /**
   *
   *
   * @param dateTime Date Date
   *
   * @return String [yyyy-MM-dd HH:mm:ss]
   */
  public static String convertTime2String(Date dateTime, boolean bWithSecond)
  {
    if( dateTime == null )
      return null;

    SimpleDateFormat formatDateTime;

    if( bWithSecond )
      formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    else
      formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    try
    {
      return formatDateTime.format(dateTime);
    }
    catch( Exception e )
    {
      m_log.error(e.getMessage());
      return null;
    }
  }

  /**
   *
   *
   * @param sDate String [yyyy-MM-dd]
   *
   * @return Date Date
   */
  public static Date convertString2Date(String sDate)
  {
    if( sDate == null || sDate.length() == 0 )
      return null;

    SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");

    try
    {
      return formatDateTime.parse(sDate);
    }
    catch( Exception e )
    {
      m_log.error(e.getMessage());
      return null;
    }
  }

  /**
   *
   *
   * @param dateDate Date Date
   *
   * @return String [yyyy-MM-dd]
   */
  public static String convertDate2String(Date dateDate)
  {
    if( dateDate == null )
      return null;

    SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");

    try
    {
      return formatDateTime.format(dateDate);
    }
    catch( Exception e )
    {
      m_log.error(e.getMessage());
      return null;
    }
  }

  /**
   * 
   *
   * @param objValue Object
   *
   * @return String
   */
  public static String toString(Object objValue)
  {
    if( objValue != null )
      return objValue.toString();
    else
      return null;
  }

  /**
   *
   *
   * @param objValue Object
   *
   * @return int
   */
  public static int toInt(Object objValue)
  {
    if( objValue != null )
    {
      if( objValue instanceof Number )
      {
        return ((Number)objValue).intValue();
      }
      else
      {
        return Integer.parseInt(objValue.toString());
      }
    }
    else
      return 0;
  }

  /**
   *
   *
   * @param objValue Object
   *
   * @return long
   */
  public static long toLong(Object objValue)
  {
    if( objValue != null )
    {
      if( objValue instanceof Number )
      {
        return ((Number)objValue).longValue();
      }
      else
      {
        return Long.parseLong(objValue.toString());
      }
    }
    else
      return 0;
  }

  /**
   *
   *
   * @param objValue Object
   *
   * @return double
   */
  public static double toDouble(Object objValue)
  {
    if( objValue != null )
    {
      if( objValue instanceof Number )
      {
        return ((Number)objValue).doubleValue();
      }
      else
      {
        return Double.parseDouble(objValue.toString());
      }
    }
    else
      return 0;
  }

  /**
   *
   *
   * @param objValue Object
   *
   * @return Date
   */
  public static Date toDate(Object objValue)
  {
    if( objValue != null )
    {
      if( objValue instanceof Date )
        return (Date)objValue;
      else
      {
        String sValue = objValue.toString();
        if( sValue.length() > 10 )
        {
          if( sValue.indexOf(':') != sValue.lastIndexOf(':') )
            return convertString2Time(sValue, true); // With Second
          else
            return convertString2Time(sValue, false); // Without Second
        }
        else
          return convertString2Date(sValue);
      }
    }
    else
      return null;
  }

  /**
   *
   *
   * @param objValue Object
   *
   * @return long
   */
  public static byte[] toBytes(Object objValue)
  {
    if( objValue != null )
    {
      if( objValue instanceof byte[] )
      {
        return (byte[])objValue;
      }
      else
      {
        return objValue.toString().getBytes();
      }
    }
    else
      return null;
  }

  /**
   *
   *
   * @param bytes byte[]
   *
   * @return String
   */
  public static String convertByte2Hex( byte[] abValue )
  {
    if( abValue == null )
    {
      return null;
    }

    String sTemp;
    StringBuffer sbHex = new StringBuffer();

    for ( int i = 0; i < abValue.length; i++ )
    {
      sTemp = Integer.toHexString( abValue[i] & 0XFF );
      if ( 1 == sTemp.length() )
      {
        sbHex.append('0').append(sTemp);
      }
      else
      {
        sbHex.append(sTemp);
      }
    }

    return sbHex.toString();
  }
}
