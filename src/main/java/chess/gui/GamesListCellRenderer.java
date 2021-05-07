package chess.gui;

import chess.data.Game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamesListCellRenderer extends JLabel implements ListCellRenderer<Game> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Game> list, Game value, int index, boolean isSelected, boolean cellHasFocus) {
        this.setBorder(new EmptyBorder(8, 0, 8, 0));
        setForeground(Color.WHITE);
        setBackground(Color.DARK_GRAY);
        if (cellHasFocus) {
            setBackground(Color.BLACK);
        }
        String title = String.format("<html>%s<br>%s<br>%s</html>", value.getWhitePlayer(), value.getBlackPlayer(), value.getResult());
        setText(title);
        return this;
    }
}
