package chess.data;

import chess.util.PGNReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

class StatisticsTest {
    Statistics stats;

    @BeforeEach
    void setUp() {
        stats = new Statistics(new PGN(StatisticsTest.class.getResource("/test.pgn").getPath()));
    }

    @Test
    void findGamesByName() {
        Assertions.assertEquals(9, stats.findGamesByName("Artemiev, Vladislav").size());
        Assertions.assertEquals(7, stats.findGamesByName("Emir, Doruk").size());
    }

    @Test
    void getWinLossRate() {
        Assertions.assertEquals(0.6666666666666666, stats.getWinLossRate("Emir, Doruk"));
        Assertions.assertEquals(0.0, stats.getWinLossRate("Pribyl, Viktor"));
        Assertions.assertEquals(0.2, stats.getWinLossRate("Tinmaz, Kerem"));
    }

    @Test
    void countGamesWithOpening() {
        Assertions.assertEquals(1, stats.countGamesWithOpening("QGD Slav accepted"));
        Assertions.assertEquals(8, stats.countGamesWithOpening("Caro-Kann"));
        Assertions.assertEquals(0, stats.countGamesWithOpening("Shayan' Gambit"));
    }

    @Test
    void getOpeningWinRate() {
        Assertions.assertEquals(100, stats.getOpeningWinRate("QGD Slav accepted"));
        Assertions.assertEquals(23.809523809523807, stats.getOpeningWinRate("Sicilian"));
    }

    @Test
    void getMostUsedMoves() {
        //  idk how to test this
        System.out.println(stats.getMostUsedMoves(5));
    }

    @Test
    void expectedMovesBeforeFirstCapture() {
        Assertions.assertEquals(12.627777777777778, stats.expectedMovesBeforeFirstCapture());
    }

    @Test
    void predictNextMove() {
        //  idk how to test this
        Game firstGame = PGNReader.readPGNFile(GameTest.class.getResource("/test.pgn").getPath()).get(0);
        System.out.println(stats.predictNextMove(firstGame.getBoard().getMoves().stream().limit(3).collect(Collectors.toList()), 3));
    }
}