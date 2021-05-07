package chess.util;

import chess.data.Board;
import chess.data.Game;
import chess.data.Move;
import chess.data.enums.Piece;
import chess.data.enums.Square;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNReader {
    public static ArrayList<Game> readPGNFile(String location) {
        try {
            File mFile = new File(location);
            FileInputStream stream = new FileInputStream(mFile);
            String pgnText = new String(stream.readAllBytes());
            return importGamesIntoList(pgnText);
        } catch (Exception e) {
            System.err.println(e instanceof FileNotFoundException ?
                    "File with given location was not found" :
                    "There was an error while reading the PGN file. Verify the validity and try again.");
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Game> importGamesIntoList(String pgnText) {
        ArrayList<Game> games = new ArrayList<>();
        String[] parts = pgnText.split("\n\n");
        for (int i = 0; i < parts.length - 1; i += 2) {
            Game game = new Game();
            String gameInfo = parts[i];
            for (String line : gameInfo.split("\n")) {
                String[] infoSplit = removeFirstAndLastCharacters(line).split(" \"");
                game.getInfo().put(infoSplit[0], infoSplit[1].substring(0, infoSplit[1].length() - 1));
            }
            // detect all notations
            String notationRegex = "([A-Z]?+[a-z]+[0-9](=[A-Z]?[a-z]?)?\\+?#?)|O(-O)+";
            Pattern notationPattern = Pattern.compile(notationRegex);
            Matcher notationMatcher = notationPattern.matcher(parts[i + 1]);

            ArrayList<String> notationList = new ArrayList<>();
            while (notationMatcher.find()) {
                String notation = notationMatcher.group();
                notationList.add(notation);
            }
            Board board = new Board(notationList);
            game.setBoard(board);
            games.add(game);
        }

        return games;
    }

    private static String removeFirstAndLastCharacters(String original) {
        return original.substring(1, original.length() - 1);
    }


}
