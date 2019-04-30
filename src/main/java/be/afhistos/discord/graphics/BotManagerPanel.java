package be.afhistos.discord.graphics;

import be.afhistos.discord.Main;
import be.afhistos.discord.extra.*;
import be.afhistos.discord.music.Audio;
import com.jagrosh.jdautilities.doc.standard.Error;
import net.dv8tion.jda.core.entities.Guild;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BotManagerPanel extends JPanel implements ActionListener {
    private static BotManagerPanel instance;
    Color customColor = new Color(12,88,210);
    private BlueButton ping;
    private BlueButton changeName;
    private BlueSlider volume;
    private static JPanel ping_panel;
    private static JFrame ping_frame;
    private static JPanel name_panel;
    private static JFrame name_frame;

    public BotManagerPanel(){
        instance = this;
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setBounds(MainPanel.contentPanelPos);
        ping = new BlueButton("Ping", new Rectangle(45,45,100,30),false);
        ping.addActionListener(this);
        ping.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(ping);
        changeName = new BlueButton("Changer le nom", new Rectangle(160,45,130,30), false);
        changeName.addActionListener(this);
        changeName.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(changeName);
        JComboBox volumeGuild = getGuildsListComboBox();
        volumeGuild.setBounds(this.getWidth()-315,45,300,30);
        this.add(volumeGuild);
        volume = new BlueSlider(0,120,true,true);
        volume.setOrientation(JSlider.VERTICAL);
        volume.setOpaque(false);
        volume.setBounds(this.getWidth()-90,100,75,240);
        volume.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String id = volumeGuild.getSelectedItem().toString().replaceAll("\\D", "");
                Guild guild = Main.getJda().getGuildById(id);
                Audio.getInstance().setVolume(volume.getValue(),guild);
            }
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}

        });
        this.add(volume);
        JTextArea volume_label = new JTextArea("Sélectionner un serveur et\najuster le volume");
        volume_label.setEditable(false);
        volume_label.setCursor(null);
        volume_label.setOpaque(false);
        volume_label.setFocusable(false);
        volume_label.setLineWrap(true);
        volume_label.setWrapStyleWord(true);
        volume_label.setBounds(this.getWidth()-315,100,150,150);
        volume_label.setFont(new Font("Arial", Font.BOLD, 22));
        volume_label.setForeground(Color.darkGray);
        this.add(volume_label);
        this.setVisible(true);
        this.repaint();
        Launch.getInstance().setState(Frame.ICONIFIED);
        Launch.getInstance().setState(Frame.NORMAL);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.ping)){
            ping_frame = new JFrame("Pong!");
            ping_frame.setResizable(false);
            ping_frame.setUndecorated(true);
            ping_frame.setSize(250,100);
            ping_frame.setIconImage(Functions.getImage("icon_radiobot.png"));
            WindowMover windowMover = new WindowMover(ping_frame);
            ping_panel = new JPanel(null);
            ping_panel.setBorder(BorderFactory.createLineBorder(customColor));
            AppHeader header = new AppHeader(ping_panel,"Pong!", true,250,true);
            header.getMover().addMouseListener(windowMover);
            header.getMover().addMouseMotionListener(windowMover);
            JLabel ping = new JLabel("",SwingConstants.CENTER);
            ping.setText(Main.getJda().getPing()+"ms");
            ping.setForeground(Color.GREEN);
            ping.setBounds(50,44,150,30);
            ping_frame.setLocationRelativeTo(this);
            ping_panel.add(ping);
            ping_frame.add(ping_panel);
            ping_frame.setVisible(true);
        }else if(e.getSource().equals(this.changeName)){
            name_frame = new JFrame("Changer le nom");
            name_frame.setResizable(false);
            name_frame.setUndecorated(true);
            name_frame.setSize(350,170);
            name_frame.setIconImage(Functions.getImage("icon_radiobot.png"));
            name_panel = new JPanel(null);
            name_panel.setBorder(BorderFactory.createLineBorder(customColor));
            AppHeader header = new AppHeader(name_panel, "Nom du bot dans le serveur:", false,350, true);
            WindowMover windowMover = new WindowMover(name_frame);
            header.getMover().addMouseListener(windowMover);
            header.getMover().addMouseMotionListener(windowMover);
            header.getClose().addActionListener(this);
            JLabel name_info = new JLabel("Choisir un serveur, puis insérer le nom du bot dans celui-ci", SwingConstants.CENTER);
            name_info.setBounds(5,20,335,30);
            JComboBox box = getGuildsListComboBox();
            box.setBounds(25,60,300,27);
            JTextArea new_name = new JTextArea();
            new_name.setBackground(new Color(1,1,1,4));
            new_name.setCaretColor(customColor.darker());
            new_name.setForeground(customColor.darker());
            new_name.setBorder(BorderFactory.createLineBorder(customColor.darker()));
            new_name.setOpaque(false);
            new_name.setFont(new_name.getFont().deriveFont(20.0f));
            new_name.setBounds(50,97,250,30);
            BlueButton validate = new BlueButton("Valider",new Rectangle(120,137,100,30),false);
            validate.addActionListener(e1 -> {
                String text = new_name.getText();
                if(!text.equals("") || text != null){
                    String id = box.getSelectedItem().toString().replaceAll("\\D", "");
                    Guild guild = Main.getJda().getGuildById(id);
                    guild.getController().setNickname(guild.getSelfMember(), text.length() >32 ?text.substring(0, 32) : text).queue();
                    name_frame.dispose();
                }
            });
            validate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            name_panel.add(validate);
            name_panel.add(name_info);
            name_panel.add(box);
            name_panel.add(new_name);
            name_frame.setContentPane(name_panel);
            name_frame.setLocationRelativeTo(this);
            name_frame.setVisible(true);
        }
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    public static BotManagerPanel getInstance() {
        return instance;
    }

    public static JPanel getPing_panel() {
        return ping_panel;
    }

    public static JPanel getName_panel() {
        return name_panel;
    }

    public static JFrame getName_frame() {
        return name_frame;
    }

    public static JFrame getPing_frame() {
        return ping_frame;
    }
    public static JComboBox getGuildsListComboBox(){
        JComboBox box = new JComboBox();
        List<Guild> guildList = Main.getJda().getGuilds();
        for(int i = 0; i<guildList.size(); i++){
            box.addItem(guildList.get(i).getName()+" ("+guildList.get(i).getId()+")");
        }
        return box;
    }

}
