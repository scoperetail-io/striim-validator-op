package com.scoperetail.commons.striim;

import com.scoperetail.commons.jaxb.JaxbUtil;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class XsdSchemaValidatorTest {
    private static final String BASE_DIR = Paths.get("").toAbsolutePath().toString();
    private static final String SCHEMA_DIR = BASE_DIR + "/src/test/schema/";
    private static final String XML_DIR = BASE_DIR + "/src/test/xml/";

    @Test
    void validXml() throws IOException, SAXException {
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(SCHEMA_DIR + "purchase_order.xsd"));
        String xml = new String(Files.readAllBytes(Paths.get(XML_DIR + "purchase_order_valid.xml")));
        assertTrue(JaxbUtil.isValidMessage(xml,schema));
    }

    @Test
    void invalidXml() throws IOException, SAXException {
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(SCHEMA_DIR + "purchase_order.xsd"));
        String xml = new String(Files.readAllBytes(Paths.get(XML_DIR + "purchase_order_invalid.xml")));
        assertFalse(JaxbUtil.isValidMessage(xml,schema));
    }
}