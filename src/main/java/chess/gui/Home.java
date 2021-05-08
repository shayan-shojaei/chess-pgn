package chess.gui;

import chess.data.Game;
import chess.data.Move;
import chess.data.PGN;
import chess.data.enums.Piece;
import chess.data.enums.Square;
import chess.settings.Setting;
import chess.settings.SettingsHelper;
import chess.util.CommonUiFuncs;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

public class Home extends JFrame {
    private final static String LAST_LOCATION = "LAST_LOCATION";
    Game selectedGame = null;
    private SettingsHelper settings;

    public void init() {
        settings = new SettingsHelper();

        CommonUiFuncs.initDefaultJFrame(this);
        CommonUiFuncs.setIconImage(this);
        CommonUiFuncs.centerifyWindow(this);


        this.setLocationRelativeTo(null);
        this.setLayout(null);

        initImportButton();
        initGamesList();
        initInfo();

        Setting lastLocation = settings.getSetting(LAST_LOCATION);
        if (lastLocation != null) {
            initPgn(lastLocation.getValue());
        }

        this.setVisible(true);

    }

    private JButton importButton;

    private void initImportButton() {
        importButton = new JButton("Import PGN");
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PGN Annotated File (.pgn)", "pgn"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getPath();
                initPgn(filePath);
                settings.put(new Setting(LAST_LOCATION, filePath));
            }
        });
        importButton.setBackground(Color.BLACK);
        importButton.setForeground(Color.WHITE);
        importButton.setBorderPainted(false);
        importButton.setBounds(0, this.getHeight() - 70, (int) (this.getWidth() * 0.2), 30);

        this.add(importButton);
    }

    private JList<Game> gamesList;
    private TitledBorder listBorder;
    private DefaultListModel<Game> gamesModel;

    private void initGamesList() {
        gamesList = new JList<>();
        gamesModel = new DefaultListModel<>();
        gamesList.setCellRenderer(new GamesListCellRenderer());
        gamesList.setModel(gamesModel);
        gamesList.setBackground(Color.DARK_GRAY);

        gamesList.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedGame = gamesList.getSelectedValue();
                updateInfo();
            }
        });

        CustomScrollPane scrollPane = new CustomScrollPane(gamesList);
        scrollPane.setBounds(0, 2, (int) (this.getWidth() * 0.2), this.getHeight() - 80);
        listBorder = new BorderUIResource.TitledBorderUIResource(new EmptyBorder(8, 4, 8, 4), "Import PGN file to start");
        listBorder.setTitleColor(Color.WHITE);
        listBorder.setTitleJustification(TitledBorder.LEFT);
        scrollPane.setBackground(Color.DARK_GRAY);
        scrollPane.setBorder(listBorder);


        this.add(scrollPane);
    }

    private JPanel infoPane;
    private JPanel cbGrid;
    private JLabel gameTags;
    private HashMap<Square, JButton> chessBoardSquares = new HashMap<>();
    private JButton last, first, next, back;
    private JPanel playthroughHolder;


    private void initInfo() {
        SpringLayout manager = new SpringLayout();
        infoPane = new JPanel(manager);
        infoPane.setBounds((int) (this.getWidth() * 0.2), 0, (int) (this.getWidth() * 0.8), this.getHeight());
        infoPane.setBorder(new EmptyBorder(6, 6, 6, 6));
        infoPane.setVisible(false);
        infoPane.setBackground(Color.DARK_GRAY);

        resetChessBoard();

        manager.putConstraint(SpringLayout.EAST, cbGrid, -14, SpringLayout.EAST, infoPane);
        infoPane.add(cbGrid);

//        game info
        gameTags = new JLabel();
        gameTags.setBackground(Color.LIGHT_GRAY);
        gameTags.setVerticalAlignment(SwingConstants.TOP);
        gameTags.setFont(new Font("Helvetica", Font.PLAIN, 16));
        gameTags.setOpaque(true);
        CustomScrollPane tagsPane = new CustomScrollPane(gameTags);
        tagsPane.setBackground(Color.DARK_GRAY);
        TitledBorder border = new TitledBorder(new EmptyBorder(12, 4, 4, 4), "Game Tags:");
        border.setTitleColor(Color.WHITE);
        tagsPane.setBorder(border);
        manager.putConstraint(SpringLayout.WEST, tagsPane, -8, SpringLayout.WEST, infoPane);
        manager.putConstraint(SpringLayout.EAST, tagsPane, -8, SpringLayout.WEST, cbGrid);
        manager.putConstraint(SpringLayout.HEIGHT, tagsPane, Spring.constant(1), SpringLayout.HEIGHT, cbGrid);
        infoPane.add(tagsPane);

        setupPlayThrough();

        manager.putConstraint(SpringLayout.NORTH, playthroughHolder, 6, SpringLayout.SOUTH, cbGrid);
        manager.putConstraint(SpringLayout.WEST, playthroughHolder, 6, SpringLayout.WEST, cbGrid);
        manager.putConstraint(SpringLayout.WIDTH, playthroughHolder, Spring.constant(1), SpringLayout.WIDTH, cbGrid);


        this.add(infoPane);
    }

    private Stack<Move> undoneMoves = new Stack<>();
    private int movesCount;
    private int undoCount = 0;

    private void setupPlayThrough() {
        // play through buttons - starts at the end of the game
        playthroughHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        playthroughHolder.setBackground(Color.DARK_GRAY);
        last = new JButton(">>");
        first = new JButton("<<");
        next = new JButton(">");
        back = new JButton("<");
        last.setBorderPainted(false);
        first.setBorderPainted(false);
        next.setBorderPainted(false);
        back.setBorderPainted(false);

        last.setBackground(Color.BLACK);
        last.setForeground(Color.WHITE);
        first.setBackground(Color.BLACK);
        first.setForeground(Color.WHITE);
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);

        undoCount = 0;
        updatePlayThroughButtons();

        last.addActionListener(e -> {
            while (!undoneMoves.isEmpty()) {
                selectedGame.getBoard().doMove(undoneMoves.pop());
                undoCount--;
            }
            updateGrid();
            updatePlayThroughButtons();
        });
        next.addActionListener(e -> {
            if (!undoneMoves.isEmpty()) {
                undoCount--;
                selectedGame.getBoard().doMove(undoneMoves.pop());
                updateGrid();
                updatePlayThroughButtons();
            }
        });
        back.addActionListener(e -> {
            if (movesCount != undoCount) {
                undoneMoves.push(selectedGame.getBoard().undoMove());
                undoCount++;
                updateGrid();
                updatePlayThroughButtons();
            }
        });
        first.addActionListener(e -> {
            while (movesCount != undoCount) {
                undoneMoves.push(selectedGame.getBoard().undoMove());
                undoCount++;
            }
            updateGrid();
            updatePlayThroughButtons();
        });


        playthroughHolder.add(first);
        playthroughHolder.add(back);
        playthroughHolder.add(next);
        playthroughHolder.add(last);
        infoPane.add(playthroughHolder);
    }

    private void updatePlayThroughButtons() {
        if (undoneMoves.isEmpty()) {
            last.setEnabled(false);
            next.setEnabled(false);
        } else {
            last.setEnabled(true);
            next.setEnabled(true);
        }
        if (movesCount == undoCount) {
            first.setEnabled(false);
            back.setEnabled(false);
        } else {
            first.setEnabled(true);
            back.setEnabled(true);
        }
    }

    private void resetChessBoard() {
        cbGrid = new JPanel(new GridLayout(0, 9));
        cbGrid.setBorder(new LineBorder(Color.BLACK));

        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }
                b.setBorderPainted(false);
                final String code = Character.toString('A' + i) + String.valueOf(j + 1);
                chessBoardSquares.put(Square.getEnumByString(code), b);
            }
        }

        cbGrid.add(new JLabel(""));
        for (int i = 0; i < 8; i++) {
            cbGrid.add(new JLabel("ABCDEFGH".substring(i, i + 1),
                    SwingConstants.CENTER));
        }

        for (int ii = 0; ii < 8; ii++) {
            cbGrid.add(new JLabel("" + (ii + 1),
                    SwingConstants.CENTER));
            for (int jj = 0; jj < 8; jj++) {
                final String code = Character.toString('A' + jj) + String.valueOf(ii + 1);
                cbGrid.add(chessBoardSquares.get(Square.getEnumByString(code)));
            }
        }
    }

    private void updateInfo() {
        undoneMoves = new Stack<>();
        undoCount = 0;
        if (selectedGame == null) {
            infoPane.setVisible(false);
            return;
        }
        if (!infoPane.isVisible()) infoPane.setVisible(true);
//        get tags
        StringBuilder tagsText = new StringBuilder("<html>");
        for (Map.Entry<String, String> tag : selectedGame.getInfo().entrySet()) {
            tagsText.append(tag.getKey());
            tagsText.append(": ");
            tagsText.append(tag.getValue());
            tagsText.append("<br/>");
        }
        tagsText.append("</html>");
        gameTags.setText(tagsText.toString());

        movesCount = selectedGame.getBoard().cloneMoves().size();
        updatePlayThroughButtons();
//        get grid
        updateGrid();
    }


    private void updateGrid() {
        for (Map.Entry<Square, Piece> entry : selectedGame.getBoard().getGrid().entrySet()) {
            if (entry.getValue() == null) {
                if (chessBoardSquares.get(entry.getKey()) != null) {
                    ImageIcon icon = new ImageIcon(
                            new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB));
                    chessBoardSquares.get(entry.getKey()).setIcon(icon);
                }
            } else {
                String fileName;
                if (entry.getValue() == Piece.BLACK_PAWN) {
                    fileName = "p";
                } else if (entry.getValue() == Piece.WHITE_PAWN) {
                    fileName = "wp";
                } else if (Character.isUpperCase(entry.getValue().toChar())) {
                    fileName = ("w" + entry.getValue().toChar()).toLowerCase(Locale.ROOT);
                } else {
                    fileName = Character.toString(entry.getValue().toChar());
                }
                InputStream stream = getClass().getResourceAsStream(String.format("/icons/%s.png", fileName));
                try {
//                    ImageIcon icon = new ImageIcon(
//                            new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB));
                    ImageIcon icon = new ImageIcon(ImageIO.read(stream));
                    if (chessBoardSquares.get(entry.getKey()) != null) {
                        chessBoardSquares.get(entry.getKey()).setIcon(icon);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private PGN pgn = null;

    private void initPgn(String path) {
        pgn = new PGN(path);
        refreshList();
    }

    private void refreshList() {
        selectedGame = null;
        gamesModel.clear();
        gamesModel.addAll(pgn.getGames());
        gamesList.setModel(gamesModel);
        listBorder.setTitle(pgn.getGames().size() > 0 ? pgn.getGames().get(0).getInfo().get("Event") : "Import PGN file to start");
        updateInfo();
    }

}
