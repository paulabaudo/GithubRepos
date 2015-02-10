package com.globant.paulabaudo.githubrepos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by paula.baudo on 10/02/2015.
 */
public class GithubRepoAdapter extends ArrayAdapter<GithubRepo> {

    List<GithubRepo> mRepos;

    public class ViewHolder {
        public final TextView textViewName;
        public final TextView textViewDescription;

        public ViewHolder(View view){
            textViewName = (TextView) view.findViewById(R.id.text_view_repo_name);
            textViewDescription = (TextView) view.findViewById(R.id.text_View_repo_description);
        }
    }

    public GithubRepoAdapter(Context context, List<GithubRepo> repos) {
        super(context, R.layout.list_item_repo, repos);
        mRepos = repos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView!=null){
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_repo, parent, false);
        }
        displayRepoInRow(position, rowView);
        return rowView;
    }

    private void displayRepoInRow(int position, View rowView) {
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.textViewName.setText(mRepos.get(position).getName());
        viewHolder.textViewDescription.setText(mRepos.get(position).getDescription());
    }
}
