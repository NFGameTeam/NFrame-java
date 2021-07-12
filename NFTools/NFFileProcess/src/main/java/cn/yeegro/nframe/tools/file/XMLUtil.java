package cn.yeegro.nframe.tools.file;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;

public class XMLUtil {


	private XPath oXpath;

	/**
	 * 构造xml文档对应的document对象
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Document parseFile(File file) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);

		// 创建XPath对象
		XPathFactory factory = XPathFactory.newInstance();
		oXpath = factory.newXPath();
		return doc;
	}

	/**
	 * 构造输入流对应的document对象
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public Document parseStream(InputStream in) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(in);

		// 创建XPath对象
		XPathFactory factory = XPathFactory.newInstance();
		oXpath = factory.newXPath();
		return doc;
	}

	/**
	 * 获取结点值
	 *
	 * @param node
	 * @return
	 */
	public String getNodeValue(Node node) {
		String dataValue = node.getTextContent();
		return dataValue;
	}

	/**
	 * 获取结点List
	 *
	 * @param node
	 * @param xpath
	 * @return
	 * @throws XPathExpressionException
	 */
	public NodeList getNodeList(Node node, String xpath)
			throws XPathExpressionException {
		NodeList nodeList = (NodeList) oXpath.evaluate(xpath, node,
				XPathConstants.NODESET);

		return nodeList;
	}

	/**
	 * 获取单个结点
	 *
	 * @param node
	 * @param xpath
	 * @return
	 * @throws XPathExpressionException
	 */
	public Node getNode(Node node, String xpath)
			throws XPathExpressionException {
		Node nodeRet = (Node) oXpath.evaluate(xpath, node, XPathConstants.NODE);

		return nodeRet;
	}

	public String getAttrVal(Node node, String name) {
		String val = "";

		Element eNode = (Element) node;
		val = eNode.getAttribute(name);
		return val;
	}
}
