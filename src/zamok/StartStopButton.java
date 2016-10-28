package zamok;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Кнопка для Замок фрейма меняющая поведение после нажатия. 
 * Меняется с "Старт" на "Стоп".
 *
 * @author Тиилл
 */
class StartStopButton extends javax.swing.JButton {
    volatile statusbutton markerButton;

    public StartStopButton() {
        super();
        this.setText("Старт");
        this.addActionListener(new actionListenerForThisButton());
        this.markerButton = statusbutton.Nachat;
    }

//    Внутренние сущности
//    доступные для пакета
//    Перечисление состояний
    /**
     *Перечисление управления состоянием кнопки.
     */
    enum statusbutton {

        Ostanovit, Nachat
    }

    /**
     *Слушатель для этой кнопки.
     */
    private class actionListenerForThisButton implements ActionListener {

//    Действие
        public actionListenerForThisButton() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//Зависимости
            WorkThread mainThread = Zamok.getMainthread();
            JTextField namefile = Zamok.getGui().getNamefile();
            JTextField namepass = Zamok.getGui().getNamepass();
            JTextField nameoutfile = Zamok.getGui().getNameoutfile();
            JLabel sostoyanie = Zamok.getGui().getSostoyanie();
            
//                Проверка входных данных
            if (namefile.getText().length() == 0) {
                sostoyanie.setText("Не выбран файл.");
                return;
            }
            if (namepass.getText().length() == 0) {
                sostoyanie.setText("Не выбран ключ.");
                return;
            }
            if (nameoutfile.getText().length() == 0) {
                sostoyanie.setText("Не выбран файл результата.");
                return;
            }
            File filecheck = new File(namefile.getText());
            if(!filecheck.exists()){
                sostoyanie.setText("Не существует файл " + '\"' + filecheck.getName() + "\"");
                return;
            }
            if(filecheck.isDirectory()){
                sostoyanie.setText("Не поддерживается шифрование папок " + '\"' + filecheck.getName() + "\"");
                return;
            }

            if (markerButton == statusbutton.Nachat) {
                setText("Стоп");

                mainThread = new WorkThread();
                mainThread.setMarkerstopthread(WorkThread.threadstatus.startPotok);
                mainThread.start();

                markerButton = statusbutton.Ostanovit;
            } else {
                setText("Старт");

                mainThread.setMarkerstopthread(WorkThread.threadstatus.stopPotok);

                markerButton = statusbutton.Nachat;
            }
        }
    }
}