package com.kj.audiodemo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kj.audiodemo.AudioCapturer.OnAudioFrameCapturedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {
    private AudioCapturer mCapturer;
    private String path = "/storage/emulated/0/";
    private String name = "abc.pcm";
    private File mFile;
    private ByteArrayOutputStream mByteArrayOutputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission" +
                        ".WEART_EXTERNAL_STORAGE"}, 1);
            }
        }

        mCapturer = new AudioCapturer();
        mFile = new File(path);

        mByteArrayOutputStream = new ByteArrayOutputStream();
        mCapturer.setOnAudioFrameCapturedListener(new OnAudioFrameCapturedListener() {
            @Override
            public void onAudioFrameCaptured(byte[] audioData) {
                try {
                    writeBytesToFile(audioData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public  void writeBytesToFile(byte[] data) throws IOException {
        RandomAccessFile randomFile = null;
        randomFile = new RandomAccessFile(new File(path+name), "rw");
   //     randomFile.write(data);
    //    OutputStream out = new FileOutputStream(path+name);
      //  InputStream is = new ByteArrayInputStream(data);
        // 文件长度，字节数
        long fileLength = randomFile.length();
        //将写文件指针移到文件尾。
        randomFile.seek(fileLength);
        randomFile.write(data);
        randomFile.close();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_take:
                take();
                break;
            case R.id.id_converter:
                PcmToWavUtil pcmToWavUtil = new PcmToWavUtil();
                String pcm = path+name;
                String wav = pcm.replace(".pcm",".wav");
                pcmToWavUtil.pcmToWav(pcm,wav);
                break;
            case R.id.id_delete:
                if (mFile!=null){
                    if (mFile.exists()){
                        mFile.delete();
                    }
                    String wavPath = mFile.getAbsolutePath().replace(".pcm",".wav");
                    File file = new File(wavPath);
                    if (file.exists()){
                        file.delete();
                    }

                }
                break;
            default:
                break;
        }
    }

    private void take() {
       if (!mCapturer.startCapture()){
           mCapturer.stopCapture();
       }
    }
}
