package edu.duke.ece651.risc.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONSerializer implements Serializer {
  private final ObjectMapper om;

  public JSONSerializer() {
    this.om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  }

  @Override
  public String serialize(Object o) {
    String res = null;
    try {
      res = om.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return res;
  }

  @Override
  public Object deserialize(String JSONString, Class<?> c) {
    try {
      return om.readValue(JSONString, c);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Class<?> deserialize(String JSONString, TypeReference<Class<?>> c) {
    try {
      return om.readValue(JSONString, c);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
