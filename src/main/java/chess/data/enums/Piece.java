package chess.data.enums;

public enum Piece {
    WHITE_KING('K'),
    WHITE_QUEEN('Q'),
    WHITE_ROOK('R'),
    WHITE_PAWN(Character.MIN_VALUE),
    WHITE_BISHOP('B'),
    WHITE_KNIGHT('N'),
    BLACK_KING('k'),
    BLACK_QUEEN('q'),
    BLACK_PAWN(Character.MIN_VALUE),
    BLACK_BISHOP('b'),
    BLACK_ROOK('r'),
    BLACK_KNIGHT('n');

    private char val;

    Piece(char stringVal) {
        val = stringVal;
    }

    public char toChar() {
        return val;
    }

    public static Piece getEnumByString(char code) {
        for (Piece e : Piece.values()) {
            if (e.val == code) return e;
        }
        return null;
    }
}
