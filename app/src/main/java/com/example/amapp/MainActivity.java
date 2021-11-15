package com.example.amapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ResultsAdapter.ItemClickListener, View.OnKeyListener {
    SearchView searchView;
    RecyclerView rvAutoCompleteListView;
    ResultsAdapter rAdapter;
    ArrayList<Results> autoCompleteList = new ArrayList<>();
    ArrayList<ResponseResult> responseResultsList = new ArrayList<>();
    ArrayList<Results> itemClickedList = new ArrayList<>();
    LinearLayout progressbarView;
    String query, text, artistName;
    int artistId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: style view!
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.searchView);
        rvAutoCompleteListView = findViewById(R.id.autoCompleteListView);
        progressbarView = findViewById(R.id.progressbarView);
        progressbarView.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(this);
        searchView.requestFocus();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
            try{
                new GetSearchResults().execute(query);
            }catch (Exception ex){
                System.out.println("Error: " + ex.getMessage());
            }

        }
        //TODO: add unit test
        //TODO: add internet connectivity check & toast if not connected
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        text = queryText;
        try{
            new GetSearchResults().execute(text);

        }catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }

        return false;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB) {
            switch (view.getId()) {
                case R.id.searchView:
                    query = searchView.getQuery().toString();
                    System.out.println(query);
                    try{
                        new GetSearchResults().execute(query);
                    }catch (Exception ex){
                        System.out.println("Error: " + ex.getMessage());
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    //TODO: add filter to URL params - LHS Brackets?
    public class GetSearchResults extends AsyncTask<String, String, ResponseResult> {
        String searchURL = "https://itunes.apple.com/search?term=" + text + "&entity=allArtist&attribute=allArtistTerm&limit=10";

        @Override
        protected ResponseResult doInBackground(String... strings) {
            ResponseResult responseResult = null;
            try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
                final HttpGet httpget = new HttpGet(searchURL);
                System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());
                Thread.sleep(3000);
                // Create a custom response handler
                final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {
                    @Override
                    public String handleResponse(final ClassicHttpResponse response) throws IOException {
                        final int status = response.getCode();
                        if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                            final HttpEntity entity = response.getEntity();
                            try {
                                return entity != null ? EntityUtils.toString(entity) : null;
                            } catch (final ParseException ex) {
                                throw new ClientProtocolException(ex);
                            }
                        } else {
                            throw new ClientProtocolException("Unexpected response status: " + status);
                        }
                    }

                };

                final String responseBody = httpclient.execute(httpget, responseHandler);

                String responseTrim = responseBody.replaceAll("[\\n\\t ]", "");
                ObjectMapper om = new ObjectMapper();
                responseResult = om.readValue(responseTrim, ResponseResult.class);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            return responseResult;
        }

        @Override
        protected void onPreExecute() {
            progressbarView.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        protected void onPostExecute(ResponseResult responseResult) {
            if(responseResult == null){
                Toast.makeText(getApplicationContext(),"No results to display!", Toast.LENGTH_LONG).show();
                progressbarView.setVisibility(View.GONE);
                searchView.setQuery("", false);
                searchView.clearFocus();
                autoCompleteList.clear();
            } else {
                progressbarView.setVisibility(View.GONE);
                generateResultsList(responseResult);
            }
        }
    }

    private void generateResultsList(ResponseResult responseResult){
        //TODO: fix spacing between words in rv
        responseResultsList.clear();
        responseResultsList.add(responseResult);
        for(int i = 0; i < responseResultsList.size(); i++){
            if(responseResultsList.get(i).getResults() != null){
                autoCompleteList.addAll(responseResultsList.get(i).getResults());
            }
        }
        RecyclerView recyclerView = findViewById(R.id.autoCompleteListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rAdapter = new ResultsAdapter(this, autoCompleteList);
        rAdapter.setClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(rAdapter);
        rAdapter.notifyDataSetChanged();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent o = new Intent(MainActivity.this, ResultsActivity.class);
        itemClickedList.clear();
        itemClickedList.add(autoCompleteList.get(position));
        artistName = autoCompleteList.get(position).getArtistName();
        artistId = autoCompleteList.get(position).getArtistId();
        o.putExtra("artistName", artistName);
        o.putExtra("artistId", artistId);
        startActivity(o);
        //TODO: clear rv & sv when navigating to next view
    }

}