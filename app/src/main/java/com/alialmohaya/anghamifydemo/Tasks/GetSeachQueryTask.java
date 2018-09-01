package com.alialmohaya.anghamifydemo.Tasks;

import android.os.AsyncTask;

import com.alialmohaya.anghamifydemo.Adapters.ResponseAdapter;
import com.alialmohaya.anghamifydemo.MainActivity;
import com.alialmohaya.anghamifydemo.Models.SearchQueryModel;
import com.alialmohaya.anghamifydemo.Utils.HTTPClient;
import com.alialmohaya.anghamifydemo.Utils.Shared;

import java.io.IOException;

public class GetSeachQueryTask extends AsyncTask<String, Void, SearchQueryModel> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected SearchQueryModel doInBackground(String... searchQuery) {
        return null;
    }
}
