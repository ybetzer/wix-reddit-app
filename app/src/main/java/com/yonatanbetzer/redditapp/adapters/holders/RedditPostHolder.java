package com.yonatanbetzer.redditapp.adapters.holders;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.application.RedditApplication;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;
import com.yonatanbetzer.redditapp.utils.Constants;
import com.yonatanbetzer.redditapp.utils.PaletteListener;
import com.yonatanbetzer.redditapp.utils.Utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class RedditPostHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView titleView;
    private TextView timeView;
    private RedditThing redditThing;

    public RedditPostHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title_view);
        imageView = itemView.findViewById(R.id.image_view);
        timeView = itemView.findViewById(R.id.time_view);
    }

    public void bindTo(RedditThing item){
        if(imageView != null && titleView != null && timeView != null) {
            redditThing = item;
            imageView.setBackgroundColor(Utils.getRandomColor());

            if(item.getData() != null) {
                if (item.getData().getThumbnail() != null &&
                        item.getData().getThumbnail().length() > 6) {
                    imageView.setVisibility(View.VISIBLE);
                    Date createdDate = item.getData().getCreatedDate();
                    if (createdDate.getTime() > 100) {
                        DateFormat dateFormat = Utils.getDateFormat(createdDate);

                        String dateText = dateFormat.format(createdDate);
                        if(Utils.isToday(createdDate)) {
                            dateText = "Today @" + dateFormat.format(createdDate);
                        }
                        timeView.setText(dateText);
                        timeView.setTypeface(Constants.openSansRegularHebrew);
                        timeView.setVisibility(View.VISIBLE);
                    } else {
                        timeView.setVisibility(View.GONE);
                    }

                    String imageUrl = item.getData().getBestImageUrl();
                    if (imageUrl != null && imageUrl.length() > 6) {
                        Glide.with(RedditApplication.getAppContext())
                                .load(imageUrl)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Utils.getImageMainColor(((BitmapDrawable) resource).getBitmap(), new PaletteListener() {
                                            @Override
                                            public void paletteExtracted(Palette palette) {
                                                if (titleView != null && palette != null) {
                                                    titleView.setBackgroundColor(palette.getLightVibrantColor(Color.WHITE));
                                                }
                                                if (timeView != null && palette != null) {
                                                    timeView.setBackgroundColor(palette.getDarkVibrantColor(Color.BLACK));
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                })
                                .into(imageView);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                } else {
                    timeView.setVisibility(View.GONE);
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
