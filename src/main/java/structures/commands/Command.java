package structures.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import structures.repository.Repo;
import structures.types.SimulationClock;

public interface Command {
    /**
     * The function that executes the given command
     */
    void execute(SimulationClock clock, Repo repo, ArrayNode output, ObjectMapper mapper);
}
