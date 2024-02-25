import com.mpatric.mp3agic.Mp3File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

public class Song {
    //used to describe a song
    private String songTitle, songArtist, songLength, filePath;
    private Mp3File mp3File;
    private double frameRatePerMilliSecond;

    public Song(String filePath){
        this.filePath = filePath;
        try {

            mp3File = new Mp3File(filePath);
            frameRatePerMilliSecond = (double) mp3File.getFrameCount()/mp3File.getLengthInMilliseconds();
            songLength = getSongLengthInString();

            //use jaudioTagger library to create audio file object to read mp3 info
            AudioFile audioFile = AudioFileIO.read(new File(filePath));

            //read through metadata of audio file
            //contains meta information like artist, title and etc. of audio file
            Tag tag = audioFile.getTag();
            if (tag != null){
                songTitle = tag.getFirst(FieldKey.TITLE);
                songArtist = tag.getFirst(FieldKey.ARTIST);
            } else {
                //could not get through metadata of audio file
                songTitle = "N/A";
                songArtist = "N/A";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getSongLengthInString(){
        long lengthInMinutes = mp3File.getLengthInSeconds() / 60;

        //return remainder of seconds
        long lengthInSeconds = mp3File.getLengthInSeconds() % 60;

        String lengthOfSongInString = String.format("%02d:%02d", lengthInMinutes, lengthInSeconds);

        return lengthOfSongInString;
    }

    //Getters
    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongLength() {
        return songLength;
    }

    public String getFilePath() {
        return filePath;
    }

    public Mp3File getMp3File() {
        return mp3File;
    }

    public double getFrameRatePerMilliSecond() {
        return frameRatePerMilliSecond;
    }
}
