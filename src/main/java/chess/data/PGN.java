package chess.data;

import chess.util.PGNReader;

import java.io.File;
import java.util.ArrayList;

public class PGN {
    private ArrayList<Game> games;

    public PGN() {
        games = new ArrayList<>();
    }

    public PGN(String path) {
        this();
        insertBatchFromPGNFile(path);
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void insertBatchFromPGNFile(String path) {
        games.addAll(PGNReader.readPGNFile(path));
    }

    public void add(Game game) {
        games.add(game);
    }

    public void remove(Game game) {
        games.remove(game);
    }
}
