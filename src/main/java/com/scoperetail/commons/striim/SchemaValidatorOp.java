package com.scoperetail.commons.striim;

import com.scoperetail.commons.jaxb.JaxbUtil;
import com.webaction.anno.AdapterType;
import com.webaction.anno.PropertyTemplate;
import com.webaction.anno.PropertyTemplateProperty;
import com.webaction.runtime.components.openprocessor.StriimOpenProcessor;
import com.webaction.runtime.containers.IBatch;
import com.webaction.runtime.containers.WAEvent;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

@PropertyTemplate(
    name = "SchemaValidatorOpV4",
    type = AdapterType.process,
    properties = {
      @PropertyTemplateProperty(
          label = "Schema Directory",
          description = "Directory where Schema File is present",
          name = "schemaFileDir",
          type = String.class,
          required = true,
          defaultValue = "/"),
      @PropertyTemplateProperty(
          label = "Schema File Name",
          description = "Name of the Schema File",
          name = "schemaFileName",
          type = String.class,
          required = true,
          defaultValue = "myschema.xsd")
    },
    // The names of outputType and inputType are relative to Striim: output from a native Striim
    // code to your custom component, and input from your custom component to a native component.
    outputType = com.webaction.proc.events.WAEvent.class,
    inputType = com.webaction.proc.events.WAEvent.class)
public class SchemaValidatorOp extends StriimOpenProcessor {
  private String schemaFileDir;
  private String schemaFileName;
  private Schema schema;
  private boolean isPropertiesLoaded = false;

  public void run() {
    IBatch<WAEvent> event = getAdded();
    Iterator<WAEvent> it = event.iterator();
    loadProperties();

    while (it.hasNext()) {
      final com.webaction.proc.events.WAEvent waEvent =
          (com.webaction.proc.events.WAEvent) it.next().data;

      String xml = waEvent.data[0].toString();
      // TODO - Remove the trimming of start and end character once we get the guidance from
      // striim
      // team
      xml = xml.replaceFirst("a", "");
      xml = xml.replaceFirst("z", "");
      // TODO - Change to Log statement
//      System.out.println("Raw Data:[" + waEvent.data[0] + "]");
      System.out.println("Cleaned Data:[" + xml + "]");

      final boolean isValid = JaxbUtil.isValidMessage(xml, schema);
      // TODO - Change to Log statement
      System.out.println("Is Valid:[" + isValid + "]");

      waEvent.userdata.put("SchemaValidatorOp.used", "true");
      waEvent.userdata.put("SchemaValidatorOp.isValid", isValid);
      // TODO - Change to Log statement
      System.out.println("waEvent.userdata:[" + waEvent.userdata + "]");
      send(waEvent);
    }
  }

  private boolean loadProperties() {
    if (!isPropertiesLoaded) {
      Map<String, Object> props = getProperties();
      if (props.containsKey("schemaFileDir")) {
        schemaFileDir = props.get("schemaFileDir").toString();
      }
      if (props.containsKey("schemaFileName")) {
        schemaFileName = props.get("schemaFileName").toString();
      }
      if (schema == null) {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
          schema = factory.newSchema(new File(schemaFileDir + schemaFileName));
        } catch (SAXException e) {
          throw new RuntimeException(e);
        }
      }
      isPropertiesLoaded = true;
    }
    return true;
  }

  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

  public Map getAggVec() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setAggVec(Map aggVec) {
    // TODO Auto-generated method stub

  }
}
