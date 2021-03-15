package edu.duke.ece651.risc.shared;

/**
 * This is a serializer for object
 */
public interface Serializer {
  /**
   * Serialize object to string
   *
   * @param o is the object to be serialize
   * @return the parsed string
   */
  public String serialize(Object o);

  /**
   * Deserialize string to object
   *
   * @param string is the string to be deserialized
   * @param c      is the type of the object
   * @return the object, need to be casted
   */
  public Object deserialize(String string, Class<?> c);
}
