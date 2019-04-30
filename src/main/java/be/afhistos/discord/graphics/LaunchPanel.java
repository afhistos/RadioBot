package be.afhistos.discord.graphics;

import be.afhistos.discord.EventsListener;
import be.afhistos.discord.Main;
import be.afhistos.discord.extra.AppHeader;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LaunchPanel extends JPanel implements FocusListener, ActionListener {
    private static LaunchPanel instance;
    private JLabel status;
    private static JTextField token;
    private JButton connect;
    public static Graphics graphics;

    public LaunchPanel(){
        this.instance = this;
        AppHeader header = new AppHeader(this,"RadioBot - Connexion", true,1038,false);
        this.add(header.getMover());
        this.add(header.getClose());
        header.getMover().addMouseListener(Launch.getWindowMover());
        header.getMover().addMouseMotionListener(Launch.getWindowMover());
        this.setLayout(null);
        this.status = new JLabel("clique sur 'Se connecter'!", SwingConstants.CENTER);
        this.status.setForeground(header.getClose().getBackground());
        this.status.setOpaque(false);
        this.status.setBounds(265,365,550,30);
        this.status.setFont(this.status.getFont().deriveFont(19.0f));
        this.add(this.status);
        token = new JTextField("Bonjour MK_16");
        token.setHorizontalAlignment(JTextField.CENTER);
        token.addFocusListener(this);
        token.setFont(this.token.getFont().deriveFont(21.0f));
        token.setForeground(header.getClose().getBackground());
        token.setCaretColor(header.getClose().getBackground());
        token.setSelectionColor(header.getClose().getBackground().brighter());
        token.setSelectedTextColor(header.getClose().getBackground().darker());
        token.setBounds(265,415,550,30);
        this.add(token);
        this.connect= new JButton("Se connecter");
        this.connect.setBounds(475,465, 130,30);
        this.connect.addActionListener(this);
        this.connect.setBackground(header.getClose().getBackground());
        this.connect.setBorderPainted(false);
        this.connect.setFocusPainted(false);
        this.connect.setHorizontalAlignment(JButton.CENTER);
        this.add(this.connect);

    }
    public static void setText(String text){
        token.setText(text);
    }


    @Override
    public void paintComponents(Graphics g) {
        graphics = g;
        super.paintComponents(g);
    }


    @Override
    public void focusGained(FocusEvent e) {
        if(e.getSource() == this.token){
            this.token.setEditable(false);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.connect){
            this.setVisible(false);
            Main.main(new String[]{"NTY3NzI2MjY5MjA3MjgxNjc0.XMR-BQ.sdt5afB7oqXbxljDn5FZWQtgMOQ", "19:00", "f6eafbf500aa4a41b3a64d5812033fbc"});
           Launch.getInstance().setContentPane(Launch.getInstance().getMainPanel());
        }
    }
    public static LaunchPanel getInstance(){
        return instance;
    }
}
