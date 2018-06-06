package com.yonatanbetzer.redditapp.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.application.RedditApplication;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;
import com.yonatanbetzer.redditapp.utils.Constants;
import com.yonatanbetzer.redditapp.utils.Utils;

public class RedditPostHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView titleView;
    private RedditThing redditThing;

    public RedditPostHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title_view);
        imageView = itemView.findViewById(R.id.image_view);
    }

    public void bindTo(RedditThing item){
        if(imageView != null) {
            redditThing = item;
            imageView.setBackgroundColor(Utils.getRandomColor());

            if(item.getData() != null) {
                if (item.getData().getThumbnail() != null &&
                        item.getData().getThumbnail().length() > 6) {
                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(RedditApplication.getAppContext())
                            .load(item.getData().getBestImageUrl())
                            .into(imageView);
                } else {
                    imageView.setVisibility(View.GONE);
                }

                if(item.getData().getTitle() != null) {
                    titleView.setText(item.getData().getTitle());
                    titleView.setTypeface(Constants.openSansRegularHebrew);
                }
            }
        }
    }
}
