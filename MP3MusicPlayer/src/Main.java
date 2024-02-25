import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Invoking SwingUtilities.invokeLater() to ensure GUI-related tasks are performed on the Event Dispatch Thread
        //This is highly recommended when running GUI Applications since it
        SwingUtilities.invokeLater(new Runnable() {
            //Overriding the run method in the runnable interface
            @Override
            public void run() {
                // Creating an instance of the MusicPlayerGUI class and setting its visibility to true.
                new MusicPlayerGUI().setVisible(true);
//                Song song = new Song("src/assets/Slime You Out.mp3");
//                System.out.println(song.getSongTitle());
//                System.out.println(song.getSongArtist());
            }
        });
    }
}
