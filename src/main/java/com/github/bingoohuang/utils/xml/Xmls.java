package com.github.bingoohuang.utils.xml;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.EnumSet;

public class Xmls {
    public enum MarshalOption {
        PrettyFormat, WithXmlDeclaration
    }

    public static String marshal(Object bean, MarshalOption... marshalOptions) {
        EnumSet<MarshalOption> enumSet = Sets.newEnumSet(Arrays.asList(marshalOptions), MarshalOption.class);
        return marshal(bean, enumSet, bean.getClass());
    }

    public static String marshal(Object bean, EnumSet<MarshalOption> marshalOptions, Class... types) {
        StringWriter sw = new StringWriter();

        try {
            JAXBContext carContext = JAXBContext.newInstance(types);
            Marshaller marshaller = carContext.createMarshaller();
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter streamWriter = xof.createXMLStreamWriter(sw);
            PrettyPrintHandler handler = new PrettyPrintHandler(streamWriter, marshalOptions);
            XMLStreamWriter prettyPrintWriter = (XMLStreamWriter) Proxy.newProxyInstance(
                    XMLStreamWriter.class.getClassLoader(), new Class[]{XMLStreamWriter.class}, handler);
            marshaller.marshal(bean, prettyPrintWriter);
            return sw.toString();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static <T> T unmarshal(String xml, Class<T> beanClass) {
        StringReader reader = new StringReader(xml);
        return JAXB.unmarshal(reader, beanClass);
    }

    public static String prettyXml(String xml) {
        final boolean omitXmlDeclaration = !xml.startsWith("<?xml");

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(parseXmlFile(xml));
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document parseXmlFile(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return db.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
