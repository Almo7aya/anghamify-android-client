package com.alialmohaya.anghamifydemo.Activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alialmohaya.anghamifydemo.Adapters.ResponseAdapter;
import com.alialmohaya.anghamifydemo.Config;
import com.alialmohaya.anghamifydemo.MainActivity;
import com.alialmohaya.anghamifydemo.Models.SongDownloadManger;
import com.alialmohaya.anghamifydemo.Models.SongViewModel;
import com.alialmohaya.anghamifydemo.R;
import com.alialmohaya.anghamifydemo.Tasks.DownloadImageTask;
import com.alialmohaya.anghamifydemo.Utils.HTTPClient;
import com.alialmohaya.anghamifydemo.Utils.PermissionManger;
import com.alialmohaya.anghamifydemo.Utils.Shared;

import java.io.File;
import java.io.IOException;

public class SongViewActivity extends AppCompatActivity {

    private String songId;

    private ImageView imgSongViewArtwork;
    private TextView txtSongViewName;
    private TextView txtSongViewAlbum;
    private Button btnSongViewDownload;
    private ProgressBar spinner;
    private DownloadManager downloadManager;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManger.WRITE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Config.BASE_APP_FOLDER_NAME), "/");
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_view);
        this.imgSongViewArtwork = findViewById(R.id.imgSongViewArtwork);
        this.txtSongViewName = findViewById(R.id.txtSongViewName);
        this.txtSongViewAlbum = findViewById(R.id.txtSongViewAlbum);
        this.spinner = findViewById(R.id.progSongView);
        this.btnSongViewDownload = findViewById(R.id.btnSongViewDownload);
        this.btnSongViewDownload.setVisibility(View.GONE);

        this.downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        // get song id form the previous activity
        songId = getIntent().getStringExtra("songId");

        if (Integer.parseInt(this.songId) < 1) {
            Toast.makeText(SongViewActivity.this, "Song Id is invalid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String songViewUrl = Config.SONGVIEW_API_ENDPOINT + "?type=song&objectid=" + songId;
        (new GetSongViewDataTask()).execute(songViewUrl);
    }

    private void setSongViewData(final SongViewModel songViewData) {
        this.txtSongViewName.setText(songViewData.getTitle());
        this.txtSongViewAlbum.setText(songViewData.getAlbum());
        String imageUrl = Config.IMAGES_WEB_STORAGE + "?id=" + songViewData.getCoverArt() +
                "&size=" + Config.IMAGES_SEARCH_SIZE * 2;
        Log.i("anghamix", imageUrl);

        (new DownloadImageTask(this.imgSongViewArtwork)).execute(imageUrl);

        if (songViewData.isCached()) {
            this.btnSongViewDownload.setVisibility(View.VISIBLE);
        } else {
            String cacheUrl = Config.CACHE_API_ENDPOINT + "?songid=" + songViewData.getId();
            Log.i("anghamix", cacheUrl);
            (new RequestSongCacheTask()).execute(cacheUrl);
        }

        this.btnSongViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionManger.checkWritePremission(SongViewActivity.this)) {
                    return;
                }
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Config.BASE_API_URL + songViewData.getUrl()));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle(songViewData.getTitle());
                request.setMimeType("audio/mp3");
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        "/anghamify/" + songViewData.getTitle() + ".mp3");
                SongViewActivity.this.downloadManager.enqueue(request);
            }
        });
    }

    private class GetSongViewDataTask extends AsyncTask<String, Void, SongViewModel> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SongViewActivity.this.txtSongViewName.setVisibility(View.GONE);
            SongViewActivity.this.txtSongViewAlbum.setVisibility(View.GONE);
            SongViewActivity.this.imgSongViewArtwork.setVisibility(View.GONE);

            SongViewActivity.this.spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected SongViewModel doInBackground(String... url) {
            try {
                String httpResponse = HTTPClient.getResponseString(url[0], Shared.Method.GET);
                return ResponseAdapter.songView(httpResponse);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("anghamix", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(SongViewModel songViewModel) {
            super.onPostExecute(songViewModel);
            if (songViewModel == null) {
                Toast.makeText(SongViewActivity.this, "NULLLL", Toast.LENGTH_SHORT).show();
                return;
            }
            SongViewActivity.this.setSongViewData(songViewModel);

            SongViewActivity.this.txtSongViewName.setVisibility(View.VISIBLE);
            SongViewActivity.this.txtSongViewAlbum.setVisibility(View.VISIBLE);
            SongViewActivity.this.imgSongViewArtwork.setVisibility(View.VISIBLE);

            if (songViewModel.isCached()) {
                SongViewActivity.this.spinner.setVisibility(View.GONE);
            }
        }
    }

    private class RequestSongCacheTask extends AsyncTask<String, Void, SongDownloadManger> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SongViewActivity.this, "Song is caching ...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected SongDownloadManger doInBackground(String... url) {
            try {
                String httpResponse = HTTPClient.getResponseString(url[0], Shared.Method.GET);
                Log.i("anghamix", httpResponse);
                return ResponseAdapter.songDownload(httpResponse);
            } catch (IOException e) {
                Log.i("anghamix", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(SongDownloadManger songDownloadManger) {
            super.onPostExecute(songDownloadManger);
            SongViewActivity.this.btnSongViewDownload.setVisibility(View.VISIBLE);
            SongViewActivity.this.spinner.setVisibility(View.GONE);
        }
    }
}
