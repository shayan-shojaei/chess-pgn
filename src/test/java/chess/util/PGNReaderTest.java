package chess.util;

import chess.data.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PGNReaderTest {

    @Test
    void testReadPGNFile() {
        ArrayList<Game> games = PGNReader.readPGNFile(PGNReaderTest.class.getResource("/test.pgn").getPath());
        assert games != null;
        Assertions.assertEquals(180, games.size());
        Assertions.assertEquals(20, games.get(0).getInfo().size());
        Assertions.assertEquals("King's Indian", games.get(0).getInfo().get("Opening"));
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
                """, games.get(0).getBoard().toString());

    }
}