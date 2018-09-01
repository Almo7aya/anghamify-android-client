package com.alialmohaya.anghamifydemo.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.alialmohaya.anghamifydemo.Adapters.ResponseAdapter;
import com.alialmohaya.anghamifydemo.Config;
import com.alialmohaya.anghamifydemo.Models.SearchQueryModel;
import com.alialmohaya.anghamifydemo.R;
import com.alialmohaya.anghamifydemo.Utils.HTTPClient;
import com.alialmohaya.anghamifydemo.Utils.Shared;
import com.alialmohaya.anghamifydemo.Utils.SongItemLayoutInflater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity {
    private EditText edtMainSeachInput;
    private Button btnMainSearchStart;
    private ListView lstViewMainSearchSongList;
    private ProgressBar spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.lstViewMainSearchSongList = findViewById(R.id.lstViewMainSearchSongList);
        this.btnMainSearchStart = findViewById(R.id.btnMainSearchStart);
        this.edtMainSeachInput = findViewById(R.id.edtMainSeachInput);
        this.spinner = findViewById(R.id.progMain);

        btnMainSearchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edtMainSeachInput.getText().toString();
                query = Config.SEARCH_API_ENDPOINT + "?query=" + query;
                (new QueryASearchTask()).execute(query);
            }
        });

    }

    private class QueryASearchTask extends AsyncTask<String, Void, SearchQueryModel> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SearchActivity.this.spinner.setVisibility(View.VISIBLE);
            SearchActivity.this.lstViewMainSearchSongList.setVisibility(View.INVISIBLE);
        }

        @Override
        protected SearchQueryModel doInBackground(String... searchQuery) {
            SearchQueryModel searchQueryModel = null;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("query", searchQuery[0].substring(searchQuery[0].indexOf("=") + 1));
                Log.i("anghamix", jsonObject.toString());

                String response = HTTPClient.getResponseString(searchQuery[0], Shared.Method.POST, jsonObject);
                searchQueryModel = ResponseAdapter.searchQuery(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return searchQueryModel;
        }

        @Override
        protected void onPostExecute(SearchQueryModel searchQueryModel) {
            super.onPostExecute(searchQueryModel);
            if (searchQueryModel == null) {
                Log.i("anghamix", "NULLLLL");
                return;
            }
            SongItemLayoutInflater songItemLayoutInflater = new SongItemLayoutInflater(SearchActivity.this,
                    searchQueryModel.getSongItems());
            SearchActivity.this.lstViewMainSearchSongList.setAdapter(songItemLayoutInflater);
            SearchActivity.this.spinner.setVisibility(View.GONE);
            SearchActivity.this.lstViewMainSearchSongList.setVisibility(View.VISIBLE);
        }
    }
}
