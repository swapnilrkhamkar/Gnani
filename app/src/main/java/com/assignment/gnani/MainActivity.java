package com.assignment.gnani;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    private static final int RC_SIGN_IN = 707;
    private GoogleSignInClient mGoogleSignInClient;
    private ConstraintLayout constraintLayoutSignIn, constraintLayoutMusicPlayer;
    private MediaPlayer mediaPlayer;
    private MaterialButton btnPlay;
    private boolean playPause;
    private boolean initialStage = true;
    private int backButtonCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        constraintLayoutSignIn = findViewById(R.id.constraintLayoutSignIn);
        constraintLayoutMusicPlayer = findViewById(R.id.constraintLayoutMusicPlayer);
        btnPlay = findViewById(R.id.btnPlay);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        signInButton.setOnClickListener(MainActivity.this);
        FirebaseApp.initializeApp(MainActivity.this);
        btnPlay.setOnClickListener(MainActivity.this);

        constraintLayoutSignIn.setVisibility(View.VISIBLE);
        constraintLayoutMusicPlayer.setVisibility(View.GONE);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            updateUI(account);
        }
//        try {
//
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.assignment.gnani",
//                    PackageManager.GET_SIGNATURES);
//
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//           // e.printStackTrace();
//
//        } catch (NoSuchAlgorithmException e) {
//          //  e.printStackTrace();
//        } catch (Exception e) {
//           // e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sign_in_button:
                signIn();
                break;

            case R.id.btnPlay:

                if (!playPause) {
                    btnPlay.setText("PAUSE");

                    if (initialStage) {
                        fetchAudioUrlFromFirebase();
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                        initialStage = false;
                    }
                    playPause = true;
                } else {
                    btnPlay.setText("PLAY");

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }
                break;

        }

    }

    private void fetchAudioUrlFromFirebase() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/gnani-1564731656611.appspot.com/o/Imagine-Dragons-Thunder.mp3?alt=media&token=f0f1de61-4f56-45cf-9625-b3464c80e880");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    // Download url of file
                    initialStage = true;
                    playPause = false;
                    mediaPlayer.stop();
                    mediaPlayer.reset();

                    final String url = uri.toString();
                    mediaPlayer.setDataSource(url);
                    // wait for media player to get prepare
                    mediaPlayer.setOnPreparedListener(MainActivity.this);
                    mediaPlayer.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("signInFailed ", " " + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount googleSignInAccount) {

        Log.e("ACC", " " + googleSignInAccount);
        if (googleSignInAccount != null) {
            constraintLayoutSignIn.setVisibility(View.GONE);
            constraintLayoutMusicPlayer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        playPause = true;
        initialStage = false;
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //Songs.this.finish();
            MainActivity.this.onRestart();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Press the back button once again to close the application.", Snackbar.LENGTH_LONG);
            snackbar.show();
            backButtonCount++;
        }
    }
}
