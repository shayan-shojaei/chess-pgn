package chess.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Game {
    private final Map<String, String> info = new HashMap<>();
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

    public String getWhitePlayer() {
        return info.getOrDefault("White", "N/A");
    }

    public String getBlackPlayer() {
        return info.getOrDefault("Black", "N/A");
    }

    public String getResult() {
        return info.getOrDefault("Result", "N/A");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game)) return false;
        Game other = (Game) o;
        if (((Game) o).getInfo().size() != this.getInfo().size()) return false;
        for (Map.Entry<String, String> info : this.getInfo().entrySet()) {
            if (other.getInfo().containsKey(info.getKey()) && !info.getValue().equals(other.getInfo().get(info.getKey()))) {
                return false;
            }
        }
        return Arrays.deepEquals(other.getBoard().getFinalPositions(), getBoard().getFinalPositions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(info, board);
    }
}
