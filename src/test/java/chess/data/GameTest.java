package chess.data;

import chess.util.PGNReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = PGNReader.readPGNFile(GameTest.class.getResource("/test.pgn").getPath()).get(0);
    }

    @Test
    void testTags() {
        Assertions.assertEquals("European Online CC GpA", game.getInfo().get("Event"));
        Assertions.assertEquals("2021.03.27", game.getInfo().get("Date"));
        Assertions.assertEquals("Rapport, Richard", game.getWhitePlayer());
        Assertions.assertEquals("Horvath, Laszlo", game.getBlackPlayer());
        Assertions.assertEquals("1-0", game.getResult());
    }

    @Test
    void testResults() {
        Assertions.assertEquals("""
                  A B C D E F G H\s
                1       K        \s
                2 P b            \s
                3 n             R\s
                4         N   P  \s
                5       P   b    \s
                6       p     Q  \s
                7 p p         n  \s
                8 r     q   r k R\s
                """, game.getBoard().toString());
        Assertions.assertEquals(13, game.getBoard().getCaptures().size());
        game.getBoard().undoMove();
        Assertions.assertEquals("""
                  A B C D E F G H\s
                1       K        \s
                2 P b            \s
                3 n             R\s
                4         N   P  \s
                5       P   b   R\s
                6       p     Q  \s
                7 p p         n  \s
                8 r     q   r k  \s
                """, game.getBoard().toString());
        Stack<Move> reverseMoves = game.getBoard().resetBoard();
        Assertions.assertEquals("""
                  A B C D E F G H\s
                1 R N B Q K B N R\s
                2 P P P P P P P P\s
                3                \s
                4                \s
                5                \s
                6                \s
                7 p p p p p p p p\s
                8 r n b q k b n r\s
                """, game.getBoard().toString());
        while (!reverseMoves.isEmpty()) {
            game.getBoard().doMove(reverseMoves.pop());
        }
        Assertions.assertEquals("""
                  A B C D E F G H\s
                1       K        \s
                2 P b            \s
                3 n             R\s
                4         N   P  \s
                5       P   b   R\s
                6       p     Q  \s
                7 p p         n  \s
                8 r     q   r k  \s
                """, game.getBoard().toString());
    }
}