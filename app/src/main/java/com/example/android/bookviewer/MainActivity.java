package com.example.android.bookviewer;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int BOOK_LOADAER_ID = 1;
    private final String Urlpostfix = "&maxResults=25&fields=items(volumeInfo/title,volumeInfo/authors,volumeInfo/imageLinks,volumeInfo/previewLink,searchInfo/textSnippet)";
    ProgressBar progressBar;
    ListView listView;
    TextView emptyView ;
    Button search ;
    EditText editText;
    private BookAdapter bookAdapter;
    private String createSearchUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         progressBar = (ProgressBar) findViewById(R.id.progress);
         listView= (ListView) findViewById(R.id.list_item);
         emptyView = (TextView) findViewById(R.id.empty_view);
         search = (Button) findViewById(R.id.button);
         editText = (EditText) findViewById(R.id.edit_text);

        setupUI();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Getting List of Books...", Toast.LENGTH_SHORT).show();
                    emptyView.setText("Enter name of book and hit on search");
                    progressBar.setVisibility(View.VISIBLE);
                    String inputString = editText.getText().toString();
                    createSearchUrl = createSearchUrl(inputString);
                    getLoaderManager().restartLoader(BOOK_LOADAER_ID, null, MainActivity.this);
                } else {
                    bookAdapter.clear();
                    emptyView.setText("No Internet Conncetion");
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Book currentBook = bookAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getPreviewUrl());
                Intent website = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(website);
            }
        });
    }


    private void setupUI() {
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(bookAdapter);
        emptyView.setText("Enter name of book and hit on search");
        listView.setEmptyView(emptyView);
        getLoaderManager().initLoader(BOOK_LOADAER_ID, null, this);
    }

    private String createSearchUrl(String inputString) {
        StringBuilder url = new StringBuilder("https://www.googleapis.com/books/v1/volumes?q=");
        return url.append(inputString.replace(" ", "+")).append(Urlpostfix).toString();
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, createSearchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar.setVisibility(View.GONE);
        bookAdapter.clear();
        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }


}
