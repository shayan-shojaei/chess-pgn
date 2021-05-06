package chess.gui;

import chess.util.CommonUiFuncs;

import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {
    public void init() {
        CommonUiFuncs.initDefaultJFrame(this);
        CommonUiFuncs.setIconImage(this);
        CommonUiFuncs.centerifyWindow(this);
        this.setVisible(true);
    }
}
