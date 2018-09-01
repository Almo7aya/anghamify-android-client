package com.alialmohaya.anghamifydemo.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alialmohaya.anghamifydemo.Activities.SongViewActivity;
import com.alialmohaya.anghamifydemo.Config;
import com.alialmohaya.anghamifydemo.MainActivity;
import com.alialmohaya.anghamifydemo.Models.SearchSongItem;
import com.alialmohaya.anghamifydemo.R;
import com.alialmohaya.anghamifydemo.Tasks.DownloadImageTask;

import org.json.JSONObject;

import java.io.IOException;

public class SongItemLayoutInflater extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private SearchSongItem[] songsList;

    public SongItemLayoutInflater(Context context, SearchSongItem[] songsList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.songsList = songsList;
    }

    @Override
    public int getCount() {
        return this.songsList.length;
    }

    @Override
    public Object getItem(int position) {
        return this.songsList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = this.layoutInflater.inflate(R.layout.song_item_list, null);
        TextView name = convertView.findViewById(R.id.txtSongItemName);
        TextView artist = convertView.findViewById(R.id.txtSongItemArtist);
        TextView album = convertView.findViewById(R.id.txtSongItemAlbum);
        ImageView imageView = convertView.findViewById(R.id.imgSongItemArtwork);

        if (this.songsList[position].getCoverArt() != null) {
            String imageUrl =
                    Config.IMAGES_WEB_STORAGE +
                            "?id=" + this.songsList[position].getCoverArt() +
                            "&size=" + Config.IMAGES_SEARCH_SIZE;
            (new DownloadImageTask(imageView)).execute(imageUrl);
        }

        name.setText(this.songsList[position].getTitle());
        artist.setText(this.songsList[position].getArtist());
        album.setText(this.songsList[position].getAlbum());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SongItemLayoutInflater.this.context,
                        "Item " + SongItemLayoutInflater.this.songsList[position].getId() + " Clicked",
                        Toast.LENGTH_LONG).show();
                Intent moveToSongViewIntent = new Intent(context, SongViewActivity.class);
                moveToSongViewIntent.putExtra("songId", songsList[position].getId());
                SongItemLayoutInflater.this.context.startActivity(moveToSongViewIntent);
            }
        });

        return convertView;
    }
}
