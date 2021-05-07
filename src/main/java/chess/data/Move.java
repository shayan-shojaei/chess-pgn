package chess.data;

import chess.data.enums.Piece;
import chess.data.enums.Square;

public class Move {
    private Square to, from;
    private Piece moving, captured, promotion;

    public Move(Square from, Square to, Piece moving) {
        this.to = to;
        this.from = from;
        this.moving = moving;
    }

    public Move(Square from, Square to, Piece moving, Piece captured) {
        this.to = to;
        this.from = from;
        this.moving = moving;
        this.captured = captured;
    }

    public Move(Square from, Square to, Piece moving, Piece captured, Piece promotion) {
        this.to = to;
        this.from = from;
        this.moving = moving;
        this.captured = captured;
        this.promotion = promotion;
    }

    public Square getTo() {
        return to;
    }

    public Square getFrom() {
        return from;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) return false;

        if (this == o) return true;

        Move move = (Move) o;

        if (to != move.to) return false;
        if (from != move.from) return false;
        if (moving != move.moving) return false;
        if (captured != move.captured) return false;
        return promotion == move.promotion;
    }
}

