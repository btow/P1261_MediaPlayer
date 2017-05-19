package com.example.samsung.p1261_mediaplayer;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.IOException;

import static android.media.MediaPlayer.*;

public class MainActivity extends AppCompatActivity
        implements OnPreparedListener, OnCompletionListener {

    private final String DATA_HTTP = "https://yadi.sk/d/D71qyCDj3JKKgu",
                         DATA_STREAM = "http://online.radiorecord.ru:8101/rr_128",
                         DATA_SD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                                 + "/music.mp3";
    private final Uri DATA_URI = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 13359
    );

    private MediaPlayer mp;
    private AudioManager am;
    private CheckBox chbLoop;
    private String message = "MainActivity.onClickStart(), ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        chbLoop = (CheckBox) findViewById(R.id.chbLoop);
        chbLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mp != null) {
                    mp.setLooping(isChecked);
                }
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        String msg = message + "onPrepared()";
        Messager.sendToAllRecipients(getBaseContext(), msg);
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        String msg = message + "onCompletion()";
        Messager.sendToAllRecipients(getBaseContext(), msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    public void onClickStart(View view) {

        releaseMP();

        try {
            switch (view.getId()) {

                case R.id.btnStartHttp :
                    String msg = message + "start HTTP";
                    Messager.sendToAllRecipients(getBaseContext(), msg);
                    mp = new MediaPlayer();
                    mp.setDataSource(DATA_HTTP);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setOnPreparedListener(this);
                    msg = message + "prepareAsync()";
                    Messager.sendToAllRecipients(getBaseContext(), msg);
                    mp.prepareAsync();
                    break;
                case R.id.btnStartStream :
                    msg = message + "start Stream";
                    Messager.sendToAllRecipients(getBaseContext(), msg);
                    mp = new MediaPlayer();
                    mp.setDataSource(DATA_STREAM);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setOnPreparedListener(this);
                    msg = message + "prepareAsync()";
                    Messager.sendToAllRecipients(getBaseContext(), msg);
                    mp.prepareAsync();
                    break;
                case R.id.btnStartSD :
                    msg = message + "start SD";
                    Messager.sendToAllRecipients(getBaseContext(), msg);
                    mp = new MediaPlayer();
                    mp.setDataSource(DATA_SD);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setOnPreparedListener(this);
                    mp.prepare();
                    mp.start();
                    break;
                case R.id.btnStartUri :
                    msg = message + "start URI";
                    Messager.sendToAllRecipients(getBaseContext(), msg);
                    mp = new MediaPlayer();
                    mp.setDataSource(this, DATA_URI);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setOnPreparedListener(this);
                    mp.prepare();
                    mp.start();
                    break;
                case R.id.btnStartRaw :
                    msg = message + "start Raw";
                    Messager.sendToAllRecipients(getBaseContext(), msg);
                    mp = MediaPlayer.create(this, R.raw.kalimba);
                    mp.start();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mp == null) return;

        mp.setLooping(chbLoop.isChecked());
        mp.setOnCompletionListener(this);
    }

    private void releaseMP() {

        if (mp != null) {
            try {
                mp.release();
                mp = null;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View view) {

        if (mp == null) return;

        switch (view.getId()) {

            case R.id.btnPause :
                if (mp.isPlaying()) {
                    mp.pause();
                }
                break;
            case R.id.btnResume :
                if (!mp.isPlaying()) {
                    mp.start();
                }
                break;
            case R.id.btnStop :
                mp.stop();
                break;
            case R.id.btnBackward :
                mp.seekTo(mp.getCurrentPosition() - 3000);
                break;
            case R.id.btnForward :
                mp.seekTo(mp.getCurrentPosition() + 3000);
                break;
            case R.id.btnInfo :
                String msg = "Playing " + mp.isPlaying() + "\n"
                        + "Time " + mp.getCurrentPosition() + "/"
                        + mp.getDuration() + "\n"
                        + "Looping " + mp.isLooping() + "\n"
                        + "Volume " + am.getStreamVolume(AudioManager.STREAM_MUSIC);
                Messager.sendToAllRecipients(getBaseContext(), msg);
                break;
            default:
                break;
        }
    }
}
