package chess;

import chess.gui.Home;
import chess.util.CommonUiFuncs;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CommonUiFuncs.setSystemLookAndFeel();
            Home home = new Home();
            home.init();
        });
    }
}
