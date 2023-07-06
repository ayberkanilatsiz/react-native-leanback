package com.rs.leanbacknative.presenters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import androidx.core.content.ContextCompat;
import android.graphics.drawable.Drawable;
import android.graphics.Typeface;

// import org.jcp.xml.dsig.internal.dom.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.react.bridge.ReadableMap;
import com.rs.leanbacknative.models.Card;
import com.rs.leanbacknative.R;
import com.rs.leanbacknative.cardViews.DefaultImageCardView;
import com.rs.leanbacknative.utils.Constants;
import androidx.leanback.widget.Presenter;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.leanback.widget.VerticalGridView;
import android.widget.TextView;
import android.widget.ImageView;

public class CategoryPresenter extends Presenter {
    private Card mCard;
    protected Drawable mDefaultCardImage;
    public CategoryPresenter(ReadableMap attributes, Card card) {
        // initializeAttributes(attributes);
        mCard = card;
    }

    public CategoryPresenter(){
      
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_presenter, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.lb_fallback_bg);
        // view.setForegroundGravity(Gravity.TOP);
        params.width = 600;
        params.height = 150;
        return new ViewHolder(view);
    }

    
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        
        if (item instanceof Card) {
          Card card = (Card) item;
          TextView titleTextView = viewHolder.view.findViewById(R.id.channel_name);
        Typeface typeface = Typeface.createFromAsset(titleTextView.getContext().getAssets(), "fonts/Montserrat-SemiBold.ttf");
        titleTextView.setTypeface(typeface);

          titleTextView.setText(card.getTitle());
          ImageView imageView = viewHolder.view.findViewById(R.id.channel_icon);
        
            RequestOptions requestOptions = new RequestOptions().transform(new FitCenter());

            Glide.with(imageView.getContext())
                .load(card.getCardImageUrl())
                .apply(requestOptions)
                .error(mDefaultCardImage)
                .into(imageView);
        }
        
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        // cardView.setBadgeImage(null);
        // cardView.setMainImage(null);
    }
}
