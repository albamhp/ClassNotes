package com.example.albam.classnote.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



public class CustomAdapter extends ArrayAdapter<String> {

    private int mResource;
    private Context mContext;
    private LayoutInflater mInflater;

    public CustomAdapter(Context context, @LayoutRes int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.mResource = resource;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView,
                                        ViewGroup parent, int resource) {
        String item = getItem(position);
        if (item.matches(".+-.+_[^-_]+_\\d+-\\d+-\\d+\\.\\w+")) {
            String[] split = item.split("_");

            LinearLayout general = new LinearLayout(mContext);
            general.setOrientation(LinearLayout.VERTICAL);
            general.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            general.setPadding(30, 30, 30, 30);

            LinearLayout horizontal = new LinearLayout(mContext);
            horizontal.setOrientation(LinearLayout.HORIZONTAL);
            horizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            horizontal.setWeightSum(1f);

            TextView name = new TextView(mContext);
            name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            name.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
            String nameText = "";
            for (int i = 0; i < split.length - 2; i++) {
                nameText += split[i];
            }
            name.setText(nameText);

            TextView category = new TextView(mContext);
            category.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            category.setTextAppearance(mContext, android.R.style.TextAppearance_Small);
            category.setText(split[split.length - 2]);

            TextView date = new TextView(mContext);
            date.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            date.setTextAppearance(mContext, android.R.style.TextAppearance_Small);
            date.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
            date.setText(split[split.length - 1].substring(0, split[split.length - 1].lastIndexOf(".")));

            general.addView(name);
            horizontal.addView(category);
            horizontal.addView(date);
            general.addView(horizontal);

            return general;

        } else {
            View view = inflater.inflate(resource, parent, false);
            TextView text;
            try {
                text = (TextView) view;
            } catch (ClassCastException e) {
                Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException(
                        "ArrayAdapter requires the resource ID to be a TextView", e);
            }
            text.setText(item);
            return view;
        }
    }

}
