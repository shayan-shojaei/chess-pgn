package chess.util;

import chess.data.Board;
import chess.data.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNReader {
    public static ArrayList<Game> readPGNFile(String location) {
        try {
            File mFile = new File(location);
            FileInputStream stream = new FileInputStream(mFile);
            String pgnText = new String(stream.readAllBytes()).strip().trim();
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
        String[] parts = pgnText.trim().split("(\\r?\\n){2}");
        for (int i = 0; i < parts.length - 1; i += 2) {
            Game game = new Game();
            String gameInfo = parts[i];
            for (String line : gameInfo.split("\n")) {
                String key = line.substring(line.indexOf("[") + 1, line.indexOf("\"") - 1);
                String value = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                game.getInfo().put(key, value);
            }
            // detect all notations
            String notationRegex = "([A-Z]?+[a-z]+[0-9](=[A-Z]?[a-z]?)?\\+?#?)|(O-O)|(O-O-O)";
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
