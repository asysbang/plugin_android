package com.asysbang.plugin.test;

import org.junit.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlTest {

    @Test
    public void testDom() {
        File gradleFile = new File(".idea/gradle.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(gradleFile);
            Node setNode = doc.getElementsByTagName("set").item(0);
            Element newOption = doc.createElement("option");
            newOption.setAttribute("name", "$PROJECT_DIR$/hellomodule");
            setNode.appendChild(newOption);
//
//            NodeList set = doc.getElementsByTagName("set").item(0).getChildNodes();
//
//            System.out.println("===>>>version : " + set.getLength());
//            for (int i = 0; i < set.getLength(); i++) {
//                Node item = set.item(i);
//                if (item.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) item;
//                    String nodeValue = element.getAttribute("value");
//                    System.out.println("===>>>nodeValue : " + nodeValue);
//                }
//            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // 10、创建 Transformer 对象
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            // 文档字符编码
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            // 11、创建 DOMSource 对象
            DOMSource domSource = new DOMSource(doc);
            // 12、创建 StreamResult 对象
            StreamResult reStreamResult = new StreamResult(".idea/gradle.xml");
            transformer.transform(domSource, reStreamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
