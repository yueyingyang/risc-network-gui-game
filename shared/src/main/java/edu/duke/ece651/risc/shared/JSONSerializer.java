package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSONSerializer based on JACKSON
 */
public class JSONSerializer implements Serializer {
  public ObjectMapper getOm() {
    return om;
  }

  private final ObjectMapper om;

  public JSONSerializer() {
    // initialize objectMapper
    this.om = new ObjectMapper();
    // set the serialization as all fields
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  /**
   * Serialize object into JSON string using writeValueAsString
   *
   * @param o is the object to be serialize
   * @return the JSON string
   */
  @Override
  public String serialize(Object o) {
    //if(o.getClass() == String.class) return (String) o;
    String res = null;
    try {
      res = om.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return res;
  }

  /**
   * Deserialize JSON String into object
   *
   * @param JSONString to be deserialized
   * @param c          is the type of the object
   * @return the object, needed to be cast
   */
  @Override
  public Object deserialize(String JSONString, Class<?> c) {
    try {
      return om.readValue(JSONString, c);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param o is the object to be cloned
   * @param c is the class of object
   * @return a deep clone object
   */
  public Object clone(Object o, Class<?> c) {
    return deserialize(serialize(o), c);
  }

  public String serializeList(List<?> list, Class<?> c) throws JsonProcessingException {
    return om.writerFor(om.getTypeFactory().constructCollectionType(List.class, c))
            .writeValueAsString(list);
  }

  public List<Object> deserializeList(String str, Class<?> c) throws JsonProcessingException {
    List<Object> data = om.readValue(str,
            om.getTypeFactory().constructCollectionType(List.class, c));
    return data;
  }
}
