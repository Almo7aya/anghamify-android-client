package com.alialmohaya.anghamifydemo.Adapters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.alialmohaya.anghamifydemo.Activities.SongViewActivity;
import com.alialmohaya.anghamifydemo.Models.SearchQueryModel;
import com.alialmohaya.anghamifydemo.Models.SearchSongItem;
import com.alialmohaya.anghamifydemo.Models.SongDownloadManger;
import com.alialmohaya.anghamifydemo.Models.SongViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseAdapter {

    public static SearchQueryModel searchQuery(String stringJSON) {
        try {
            JSONArray jsonArray = new JSONArray(stringJSON);
            Log.i("anghamix", "Found a : " + jsonArray.length() + " items");
            return ResponseAdapter.fromJSONArrayToSeachQuery(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("anghamix", e.getMessage());
        }
        return null;
    }

    public static SongViewModel songView(String stringJSON) {
        try {
            SongViewModel songViewModel = new SongViewModel();
            JSONObject jsonObject = new JSONObject(stringJSON);

            songViewModel.setTitle(jsonObject.getString("title"));
            songViewModel.setAlbum(jsonObject.getString("album"));
            songViewModel.setArtist(jsonObject.getString("artist"));
            songViewModel.setCoverArt(jsonObject.getString("coverArt"));
            songViewModel.setId(jsonObject.getString("id"));
            songViewModel.setType(jsonObject.getString("type"));
            songViewModel.setLocation(jsonObject.getString("location"));

            if (!jsonObject.getString("cached").equals("null")) {
                songViewModel.setCached(true);
                songViewModel.setUrl(jsonObject.getString("url"));
            } else {
                songViewModel.setCached(false);
                songViewModel.setUrl("");
            }

            return songViewModel;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("anghamix", e.getMessage());
        }


        return null;
    }

    public static SongDownloadManger songDownload(String stringJSON) {

        try {
            JSONObject jsonObject = new JSONObject(stringJSON);
            SongDownloadManger songDownloadManger = new SongDownloadManger();
            songDownloadManger.setId(jsonObject.getString("id"));
            songDownloadManger.setUrl(jsonObject.getString("url"));
            return songDownloadManger;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static SearchSongItem fromJSONObjectToSearchSongItem(JSONObject jsonObject) {
        SearchSongItem searchSongItem = new SearchSongItem();
        try {
            searchSongItem.setTitle(jsonObject.getString("title"));
            searchSongItem.setAlbum(jsonObject.getString("album"));
            searchSongItem.setArtist(jsonObject.getString("artist"));
            searchSongItem.setCoverArt(jsonObject.getString("coverArt"));
            searchSongItem.setId(jsonObject.getString("id"));
            searchSongItem.setType(jsonObject.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchSongItem;
    }

    private static SearchQueryModel fromJSONArrayToSeachQuery(JSONArray jsonArray) {
        SearchQueryModel searchQueryModel = new SearchQueryModel();
        ArrayList<SearchSongItem> songItemArrayList = new ArrayList<>();
        for (int index = 0; index < jsonArray.length(); index++) {
            try {
                SearchSongItem songItem = ResponseAdapter.
                        fromJSONObjectToSearchSongItem(jsonArray.getJSONObject(index));
                songItemArrayList.add(songItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        searchQueryModel.setSongItems(songItemArrayList.toArray(new SearchSongItem[songItemArrayList.size()]));
        return searchQueryModel;
    }
}
