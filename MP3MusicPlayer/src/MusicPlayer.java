import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MusicPlayer extends PlaybackListener {
    //to update GUI
    private MusicPlayerGUI musicPlayerGUI;

    //used to update value of slider more synchronously since slider fails at some instance and returns
    // true which makes it not move
    private static final Object SignalPlay = new Object();

    //to store songs
    private Song currentSong;

    public Song getCurrentSong() {
        return currentSong;
    }

    private ArrayList<Song> playList;
    //to keep track of the index that we are in the playList
    private int currentPlaylistIndex;

    //use Jlayer to handle playing the music
    private AdvancedPlayer advancedPlayer;

    //bool flag to show if song is paused
    private boolean isPaused;

    //to know when song finishes
    private boolean songFinished;

    private boolean pressedNext, pressedPrev;

    //to store current position of song
    private int currentMinute;
    public void setCurrentMinute(int frame){
        currentMinute = frame;
    }
    private int currentTime;
    public void setCurrentTime(int time){
        currentTime = time;
    }


    public MusicPlayer(MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
    }

    public void loadSong(Song song){
        currentSong = song;

        //if we load a song while a playlist is playing then we set playlist to null and play selected song
        playList = null;

        //stop song if possible
        if (!songFinished){
            stopPlayer();
        }

        //play current song if not null
        if (currentSong != null){
            //reset frame
            currentTime = 0;

            //reset minute
            currentMinute = 0;

            //update gui
            musicPlayerGUI.setPlaybackSliderValue(0);

            playCurrentSong();
        }
    }

    public void loadPlayList(File playlistFile){
        playList = new ArrayList<>();

        //store the paths from the ext file into the playlist array list

        try{
            FileReader fileReader = new FileReader(playlistFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //reach until the end of the file
            String songPath;
            while((songPath = bufferedReader.readLine()) != null){
                Song song = new Song(songPath);

                //add to playList array list
                playList.add(song);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(playList.size() > 0){
            //reset playBack Slider
            musicPlayerGUI.setPlaybackSliderValue(0);
            currentMinute = 0;

            //update current song to the first song in the playlist
            currentSong = playList.get(0);

            //start from the beginning frame
            currentTime = 0;

            //update gui
            musicPlayerGUI.enablePauseButtonDisablePlayButton();
            musicPlayerGUI.updateSongTitleAndArtist(currentSong);
            musicPlayerGUI.updatePlaybackSlider(currentSong);

            //start song
            playCurrentSong();
        }

    }

    public void playCurrentSong() {
        if (currentSong == null) return;
        try{
            //read mp3 audio data
            // FileInputStream is a class that represents an input stream for reading bytes from a file.
            // It's commonly used to read data from files,
            // such as text files, binary files, or audio files, byte by byte or in chunks.
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());

            //improves performance while reading audio data
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            //create new Advanced Player
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);

            //sets the current instance of this class as the playback listener to handle events
            advancedPlayer.setPlayBackListener(this);

            startMusicThread();

            beginPlayBackSliderFunction();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pauseSong(){
        if(advancedPlayer != null){
            isPaused = true;

            //stop the player
            stopPlayer();
        }

    }

    public void stopPlayer(){
        if (advancedPlayer != null){
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }

    public void nextSong() {
        if (playList == null) return;

        if (currentPlaylistIndex + 1 > playList.size() - 1) return;

        pressedNext = true;

        //stop song if possible
        if (!songFinished){
            stopPlayer();
        }
        //increase current playlist index
        currentPlaylistIndex++;

        //update current song
        currentSong = playList.get(currentPlaylistIndex);

        //reset frame
        currentTime = 0;

        //reset current minute
        currentMinute = 0;

        //update gui
        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        //play song
        playCurrentSong();

    }

    public void previousSong(){
        if(playList == null) return;

        //check uf there are songs to go back to
        if(currentPlaylistIndex - 1 < 0) return;

        pressedPrev = true;

        //stop song if possible
        if (!songFinished){
            stopPlayer();
        }

        //decrement current playlist index
        currentPlaylistIndex--;

        //update current song
        currentSong = playList.get(currentPlaylistIndex);

        //reset frame
        currentTime = 0;

        //reset current minute
        currentMinute = 0;

        //update gui
        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        //play song
        playCurrentSong();
    }

    //create a thread that will handle playing the music
    private void startMusicThread() {
        if(advancedPlayer == null) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isPaused){
                    try{
                        //wait until notfied by other thread to continue
                        synchronized (SignalPlay){
                            SignalPlay.wait();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                try{

                    if(isPaused){
                        synchronized (SignalPlay){
                            isPaused = false;

                            //notify other thread to continue if isPaused is updated properly
                            SignalPlay.notify();
                        }
                        advancedPlayer.play(currentMinute, Integer.MAX_VALUE);
                    }else{
                        advancedPlayer.play();
                    }
                    //play music
                    //advancedPlayer.play();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void beginPlayBackSliderFunction(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //to prevent multiple threads updating the slider at the same time
                while(!isPaused && !songFinished && !pressedNext && !pressedPrev){
                    try {
                        currentTime++;

                        //calculate into frame value
                        //2.08 to increase its accuracy as slider wasn't representing the true point of
                        // where the song actually was
                        //I compared the getFrame() with the extracted frame and there was inaccuracy
                        int extractedFrame = (int) ((double) currentTime * 2.08 * currentSong.getFrameRatePerMilliSecond());

                        //update the GUI
                        musicPlayerGUI.setPlaybackSliderValue(extractedFrame);

                        Thread.sleep(1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        //called when song starts
        System.out.println("Playback started");
        songFinished = false;
        pressedNext = false;
        pressedPrev = false;
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        //called when song finishes or closed
        System.out.println("Play back finished");

        System.out.println("Stopped song at: " + evt.getFrame());

        if(isPaused){
            //when we press pause, the value starts from 0 again instead of where we left off which is why we put the
            //+= operator
            currentMinute += (int) ((double) evt.getFrame() * currentSong.getFrameRatePerMilliSecond());
        } else{
            //if user pressed next or prev then we don't need to execute rest of code
            if(pressedNext || pressedPrev) return;
            songFinished = true;

            if(playList == null){
                //update gui
                musicPlayerGUI.enablePlayButtonDisablePauseButton();
            } else{
                //last song in playList
                if(currentPlaylistIndex == playList.size()-1){
                    //update gui
                    musicPlayerGUI.enablePlayButtonDisablePauseButton();
                } else{
                    nextSong();
                }
            }
        }

    }
}
