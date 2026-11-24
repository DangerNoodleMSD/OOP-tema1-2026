package main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.CommandInput;
import fileio.InputLoader;
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

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         *
         * ObjectNode objectNode = MAPPER.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = MAPPER.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */

        Repo repo = Repo.getRepo();
        repo.init();

        List<CommandInput> cmds = inputLoader.getCommands();

        for (CommandInput cmd : cmds) {
            String command = cmd.getCommand();
            switch (command) {
                case "startImport" -> repo.startImportCmd(cmd);
                case "stopImport" -> repo.stopImportCmd(cmd);
                case "import" -> repo.importCmd(cmd);
                case "patch" -> repo.patchCmd(cmd);
                case "get" -> repo.patchCmd(cmd);
                default -> System.out.println("[ERROR] Shouldn't be able to get here");
            }
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
