package be.afhistos.discord.graphics;

import be.afhistos.discord.EventsListener;
import be.afhistos.discord.extra.AppHeader;
import be.afhistos.discord.extra.BlueButton;
import be.afhistos.discord.extra.Functions;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainPanel extends JPanel implements MouseListener, ActionListener {
    private JPanel selectPanel;
    private JPanel contentPanel;
    private JLabel showSelectedButtonLabel;
    private JButton[] selectButtons;
    public static Rectangle contentPanelPos = new Rectangle(200,26,880,694);
    private Color selectColor = new Color(53,124,208);

    public MainPanel(){
        AppHeader header = new AppHeader(this,"RadioBot - Vue d'ensemble", true,1038,false);
        this.add(header.getMover());
        this.add(header.getClose());
        header.getMover().addMouseListener(Launch.getWindowMover());
        header.getMover().addMouseMotionListener(Launch.getWindowMover());
        this.setLayout(null);
        this.selectPanel = new JPanel();
        this.selectPanel.setLayout(new BoxLayout(selectPanel,BoxLayout.Y_AXIS));
        this.selectPanel.setVisible(true);
        this.selectPanel.setBounds(0,26,200,694);
        this.selectPanel.setBackground(selectColor);
        selectButtons = new JButton[5];
        for(int i = 0; i<selectButtons.length; i++){
            selectButtons[i] = new JButton();
        }
        selectButtons[0].setText("Gestion Bot");
        selectButtons[1].setText("Gestion serveurs");
        selectButtons[2].setText("Radio");
        selectButtons[3].setText("News");
        selectButtons[4].setText("Messages");
        for(int i = 0; i<selectButtons.length; i++){
            selectButtons[i].setFont(selectButtons[i].getFont().deriveFont(19.0f));
            selectButtons[i].setToolTipText(selectButtons[i].getText());
            selectButtons[i].setSelected(false);
            selectButtons[i].setFocusPainted(false);
            selectButtons[i].setBorderPainted(false);
            selectButtons[i].addMouseListener(this);
            selectButtons[i].addActionListener(this);
            selectButtons[i].setBackground(this.selectPanel.getBackground());
            selectButtons[i].setMaximumSize(new Dimension(200,75));
            selectButtons[i].setHorizontalAlignment(JButton.CENTER);
            selectPanel.add(selectButtons[i]);
        }
        this.add(selectPanel);
        ImageIcon imageIcon = new ImageIcon(Functions.getImage("showSelectedButton.png"));
        this.showSelectedButtonLabel = new JLabel(imageIcon, JLabel.CENTER);
        showSelectedButtonLabel.setVisible(false);
        showSelectedButtonLabel.setBounds(200,26,25,75);
        this.add(showSelectedButtonLabel);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        e.getComponent().setBackground(e.getComponent().getBackground().darker());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        e.getComponent().setBackground(e.getComponent().getBackground().brighter());

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        e.getComponent().setBackground(e.getComponent().getBackground().darker());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setBackground(this.selectColor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton component = (JButton) e.getSource();
        if(component.getText().equals("Gestion Bot")){
            setContentPanel(new BotManagerPanel());
            showSelectedButtonLabel.setBounds(200,26,25,75);
            showSelectedButtonLabel.setVisible(true);
            this.repaint();
        }
    }

    private void setContentPanel(Object panel){
        this.contentPanel = (JPanel) panel;
        this.add(this.contentPanel);
        contentPanel.repaint();
    }
}
