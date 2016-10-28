package zamok;

import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import update.updatebutton.UpdateButton;

/**
 *
 * @author Тиилл
 */
public class GraphicInterface {
    private final JFrame mainframe;
    private final JRadioButton buttonencrypt;
    private final JRadioButton buttondecrypt;
    private final JTextField namefile;
    private final JTextField namepass;
    private final JTextField nameoutfile;
    private final JLabel sostoyanie;
    private final JProgressBar progress;
    private final StartStopButton buttonstart;
    private final UpdateButton updButton;

    public GraphicInterface() {
        //        Инициализация всех
        mainframe = new JFrame("Замок");
        JPanel contentpanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        buttonencrypt = new JRadioButton("Зашифровать");
        buttondecrypt = new JRadioButton("Расшифровать");
        namefile = new JTextField();
        namepass = new JTextField();
        nameoutfile = new JTextField();
        buttonstart = new StartStopButton();
        JButton buttonobzor = new JButton("Обзор");
        JButton buttonspravka = new JButton("Справка");
        JLabel namelabel = new JLabel("Имя файла:");
        JLabel passlabel = new JLabel("Пароль:");
        JLabel nameoutlabel = new JLabel("Файл результат:");
        sostoyanie = new JLabel("Готов к работе!");
        progress = new JProgressBar();
        updButton = new UpdateButton(mainframe);

//        Свойства окна
        mainframe.setSize(500, 300);
        mainframe.setResizable(false);
        mainframe.setLayout(null);
        mainframe.setLocationRelativeTo(null);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setContentPane(contentpanel);
        mainframe.setIconImage(new ImageIcon(this.getClass().getResource("/рес/zamokIcon.png")).getImage());
        contentpanel.setLayout(null);

        buttonencrypt.setBounds(97, 0, 130, 25);
        buttonencrypt.setSelected(true);
        buttondecrypt.setBounds(267, 0, 130, 25);
        group.add(buttonencrypt);
        group.add(buttondecrypt);
        contentpanel.add(buttonencrypt);
        contentpanel.add(buttondecrypt);

        namefile.setBounds(10, 50, 374, 28);
        namefile.setFont(new Font("Serif", Font.BOLD, 13));
        contentpanel.add(namefile);
        namefile.setTransferHandler(new TransferHandler(null){

            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
                        || support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {

                Transferable t = support.getTransferable();
                try {
                    if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

                        Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) o;
                        if(files.size() > 1) namefile.setText("Слишком много файлов");
                        else namefile.setText(files.get(0).getAbsolutePath());
                    }
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(GraphicInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GraphicInterface.class.getName()).log(Level.SEVERE, null, ex);
                }

                return true;
            }
        });
        PlainDocument doc = new PlainDocument();
        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                String posred = getNamefile().getText();
                if (posred.length() > 0) {
                    for (int i = posred.length() - 1; i != 0; --i) {
                        if (posred.charAt(i) == '.') {
                            posred = posred.substring(0, i);
                            break;
                        }
                    }
                }
                getNameoutfile().setText(posred);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String posred = getNamefile().getText();
                if (posred.length() > 0) {
                    for (int i = posred.length() - 1; i != 0; --i) {
                        if (posred.charAt(i) == '.') {
                            posred = posred.substring(0, i);
                            break;
                        }
                    }
                }
                getNameoutfile().setText(posred);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String posred = getNamefile().getText();
                if (posred.length() > 0) {
                    for (int i = posred.length() - 1; i != 0; --i) {
                        if (posred.charAt(i) == '.') {
                            posred = posred.substring(0, i);
                            break;
                        }
                    }
                }
                getNameoutfile().setText(posred);
            }
        });
        namefile.setDocument(doc);

        namelabel.setBounds(10, 28, 100, 25);
        contentpanel.add(namelabel);

        namepass.setBounds(10, 100, 474, 28);
        namepass.setFont(new Font("Serif", Font.BOLD, 13));
        contentpanel.add(namepass);

        passlabel.setBounds(10, 78, 100, 25);
        contentpanel.add(passlabel);

        nameoutfile.setBounds(10, 150, 474, 28);
        nameoutfile.setFont(new Font("Serif", Font.BOLD, 13));
        contentpanel.add(nameoutfile);

        nameoutlabel.setBounds(10, 128, 100, 25);
        contentpanel.add(nameoutlabel);

        buttonstart.setBounds(10, 183, 100, 28);
        contentpanel.add(buttonstart);

        buttonobzor.setBounds(384, 50, 99, 28);
        contentpanel.add(buttonobzor);
        buttonobzor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
//            Очень интерестная функция для выбора файла
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                int returnVal = fileChooser.showOpenDialog(getMainframe());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    getNamefile().setText(file.getAbsolutePath());
                } else {
                    System.out.println("File access cancelled by user.");
                }

            }
        });
        
        buttonspravka.setBounds(384, 183, 100, 28);
        contentpanel.add(buttonspravka);
        buttonspravka.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getMainframe(), "Программа ЗамОк зашифровывает/разшифровывает файл \n"
                        + "выбранным ключем.\n"
                        + "Максимальный размер ключа 32 байта.\n"
                        + "Расширение результирующего файла нужно указать вручную.\n"
                        + "                                                                                                      Артем");
            }
            
        });
        
        sostoyanie.setBounds(10, 213, 474, 28);
        contentpanel.add(sostoyanie);
        
        progress.setBorderPainted(false);
//        progress.setForeground(Color.red);
        progress.setBounds(10, 243, 474, 20);
        contentpanel.add(progress);
        
        updButton.setBounds(284, 183, 100, 28);
        updButton.setText("Обновление");
        contentpanel.add(updButton);

//        contentpanel.setBackground(Color.red);
        mainframe.setVisible(true);
    }

    /**
     * @return the buttondecrypt
     */
    public JRadioButton getButtondecrypt() {
        return buttondecrypt;
    }

    /**
     * @return the buttonencrypt
     */
    public JRadioButton getButtonencrypt() {
        return buttonencrypt;
    }

    /**
     * @return the sostoyanie
     */
    public JLabel getSostoyanie() {
        return sostoyanie;
    }

    /**
     * @return the namefile
     */
    public JTextField getNamefile() {
        return namefile;
    }

    /**
     * @return the nameoutfile
     */
    public JTextField getNameoutfile() {
        return nameoutfile;
    }

    /**
     * @return the namepass
     */
    public JTextField getNamepass() {
        return namepass;
    }

    /**
     * @return the mainframe
     */
    public JFrame getMainframe() {
        return mainframe;
    }

    /**
     * @return the progress
     */
    public JProgressBar getProgress() {
        return progress;
    }

    /**
     * @return the buttonstart
     */
    public StartStopButton getButtonstart() {
        return buttonstart;
    }
    
    
}
