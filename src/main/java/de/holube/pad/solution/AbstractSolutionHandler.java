package de.holube.pad.solution;

import com.google.gson.Gson;
import de.holube.pad.model.Board;
import de.holube.pad.util.Config;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractSolutionHandler implements SolutionHandler {

    @Getter
    private static final SolutionSaver saver;

    static {
        String json = null;
        try {
            json = Files.readString(Path.of("config.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        Config config = gson.fromJson(json, Config.class);
        if (config.SAVE_SOLUTIONS && config.SAVE_IMAGES) {
            saver = null;
        } else if (config.SAVE_SOLUTIONS) {
            saver = new FileSolutionSaver();
        } else if (config.SAVE_IMAGES) {
            saver = new ImageSolutionSaver();
        } else {
            saver = new EmptySolutionSaver();
        }
    }

    public void handleSolution(Board board) {
        getStats().addSolution(board);
        saver.save(board);
    }

}
