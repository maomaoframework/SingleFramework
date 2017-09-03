package huxg.framework.util.dom4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


public class DTDEntityResolver implements EntityResolver, Serializable
{

	private static final long serialVersionUID = -8634599551991092999L;	

	private static final String URL = "http://www.thunisoft.com/";

	private transient ClassLoader resourceLoader;

	/**
	 * Default constructor using DTDEntityResolver classloader for
	 * resource loading.
	 */
	public DTDEntityResolver()
	{
		//backward compatibility
		resourceLoader = this.getClass().getClassLoader();
	}

	/**
	 * Set the class loader used to load resouces
	 *
	 * @param resourceLoader class loader to use
	 */
	public DTDEntityResolver(ClassLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;
	}

	public InputSource resolveEntity(String publicId, String systemId)
	{
		if (systemId != null && systemId.startsWith(URL))
		{
			
			// Search for DTD
			String path = "com/thunisfot/" + systemId.substring(URL.length());
			InputStream dtdStream = resourceLoader == null ? getClass()
					.getResourceAsStream(path) : resourceLoader
					.getResourceAsStream(path);
			if (dtdStream == null)
			{
				return null;
			}
			else
			{				
				InputSource source = new InputSource(dtdStream);
				source.setPublicId(publicId);
				source.setSystemId(systemId);
				return source;
			}
		}
		else
		{
			// use the default behaviour
			return null;
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException,
			ClassNotFoundException
	{
		/** to allow serialization of configuration */
		ois.defaultReadObject();
		this.resourceLoader = this.getClass().getClassLoader();
	}
}
