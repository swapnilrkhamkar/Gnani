//package com.assignment.gnani;
//
//import android.app.ProgressDialog;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.button.MaterialButton;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.IOException;
//
//public class AudioActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, View.OnClickListener {
//
//    private MediaPlayer mMediaplayer;
//    private MaterialButton btnPlay;
//    private boolean playPause;
//    private boolean initialStage = true;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_audio);
//
//        mMediaplayer = new MediaPlayer();
//        mMediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//        btnPlay = findViewById(R.id.btnPlay);
//        btnPlay.setOnClickListener(this);
//    }
//
////    private void fetchAudioUrlFromFirebase() {
////        final FirebaseStorage storage = FirebaseStorage.getInstance();
////        // Create a storage reference from our app
////        StorageReference storageRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/gnani-1564731656611.appspot.com/o/Imagine-Dragons-Thunder.mp3?alt=media&token=f0f1de61-4f56-45cf-9625-b3464c80e880");
////        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////            @Override
////            public void onSuccess(Uri uri) {
////                try {
////                    // Download url of file
////                    initialStage = true;
////                    playPause = false;
////                    mMediaplayer.stop();
////                    mMediaplayer.reset();
////
////                    final String url = uri.toString();
////                    mMediaplayer.setDataSource(url);
////                    // wait for media player to get prepare
////                    mMediaplayer.setOnPreparedListener(AudioActivity.this);
////                    mMediaplayer.prepareAsync();
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////            }
////        })
////                .addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        Log.e("TAG", e.getMessage());
////                    }
////                });
////
////    }
//
//    @Override
//    public void onPrepared(MediaPlayer mediaPlayer) {
//        mMediaplayer.start();
//        initialStage = false;
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//
//            case R.id.btnPlay:
//
//                if (mMediaplayer == null){
//
//                    mMediaplayer = new MediaPlayer();
//                    mMediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//                }
//
//                if (!playPause) {
//                    btnPlay.setText("Pause");
//
//                    if (initialStage) {
//                        fetchAudioUrlFromFirebase();
//                    } else {
//                        if (!mMediaplayer.isPlaying())
//                            mMediaplayer.start();
//                    }
//
//                    playPause = true;
//
//                } else {
//                    btnPlay.setText("Play");
//
//                    if (mMediaplayer.isPlaying()) {
//                        mMediaplayer.pause();
//                    }
//
//                    playPause = false;
//                }
//                break;
//        }
//
//
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (mMediaplayer != null) {
//            mMediaplayer.reset();
//            mMediaplayer.release();
//            mMediaplayer = null;
//        }
//    }
//    class Player extends AsyncTask<String, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            Boolean prepared = false;
//
//            try {
//                mediaPlayer.setDataSource(strings[0]);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        initialStage = true;
//                        playPause = false;
//                        btn.setText("Launch Streaming");
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                });
//
//                mediaPlayer.prepare();
//                prepared = true;
//
//            } catch (Exception e) {
//                Log.e("MyAudioStreamingApp", e.getMessage());
//                prepared = false;
//            }
//
//            return prepared;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//            if (progressDialog.isShowing()) {
//                progressDialog.cancel();
//            }
//
//            mediaPlayer.start();
//            initialStage = false;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog.setMessage("Buffering...");
//            progressDialog.show();
//        }
//    }
//}
