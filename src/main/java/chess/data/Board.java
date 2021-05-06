package chess.data;

import chess.data.enums.Piece;
import chess.data.enums.Square;
import chess.util.Util;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Board {

    private final char[][] finalPositions = new char[8][8];

    private HashMap<Square, Piece> grid = new HashMap<>();
    private ArrayList<Piece> capturedPieces = null;
    private Stack<Move> moves = null;

    public Board() {
        this.capturedPieces = new ArrayList<>();
        this.moves = new Stack<>();
        resetBoard();
    }

    public Board(ArrayList<String> doneMoves) {
        this();
        for (int i = 0; i < doneMoves.size(); i++) {
            doMove(doneMoves.get(i), i % 2 == 0);
        }
    }


    public void doMove(String notation, boolean whiteMove) {

        // retrieve square address from notation
        String squareRegex = "[a-h][0-8]";
        Pattern squarePattern = Pattern.compile(squareRegex);
        Matcher squareMatcher = squarePattern.matcher(notation);
        squareMatcher.find();
        Square to = Square.getEnumByString(squareMatcher.group().toUpperCase(Locale.ROOT));
        Piece movingPiece = getPieceFromNotation(notation, whiteMove);

        boolean isCapturing = notation.contains("x");
        notation = notation.replaceFirst("x", "");
        String promotion = notation.contains("=") ? notation.substring(notation.indexOf('='), notation.indexOf('=') + 1) : null;
        notation = notation.replaceFirst("x?(=[A-Z])?", "");

        int fromNotationEnd = notation.indexOf(to.toString().toLowerCase(Locale.ROOT));
        int fromNotationStart = movingPiece.toChar() == Character.MIN_VALUE ? 0 : 1;

        switch (movingPiece) {
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                moveBishop(to, movingPiece, whiteMove, isCapturing);
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                moveKnight(to, movingPiece, whiteMove, isCapturing);
                break;
        }
    }

    private void moveBishop(Square to, Piece moving, boolean whiteMove, boolean isCapturing) {
        Square[] bishopPositions = findPieceLocations(moving);
        char[] fr = to.toString().toCharArray();
        for (int i = 0; i < bishopPositions.length; i++) {
            Square from = bishopPositions[i];
            char[] currentFr = from.toString().toCharArray();
            if (Math.abs(currentFr[0] - fr[0]) == Math.abs(currentFr[1] - fr[1])) {
                movePiece(isCapturing, from, to, moving);
                break;
            }
        }
    }


    private void moveKnight(Square to, Piece moving, boolean whiteMove, boolean isCapturing) {
        ArrayList<Square> knightPositions = (ArrayList<Square>) Arrays.stream(findPieceLocations(moving)).collect(Collectors.toList());
        char[] fr = to.toString().toCharArray();

        Stack<Move> pastMoves = (Stack<Move>) moves.clone();
        while (!pastMoves.isEmpty()) {
            Move currentMove = pastMoves.pop();
            if (knightPositions.contains(currentMove.getTo())) {
                char[] currentFr = currentMove.getTo().toString().toCharArray();
                int horizontalDiff = Math.abs(currentFr[1] - fr[1]);
                int verticalDiff = Math.abs(currentFr[0] - fr[0]);
                if ((horizontalDiff == 1 && verticalDiff == 2) || (horizontalDiff == 2 && verticalDiff == 1)) {
                    movePiece(isCapturing, currentMove.getTo(), to, moving);
                    return;
                }
            }
        }

        //no result in loop --- default positions
        Square from = null;
        switch (to) {
            case D2:
            case C3:
            case A3:
                from = Square.B1;
                break;
            case E2:
            case F3:
            case H3:
                from = Square.G1;
                break;
            case D7:
            case C6:
            case A6:
                from = Square.B8;
                break;
            case E7:
            case F6:
            case H6:
                from = Square.G8;
                break;
        }
        movePiece(isCapturing, from, to, moving);
    }

    private void movePiece(boolean isCapturing, Square from, Square to, Piece moving) {
        Move move = null;
        if (isCapturing) {
            Piece captured = grid.get(to);
            capturedPieces.add(captured);
            move = new Move(from, to, moving, captured);
        } else {
            move = new Move(from, to, moving);
        }
        grid.put(from, null);
        grid.put(to, moving);
        moves.push(move);
    }

    private Square[] findPieceLocations(Piece piece) {
        return grid.keySet().stream().filter(square -> grid.get(square) == piece).toArray(Square[]::new);
    }


    private Piece getPieceFromNotation(String notation, boolean whiteMove) {
        Piece movingPiece = null;
        if (notation.substring(0, 1).matches("[A-Z]")) {
            char movingChar = notation.charAt(0);
            if (!whiteMove) {
                movingChar = String.valueOf(movingChar).toLowerCase(Locale.ROOT).charAt(0);
            }
            movingPiece = Piece.getEnumByString(movingChar);
        } else {
            movingPiece = whiteMove ? Piece.WHITE_PAWN : Piece.BLACK_PAWN;
        }
        return movingPiece;
    }

    public Move undoMove() {
        Move toUndo = moves.pop();


        return toUndo;
    }

    public HashMap<Square, Piece> getGrid() {
        return grid;
    }

    private static char[] WHITE_MAIN = new char[]{'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};

    private static char[] BLACK_MAIN = new char[]{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'};

    private void resetBoard() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 'A'; j <= 'H'; j++) {
                Piece piece = null;
                if (i == 7) {
                    piece = Piece.BLACK_PAWN;
                } else if (i == 2) {
                    piece = Piece.WHITE_PAWN;
                } else if (i == 8) {
                    piece = Piece.getEnumByString(BLACK_MAIN[j - 'A']);
                } else if (i == 1) {
                    piece = Piece.getEnumByString(WHITE_MAIN[j - 'A']);
                }
                grid.put(Square.getEnumByString((char) j + String.valueOf(i)), piece);
            }
        }
        updateCharArray();
        while (!moves.isEmpty()) {
            moves.pop();
        }
        capturedPieces.clear();
    }

    private void updateCharArray() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 'A'; j <= 'H'; j++) {
                Square square = Square.getEnumByString((char) j + "" + i);
                Piece piece = grid.get(square);
                char text = ' ';
                if (piece != null) {
                    if (piece == Piece.WHITE_PAWN) {
                        text = 'P';
                    } else if (piece == Piece.BLACK_PAWN) {
                        text = 'p';
                    } else {
                        text = piece.toChar();
                    }
                }
                finalPositions[i - 1][j - 'A'] = text;
            }
        }

    }

    public char[][] getFinalPositions() {
        updateCharArray();
        return finalPositions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = 0; i < 8; i++) {
            sb.append((char) ('A' + i));
            sb.append(' ');
        }
        sb.append('\n');
        char[][] positions = getFinalPositions();
        for (int i = 0; i < 8; i++) {
            char[] row = positions[i];
            sb.append((i + 1));
            sb.append(" ");
            for (char square : row) {
                sb.append(square);
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public ArrayList<Piece> getCaptures() {
        return capturedPieces;
    }

}
