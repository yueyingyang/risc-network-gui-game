package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.ServerPlayer;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface actionHandler {
    void apply(ServerPlayer player, JsonNode rootNode);
}
