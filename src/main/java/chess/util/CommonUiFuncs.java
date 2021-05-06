package chess.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class CommonUiFuncs {
    public static void initDefaultJFrame(JFrame frame) {
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(new Dimension(800, 500));
        frame.setTitle("Chess App");
    }

    public static void centerifyWindow(Window window) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(dim.width / 2 - window.getSize().width / 2, dim.height / 2 - window.getSize().height / 2);
    }

    public static void setIconImage(Window window) {
        try {
            InputStream imageInputStream = window.getClass().getResourceAsStream("/icons/chessicon.png");
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            window.setIconImage(bufferedImage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
