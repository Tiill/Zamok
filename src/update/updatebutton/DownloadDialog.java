package update.updatebutton;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

final class DownloadDialog extends JDialog{
        private final JProgressBar progressline;
        private int progressPosrednik;
        private int progressStatus;
        private int step;
        

        public DownloadDialog(JFrame mainframe) {
            super(mainframe, "Обновление...");
            setSize(350, 75);
            setLocationRelativeTo(mainframe);
            
            progressline = new JProgressBar();
            progressline.setBounds(0, 0, 100, 20);
            super.getContentPane().add(progressline);
            
            
            System.err.println("Посредник:" + progressPosrednik);
            
            
            setVisible(true);
            starThread();
        }
        
        void nextValue(long bytes){
            progressStatus += bytes;
            int pos = progressStatus / progressPosrednik;
            if(pos > step){
                step = pos;
                progressline.setValue(step);
            }
//            System.err.println("байты:" + bytes + ' ' + "прогресСтатус:"+ progressStatus + ' ' + "линия:" + progressStatus / progressPosrednik);
//            repaint();
        }
        
        void resetStatus(){
            progressStatus = 0;
            step = 0;
        }
        
        void exitDialog(){
            setVisible(false);
            dispose();
        }
        
        void setSizeOfFile(int sizeOfFile){
            progressPosrednik = sizeOfFile / 100;
        }
        
        void starThread(){
            UpdateThread upthread = new UpdateThread(this);
            upthread.start();
        }
}