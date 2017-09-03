package huxg.framework.util.dom4j;

import java.io.Serializable;
import java.io.StringBufferInputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class NoneDTDEntityResolver implements EntityResolver, Serializable {
	public InputSource resolveEntity(String publicId, String systemId) {
		return new InputSource(new StringBufferInputStream(""));
	}
}
