package edu.duke.ece651.risc.shared;

import java.io.IOException;

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
    if(o.getClass() == String.class) return (String) o;
    String res = null;
    try {
      res = om.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return res;
  }

  /**
   * Deserialze JSON String into object
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

}
