package update.updatebutton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Тиилл
 */
public class UpdateThread extends Thread{
    private final DownloadDialog dialog;


    public UpdateThread(DownloadDialog dialog) {
        this.dialog = dialog;
        
    }
    
    

    @Override
    public void run() {
                OutputStream serverout = null;
        InputStream serverin = null;
        FileOutputStream fout = null;
        try {
//Подключение к серверу
            InetAddress ip = InetAddress.getByName(UpdateButton.serverip);
            Socket server;
            try {
                server = new Socket(ip, UpdateButton.serverport);
            } catch (IOException ex) {
                Logger.getLogger(UpdateThread.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            server.setSoTimeout(20000);
            
//Потоки связи с сервером
            
            serverout = server.getOutputStream();
            serverin = server.getInputStream();
            
//Посредник файла
            File filenewversion;
            int filesize;
            
//Запрос на скачивание
            serverout.write(("#" + UpdateButton.nameprogramm + "#download#").getBytes());
            serverout.flush();
            
//Ответ сервера с именем файла и размером
            
            
            byte buffer = (byte)serverin.read();                                //исключение
            byte bpos[] = new byte[serverin.available() + 1];
            bpos[0] = buffer;
            serverin.read(bpos, 1, bpos.length-1);
            String answer = new String(bpos);
            
//Разбор ответа
            StringTokenizer parser = new StringTokenizer(answer, "#");
            answer = parser.nextToken();
            
//Заполнение данных файла из ответа
            filenewversion = new File(answer);
            answer = parser.nextToken();
            filesize = Integer.parseInt(answer);
            
//Создание файла
            if(filenewversion.exists()){
                filenewversion.delete();
            }
            filenewversion.createNewFile();
            fout = new FileOutputStream(filenewversion);
            
            
            
//Подтверждение получения имени файла для сервера
            String podtverjdenie = "#" + UpdateButton.nameprogramm + "#startDownload#";
            serverout.write(podtverjdenie.getBytes());
            serverout.flush();
            
            dialog.setSizeOfFile(filesize);
            
//Скачивание файла
            int total = filesize;
            byte [] readingposrednik = new byte [8192];
            
            while ( total > 0 ){
                int q = serverin.read(readingposrednik);
                total -= q;
                fout.write(readingposrednik, 0, q);
                dialog.nextValue(q);
            }
            dialog.exitDialog();
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(UpdateButtonClickListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UpdateButtonClickListener.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(serverout != null) serverout.close();
                if(serverin != null) serverin.close();
                if(fout != null) fout.close();
            } catch (IOException ex) {
                Logger.getLogger(UpdateButtonClickListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}
