import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class PlaylistDialog extends JDialog {

    private MusicPlayerGUI musicPlayerGUI;

    //store all of the paths to be written in a txt file (when we load a playlist)
    private ArrayList<String> songPaths;

    public PlaylistDialog(MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
        songPaths = new ArrayList<>();
        //configure dialog
        setTitle("Create Playlist");

        setSize(400,400);
        setResizable(false);

        getContentPane().setBackground(MusicPlayerGUI.FRAME_COLOR);
        setLayout(null);

        //dialog has to be closed to give focus
        setModal(true);

        setLocationRelativeTo(musicPlayerGUI);

        addDialogComponents();

    }

    private void addDialogComponents() {
        JPanel songContainer = new JPanel();
        //BoxLayout arranges components either horizontally or vertically in a single line.
        // In this case, it's configured to arrange components vertically (BoxLayout.Y_AXIS),
        // so components added to songContainer will be stacked vertically on top of each other.
        songContainer.setLayout(new BoxLayout(songContainer, BoxLayout.Y_AXIS));

        songContainer.setBounds((int) (getWidth()*0.025), 10,(int)(getWidth()*0.90),(int) (getHeight() * 0.75));
        add(songContainer);

        //add song buttons
        JButton addSongButton = new JButton("Add");
        addSongButton.setBounds(60, (int) (getHeight()*0.80), 100, 25);
        addSongButton.setFont(new Font("Dialog", Font.BOLD, 14));
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open file explorer
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
                jFileChooser.setCurrentDirectory(new File("src/assets"));

                //return an int representing the result of the user's itneraction
                //jfilechooser.APPROVE_OPTION and etc.
                int result = jFileChooser.showOpenDialog(PlaylistDialog.this);

                File selectedFile = jFileChooser.getSelectedFile();
                if(result == JFileChooser.APPROVE_OPTION && selectedFile!= null){
                    JLabel filePathLabel = new JLabel(selectedFile.getPath());
                    filePathLabel.setFont(new Font("Dialog",Font.BOLD, 12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    //add to the list
                    //return text string that the label displays
                    songPaths.add(filePathLabel.getText());

                    //add to container
                    songContainer.add(filePathLabel);

                    //refreshes dialog to show newly added JLabel
                    //The revalidate() function in Java Swing is used to recalculate and update the layout of a
                    // container and its components. When you add or remove components dynamically at runtime,
                    // the container's layout needs to be recalculated to accommodate the changes.
                    // This is where revalidate() comes into play.
                    songContainer.revalidate();

                }

            }
        });
        add(addSongButton);

        //save Playlist button
        JButton savePlaylist = new JButton("Save");
        savePlaylist.setBounds(215, (int) (getHeight()*0.80), 100, 25);
        savePlaylist.setFont(new Font("Dialog", Font.BOLD, 14));
        savePlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setCurrentDirectory(new File("src/assets"));
                    int result = jFileChooser.showSaveDialog(PlaylistDialog.this);

                    if (result == JFileChooser.APPROVE_OPTION){
                        File selectedFile = jFileChooser.getSelectedFile();

                        //convert to txt file
                        //checks to see if file has the .txt extension
                        if(!selectedFile.getName().substring(selectedFile.getName().length()-4).equalsIgnoreCase(".text")){
                            selectedFile = new File(selectedFile.getAbsoluteFile()+".txt");
                        }

                        //create file at current directory
                        selectedFile.createNewFile();

                        //write all song paths into this file
                        FileWriter fileWriter = new FileWriter(selectedFile);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        //iterate through song paths list and write each string
                        for(String songPath: songPaths){
                            bufferedWriter.write(songPath+"\n");
                        }
                        bufferedWriter.close();

                        //display success dialog
                        JOptionPane.showMessageDialog(PlaylistDialog.this, "Playlist Created");

                        //close this dialog
                        PlaylistDialog.this.dispose();

                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });
        add(savePlaylist);


    }

}
