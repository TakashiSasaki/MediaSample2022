package com.example.mediasample;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer _player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._player = new MediaPlayer();

        final String mediaFileUriStr =
                "android.resource://" + getPackageName() + "/"
                        + R.raw.mountain_stream;

        Uri mediaFileUri = Uri.parse(mediaFileUriStr);

        try {
            _player.setDataSource(getApplicationContext(), mediaFileUri);
            _player.setOnPreparedListener(new PlayerPreparedListener()); //TODO
            _player.setOnCompletionListener(null); //TODO
            _player.prepareAsync(); //ここでmp3ファイルがロードされる
        } catch (IOException e) {
            e.printStackTrace();
        }//try
    }//onCreate method

    @Override
    protected void onDestroy() {
        if (_player.isPlaying()) {
            _player.stop();
        }//if

        //MediaPlayerが確保しているリソースを解放する。
        //メディアファイルのバッファリングに使ったメモリなども解放される。
        _player.release();

        _player = null; //デストラクタでの解放を促す
        //子クラスの終了処理が終わってから親クラスの終了処理
        super.onDestroy();
    }//onDestroy

    //リスナクラスをクラス内クラスとして実装する。

    //プレーヤーの再生準備が整った時のリスナクラス。
    private class PlayerPreparedListener
            implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //無駄な一時変数は使わない（場合による）
            findViewById(R.id.btPlay).setEnabled(true); //再生準備が整ったからね
            findViewById(R.id.btBack).setEnabled(true); //再生準備が整ったからね
            findViewById(R.id.btForward).setEnabled(true); //再生準備が整ったからね
        }//onPrepared method
    }//PlayerPreparedListener class

    //再生が終了したときのリスナクラス。
    private class PlayerCompletionListener
            implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            //再生終了時には再生開始を促すテキストにする
            ((Button) findViewById(R.id.btPlay))
                    .setText(R.string.bt_play_play);
        }//onCompletion method
    }//PlayerCompletionListener class

    //再歳ボタンがタップされた時の処理
    //レイアウトインフレーターから見つけてもらうために public にする
    public void onPlayButtonClick(View view) {
        Button btPlay = findViewById(R.id.btPlay);
        if (_player.isPlaying()) {
            //プレーヤーが再生中なら一時停止
            _player.pause();
            //一時停止中には再開を促すテキストにする
            btPlay.setText(R.string.bt_play_play);
        } else {
            //プレーヤーが再生中でなければ再生開始
            _player.start();
            //再生中は一時停止を促すテキストにする
            btPlay.setText(R.string.bt_play_pause);
        }//if
    }//onPlayButtonClick method


}// MainActivity class