package com.kj.audiodemo;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by kangjie on 2018/8/6.
 */

public class PlayAudioActivity extends Activity {
    private static final String TAG = "MainActivity";
    private String path = "/storage/emulated/0/";
    private String name = "abc.pcm";
    private Button playBtn;
    private AudioTrack player;
    private int audioBufSize;
    private byte[] audioData;

    String  filePath  = path+name;//"/sdcard/testmusic.pcm";
    private byte[] abc;
    int offset;
    Player player1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        audioBufSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        player = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioBufSize,
                AudioTrack.MODE_STREAM);

        playBtn = (Button)findViewById(R.id.button1);

        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                player.play();

                player1=new Player();
                player1.start();


            }
        });
    }
    class Player extends Thread{
        byte[] data1=new byte[audioBufSize];
        File file=new File(filePath);
        int off1=0;
        FileInputStream fileInputStream;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            while(true){
                try {
                    fileInputStream=new FileInputStream(file);
                    fileInputStream.skip((long)off1);
                    fileInputStream.read(data1,0,audioBufSize);
                    off1 += audioBufSize;
                } catch (Exception e) {
                    // TODO: handle exception
                }
                player.write(data1, offset, audioBufSize );
            }
        }
    }
}