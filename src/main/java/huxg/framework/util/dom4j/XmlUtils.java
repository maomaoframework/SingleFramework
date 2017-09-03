/*
 * filename: XMLHelper.java
 * date    : 2006-12-20
 * author  : limin(<a href="mailto:limb@thunisoft.com">limb@thunisoft.com</a>)
 * version : 1.0
 */
package huxg.framework.util.dom4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 * Small helper class that lazy loads DOM and SAX reader and keep them for fast
 * use afterwards.
 */
public final class XmlUtils {
	public static final EntityResolver DEFAULT_DTD_RESOLVER = new DTDEntityResolver();

	public static final EntityResolver NONE_DTD_RESOLVER = new NoneDTDEntityResolver();

	private DOMReader domReader;

	private SAXReader saxReader;

	/**
	 * Create a dom4j SAXReader which will append all validation errors to
	 * errorList
	 */
	public SAXReader createSAXReader(String file, List errorsList,
			EntityResolver entityResolver) {
		if (saxReader == null)
			saxReader = new SAXReader();
		saxReader.setEntityResolver(NONE_DTD_RESOLVER);
		saxReader.setErrorHandler(new ErrorLogger(file, errorsList));
		saxReader.setMergeAdjacentText(true);
		saxReader.setValidation(false);
		return saxReader;
	}

	public String Dom2String(Document doc) {

		XMLWriter writer = null;
		try {
			StringWriter sw = new StringWriter();
			// OutputFormat format = OutputFormat.createPrettyPrint();
			// format.setEncoding("gb2312");

			writer = new XMLWriter(sw);
			writer.write(doc);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer)
				try {
					writer.close();
				} catch (Exception ie) {
				}
		}
		return null;
	}

	public String Dom2String(Document doc, String encoding) {

		XMLWriter writer = null;
		try {
			StringWriter sw = new StringWriter();
			writer = new XMLWriter(sw);
			writer.write(doc);
			String xml = sw.toString();

			String pattern = ">";
			int idx = xml.indexOf(pattern);
			String last = xml.substring(idx + 1);
			xml = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>"
					+ last;
			return xml;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer)
				try {
					writer.close();
				} catch (Exception ie) {
				}
		}
		return null;
	}

	public void saveXml2File(String xml, String fileName) {
		File f = new File(fileName);
		saveXml2File(xml, f);
	}

	public void saveXml2File(String xml, File file) {
		try {
			Document doc = load(xml);
			saveXml2File(doc, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		XmlUtils x = new XmlUtils();
		File file = new File(
				"/home/huxg/temp/applicationContext-acegi-security.xml");
		Document doc = x.load(new FileInputStream(file));

		x.saveXml2File(doc, new File("/home/huxg/temp/a.xml"));
	}

	public void saveXml2File(Document doc, File file) {
		XMLWriter writer = null;
		try {
			FileWriter fw = new FileWriter(file);
			// 这里保持xml文件原来的样式，不做修改，否则会导致XML错误。
			// OutputFormat format = OutputFormat.createCompactFormat();
			// format.setEncoding("UTF-8");

			writer = new XMLWriter(fw);
			writer.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer)
				try {
					writer.close();
				} catch (Exception ie) {
				}
		}
	}

	/**
	 * @author lizhi 向xml中写入元素数据
	 * @param doc
	 * @param file
	 * @param encoding
	 */
	public void saveXml2File(Document doc, File file, String encoding) {
		XMLWriter writer = null;
		try {
			String sXMLContent = Dom2String(doc, "GBK");
			FileWriter filer = new FileWriter(file);
			filer.write(sXMLContent);
			filer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer)
				try {
					writer.close();
				} catch (Exception ie) {
				}
		}
	}

	/**
	 * Document
	 * 
	 * @param in
	 * @return
	 */
	public Document load(InputStream in) throws DocumentException {
		return createSAXReader("XML InputStream", new ArrayList(), null).read(
				new InputSource(in));
	}

	/**
	 * Document
	 * 
	 * @param in
	 * @return
	 */
	public Document load(String xml) throws DocumentException {
		return createSAXReader("XML InputStream", new ArrayList(), null).read(
				new StringReader(xml));
	}

	/**
	 * Create a dom4j DOMReader
	 */
	public DOMReader createDOMReader() {
		if (domReader == null)
			domReader = new DOMReader();
		return domReader;
	}

	public static class ErrorLogger implements ErrorHandler {
		private String file;

		private List errors;

		ErrorLogger(String file, List errors) {
			this.file = file;
			this.errors = errors;
		}

		public void error(SAXParseException error) {
			errors.add(error);
		}

		public void fatalError(SAXParseException error) {
			error(error);
		}

		public void warning(SAXParseException warn) {
		}
	}

	public static Element generateDom4jElement(String elementName) {
		return DocumentFactory.getInstance().createElement(elementName);
	}
	
	public static void removeChildren(Element el){
		for (Iterator<Element> iter = el.elements().iterator(); iter.hasNext(); ){
			el.remove(iter.next());
		}
	}
}
