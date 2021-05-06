package chess.util;

import chess.data.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PGNReaderTest {


    @Test
    void testReadPGNFile() {
        ArrayList<Game> games = PGNReader.readPGNFile("C:\\Users\\shaya\\Downloads\\euroonccgpa21.pgn");
        assert games != null;
        Assertions.assertEquals(180, games.size());
        Assertions.assertEquals(20, games.get(0).getInfo().size());
        Assertions.assertEquals("King's Indian", games.get(0).getInfo().get("Opening"));
    }
}