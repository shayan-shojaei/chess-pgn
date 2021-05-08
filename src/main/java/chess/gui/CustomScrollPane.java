package chess.gui;

import chess.data.Game;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollPane extends JScrollPane {
    public CustomScrollPane(Component c) {
        super(c);
    }

    @Override
    public JScrollBar createVerticalScrollBar() {
        JScrollBar scrollBar = super.createVerticalScrollBar();
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBorderPainted(false);
                button.setBackground(Color.BLACK);
                button.setForeground(Color.WHITE);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBorderPainted(false);
                button.setBackground(Color.BLACK);
                button.setForeground(Color.WHITE);
                return button;
            }

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.BLACK;
                this.trackColor = Color.DARK_GRAY;
            }
        });
        return scrollBar;
    }

    @Override
    public JScrollBar getVerticalScrollBar() {
        return super.getVerticalScrollBar();
    }
}
