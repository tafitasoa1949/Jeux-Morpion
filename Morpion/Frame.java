import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Frame  extends JFrame{
	private JButton[][] buttons;
    private JButton reinitialiserButton;
    private boolean estTourCroix;
    private boolean estFinPartie;
    private boolean estModeFacile;
    private boolean estModeNormale;
    public Frame() {
        setTitle("Morpion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 900);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                buttons[ligne][colonne] = new JButton();
                buttons[ligne][colonne].setForeground(Color.BLACK);
                buttons[ligne][colonne].setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 60));
                buttons[ligne][colonne].addActionListener(new ButtonClickListener(ligne, colonne));
                gridPanel.add(buttons[ligne][colonne]);
            }
        }

        reinitialiserButton = new JButton("Rejouer");
        Dimension boutonSize = new Dimension(100, 50);
        reinitialiserButton.setPreferredSize(boutonSize);
        reinitialiserButton.addActionListener(new ReinitialiserButtonClickListener());
        add(gridPanel, BorderLayout.CENTER);
        add(reinitialiserButton, BorderLayout.NORTH);

        estTourCroix = true;
        estFinPartie = false;
        //
        estModeFacile = true;
        estModeNormale = false;
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        private int ligne;
        private int colonne;

        public ButtonClickListener(int row, int col) {
            this.ligne = row;
            this.colonne = col;
        }

        public void actionPerformed(ActionEvent e) {
            if (estFinPartie) {
                return;
            }

            JButton buttonClicked = (JButton) e.getSource();

            if (buttonClicked.getText().isEmpty()) {
                buttonClicked.setText("X");
                buttonClicked.setEnabled(false);

                if (PartieGagne("X")) {
                	estFinPartie = true;
                    JOptionPane.showMessageDialog(null, "Vous avez gagné !");
                    ReinitialiserJeu();
                } else if (MatchNull()) {
                	estFinPartie = true;
                    JOptionPane.showMessageDialog(null, "Match nul !");
                    ReinitialiserJeu();
                } else {
                    if (estModeFacile) {
                    	alea_facile();
                    } else {
                    	deplacerIA();
                    }
                }
            }
        }
    }

    private void deplacerIA() {
        if (estModeFacile) {
        	System.out.println("facile");
        	MouvementIA_Facile();
        }else if(estModeNormale){
        	System.out.println("normal");
        	int[] bestMove = CoupNormale();
            int ligne = bestMove[0];
            int colonne = bestMove[1];

            buttons[ligne][colonne].setText("O");
            buttons[ligne][colonne].setEnabled(false);

            if (PartieGagne("O")) {
                estFinPartie = true;
                JOptionPane.showMessageDialog(null, "L'IA a gagné !");
                ReinitialiserJeu();
            } else if (MatchNull()) {
                estFinPartie = true;
                JOptionPane.showMessageDialog(null, "Match nul !");
                ReinitialiserJeu();
            }
        } else {
        	System.out.println("difficile");
            int[] bestMove = MeilleurMouvement();
            int ligne = bestMove[0];
            int colonne = bestMove[1];

            buttons[ligne][colonne].setText("O");
            buttons[ligne][colonne].setEnabled(false);

            if (PartieGagne("O")) {
            	estFinPartie = true;
                JOptionPane.showMessageDialog(null, "L'IA a gagné !");
                ReinitialiserJeu();
            } else if (MatchNull()) {
            	estFinPartie = true;
                JOptionPane.showMessageDialog(null, "Match nul !");
                ReinitialiserJeu();
            }
        }
    }

    private void MouvementIA_Facile() {
        boolean ahetsika = false;
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                if (buttons[ligne][colonne].getText().isEmpty()) {
                    buttons[ligne][colonne].setText("O");
                    buttons[ligne][colonne].setEnabled(false);

                    if (PartieGagne("O")) {
                    	estFinPartie = true;
                        JOptionPane.showMessageDialog(null, "L'IA a gagné !");
                        ReinitialiserJeu();
                    } else if (MatchNull()) {
                    	estFinPartie = true;
                        JOptionPane.showMessageDialog(null, "Match nul !");
                        ReinitialiserJeu();
                    }

                    ahetsika = true;
                    break;
                }
            }

            if (ahetsika) {
                break;
            }
        }

        if (!ahetsika) {
        	deplacerIA();
        }
        //normalT
        
    }
    ////////
    private int[] CoupNormale() {
        int[] meilleurMouvement = new int[]{-1, -1};
        //
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                if (buttons[ligne][colonne].getText().isEmpty()) {
                    buttons[ligne][colonne].setText("O");

                    if (PartieGagne("O")) {
                        meilleurMouvement[0] = ligne;
                        meilleurMouvement[1] = colonne;
                        buttons[ligne][colonne].setText("");
                        return meilleurMouvement;
                    }

                    buttons[ligne][colonne].setText("");
                }
            }
        }
        //
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                if (buttons[ligne][colonne].getText().isEmpty()) {
                    buttons[ligne][colonne].setText("X");

                    if (PartieGagne("X")) {
                        meilleurMouvement[0] = ligne;
                        meilleurMouvement[1] = colonne;
                        buttons[ligne][colonne].setText("");
                        return meilleurMouvement;
                    }

                    buttons[ligne][colonne].setText("");
                }
            }
        }
        //
        ArrayList<int[]> mouvementsVides = new ArrayList<>();
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                if (buttons[ligne][colonne].getText().isEmpty()) {
                    mouvementsVides.add(new int[]{ligne, colonne});
                }
            }
        }

        if (!mouvementsVides.isEmpty()) {
            meilleurMouvement = mouvementsVides.get(0);
        }

        return meilleurMouvement;
    }

    private void alea_facile() {
    	if(buttons[1][1].getText().isEmpty()) {
    		buttons[1][1].setText("O");
            buttons[1][1].setEnabled(false);
    	}else {
    		ArrayList<int[]> mouvementsVides = new ArrayList<>();
            for (int ligne = 0; ligne < 3; ligne++) {
                for (int colonne = 0; colonne < 3; colonne++) {
                    if (buttons[ligne][colonne].getText().isEmpty()) {
                        mouvementsVides.add(new int[]{ligne, colonne});
                    }
                }
            }
            for(int i=0;i < mouvementsVides.size() ; i++) {
            	int ligne = mouvementsVides.get(i)[0];
            	int colonne = mouvementsVides.get(i)[1];
            }
    	}
        
    }
    //////////////
    private int[] MeilleurMouvement() {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;

        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                if (buttons[ligne][colonne].getText().isEmpty()) {
                    buttons[ligne][colonne].setText("O");

                    int score = minimax(0, false);

                    buttons[ligne][colonne].setText("");

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = ligne;
                        bestMove[1] = colonne;
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(int profondeur, boolean maxJoueur) {
        if (PartieGagne("O")) {
            return 1;
        } else if (PartieGagne("X")) {
            return -1;
        } else if (MatchNull()) {
            return 0;
        }

        if (maxJoueur) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (buttons[row][col].getText().isEmpty()) {
                        buttons[row][col].setText("O");

                        int score = minimax(profondeur + 1, false);

                        buttons[row][col].setText("");

                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            System.out.println("best max : "+bestScore);
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (buttons[row][col].getText().isEmpty()) {
                        buttons[row][col].setText("X");

                        int score = minimax(profondeur + 1, true);

                        buttons[row][col].setText("");

                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            System.out.println("best min : "+bestScore);
            return bestScore;
        }
    }

    private boolean PartieGagne(String player) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(player) && buttons[i][1].getText().equals(player) && buttons[i][2].getText().equals(player)) {
                return true;
            }
            if (buttons[0][i].getText().equals(player) && buttons[1][i].getText().equals(player) && buttons[2][i].getText().equals(player)) {
                return true;
            }
        }

        if (buttons[0][0].getText().equals(player) && buttons[1][1].getText().equals(player) && buttons[2][2].getText().equals(player)) {
            return true;
        }
        if (buttons[0][2].getText().equals(player) && buttons[1][1].getText().equals(player) && buttons[2][0].getText().equals(player)) {
            return true;
        }
        return false;
    }

    private boolean MatchNull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private class ReinitialiserButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	ReinitialiserJeu();
        }
    }

    private void ReinitialiserJeu() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setEnabled(true);
                buttons[row][col].setText("");
            }
        }
        estTourCroix = true;
        estFinPartie = false;
    }
}
