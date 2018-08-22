package com.example.android.reader.ui;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.reader.R;
import com.example.android.reader.adapter.ArticleAdapter;
import com.example.android.reader.data.Article;
import com.example.android.reader.loader.ArticleLoader;

import android.app.LoaderManager;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private CoordinatorLayout layout;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LOAD_ID = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout =  findViewById(R.id.coordinator_layout);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new ArticleAdapter(this);
        recyclerView.setAdapter(adapter);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info!=null && info.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOAD_ID, null, this);
        }

        else {
            progressBar.setVisibility(View.GONE);
        }

    }


    public boolean isChecked(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info.isConnected() && info!=null){
            return true;
        }

        return false;
    }

    public void snack(){
        Snackbar snackbar = Snackbar.make(layout, "I hope you will enjoy!", Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        String link = "https://go.udacity.com/xyz-reader-json";
        return new ArticleLoader(this, link);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        progressBar.setVisibility(View.GONE);

        if (data!=null && !data.isEmpty()){
           adapter.bindData(data);
           snack();
        } else {
            Toast.makeText(this, getString(R.string.sorry), Toast.LENGTH_SHORT).show();
        } if (!isChecked()){
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Network is available!");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.bindData(null);

    }
}
