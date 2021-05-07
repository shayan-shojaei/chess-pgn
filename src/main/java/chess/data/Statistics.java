package chess.data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {
    private PGN pgn;

    public Statistics() {
        pgn = new PGN();
    }

    public Statistics(PGN pgn) {
        this.pgn = pgn;
    }

    public List<Game> findGamesByName(String playerName) {
        return pgn.getGames().stream().filter(game -> game.getBlackPlayer().equals(playerName) || game.getWhitePlayer().equals(playerName)).collect(Collectors.toList());
    }

    public double getWinLossRate(String playerName) {
        List<Game> playerGames = findGamesByName(playerName);
        double winCount = playerGames.stream()
                .filter(game -> (game.getWhitePlayer().equals(playerName) && game.getResult().equals("1-0"))
                        || (game.getBlackPlayer().equals(playerName) && game.getResult().equals("0-1")))
                .count();
        double lossCount = playerGames.stream()
                .filter(game -> (game.getWhitePlayer().equals(playerName) && game.getResult().equals("0-1"))
                        || (game.getBlackPlayer().equals(playerName) && game.getResult().equals("1-0")))
                .count();
        return winCount / lossCount;
    }

    public int countGamesWithOpening(String opening) {
        return (int) pgn.getGames().stream().filter(game -> game.getInfo().get("Opening").equals(opening)).count();
    }

    public double getOpeningWinRate(String opening) {
        Stream<Game> gamesWithOpening = pgn.getGames().stream().filter(game -> game.getInfo().get("Opening").equals(opening));
        double winCount = gamesWithOpening.filter(game -> game.getResult().equals("1-0")).count();
        return winCount / gamesWithOpening.count() * 100;
    }

    public List<Move> getMostUsedMoves(int n) {
        HashMap<Move, Integer> moves = new HashMap<>();
        int gameIndex = 0;
        while (moves.size() < n) {
            for (Move m : pgn.getGames().get(gameIndex).getBoard().getMoves()) {
                moves.put(m, moves.getOrDefault(m, 0) + 1);
            }
            gameIndex++;
        }

        return moves.keySet().stream().sorted(Comparator.comparingInt(moves::get)).collect(Collectors.toList());
    }

    public double expectedMovesBeforeFirstCapture() {
        double[] movesBeforeFirstCapture = new double[pgn.getGames().size()];
        double foundCount = 0;
        for (int i = 0; i < movesBeforeFirstCapture.length; i++) {
            Game game = pgn.getGames().get(i);
            Optional<Move> firstCapture = game.getBoard().getMoves().stream().filter(m -> m.getCaptured() != null).findFirst();
            if (firstCapture.isPresent()) {
                int captureIndex = game.getBoard().getMoves().indexOf(firstCapture.get());
                movesBeforeFirstCapture[i] = captureIndex;
                foundCount++;
            }
        }

        return foundCount == 0 ? -1 : Arrays.stream(movesBeforeFirstCapture).sum() / foundCount;
    }

    public List<Move> predictNextMove(List<Move> previousMoves) {
        HashMap<Move, Integer> moves = new HashMap<>();
        for (Game game : pgn.getGames()) {
            Stream<Move> movesSublist = game.getBoard().getMoves().stream().limit(previousMoves.size());
            if (movesSublist.equals(previousMoves.stream())) {
                Move toUpdate = game.getBoard().getMoves().get(game.getBoard().getMoves().size());
                moves.put(toUpdate, moves.getOrDefault(toUpdate, 0) + 1);
            }
        }
        return moves.keySet().stream().sorted(Comparator.comparingInt(moves::get)).collect(Collectors.toList());
    }
}
