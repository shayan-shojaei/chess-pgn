package chess.util;

import java.awt.*;

public class Util {
    public static Point findCharIn2dArray(char[][] array, char query) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == query) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }
}
