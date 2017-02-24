package com.lsh.base.common.config;

import com.lsh.base.common.utils.ClassLoaderUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * config.xml格式：
 * <?xml version="1.0" encoding="UTF-8"?>
 * <configuration>
 * <properties resource="classpath:properties/cms.properties" />
 * </configuration>
 */
public class ConfigFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);

    private static final String DEFAULT_CFG_FILE = "config.xml";
    private static final XPathFactory xpathFactory = XPathFactory.newInstance();
    private static final XPath xpath = xpathFactory.newXPath();
    private static ItfConfig _configInstance = null;
    private static XPathExpression RESOURCE_XPATH_EXPRESSION;

    static {
        try {
            RESOURCE_XPATH_EXPRESSION = xpath.compile("//properties/@resource");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized static ItfConfig getConfig() {
        try {
            if (_configInstance != null) {
                return _configInstance;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = ClassLoaderUtils.getStream(DEFAULT_CFG_FILE);
            if (is == null) {
                logger.warn("没有默认资源配置文件：" + DEFAULT_CFG_FILE);
                return null;
            }
            Document xmlDoc = builder.parse(is);
            if (xmlDoc == null) {
                return null;
            }
            NodeList resourceNodes = (NodeList) RESOURCE_XPATH_EXPRESSION.evaluate(xmlDoc, XPathConstants.NODESET);
            List<String> resourceNames = new ArrayList<String>();
            for (int i = 0, size = resourceNodes.getLength(); i < size; i++) {
                resourceNames.add(StringUtils.trimToEmpty(resourceNodes.item(i).getTextContent()));
            }
            _configInstance = new MixedConfig(resourceNames.toArray(new String[]{}));
            return _configInstance;
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

}
