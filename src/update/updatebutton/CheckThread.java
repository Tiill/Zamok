package update.updatebutton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Тиилл
 */
class CheckThread extends Thread{
    private final UpdateButton button;
    private final JFrame mainFrame;

    public CheckThread(UpdateButton mainbutton, JFrame mainframe) {
        button = mainbutton;
        mainFrame = mainframe;
        setDaemon(true);
    }

    @Override
    public void run() {
        
        
        try {
//Подключение к серверу
            InetAddress ip = InetAddress.getByName(UpdateButton.serverip);
            Socket server;
            server = new Socket(ip, UpdateButton.serverport);
            
//Потоки связи с сервером
            OutputStream serverout = server.getOutputStream();
            InputStream serverin = server.getInputStream();
            
//Запрос
            String zapros = "#" + UpdateButton.nameprogramm + "#zapros#" + UpdateButton.version + "#";
            serverout.write(zapros.getBytes());
            serverout.flush();
            
//Ответ
            server.setSoTimeout(7000);
            PushbackInputStream inp = new PushbackInputStream(serverin);
            int i = inp.read();
            inp.unread(i);
            byte[] bpos = new byte[inp.available()];
            inp.read(bpos);
            String answer = new String(bpos);
            
//Разбор ответа
            StringTokenizer parser = new StringTokenizer(answer, "#");
            answer = parser.nextToken();

            if (null != answer) switch (answer) {
                case "no":
                    button.setEnabled(false);
                    mainFrame.repaint();
                    break;
                case "ok":
                    button.setEnabled(true);
                    break;
                default:
                    Logger.getLogger(CheckThread.class.getName()).log(Level.SEVERE, null, "Неверный ответ сервера.");
                    break;
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Ответ сервера пуст. Обьект ответ не сформирован.", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            Logger.getLogger(CheckThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}

