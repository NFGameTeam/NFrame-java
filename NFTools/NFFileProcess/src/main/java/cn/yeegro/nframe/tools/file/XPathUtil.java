package cn.yeegro.nframe.tools.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 一个使用JDOM读取XML文件的工具类。 这个类的内部封装了JDOM的具体实现，提供了常用的一些方法，避免直接使用JDOM的API。 同时，也提供了方法可以返回JDOM的一些基本类型。目前的版本中还没有实现关于Namespace
 * 的操作。<br/>
 *
 * <pre>
 *  &lt;b&gt;使用了XPath，所以必须使用JDOM-beta9及以上的版本。&lt;/b&gt;&lt;br/&gt;
 *  XML需要引入的包：jdom.jar，xerces.jar，xml-apis.jar，xalan.jar，
 *  jaxen-core.jar，jaxen-jdom.jar，saxpath.jar&lt;br/&gt;
 *  Log需要引入的包：commons-logging.jar
 * </pre>
 * <pre>
 *  基本示例：&lt;br/&gt;
 *       String filePath = &quot;c:/xx/xxx.xml&quot;;
 *       XMLUtil util = XMLUtil.getInsance(filePath);
 *       Element element = util.getSingleElement(&quot;/root/elemA/elemB&quot;);
 *       String text = util.getSingleElementText(&quot;/root/elemA/elemB&quot;);
 * </pre>
 */
public class XPathUtil {

    protected static Log log = LogFactory.getLog(XPathUtil.class);

    private Document doc = null;

    // 用于快速查询的cache
    private Map<String, Element> lookupCache = new HashMap<String, Element>();

    /**
     * 私有的Constructor
     * @param is 流文件
     * @param validate 是否需要验证
     * @throws Exception
     */
    private XPathUtil(InputStream is, boolean validate) throws Exception {
        SAXBuilder builder = null;
        if (validate == true) {
            if (log.isInfoEnabled()) {
                log.info("需要使用文档验证，可以使用Schema或者DTD。");
            }
            // 为了支持Schema，必须进行下面的处理
            builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
            builder.setFeature("http://apache.org/xml/features/validation/schema", true);
            builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
                    "http://www.w3.org/2001/XMLSchema-instance");
        } else {
            builder = new SAXBuilder();
        }

        try {
            doc = builder.build(is);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("解析XML文件发生异常！" + e.getLocalizedMessage());
            }
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * 得到一个工具类的实例。默认不需要验证。
     * @param filePath 需要读取的文件路径
     * @return XMLUtil
     * @throws Exception
     */
    public static XPathUtil getInsance(String filePath) throws Exception {
        return getInsance(filePath, false);
    }

    /**
     * 得到一个工具类的实例。 <br/><b>如果指明需要Schema验证，XML文件中指明Schema的路径分隔符号 必须使用左斜线</b>
     * @param filePath 需要读取的文件路径
     * @param validate 是否需要验证
     * @return XMLUtil
     * @throws Exception
     */
    public static XPathUtil getInsance(String filePath, boolean validate) throws Exception {
        try {
            return getInsance(new FileInputStream(filePath), validate);
        } catch (FileNotFoundException e) {
            if (log.isErrorEnabled()) {
                log.error("读取指定的文件发生异常！" + e.getLocalizedMessage());
            }
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * 得到一个工具类的实例。默认不需要验证。
     * @param is 流文件
     * @return XMLUtil
     * @throws Exception
     */
    public static XPathUtil getInsance(InputStream is) throws Exception {
        return getInsance(is, false);
    }

    /**
     * 得到一个工具类的实例。 <br/><b>如果指明需要验证，只能使用DTD</b>
     * @param is 流文件
     * @param validate
     * @return XMLUtil
     * @throws Exception
     */
    public static XPathUtil getInsance(InputStream is, boolean validate) throws Exception {
        return new XPathUtil(is, validate);
    }

    /**
     * 把XML文档的内容输出到一个给定的流对象中。默认编码GB2312
     * @param stream 给定的输出流对象
     * @throws Exception
     */
    public void writeToStream(OutputStream stream) throws Exception {
        writeToStream(stream, "utf8");
    }

    /**
     * 把XML文档的内容输出到一个给定的流对象中。
     * @param stream 给定的输出流对象
     * @param encoding 指定字符编码。
     * @throws Exception
     */
    public void writeToStream(OutputStream stream, String encoding) throws Exception {
        // 表现形式的常量
        // final String INDENT = " ";
        // final boolean NEW_LINES = true;

        try {
            // XMLOutputter out = new XMLOutputter(INDENT, NEW_LINES, encoding);
            XMLOutputter out = new XMLOutputter();
            out.output(doc, stream);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("输出文件到指定的流对象发生异常！" + e.getLocalizedMessage());
            }
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * 返回所有满足XPath条件的节点元素集合。
     * @param xpath
     * @return List
     * @throws Exception
     */
    public List getAllElements(String xpath) throws Exception {
        List elements = null;
        try {
            elements = XPath.selectNodes(doc, xpath);
        } catch (JDOMException e) {
            if (log.isErrorEnabled()) {
                log.error("获取节点元素集合发生异常！" + e.getLocalizedMessage());
            }
            throw new Exception(e.getLocalizedMessage());
        }
        return elements;
    }

    /**
     * 返回满足XPath条件的第一个节点元素。
     * @param xpath
     * @return Element
     * @throws Exception
     */
    public Element getSingleElement(String xpath) throws Exception {
        // 所有查询单个元素的方法都调用了这个方法，所以只在这里使用cache
        if (lookupCache.containsKey(xpath)) {
            return (Element) lookupCache.get(xpath);
        } else {
            Element element = null;
            try {
                element = (Element) XPath.selectSingleNode(doc, xpath);
            } catch (JDOMException e) {
                if (log.isErrorEnabled()) {
                    log.error("获取节点元素发生异常！" + e.getLocalizedMessage());
                }
                throw new Exception(e.getLocalizedMessage());
            }
            lookupCache.put(xpath, element);
            return element;
        }
    }

    public Element getSingleElement(String xpath, Element node)
    {
        Element element=null;
        try {
            element=(Element) XPath.selectSingleNode(node, xpath);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return element;
    }

    public List<Element> getElements(String xpath, Element node)
    {
        List<Element> element=null;
        try {
            element=(List<Element>) XPath.selectNodes(node, xpath);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return element;
    }

    /**
     * 返回满足XPath条件的第一个节点元素的内容，字符串格式
     * @param xpath
     * @return String。如果指定的元素不存在，返回null。
     * @throws Exception
     */
    public String getSingleElementText(String xpath) throws Exception {
        Element element = getSingleElement(xpath);
        if (element == null) {
            return null;
        } else {
            return element.getTextTrim();
        }
    }

    /**
     * 返回满足XPath条件的第一个节点元素的指定属性。
     * @param xpath
     * @param attrName
     * @return Attribute
     * @throws Exception
     */
    public Attribute getElementAttribute(String xpath, String attrName) throws Exception {
        if (getSingleElement(xpath) == null) {
            return null;
        } else {
            return getSingleElement(xpath).getAttribute(attrName);
        }
    }

    /**
     * 返回满足XPath条件的第一个节点元素的指定属性的内容值。
     * @param xpath
     * @param attrName
     * @return String 属性的内容值，如果指定的属性不存在，返回null
     * @throws Exception
     */
    public String getElementAttributeValue(String xpath, String attrName) throws Exception {
        Attribute attr = getElementAttribute(xpath, attrName);
        if (attr == null) {
            return null;
        } else {
            return attr.getValue().trim();
        }
    }

    /**
     * 在指定的元素下面增加一个元素。
     * @param xpath 指定的元素
     * @param elemName 增加元素的名称
     * @param elemText 增加元素的内容
     * @throws Exception
     */
    public void addElement(String xpath, String elemName, String elemText) throws Exception {
        Element parent = getSingleElement(xpath);
        parent.addContent(new Element(elemName).addContent(elemText));
    }

    /**
     * 使指定位置的元素从他的上级脱离。并且返回这个元素。如果没有上级，不作任何删除的操作。
     * @param xpath
     * @return 被修改的元素
     * @throws Exception
     */
    public Content removeElement(String xpath) throws Exception {
        lookupCache.remove(xpath);
        Element element = getSingleElement(xpath);
        if (element.isRootElement()) {
            return element;
        } else {
            return element.detach();
        }
    }

    /**
     * 改变指定元素的文本内容。
     * @param xpath 指定元素
     * @param elemText 需要设置的文本
     * @throws Exception 如果指定的元素不存在
     */
    public void setElementText(String xpath, String elemText) throws Exception {
        Element element = getSingleElement(xpath);
        if (element == null) {
            throw new Exception("指定的元素不存在！");
        } else {
            element.setText(elemText);
        }
    }

    /**
     * 在指定路径的元素上增加一个属性。如果同名属性已经存在，重新设置这个属性的值。
     * @param xpath
     * @param attrName
     * @param attrValue
     * @throws Exception
     */
    public void setAttribute(String xpath, String attrName, String attrValue) throws Exception {
        Element element = getSingleElement(xpath);
        try {
            element.setAttribute(attrName, attrValue);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("设置节点元素的属性发生异常！" + e.getLocalizedMessage());
            }
            throw new Exception(e.getLocalizedMessage());
        }
    }

    /**
     * 删除指定元素的指定属性。
     * @param xpath
     * @param attrName
     * @return boolean
     * @throws Exception
     */
    public boolean removeAttribute(String xpath, String attrName) throws Exception {
        Element element = getSingleElement(xpath);
        if (element == null) {
            return false;
        } else {
            return element.removeAttribute(attrName);
        }
    }
}
