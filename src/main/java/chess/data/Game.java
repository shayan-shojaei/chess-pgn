package chess.data;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private Map<String, String> info = new HashMap<>();
    private Board board;

    public Game() {
        this.board = new Board();
    }

    public Game(Board board, String whitePlayer, String blackPlayer, String opening, String result) {
        info.put("White", whitePlayer);
        info.put("Black", blackPlayer);
        info.put("Opening", opening);
        info.put("Result", result);
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Map<String, String> getInfo() {
        return info;
    }

}
