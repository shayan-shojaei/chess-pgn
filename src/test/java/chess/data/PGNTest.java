package chess.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class PGNTest {
    @Test
    void fileTest() {
        PGN pgn = new PGN(PGNTest.class.getResource("/test2.pgn").getPath());
        Game game = pgn.getGames().get(2);
        Assertions.assertEquals("""
                0 A B C D E F G H\s
                1             K R\s
                2 P           R P\s
                3     P   P     r\s
                4           P p  \s
                5       p        \s
                6   p p          \s
                7 p       k      \s
                8               r\s
                """, game.getBoard().toString());

    }
}