package zamok;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *Класс потока для программы Замок.
 * Основной поток выполняющий шифрование файлов.
 * @author Тиилл
 */
public class WorkThread extends Thread{
    private volatile threadstatus markerstopthread;

    @Override
    public void run() {
//Зависимости
        
        JRadioButton buttonencrypt = Zamok.getGui().getButtonencrypt();
        JRadioButton buttondecrypt = Zamok.getGui().getButtondecrypt();
        JLabel sostoyanie = Zamok.getGui().getSostoyanie();
        JTextField namefile = Zamok.getGui().getNamefile();
        JTextField namepass = Zamok.getGui().getNamepass();
        JTextField nameoutfile = Zamok.getGui().getNameoutfile();
        JFrame mainframe = Zamok.getGui().getMainframe();
                
                
                
                
        
        if (markerstopthread == threadstatus.stopPotok) {
            return;
        }
        Cryptor cryptor = new Cryptor();
        
//        Вызов основных функций
                if (buttonencrypt.isSelected()) {
                    sostoyanie.setText("Работаю...");
                    mainframe.repaint();
                    
                    cryptor.encryptFile(namefile.getText(), namepass.getText().getBytes(), nameoutfile.getText());
                    
                    sostoyanie.setText("Выполнено!");
                } else if (buttondecrypt.isSelected()) {
                    sostoyanie.setText("Работаю...");
                    mainframe.repaint();
                    
                    cryptor.decryptFile(namefile.getText(), namepass.getText().getBytes(), nameoutfile.getText());
                    
                    sostoyanie.setText("Выполнено!");
                } else {
                    JOptionPane.showMessageDialog(mainframe, "Херня с переключателями.", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
    }

    /**
     *Установка флага управляющего завершением потока.
     */
    public void setMarkerstopthread(threadstatus markerstopthread) {
        this.markerstopthread = markerstopthread;
    }

    /**
     *Перечисление управления потоком.
     */
    public enum threadstatus {

        startPotok, stopPotok
    }
    
    /**
     *Класс владеющий методами шифрования.
     */
    private class Cryptor {

/**
 *Расшифровывает массив байт с помощью ключа.
 */
    byte[] decrypt(byte[] encryptionBytes, byte[] keybytes)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {

        try {
            keybytes = Arrays.copyOf(keybytes, 16);
            Key key = new SecretKeySpec(keybytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] recoveredBytes
                    = cipher.doFinal(encryptionBytes);
            return recoveredBytes;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

/**
 *Зашифровывает массив байт с помощью ключа.
 */
    byte[] encrypt(byte[] inputBytes, byte[] keybytes)
            throws InvalidKeyException,
            IllegalBlockSizeException {

        try {
            keybytes = Arrays.copyOf(keybytes, 16);
            Key key = new SecretKeySpec(keybytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(inputBytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
    
/**
 *Зашифровывает файл с помощью ключа,
 * хранит весь файл в буфере (если файл слишком большой то ничего не делает).
 */
    void protoEncrypt(String file, byte[] keybytes, String namefileout) {
        long timeofproc = System.currentTimeMillis() / 1000;
        System.out.println("время запуска " + timeofproc);

        InputStream in = null;
        OutputStream out2 = null;

        try {

            in = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int posi;
            while ((posi = in.read()) != -1) {
                baos.write(posi);
            }
            byte[] p = baos.toByteArray();
            System.out.println("Размер рабочих файлов " + p.length);

            File outfile = new File(namefileout);
            outfile.createNewFile();

            out2 = new BufferedOutputStream(new FileOutputStream(namefileout));
            out2.write(encrypt(p, keybytes));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out2.close();
            } catch (IOException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("время остановки " + (System.currentTimeMillis() / 1000));
        timeofproc = (System.currentTimeMillis() / 1000) - timeofproc;
        System.out.println("время работы " + timeofproc);
    }
    
/**
 *Расшифровывает файл с помощью ключа,
 * хранит весь файл в буфере (если файл слишком большой то ничего не делает).
 */
    void protoDecrypt(String file, byte[] keybytes, String namefileout) {
        long timeofproc = System.currentTimeMillis() / 1000;
        System.out.println("время запуска " + timeofproc);

        InputStream in = null;
        OutputStream out2 = null;

        try {
            in = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int posi;
            while ((posi = in.read()) != -1) {
                baos.write(posi);
            }
            byte[] p = baos.toByteArray();
            System.out.println("Размер рабочих файлов " + p.length);

            File outfile = new File(namefileout);
            outfile.createNewFile();

            out2 = new BufferedOutputStream(new FileOutputStream(namefileout));
            out2.write(decrypt(p, keybytes));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out2.close();
            } catch (IOException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("время остановки " + (System.currentTimeMillis() / 1000));
        timeofproc = (System.currentTimeMillis() / 1000) - timeofproc;
        System.out.println("время работы " + timeofproc);
    }
    
/**
 *Зашифровывает файл с помощью ключа,
 * использует поток шифровки.
 * Зашифровывает файл по частям любого размера.
 */
    void encryptFile(String namefilein, byte[] keybytes, String namefileout) {
//Зависимости
        JProgressBar progress = Zamok.getGui().getProgress();
        JFrame mainframe = Zamok.getGui().getMainframe();
        StartStopButton buttonstart = Zamok.getGui().getButtonstart();
        
long timeofproc = System.currentTimeMillis() / 1000;
System.out.println("время запуска " + timeofproc);


        InputStream in = null;
        OutputStream out = null;
        try {
            try {
                keybytes = Arrays.copyOf(keybytes, 16);
                Key key = new SecretKeySpec(keybytes, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                
                in = new BufferedInputStream(new FileInputStream(namefilein));

                out = new CipherOutputStream(new FileOutputStream(namefileout), cipher);
                
//                double progressPosrednik = 100/((new File(file).length())/4194304);
                double progressPosrednik = (new File(namefilein)).length();
                System.out.println("Прогрес посредник = " + progressPosrednik);
                progressPosrednik = progressPosrednik / 4194304;
                progressPosrednik = (int)progressPosrednik;
                System.out.println("Прогрес посредник = " + progressPosrednik);
                progressPosrednik = 100 / progressPosrednik;
                System.out.println("Прогрес посредник = " + progressPosrednik);
                
                double progressStatus = 0;
                progress.setValue(0);

//            Размер буфера 4 Кбайта
                byte[] posredBytes = new byte[4194304];
                while (in.available() > 0) {
//                Правка для работы потока
                    if (markerstopthread == WorkThread.threadstatus.stopPotok) {
                        throw new WorkThread.MyException();
                    }

                    int n = in.read(posredBytes);
                    out.write(posredBytes, 0, n);
                    out.flush();
                    
                    System.out.println("Прогрес progressStatus = " + progressStatus);
                    progress.setValue((int)progressStatus);
                    mainframe.repaint();
                    progressStatus += progressPosrednik;
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                    out.close();
                    
                    buttonstart.setText("Старт");
                    buttonstart.markerButton = StartStopButton.statusbutton.Nachat;
                } catch (IOException ex) {
                    Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (WorkThread.MyException ex) {
            new File(namefileout).delete();
            return;
        }
System.out.println("время остановки " + (System.currentTimeMillis() / 1000));
timeofproc = (System.currentTimeMillis() / 1000) - timeofproc;
System.out.println("время работы " + timeofproc);
    }
    
/**
 *Расшифровывает файл с помощью ключа,
 * использует поток шифровки.
 * Расшифровывает файл по частям любого размера.
 */
    void decryptFile(String namefilein, byte[] keybytes, String namefileout) {
//Зависимости
        JProgressBar progress = Zamok.getGui().getProgress();
        JFrame mainframe = Zamok.getGui().getMainframe();
        StartStopButton buttonstart = Zamok.getGui().getButtonstart();
        
long timeofproc = System.currentTimeMillis() / 1000;
System.out.println("время запуска " + timeofproc);


        InputStream in = null;
        OutputStream out = null;
        try {
            try {
                keybytes = Arrays.copyOf(keybytes, 16);
                Key key = new SecretKeySpec(keybytes, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, key);
                
                in = new BufferedInputStream(new FileInputStream(namefilein));

                out = new CipherOutputStream(new FileOutputStream(namefileout), cipher);
                
//                double progressPosrednik = 100/(new File(file)).length()/4194304);
                double progressPosrednik = (new File(namefilein)).length();
                System.out.println("Прогрес посредник = " + progressPosrednik);
                progressPosrednik = progressPosrednik / 4194304;
                progressPosrednik = (int)progressPosrednik;
                System.out.println("Прогрес посредник = " + progressPosrednik);
                progressPosrednik = 100 / progressPosrednik;
                System.out.println("Прогрес посредник = " + progressPosrednik);
                
                double progressStatus = 0;
                progress.setValue(0);

//            Размер буфера 4 Кбайта
                byte[] posredBytes = new byte[4194304];
                while (in.available() > 0) {
//            Правка для работы потока
                    if (markerstopthread == WorkThread.threadstatus.stopPotok) {
                        throw new MyException();
                    }
                    int n = in.read(posredBytes);
                    out.write(posredBytes, 0, n);
                    out.flush();
                    
                    System.out.println("Прогрес progressStatus = " + progressStatus);
                    progress.setValue((int)progressStatus);
                    mainframe.repaint();
                    progressStatus += progressPosrednik;
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                    out.close();
                    
                    buttonstart.setText("Старт");
                    buttonstart.markerButton = StartStopButton.statusbutton.Nachat;
                } catch (IOException ex) {
                    Logger.getLogger(Zamok.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (MyException ex) {
            new File(namefileout).delete();
            return;
        }
System.out.println("время остановки " + (System.currentTimeMillis() / 1000));
timeofproc = (System.currentTimeMillis() / 1000) - timeofproc;
System.out.println("время работы " + timeofproc);
    }

    
}
    
    /**
     *Класс ошибок для потока.
     */
    private class MyException extends Exception{
    }
}
