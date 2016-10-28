package zamok;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Тиилл
 */
public class Zamok {
    private static GraphicInterface gui;
    private static WorkThread mainthread;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(gui.getMainframe(), "Проблема с Look and Feel.", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        UIManager.put("nimbusOrange", Color.DARK_GRAY);
        gui = new GraphicInterface();


    }

    public static GraphicInterface getGui() {
        return gui;
    }

    

    public static WorkThread getMainthread() {
        return mainthread;
    }

    



    

    

    
    
    
}