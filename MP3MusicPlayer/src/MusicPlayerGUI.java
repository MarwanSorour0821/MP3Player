//import javax.imageio.ImageIO;
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.Hashtable;
//
//public class MusicPlayerGUI extends JFrame{
//
//    public static final Color Frame_Color = Color.BLACK;
//    public static final Color text_color = Color.WHITE;
//
//    private MusicPlayer musicPlayer;
//
//    //allow us to use file explorer in our app
//    private JFileChooser jFileChooser;
//
//    private JLabel songTitle, songArtist;
//
//    private JPanel playBackButtons;
//
//    private JSlider playBackSlider;
//
//
//    public MusicPlayerGUI(){
//        //calls JFrame to configure the GUI and set the title header to "Music Player"
//        super("Music Player");
//
//        //Sets width and height size
//        setSize(400,600);
//
//        //end app when process is closed
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
//        //set GUI at the center of the screen
//        setLocationRelativeTo(null);
//
//        //Don't allow GUI to be resized
//        setResizable(false);
//
//        //set layout to null which allows us to control the (x,y) coordinates of our components
//        //and also to set the height and width
//        setLayout(null);
//
//        //change frame color
//        //content pane is where the main content of the frame is typically placed.
//        // It's a Container object that holds all the visible components (such as buttons, labels, etc.) of the frame.
//        getContentPane().setBackground(Frame_Color);
//
//        musicPlayer = new MusicPlayer(this);
//
//        jFileChooser = new JFileChooser();
//
//        //set a default path for file explorer
//        jFileChooser.setCurrentDirectory(new File("src/assets"));
//
//        //filter chooser to only see .mp3 files
//        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
//
//        addComponents();
//    }
//
//    //Function to add GUI components
//    private void addComponents(){
//        //add toolBar
//        addToolBar();
//
//        //load record image
//        //JLabel is a class that represents a display area for a short text, image
//        JLabel songImage = new JLabel(loadImage("src/assets/record.png"));
//        //set bounds of image
//        songImage.setBounds(0,50,getWidth()-20, 225);
//        add(songImage);
//
//        songTitle = new JLabel("Song Title");
//        songTitle.setBounds(0, 285,getWidth()-10, 30);
//        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
//
//        //sets text color to white
//        songTitle.setForeground(text_color);
//
//        //centers the text
//        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
//        add(songTitle);
//
//        //--------------------------------------------------------------------------------//
//        //Song Artist
//        songArtist = new JLabel("Artist");
//        songArtist.setBounds(0, 315,getWidth()-10, 30);
//        songArtist.setFont(new Font("Dialog", Font.PLAIN, 24));
//        songArtist.setForeground(text_color);
//        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
//        add(songArtist);
//
//        //--------------------------------------------------------------------------------//
//        //Playback slider
//        JSlider playBackSlider = new JSlider(JSlider.HORIZONTAL,0,100,0);
//        playBackSlider.setBounds(getWidth()/2 - 300/2,365, 300,40);
//        playBackSlider.setBackground(null);
//        playBackSlider.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                //when the user holds the tick, song needs to pause
//                musicPlayer.pauseSong();
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                //when user leaves the tick
//                JSlider source = (JSlider) e.getSource();
//
//                //get play back from where the user drops the tick
//                int frame = source.getValue();
//
//                //update current frame in music frame to this value
//                musicPlayer.setCurrentMinute(frame);
//
//                //update current time
//                musicPlayer.setCurrentTime((int) (frame / (2.08 * musicPlayer.getCurrentSong().getFrameRatePerMilliSecond())));
//
//                //resume the song
//                musicPlayer.playCurrentSong();
//
//                //toggle on pause button and play button off
//                enablePauseDisablePlayButton();
//
//            }
//        });
//        add(playBackSlider);
//
//        //--------------------------------------------------------------------------------//
//        //playback buttons
//        addPlayBackButtons();
//
//    }
//
//    private void addPlayBackButtons() {
//        playBackButtons = new JPanel();
//        playBackButtons.setBounds(0,435,getWidth()-10,80);
//        playBackButtons.setBackground(null);
//
//        //previous button
//        JButton previousButton = new JButton(loadImage("src/assets/previous.png"));
//        previousButton.setBorderPainted(false);
//        previousButton.setBackground(null);
//        playBackButtons.add(previousButton);
//
//        //Play Button
//        JButton playButton = new JButton(loadImage("src/assets/play.png"));
//        playButton.setBorderPainted(false);
//        playButton.setBackground(null);
//        playButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //toggle on pause button
//                enablePauseDisablePlayButton();
//
//                //play or resume song
//                musicPlayer.playCurrentSong();
//
//            }
//        });
//        playBackButtons.add(playButton);
//
//        //Pause Button
//        JButton pauseButton = new JButton(loadImage("src/assets/pause.png"));
//        pauseButton.setBorderPainted(false);
//        pauseButton.setVisible(false);
//        pauseButton.setBackground(null);
//        pauseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //toggle play button
//                enablePlayButtonDisablePauseButton();
//
//                //pause
//                musicPlayer.pauseSong();
//
//            }
//        });
//        playBackButtons.add(pauseButton);
//
//        //Forward Button
//        JButton forwardButton = new JButton(loadImage("src/assets/next.png"));
//        forwardButton.setBorderPainted(false);
//        forwardButton.setBackground(null);
//        playBackButtons.add(forwardButton);
//
//        add(playBackButtons);
//    }
//
//    private void addToolBar(){
//        JToolBar toolBar = new JToolBar();
//
//        //x and y are the position of the toolbar and the getWidth gets the width of the GUI
//        toolBar.setBounds(0,0,getWidth(),20);
//
//        //prevent the toolbar from being moved
//        toolBar.setFloatable(false);
//
//        //add dropdown menu
//        JMenuBar menuBar = new JMenuBar();
//        toolBar.add(menuBar);
//
//        //add the song menu where we will place the loading song option
//        JMenu songMenu = new JMenu("Song");
//        menuBar.add(songMenu);
//
//        //add load song option
//        JMenuItem loadSong = new JMenuItem("Load Song");
//        loadSong.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //to know what the user did
//                int res = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
//                File selectedFile = jFileChooser.getSelectedFile();
//
//                //also checking if user pressed "open" button in the GUI
//                if (res == JFileChooser.APPROVE_OPTION && selectedFile != null){
//                    Song song = new Song(selectedFile.getPath());
//
//                    //load song in music player
//                    musicPlayer.loadSong(song);
//
//                    //update song and title
//                    updateSongArtistAndTitle(song);
//
//                    //toggle play and pause button
//                    enablePauseDisablePlayButton();
//
//                    //update play back slider
//                    changePlayBackSlider(song);
//
//                }
//
//            }
//        });
//        songMenu.add(loadSong);
//
//        //add the playlist menu
//        JMenu playList = new JMenu("Playlist");
//        menuBar.add(playList);
//
//        //add items to playlist ,menu
//        JMenuItem createPlayList = new JMenuItem("Create Playlist");
//        playList.add(createPlayList);
//
//        JMenuItem loadPlayList = new JMenuItem("Load a Playlist");
//        playList.add(loadPlayList);
//
//
//        //adding the JToolBar to the container, it becomes part of the GUI and will be displayed according to its specified bounds
//        add(toolBar);
//    }
//
//    private void updateSongArtistAndTitle(Song song){
//        songTitle.setText(song.getSongTitle());
//        songArtist.setText(song.getSongArtist());
//    }
//
//    //will be used to update slider from Music Player Class
//    public void setPlayBackSliderValue(int frame){
//        playBackSlider.setValue(frame);
//    }
//
//    private void enablePauseDisablePlayButton(){
//        //get reference to play buttons
//        //get component is like an array, as long as you know the sequence in which the buttons were added
//        JButton playButton = (JButton) playBackButtons.getComponent(1);
//        JButton pauseButton = (JButton) playBackButtons.getComponent(2);
//
//        //turn off
//        playButton.setVisible(false);
//        playButton.setEnabled(false);
//
//        //turn on pause button
//        pauseButton.setVisible(true);
//        pauseButton.setEnabled(true);
//    }
//
//    private void enablePlayButtonDisablePauseButton(){
//        //get reference to play buttons
//        //get component is like an array, as long as you know the sequence in which the buttons were added
//        JButton playButton = (JButton) playBackButtons.getComponent(1);
//        JButton pauseButton = (JButton) playBackButtons.getComponent(2);
//
//        //turn on
//        playButton.setVisible(true);
//        playButton.setEnabled(true);
//
//        //turn off pause button
//        pauseButton.setVisible(false);
//        pauseButton.setEnabled(false);
//    }
//
//    private ImageIcon loadImage(String imagePath){
//        //handles any errors trying to access the file in case the file does not exist
//        try{
//            //read the image file from the given path
//            BufferedImage image = ImageIO.read(new File(imagePath));
//
//            //return an image icon so that the component can render the image
//            return new ImageIcon(image);
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        //if resource not found
//        return null;
//    }
//
//    private void changePlayBackSlider(Song song){
//        //update max count for slider
//        //getFrameCount returns the total number of frames in the file
//        playBackSlider.setMaximum(song.getMp3File().getFrameCount());
//
//        //create song length label
//        Hashtable<Integer,JLabel> labelTabel = new Hashtable<>();
//
//        //beginning will be 0
//        JLabel labelBeginning = new JLabel("00:00");
//        labelBeginning.setFont(new Font("Dialog", Font.BOLD, 18));
//        labelBeginning.setForeground(text_color);
//
//        //The ending of each song will not be a constant since all songs have different lengths
//        JLabel endOfLabel = new JLabel(song.getSongLength());
//        endOfLabel.setFont(new Font("Dialog", Font.BOLD, 18));
//        endOfLabel.setForeground(text_color);
//
//
//        labelTabel.put(0,labelBeginning);
//        labelTabel.put(song.getMp3File().getFrameCount(), endOfLabel);
//
//        playBackSlider.setLabelTable(labelTabel);
//        playBackSlider.setPaintLabels(true);
//
//    }
//    public void updatePlaybackSlider(Song song){
//        // update max count for slider
//        playBackSlider.setMaximum(song.getMp3File().getFrameCount());
//
//        // create the song length label
//        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
//
//        // beginning will be 00:00
//        JLabel labelBeginning = new JLabel("00:00");
//        labelBeginning.setFont(new Font("Dialog", Font.BOLD, 18));
//        labelBeginning.setForeground(text_color);
//
//        // end will vary depending on the song
//        JLabel labelEnd =  new JLabel(song.getSongLength());
//        labelEnd.setFont(new Font("Dialog", Font.BOLD, 18));
//        labelEnd.setForeground(text_color);
//
//        labelTable.put(0, labelBeginning);
//        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);
//
//        playBackSlider.setLabelTable(labelTable);
//        playBackSlider.setPaintLabels(true);
//    }
//
//}

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class MusicPlayerGUI extends JFrame {
    // color configurations
    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

    private MusicPlayer musicPlayer;

    // allow us to use file explorer in our app
    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
    private JSlider playbackSlider;

    public MusicPlayerGUI(){
        // calls JFrame constructor to configure out gui and set the title heaader to "Music Player"
        super("Music Player");

        // set the width and height
        setSize(400, 600);

        // end process when app is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // launch the app at the center of the screen
        setLocationRelativeTo(null);

        // prevent the app from being resized
        setResizable(false);

        // set layout to null which allows us to control the (x, y) coordinates of our components
        // and also set the height and width
        setLayout(null);

        // change the frame color
        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new MusicPlayer(this);
        jFileChooser = new JFileChooser();

        // set a default path for file explorer
        jFileChooser.setCurrentDirectory(new File("src/assets"));

        // filter file chooser to only see .mp3 files
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents(){
        // add toolbar
        addToolbar();

        // load record image
        JLabel songImage = new JLabel(loadImage("src/assets/record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        // song title
        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        // song artist
        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 24));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        // playback slider
        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 - 300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // when the user is holding the tick we want to the pause the song
                musicPlayer.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // when the user drops the tick
                JSlider source = (JSlider) e.getSource();

                // get the frame value from where the user wants to playback to
                int frame = source.getValue();

                // update the current frame in the music player to this frame
                musicPlayer.setCurrentMinute(frame);

                // update current time in milli as well
                musicPlayer.setCurrentTime((int) (frame / (2.08 * musicPlayer.getCurrentSong().getFrameRatePerMilliSecond())));

                // resume the song
                musicPlayer.playCurrentSong();

                // toggle on pause button and toggle off play button
                enablePauseButtonDisablePlayButton();
            }
        });
        add(playbackSlider);

        // playback buttons (i.e. previous, play, next)
        addPlaybackBtns();
    }

    private void addToolbar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 20);

        // prevent toolbar from being moved
        toolBar.setFloatable(false);

        // add drop down menu
        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        // now we will add a song menu where we will place the loading song option
        JMenu songMenu = new JMenu("Song");
        menuBar.add(songMenu);

        // add the "load song" item in the songMenu
        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // an integer is returned to us to let us know what the user did
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                // this means that we are also checking to see if the user pressed the "open" button
                if(result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    // create a song obj based on selected file
                    Song song = new Song(selectedFile.getPath());

                    // load song in music player
                    musicPlayer.loadSong(song);

                    // update song title and artist
                    updateSongTitleAndArtist(song);

                    // update playback slider
                    updatePlaybackSlider(song);

                    // toggle on pause button and toggle off play button
                    enablePauseButtonDisablePlayButton();
                }
            }
        });
        songMenu.add(loadSong);

        // now we will add the playlist menu
        JMenu playlistMenu = new JMenu("Playlist");
        menuBar.add(playlistMenu);

        // then add the items to the playlist menu
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //load music playlist
                new PlaylistDialog(MusicPlayerGUI.this).setVisible(true);
            }
        });
        playlistMenu.add(createPlaylist);

        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        loadPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("Playlist", "txt"));
                jFileChooser.setCurrentDirectory(new File("src/assets"));

                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if(result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    //stop music
                    musicPlayer.stopPlayer();

                    //load Playlist
                    musicPlayer.loadPlayList(selectedFile);
                }

            }
        });
        playlistMenu.add(loadPlaylist);

        add(toolBar);
    }

    private void addPlaybackBtns(){
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 435, getWidth() - 10, 80);
        playbackBtns.setBackground(null);

        // previous button
        JButton prevButton = new JButton(loadImage("src/assets/previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //previous song
                musicPlayer.previousSong();
            }
        });
        playbackBtns.add(prevButton);

        // play button
        JButton playButton = new JButton(loadImage("src/assets/play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // toggle off play button and toggle on pause button
                enablePauseButtonDisablePlayButton();

                // play or resume song
                musicPlayer.playCurrentSong();
            }
        });
        playbackBtns.add(playButton);

        // pause button
        JButton pauseButton = new JButton(loadImage("src/assets/pause.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // toggle off pause button and toggle on play button
                enablePlayButtonDisablePauseButton();

                // pause the song
                musicPlayer.pauseSong();
            }
        });
        playbackBtns.add(pauseButton);

        // next button
        JButton nextButton = new JButton(loadImage("src/assets/next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to the next song
                musicPlayer.nextSong();
            }
        });
        playbackBtns.add(nextButton);

        add(playbackBtns);
    }

    // this will be used to update our slider from the music player class
    public void setPlaybackSliderValue(int frame){
        playbackSlider.setValue(frame);
    }

    public void updateSongTitleAndArtist(Song song){
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    public void updatePlaybackSlider(Song song){
        // update max count for slider
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        // create the song length label
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        // beginning will be 00:00
        JLabel labelBeginning = new JLabel("00:00");
        labelBeginning.setFont(new Font("Dialog", Font.BOLD, 18));
        labelBeginning.setForeground(TEXT_COLOR);

        // end will vary depending on the song
        JLabel labelEnd =  new JLabel(song.getSongLength());
        labelEnd.setFont(new Font("Dialog", Font.BOLD, 18));
        labelEnd.setForeground(TEXT_COLOR);

        labelTable.put(0, labelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);

        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }

    public void enablePauseButtonDisablePlayButton(){
        // retrieve reference to play button from playbackBtns panel
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        // turn off play button
        playButton.setVisible(false);
        playButton.setEnabled(false);

        // turn on pause button
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    public void enablePlayButtonDisablePauseButton(){
        // retrieve reference to play button from playbackBtns panel
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        // turn on play button
        playButton.setVisible(true);
        playButton.setEnabled(true);

        // turn off pause button
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    private ImageIcon loadImage(String imagePath){
        try{
            // read the image file from the given path
            BufferedImage image = ImageIO.read(new File(imagePath));

            // returns an image icon so that our component can render the image
            return new ImageIcon(image);
        }catch(Exception e){
            e.printStackTrace();
        }

        // could not find resource
        return null;
    }
}










