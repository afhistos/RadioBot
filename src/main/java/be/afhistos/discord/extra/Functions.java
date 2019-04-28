package be.afhistos.discord.extra;

import be.afhistos.consoleLogger.Console;
import be.afhistos.consoleLogger.TextColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Functions {
    private static Font montserrat;
    public static Point getRecCenterPos(Rectangle parent, Rectangle rectangle){
        double x = parent.getWidth()/2.0D-rectangle.getWidth()/2.0D;
        double y = parent.getHeight()/2.0D-rectangle.getHeight()/2.0D;
        return new Point((int)x, (int)y);
    }

    public static void drawCenteredString(Graphics g, String string, Rectangle parent){
        FontMetrics fontMetrics= g.getFontMetrics();
        Rectangle2D stringBounds = fontMetrics.getStringBounds(string, g);
        Point center = getRecCenterPos(parent,stringBounds.getBounds());
        g.drawString(string, (int)center.getX(), (int)center.getY());
    }
    public static BufferedImage getImage(String name){
        try{
            return ImageIO.read(Functions.class.getResourceAsStream("/be/afhistos/mathutil/resources/"+name));
        } catch (IOException e){
            Console.log(TextColor.BG_BLACK+TextColor.BRIGHT_BG_RED+"Impossible de charger la ressource données ('../resources/"+name+")! \n Assurez vous que le fichier est bien placé dans le package 'resources' et que le nom entré est correct!");
            return null;
        } catch (IllegalArgumentException e){
            Console.log(TextColor.BG_YELLOW+TextColor.RED+"Impossible de charger la ressource données ('../resources/"+name+"')! \n Assurez vous que le fichier est bien placé dans le package 'resources' et que le nom entré est correct!");
            return null;
        }
    }
    public static File getFile(String path){
        return new File(Functions.class.getResource("/"+path).getFile());
    }
    public Functions() throws IOException, FontFormatException {
        montserrat = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource(String.valueOf(getFile("Montserrat-Black.ttf"))).openStream());
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(montserrat);
        montserrat = montserrat.deriveFont(12f);

    }

    public static Font getMontserrat() {
        return montserrat;
    }

}
