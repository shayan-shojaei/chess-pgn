package chess.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }


    @Test
    void doMove() {
        board.doMove("d4", true);
        board.doMove("Nf6", false);
        board.doMove("Nc3", true);
        board.doMove("Bg7", false);

        System.out.println(board.toString());
    }

    @Test
    void undoMove() {
    }
}