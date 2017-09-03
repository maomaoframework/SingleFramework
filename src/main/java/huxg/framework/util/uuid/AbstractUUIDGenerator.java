package huxg.framework.util.uuid;

import java.net.InetAddress;

/**
 * UUID
 */

public abstract class AbstractUUIDGenerator
{
  private static final int IP;
  private static short counter = (short) 0;
  private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

  static
  {
    int ipadd;
    try
    {
      ipadd = BytesHelper.toInt(InetAddress.getLocalHost().getAddress());
    }
    catch (Exception e)
    {
      ipadd = 0;
    }
    IP = ipadd;
  }

  public AbstractUUIDGenerator()
  {
  }

  protected int getJVM()
  {
    return JVM;
  }

  protected short getCount()
  {
    synchronized (AbstractUUIDGenerator.class)
    {
      if (counter < 0)
        counter = 0;
      return counter++;
    }
  }

  protected int getIP()
  {
    return IP;
  }

  protected short getHiTime()
  {
    return (short) (System.currentTimeMillis() >>> 32);
  }

  protected int getLoTime()
  {
    return (int) System.currentTimeMillis();
  }
}

class BytesHelper
{
	private BytesHelper() {}
	public static int toInt( byte[] bytes )
	{
          int result = 0;
          for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
          }
          return result;
        }
}