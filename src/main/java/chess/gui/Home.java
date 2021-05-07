package chess.gui;

import chess.data.Game;
import chess.data.PGN;
import chess.util.CommonUiFuncs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;

public class Home extends JFrame {

    public void init() {
        CommonUiFuncs.initDefaultJFrame(this);
        CommonUiFuncs.setIconImage(this);
        CommonUiFuncs.centerifyWindow(this);


        this.setLocationRelativeTo(null);
        this.setLayout(null);

        initImportButton();
        initGamesList();

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
        gamesList.setBounds(0, 4, (int) (this.getWidth() * 0.2), this.getHeight() - 80);
        listBorder = new BorderUIResource.TitledBorderUIResource(new EmptyBorder(8, 8, 8, 8), "Import PGN file to start");
        listBorder.setTitleColor(Color.WHITE);
        listBorder.setTitleJustification(TitledBorder.LEFT);
        gamesList.setBorder(listBorder);
        gamesList.setBackground(Color.DARK_GRAY);


        this.add(gamesList);
    }

    private PGN pgn = null;

    private void initPgn(String path) {
        pgn = new PGN(path);
        refreshList();
    }

    private void refreshList() {
        gamesModel.clear();
        gamesModel.addAll(pgn.getGames());
        gamesList.setModel(gamesModel);
        listBorder.setTitle(pgn.getGames().size() > 0 ? pgn.getGames().get(0).getInfo().get("Event") : "Import PGN file to start");
    }
}
