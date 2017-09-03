package huxg.framework.util.uuid;

import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

/**
 * 
 * @author yuyh
 * 
 */
public class KeyGenerator {

	/**
     *
     */
	public final static String separator = "separator";

	/**
     * 
     */
	public final static String separatorValue = "";

	/**
     * 
     */
	public final static long uintIDScale = Long.valueOf("10000000000").longValue();

	/**
     *
     */
	public final static long intScale = Integer.MAX_VALUE - Integer.MIN_VALUE;

	/**
	 * 
	 * @return
	 */
	public final static long Key(int times) {

		Properties props = new Properties();
		props.setProperty(KeyGenerator.separator, KeyGenerator.separatorValue);

		IdentifierGenerator gen = new UUIDHexGenerator();
		((Configurable) gen).configure(Hibernate.STRING, props, null);
		String id = (String) gen.generate(null, null);

		if (times < 0) {
			times = 0;
		}

		long key;
		key = (long) id.hashCode() + Integer.MAX_VALUE + KeyGenerator.intScale * times;
		return key;
	}

	/**
	 * 
	 * @param corpID
	 * @return
	 */
	public final static long KeyForCorp(int corpID) {
		return corpID * KeyGenerator.uintIDScale + KeyGenerator.Key(0);
	}

	/**
	 * 
	 * @param corpID
	 * @return
	 */
	public final static long KeyForCorpHigh(int corpID) {
		return KeyGenerator.KeyForCorp(corpID) + KeyGenerator.intScale;
	}
}
