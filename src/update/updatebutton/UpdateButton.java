package update.updatebutton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Класс кнопки обновления.
 * Для использования нужно заменить статические константы и передать
 * главное окно в конструктор.
 * @author Тиилл
 */
public class UpdateButton extends JButton {
    static final String nameprogramm = "zamok";
    static final double version = 0.1;
    static final String serverip = "192.168.1.139";
    static final int serverport = 732;
    


    public UpdateButton(JFrame mainFrame) {
        setToolTipText("Обновление");
        setEnabled(false);
        addActionListener(new UpdateButtonClickListener(mainFrame));
        CheckThread ucthread = new CheckThread(this, mainFrame);
        ucthread.start();
    }

}

class UpdateButtonClickListener implements ActionListener{
    private final JFrame mainframe;

    public UpdateButtonClickListener(JFrame mainframe) {
        this.mainframe = mainframe;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new DownloadDialog(mainframe);
    }
}    