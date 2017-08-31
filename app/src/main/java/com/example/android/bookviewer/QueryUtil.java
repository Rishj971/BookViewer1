package com.example.android.bookviewer;


import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.bookviewer.MainActivity.LOG_TAG;


public final class QueryUtil {

    static String  imageLink;

    private QueryUtil() {
    }

    //  Fetch booklist data from server
    public static List<Book> fetchBooks(String urlString) {
        URL url = createUrl(urlString);
        String jsonResponse = makeHttpRequest(url);
        return extractBooks(jsonResponse);
    }

    @Nullable
    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "problem with create URL", e);
            return null;
        }
        return url;
    }

    //  Make HTTP request and retrieve JSON string from server
    private static String makeHttpRequest(URL url) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                jsonResponse = getJsonFromStream(inputStream);
            } else {
                throw new IOException("HTTP error code: " + connection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "problem with makeHTTPrequest", e);
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return jsonResponse;
    }

    private static String getJsonFromStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();

        if (inputStream != null) {
            Reader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
        }
        return result.toString();
    }

    //  Parse JSON string to get book list
    public static ArrayList<Book> extractBooks(String BooksJSONString) {
        if (BooksJSONString == null) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(BooksJSONString);

            JSONArray jsonArray;
            if (jsonObject.has("items")) {
                jsonArray = jsonObject.getJSONArray("items");
            } else {
                return null;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentBook = jsonArray.getJSONObject(i);
                JSONObject volumeInfo;
                String bookTitle;
                String previewLink;
                String author;
             JSONObject  imageLinks ;


                if (currentBook.has("volumeInfo")) {
                    volumeInfo = currentBook.getJSONObject("volumeInfo");

                    bookTitle = volumeInfo.optString("title");
                    previewLink = volumeInfo.optString("previewLink");

                    if (volumeInfo.has("authors")) {
                        JSONArray authors = volumeInfo.getJSONArray("authors");
                        author = authors.optString(0);
                    } else {
                        author = "No author information";
                    }

                  // volumeInfo =  imageLinks.getString("thumbnail");
                    //imageLinks.getString("smallThumbnail");

                    if (volumeInfo.has("imagelinks"))
                    {
                        imageLinks = volumeInfo.getJSONObject("imageLinks");
                   imageLink = imageLinks.getString("smallThumbnail");


                    }


                } else {
                    bookTitle = null;
                    author = null;
                    previewLink = null;
                    //image = null;
                }

                String description;
                if (currentBook.has("searchInfo")) {
                    JSONObject searchInfo = null;
                    try {
                        searchInfo = currentBook.getJSONObject("searchInfo");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    description = searchInfo.optString("textSnippet");
                } else {
                    description = null;
                }

                books.add(new Book(previewLink, imageLink, bookTitle, author, description));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book volume JSON results", e);
        }
        return books;
    }
}

