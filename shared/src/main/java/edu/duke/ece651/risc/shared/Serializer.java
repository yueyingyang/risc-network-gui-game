package edu.duke.ece651.risc.shared;

public interface Serializer {
  public String serialize(Object o);

  public Object deserialize(String JSONString, Class<?> c);
}
