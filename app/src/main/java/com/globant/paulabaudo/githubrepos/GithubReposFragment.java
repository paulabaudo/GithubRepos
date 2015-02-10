package com.globant.paulabaudo.githubrepos;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class GithubReposFragment extends ListFragment {

    EditText mEditTextUsername;
    TextView mTextViewRepos;
    final static String LOG_TAG = GithubReposFragment.class.getSimpleName();
    GithubRepoAdapter mAdapter;

    public GithubReposFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_github_repos, container, false);
        wireUpViews(rootView);
        prepareButton(rootView);
        return rootView;
    }

    private void prepareButton(View rootView) {
        Button buttonGetRepos = (Button) rootView.findViewById(R.id.button_get_repos);
        buttonGetRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEditTextUsername.getText().toString();
                displayToast(username);
                fetchReposInQueue(username);
            }

            private void displayToast(String username) {
                String message = String.format(getString(R.string.getting_repos_for_user),username);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareListView();
    }

    private void prepareListView() {
        List<GithubRepo> repos = new ArrayList<>();
        mAdapter = new GithubRepoAdapter(getActivity(), repos);
        setListAdapter(mAdapter);
    }

    private void fetchReposInQueue(String username){
        try {
            URL url = constructURLQuery(username);
            Request request = new Request.Builder().url(url.toString()).build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String responseString = response.body().string();
                    final List<GithubRepo> listOfRepos = parseResponse(responseString);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();
                            mAdapter.addAll(listOfRepos);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private URL constructURLQuery(String username) throws MalformedURLException {
        final String GITHUB_BASE_URL = "api.github.com";
        final String USERS_PATH = "users";
        final String REPOS_ENDPOINT = "repos";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority(GITHUB_BASE_URL).
                appendPath(USERS_PATH).
                appendPath(username).
                appendPath(REPOS_ENDPOINT);
        Uri uri = builder.build();
        Log.d(LOG_TAG, "Built URI: " + uri.toString());
        return new URL(uri.toString());
    }

    private List<GithubRepo> parseResponse(String response){
        final String REPO_NAME = "name";
        final String REPO_DESC = "description";
        final String REPO_URL = "html_url";
        List<GithubRepo> repos = new ArrayList<>();
        GithubRepo repo;
        try {
            JSONArray responseJsonArray = new JSONArray(response);
            JSONObject object;
            for (int i = 0; i < responseJsonArray.length(); i++){
                object = responseJsonArray.getJSONObject(i);
                repo = new GithubRepo();
                repo.setName(object.getString(REPO_NAME));
                repo.setDescription(object.getString(REPO_DESC));
                repo.setUrl(object.getString(REPO_URL));
                repos.add(repo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return repos;
    }

    private void wireUpViews(View rootView) {
        mEditTextUsername = (EditText) rootView.findViewById(R.id.edit_text_username);
        mTextViewRepos = (TextView) rootView.findViewById(R.id.text_view_repos);
    }

}
