package chess.data;

import chess.data.enums.Piece;
import chess.data.enums.Square;
import chess.util.Util;

import java.awt.*;
import java.util.*;
import java.util.List;
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
            updateCharArray();
        }
    }

    public Stack<Move> getMoves() {
        return moves;
    }

    public List<Move> cloneMoves() {
        return new ArrayList<>(moves);
    }

    public void doMove(Move move) {
        movePiece(move.getCaptured() != null, move.getFrom(), move.getTo(), move.getMoving(), move.getPromotion());
    }

    public void doMove(String notation, boolean whiteMove) {

        if (notation.equals("O-O-O")) {
            castle(false, whiteMove);
            return;
        } else if (notation.equals("O-O")) {
            castle(true, whiteMove);
            return;
        }

        // retrieve square address from notation
        String squareRegex = "[a-h][0-8]";
        Pattern squarePattern = Pattern.compile(squareRegex);
        Matcher squareMatcher = squarePattern.matcher(notation);
        squareMatcher.find();
        Square to = Square.getEnumByString(squareMatcher.group().toUpperCase(Locale.ROOT));
        Piece movingPiece = getPieceFromNotation(notation, whiteMove);

        boolean isCapturing = notation.contains("x");
        notation = notation.replaceFirst("x", "");
        String promotion = notation.contains("=") ? notation.substring(notation.indexOf('=') + 1, notation.indexOf('=') + 2) : null;
        if (promotion != null && !whiteMove) {
            promotion = promotion.toLowerCase(Locale.ROOT);
        }
        notation = notation.replaceFirst("x?(=[A-Z])?", "");

        int fromNotationEnd = notation.indexOf(to.toString().toLowerCase(Locale.ROOT));
        int fromNotationStart = movingPiece.toChar() == Character.MIN_VALUE ? 0 : 1;
        String fromCol = null;
        if (fromNotationStart < fromNotationEnd) {
            fromCol = notation.substring(fromNotationStart, fromNotationEnd).toUpperCase(Locale.ROOT);
        }

        switch (movingPiece) {
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                moveBishop(to, movingPiece, whiteMove, isCapturing);
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                moveKnight(to, movingPiece, whiteMove, isCapturing, fromCol);
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                moveRook(to, movingPiece, whiteMove, isCapturing, fromCol);
                break;
            case WHITE_KING:
            case BLACK_KING:
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                moveKingAndQueen(to, movingPiece, whiteMove, isCapturing);
                break;
            default:
                //pawn
                movePawn(to, movingPiece, whiteMove, isCapturing, promotion != null ? Piece.getEnumByString(promotion.charAt(0)) : null, fromCol);
        }
    }

    private void moveBishop(Square to, Piece moving, boolean whiteMove, boolean isCapturing) {
        Square[] bishopPositions = findPieceLocations(moving);
        char[] fr = to.toString().toCharArray();
        for (int i = 0; i < bishopPositions.length; i++) {
            Square from = bishopPositions[i];
            char[] currentFr = from.toString().toCharArray();
            if (Math.abs(currentFr[0] - fr[0]) == Math.abs(currentFr[1] - fr[1])) {
                movePiece(isCapturing, from, to, moving, null);
                break;
            }
        }
    }


    private void moveKnight(Square to, Piece moving, boolean whiteMove, boolean isCapturing, String col) {
        ArrayList<Square> knightPositions = (ArrayList<Square>) Arrays.stream(findPieceLocations(moving)).collect(Collectors.toList());
        char[] fr = to.toString().toCharArray();

        Square from = null;
        if (col != null) {
            Optional<Square> maybe = knightPositions.stream().filter(square -> square.toString().startsWith(col)).findFirst();
            if (maybe.isPresent()) {
                from = maybe.get();
                movePiece(isCapturing, from, to, moving, null);
                return;
            }
        }

        Stack<Move> pastMoves = (Stack<Move>) moves.clone();
        while (!pastMoves.isEmpty()) {
            Move currentMove = pastMoves.pop();
            if (whiteMove == Character.isLowerCase(currentMove.getMoving().toChar())) {
                continue;
            }
            if (knightPositions.contains(currentMove.getTo())) {
                char[] currentFr = currentMove.getTo().toString().toCharArray();
                int horizontalDiff = Math.abs(currentFr[1] - fr[1]);
                int verticalDiff = Math.abs(currentFr[0] - fr[0]);
                if ((horizontalDiff == 1 && verticalDiff == 2) || (horizontalDiff == 2 && verticalDiff == 1)) {
                    movePiece(isCapturing, currentMove.getTo(), to, moving, null);
                    return;
                }
            }
        }

        //no result in loop --- default positions
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
        movePiece(isCapturing, from, to, moving, null);
    }

    private void moveKingAndQueen(Square to, Piece moving, boolean whiteMove, boolean isCapturing) {
        Square[] positions = findPieceLocations(moving);
        if (positions.length > 0) {
            Square from = positions[0];
            movePiece(isCapturing, from, to, moving, null);
        }

    }

    private void moveRook(Square to, Piece moving, boolean whiteMove, boolean isCapturing, String col) {
        ArrayList<Square> positions = (ArrayList<Square>) Arrays.stream(findPieceLocations(moving)).collect(Collectors.toList());
        char[] fr = to.toString().toCharArray();

        if (col != null) {
            col = col.toUpperCase(Locale.ROOT);
            for (Square fromPos : positions) {
                if (fromPos.toString().contains(col)) {
                    movePiece(isCapturing, fromPos, to, moving, null);
                    return;
                }
            }

        }

        Stack<Move> pastMoves = (Stack<Move>) moves.clone();
        while (!pastMoves.isEmpty()) {
            Move currentMove = pastMoves.pop();
            if (positions.contains(currentMove.getTo())) {
                char[] currentFr = currentMove.getTo().toString().toCharArray();
                int horizontalDiff = Math.abs(currentFr[0] - fr[0]);
                int verticalDiff = Math.abs(currentFr[1] - fr[1]);
                if ((horizontalDiff > 0 && verticalDiff == 0) || (horizontalDiff == 0 && verticalDiff > 0)) {
                    movePiece(isCapturing, currentMove.getTo(), to, moving, null);
                    return;
                }
            }
        }

        //no result in loop --- default positions
        Square from = null;
        if (whiteMove) {
            char[] daf = Square.A1.toString().toCharArray();
            char[] dhf = Square.H1.toString().toCharArray();
            int horizontalADiff = Math.abs(daf[0] - fr[0]);
            int horizontalHDiff = Math.abs(dhf[0] - fr[0]);
            int verticalADiff = Math.abs(daf[1] - fr[1]);
            int verticalHDiff = Math.abs(dhf[1] - fr[1]);
            if (verticalADiff > 0 && horizontalADiff == 0 || horizontalADiff > 0 && verticalADiff == 0) {
                from = Square.A1;
            } else if (verticalHDiff > 0 && horizontalHDiff == 0 || horizontalHDiff > 0 && verticalHDiff == 0) {
                from = Square.H1;
            }
        } else {
            char[] daf = Square.A8.toString().toCharArray();
            char[] dhf = Square.H8.toString().toCharArray();
            int horizontalADiff = Math.abs(daf[0] - fr[0]);
            int horizontalHDiff = Math.abs(dhf[0] - fr[0]);
            int verticalADiff = Math.abs(daf[1] - fr[1]);
            int verticalHDiff = Math.abs(dhf[1] - fr[1]);
            if (verticalADiff > 0 && horizontalADiff == 0 || horizontalADiff > 0 && verticalADiff == 0) {
                from = Square.A8;
            } else if (verticalHDiff > 0 && horizontalHDiff == 0 || horizontalHDiff > 0 && verticalHDiff == 0) {
                from = Square.H8;
            }
        }
        movePiece(isCapturing, from, to, moving, null);
    }

    private void castle(boolean kingSide, boolean whiteMove) {
        Piece king = whiteMove ? Piece.WHITE_KING : Piece.BLACK_KING;
        Piece rook = whiteMove ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;
        Square rookFrom = whiteMove ? Square.E1 : Square.E8;
        Square kingFrom = whiteMove && kingSide ? Square.H1 : whiteMove ? Square.A1 : kingSide ? Square.H8 : Square.H1;
        Square rookTo = whiteMove && kingSide ? Square.F1 : whiteMove ? Square.D1 : kingSide ? Square.F8 : Square.D1;
        Square kingTo = whiteMove && kingSide ? Square.G1 : whiteMove ? Square.C1 : kingSide ? Square.G8 : Square.C1;
        movePiece(false, kingFrom, kingTo, king, null);
        movePiece(false, rookFrom, rookTo, rook, null);
    }


    private void movePawn(Square to, Piece moving, boolean whiteMove, boolean isCapturing, Piece promotion, String col) {
        ArrayList<Square> positions = (ArrayList<Square>) Arrays.stream(findPieceLocations(moving)).collect(Collectors.toList());
        char[] fr = to.toString().toCharArray();


        Square from = null;
        if (isCapturing) {
            int rank = to.toString().charAt(1) - '0';
            rank += whiteMove ? -1 : 1;
            from = Square.getEnumByString(col + rank);
            movePiece(true, from, to, moving, promotion);
            return;
        }


        for (Square fromPos : positions) {
            char[] currentFr = fromPos.toString().toCharArray();
            int horizontalDiff = Math.abs(currentFr[0] - fr[0]);
            int verticalDiff = Math.abs(currentFr[1] - fr[1]);
            if (verticalDiff == 1 && (isCapturing && horizontalDiff == 1) || (horizontalDiff == 0)) {
                movePiece(isCapturing, fromPos, to, moving, promotion);
                return;
            }
        }

        //no result in loop --- default positions
        from = Square.getEnumByString(to.toString().charAt(0) + (whiteMove ? "2" : "7"));
        movePiece(isCapturing, from, to, moving, promotion);
    }

    private void movePiece(boolean isCapturing, Square from, Square to, Piece moving, Piece promotion) {
        Move move = null;
        if (isCapturing) {
            Piece captured = grid.get(to);
            capturedPieces.add(captured);
            if (promotion != null) {
                capturedPieces.add(moving);
                capturedPieces.remove(promotion);
                move = new Move(from, to, moving, captured, promotion);
            } else {
                move = new Move(from, to, moving, captured);
            }
        } else {
            move = new Move(from, to, moving);
        }
        grid.put(from, null);
        grid.put(to, promotion == null ? moving : promotion);
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
        grid.put(toUndo.getTo(), toUndo.getCaptured() == null ? null : toUndo.getCaptured());
        if (toUndo.getCaptured() != null) {
            capturedPieces.remove(toUndo.getCaptured());
        }
        if (toUndo.getPromotion() != null) {
            capturedPieces.remove(toUndo.getMoving());
            capturedPieces.add(toUndo.getPromotion());
        }
        grid.put(toUndo.getFrom(), toUndo.getMoving());
        return toUndo;
    }

    public HashMap<Square, Piece> getGrid() {
        return grid;
    }

    private static char[] WHITE_MAIN = new char[]{'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};

    private static char[] BLACK_MAIN = new char[]{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'};

    public Stack<Move> resetBoard() {
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
        Stack<Move> movesToGetBack = new Stack<>();
        while (!moves.isEmpty()) {
            movesToGetBack.push(moves.pop());
        }
        capturedPieces.clear();
        return movesToGetBack;
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
        sb.append("0 ");
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
