package huxg.framework.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * XML 文档生成及解析工具类
 */
public class XMLUtil {
	/** 解析字符串类型的xml，生成docement实例*/
	public static NodeList getXmlNodeList(String xml,String characterEncoding) throws IOException,
			ParserConfigurationException, SAXException {
		if(null == xml || xml.length()==0)
			return null;
		InputStream in = new ByteArrayInputStream(xml.getBytes(characterEncoding));
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfac.newDocumentBuilder();
		Document doc = dombuilder.parse(in);
		Element root = doc.getDocumentElement();
		NodeList nlist = root.getChildNodes();
		return nlist;
	}
	
	/** 解析字符串类型的xml，生成docement实例 返回顶元素 */
	public static Node getXmlRootNode(String xml,String characterEncoding) throws IOException,
			ParserConfigurationException, SAXException {
		if(null == xml || xml.length()==0)
			return null;
		InputStream in = new ByteArrayInputStream(xml.getBytes(characterEncoding));
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfac.newDocumentBuilder();
		Document doc = dombuilder.parse(in);
		Element root = doc.getDocumentElement();
		return root;
	}	
	
	/** 获取指定节点列表*/
	public static NodeList getSubNodeListByNodeName(NodeList nodeList, String nodeName)
			throws IOException, ParserConfigurationException, SAXException {
		
		if(null== nodeList || nodeList.getLength()==0)
			return nodeList;
		
		NodeList subNodeList = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (null != node && node.getNodeName().equals(nodeName)) {
				subNodeList = node.getChildNodes();
				break;
			}
		}
		return subNodeList;
	}
	
	/** 根据节点名称和该节点的属性名称、属性值获取指定节点列表*/
	public static NodeList getSubNodeListByNodeNameAndAttributeNameValue(
			NodeList nodeList, String nodeName, String attributeName, String attributeValue)
			throws IOException, ParserConfigurationException, SAXException {
		
		if(null== nodeList || nodeList.getLength()==0)
			return nodeList;
		
		NodeList subNodeList = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (null != node && node.getNodeName().equals(nodeName)) { //定位到指定的节点
				NamedNodeMap nodeMap = node.getAttributes();
				if(nodeMap==null) continue;
				String attValue = StringUtils.defaultIfEmpty(nodeMap.getNamedItem(attributeName).getNodeValue(), "");
				if(attValue.equals(attributeValue)){
					subNodeList = node.getChildNodes();
					break;
				}
			}
		}
		return subNodeList;
	}
	
	/**
	 *  检测XML代码，替换掉特殊字符
	 *  Verify that no character has a hex value greater than 0xFFFD, or less than 0x20.
	 * Check that the character is not equal to the tab ("t), the newline ("n), the carriage 
	 * return ("r), or is an invalid XML character below the range of 0x20.
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static String checkUnicodeString(String xmlString) throws Exception {
		if (xmlString == null || xmlString.equals(""))
			return xmlString;
		char c;
		StringBuffer newXMLCode = new StringBuffer("");
		for (int i = 0; i < xmlString.length(); ++i) {
			c = xmlString.charAt(i);
			if (c > 0xFFFD) {
				newXMLCode.append("\n");
				// throw new Exception("Invalid Unicode");//或者直接替换掉0x0
			} else if (c < 0x20 && c != '\t' & c != '\n' & c != '\r') {
				newXMLCode.append("\n");
				// throw new Exception("Invalid Xml Characters");//或者直接替换掉0x0
			} else {
				newXMLCode.append(c);
			}
		}
		return newXMLCode.toString();
	}
}
