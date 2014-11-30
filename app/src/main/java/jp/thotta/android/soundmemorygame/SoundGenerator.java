package jp.thotta.android.soundmemorygame;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by thotta on 14/11/21.
 */
public class SoundGenerator {
    byte[][] audioData = new byte[16][];
    byte[] emptyData;
    byte[] booData;
    AudioTrack track;
    final static int SAMPLE_RATE = 44100;
    final static int SOUND_VOLUME = 5;
    final static int BUFFER_SIZE = (int) Math.ceil(SAMPLE_RATE/2);

    public SoundGenerator() {
        createData();
        createAudioTrack();
    }

    synchronized public void play(int idx) {
        idx = idx % audioData.length;
        track.play();
        track.write(audioData[idx], 0, BUFFER_SIZE);
        track.write(emptyData, 0, BUFFER_SIZE);
    }

    synchronized public void playChimeMelody() {
        track.play();
        track.write(audioData[7], 0, BUFFER_SIZE);
        track.write(audioData[6], 0, BUFFER_SIZE);
        track.write(audioData[5], 0, BUFFER_SIZE);
        track.write(audioData[4], 0, BUFFER_SIZE);
        track.write(audioData[7], 0, BUFFER_SIZE);
        track.write(audioData[6], 0, BUFFER_SIZE);
        track.write(audioData[5], 0, BUFFER_SIZE);
        track.write(audioData[4], 0, BUFFER_SIZE);
        track.write(audioData[7], 0, BUFFER_SIZE*2);
        track.write(audioData[9], 0, BUFFER_SIZE*2);
        track.write(audioData[8], 0, BUFFER_SIZE*2);
        track.write(emptyData, 0, BUFFER_SIZE);
    }

    synchronized public void playBoo() {
        track.play();
        track.write(booData, 0, BUFFER_SIZE/2);
        track.write(emptyData, 0, BUFFER_SIZE/2);
        track.write(booData, 0, BUFFER_SIZE*2);
    }

    void createAudioTrack() {
        track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE,
                AudioTrack.MODE_STREAM
        );
    }

    void createData() {
        emptyData = createEmptySound();
        booData = createSquareWave(200, SOUND_VOLUME * 3);
        audioData[0] = createSquareWave(523.25);
        audioData[1] = createSquareWave(587.33);
        audioData[2] = createSquareWave(659.26);
        audioData[3] = createSquareWave(698.456);
        audioData[4] = createSquareWave(783.991);
        audioData[5] = createSquareWave(880.000);
        audioData[6] = createSquareWave(987.767);
        audioData[7] = createSquareWave(1046.5);
        audioData[8] = createSquareWave(1174.66);
        audioData[9] = createSquareWave(1318.51);
        audioData[10] = createSquareWave(1396.91);
        audioData[11] = createSquareWave(1567.98);
        audioData[12] = createSquareWave(1760);
        audioData[13] = createSquareWave(1975.53);
        audioData[14] = createSquareWave(2093);
        audioData[15] = createSquareWave(2349.32);
    }

    private byte[] createSquareWave(double frequency, int volume) {
        byte[] b = new byte[SAMPLE_RATE];
        for (int i = 0; i < b.length; i++) {
            double r = i / (SAMPLE_RATE / frequency);
            b[i] = (byte)((Math.round(r) % 2 == 0) ? volume : -volume);
        }
        return b;
    }
    private byte[] createSquareWave(double frequency) {
        return createSquareWave(frequency, SOUND_VOLUME);
    }

    private byte[] createEmptySound() {
        byte[] b = new byte[SAMPLE_RATE];
        for(int i = 0; i < b.length; i++) {
            b[i] = (byte)0;
        }
        return b;
    }
}
