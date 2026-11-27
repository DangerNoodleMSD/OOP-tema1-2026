package main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CommandInput;
import fileio.InputLoader;
import structures.commands.Command;
import structures.commands.CommandFactory;
import structures.repository.Repo;

/**
 * The entry point to this homework. It runs the checker that tests your
 * implementation.
 */
public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath  input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
            final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        Repo repo = Repo.getRepo();
        repo.init();

        List<CommandInput> cmds = inputLoader.getCommands();

        for (CommandInput cmd : cmds) {
            Command command = CommandFactory.create(cmd);
            command.execute(repo.getSimClk(), repo, output, MAPPER);
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
