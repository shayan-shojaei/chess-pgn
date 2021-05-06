package chess.data;

import chess.data.enums.Piece;
import chess.data.enums.Square;

public class Move {
    private Square to, from;
    private Piece moving, captured, promotion;

    public Move(Square from, Square to, Piece moving) {
        this.to = to;
        this.moving = moving;
    }

    public Move(Square from, Square to, Piece moving, Piece captured) {
        this.to = to;
        this.moving = moving;
        this.captured = captured;
    }

    public Move(Square from, Square to, Piece moving, Piece captured, Piece promotion) {
        this.to = to;
        this.moving = moving;
        this.captured = captured;
        this.promotion = promotion;
    }

    public Square getTo() {
        return to;
    }

    public Piece getMoving() {
        return moving;
    }

    public Piece getCaptured() {
        return captured;
    }

    public Piece getPromotion() {
        return promotion;
    }

}

