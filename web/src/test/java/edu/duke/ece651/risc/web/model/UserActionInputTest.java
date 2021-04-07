package edu.duke.ece651.risc.web.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserActionInputTest {
  @Test
  void test_getter_setter() {
    UserActionInput input = new UserActionInput();
    input.setFromName("form name");
    input.setToName("to name");
    input.setFromType("from type");
    input.setToType("to type");
    input.setSoldierNum("1");
    assertEquals("form name", input.getFromName());
    assertEquals("to name", input.getToName());
    assertEquals("from type", input.getFromType());
    assertEquals("to type", input.getToType());
    assertEquals(1, input.getSoldierNum());
  }

}