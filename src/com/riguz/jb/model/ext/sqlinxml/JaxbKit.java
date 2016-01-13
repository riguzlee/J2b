package com.riguz.jb.model.ext.sqlinxml;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxbKit {
    protected static final Logger LOG = LoggerFactory.getLogger(JaxbKit.class);

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(String src, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(new Class[] { clazz }).createUnmarshaller();
            result = (T) avm.unmarshal(new StringReader(src));
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(File xmlFile, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(new Class[] { clazz }).createUnmarshaller();
            result = (T) avm.unmarshal(xmlFile);
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(InputStream xmlStream, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(new Class[] { clazz }).createUnmarshaller();
            result = (T) avm.unmarshal(xmlStream);
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String marshal(Object jaxbElement) {
        StringWriter sw = null;
        try {
            Marshaller fm = JAXBContext.newInstance(new Class[] { jaxbElement.getClass() }).createMarshaller();
            fm.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
            sw = new StringWriter();
            fm.marshal(jaxbElement, sw);
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }
}
