package chess.data;

import java.util.*;
import java.util.stream.Collectors;

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
        List<Game> gamesWithOpening = pgn.getGames().stream().filter(game -> game.getInfo().get("Opening").equals(opening)).collect(Collectors.toList());
        double winCount = gamesWithOpening.stream().filter(game -> game.getResult().equals("1-0")).count();
        return winCount / gamesWithOpening.size() * 100;
    }

    public List<Move> getMostUsedMoves(int limit) {
        HashMap<Move, Integer> moves = new HashMap<>();
        for (Game game : pgn.getGames()) {
            for (Move m : game.getBoard().cloneMoves()) {
                moves.put(m, moves.getOrDefault(m, 0) + 1);
            }
        }

        return moves.keySet().stream().sorted(Comparator.comparingInt(moves::get).reversed()).limit(limit).collect(Collectors.toList());
    }

    public double expectedMovesBeforeFirstCapture() {
        double[] movesBeforeFirstCapture = new double[pgn.getGames().size()];
        double foundCount = 0;
        for (int i = 0; i < movesBeforeFirstCapture.length; i++) {
            Game game = pgn.getGames().get(i);
            Optional<Move> firstCapture = game.getBoard().cloneMoves().stream().filter(m -> m.getCaptured() != null).findFirst();
            if (firstCapture.isPresent()) {
                int captureIndex = game.getBoard().cloneMoves().indexOf(firstCapture.get());
                movesBeforeFirstCapture[i] = captureIndex;
                foundCount++;
            }
        }

        return foundCount == 0 ? -1 : Arrays.stream(movesBeforeFirstCapture).sum() / foundCount;
    }

    public List<Move> predictNextMove(List<Move> previousMoves, int limit) {
        HashMap<Move, Integer> moves = new HashMap<>();
        for (Game game : pgn.getGames()) {
            int indexOfSub = Collections.indexOfSubList(game.getBoard().cloneMoves(), previousMoves);
            if (indexOfSub != -1) {
                Move toUpdate = game.getBoard().cloneMoves().get(indexOfSub + previousMoves.size());
                moves.put(toUpdate, moves.getOrDefault(toUpdate, 0) + 1);
            }
        }
        return moves.keySet().stream().sorted(Comparator.comparingInt(moves::get).reversed()).limit(limit).collect(Collectors.toList());
    }
}
