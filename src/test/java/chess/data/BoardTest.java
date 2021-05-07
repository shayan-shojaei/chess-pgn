package chess.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }


    @Test
    void basicMovement() {
        String[] moves = """
                c4
                Nf6
                Nc3
                g6
                e4
                d6
                d4
                Bg7
                Nge2
                O-O
                Ng3
                c6
                Be2
                e5
                d5
                cxd5
                exd5
                Ne8
                h4
                h6
                Be3
                f5
                f4
                Nd7
                Qd2
                h5
                O-O-O
                e4
                Kb1
                Bf6
                Rh3
                Bxh4
                Rdh1
                Bf6
                Bxh5
                gxh5
                Nxf5
                Ng7
                g4
                Nb6
                Nh6+
                Kh7
                Nxe4
                Kxh6
                f5+
                Kh7
                Bd4
                Nxc4
                Rxh5+
                Kg8
                Qh6
                Bxd4
                Qg6
                Na3+
                Kc1
                Bxb2+
                Kd1
                Bxf5
                Rh8#
                """.split("\n");
        for (int i = 0; i < moves.length; i++) {
            board.doMove(moves[i], i % 2 == 0);
        }
        Assertions.assertEquals("""
                  A B C D E F G H\s
                1       K        \s
                2 P b            \s
                3 n             R\s
                4         N   P  \s
                5       P   b    \s
                6       p     Q  \s
                7 p p         n  \s
                8 r     q   r k R\s
                """, board.toString());

    }

    @Test
    void promotion() {
        String[] moves = """
                d4
                Nf6
                c4
                g6
                Nc3
                d5
                cxd5
                Nxd5
                e4
                Nxc3
                bxc3
                Bg7
                Bg5
                c5
                Rc1
                cxd4
                cxd4
                O-O
                d5
                f6
                Bf4
                Qa5+
                Bd2
                Qxa2
                Nf3
                Qa3
                Bc4
                Qd6
                O-O
                Kh8
                Nd4
                a6
                Re1
                b5
                Ba2
                Bd7
                Qb3
                Rc8
                Rxc8+
                Bxc8
                Bb4
                Qb6
                d6
                e6
                Nxe6
                Bxe6
                Qxe6
                Qd8
                Ba5
                Qf8
                Rc1
                Nc6
                Rxc6
                Re8
                d7
                Rxe6
                Bxe6
                f5
                Rc8
                Qxc8
                dxc8=Q+
                Bf8
                Qxf8#
                """.split("\n");
        for (int i = 0; i < moves.length; i++) {
            board.doMove(moves[i], i % 2 == 0);
        }
        System.out.println(board.toString());
        /*Assertions.assertEquals("""
                  A B C D E F G H\s
                1       K        \s
                2 P b            \s
                3 n             R\s
                4         N   P  \s
                5       P   b    \s
                6       p     Q  \s
                7 p p         n  \s
                8 r     q   r k R\s
                """, board.toString());*/

    }
}