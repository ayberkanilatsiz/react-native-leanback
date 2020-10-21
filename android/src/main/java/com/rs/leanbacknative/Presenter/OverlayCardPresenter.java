package com.rs.leanbacknative.Presenter;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.rs.leanbacknative.Model.NativeRowItem;
import com.rs.leanbacknative.R;
import com.rs.leanbacknative.Widget.NativeImageOverlayView;


public class OverlayCardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static final int DEFAULT_CARD_WIDTH = 313;
    private static final int DEFAULT_CARD_HEIGHT = 176;

    private Drawable mDefaultCardImage;

    private Integer mCardWidth = DEFAULT_CARD_WIDTH;
    private Integer mCardHeight = DEFAULT_CARD_HEIGHT;
    private ReadableArray mForbiddenFocusDirections;
    private int nextFocusUpId = -1;
    private int nextFocusDownId = -1;
    private int mBorderRadius;

    public OverlayCardPresenter(ReadableMap attributes) {
        mCardWidth = Math.round(PixelUtil.toPixelFromDIP(attributes.getInt("width")));
        mCardHeight = Math.round(PixelUtil.toPixelFromDIP(attributes.getInt("height")));
        mForbiddenFocusDirections = attributes.hasKey("forbiddenFocusDirections") ? attributes.getArray("forbiddenFocusDirections") : null;
        nextFocusUpId = attributes.getInt("nextFocusUpId");
        nextFocusDownId = attributes.getInt("nextFocusDownId");
        mBorderRadius = attributes.getInt("borderRadius");
    }

    public OverlayCardPresenter() { }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.lb_ic_sad_cloud);
        NativeImageOverlayView cardView =  new NativeImageOverlayView(parent.getContext());
        cardView.buildImageCardView();

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        NativeRowItem rowItem = (NativeRowItem) item;
        NativeImageOverlayView cardView = (NativeImageOverlayView) viewHolder.view;

        if (cardView.getId() == -1)
            cardView.setId(rowItem.getViewId());

        if (mForbiddenFocusDirections != null)
            CardUtils.setForbiddenFocusDirections(mForbiddenFocusDirections, cardView);

        if (nextFocusUpId != -1)
            cardView.setNextFocusUpId(nextFocusUpId);

        if (nextFocusDownId != -1)
            cardView.setNextFocusDownId(nextFocusDownId);

        CardUtils.setupTextOverlay(cardView, rowItem);
        CardUtils.setupOverlayImage(cardView, rowItem);
        CardUtils.setupLiveAssetElements(cardView, rowItem);

        if (rowItem.getCardImageUrl() != null) {
            cardView.setLayoutDimensions(mCardWidth, mCardHeight);
            cardView.setMainImageDimensions(mCardWidth, mCardHeight);

            RequestOptions requestOptions = mBorderRadius != 0 ?
                    (new RequestOptions()).transform(new CenterCrop(), new RoundedCorners(mBorderRadius)) :
                    RequestOptions.fitCenterTransform();

            Glide.with(viewHolder.view.getContext())
                .load(rowItem.getCardImageUrl())
                .apply(requestOptions)
                .error(mDefaultCardImage)
                .into(cardView.getMainImageView());

            Glide.with(viewHolder.view.getContext())
                    .load(rowItem.getOverlayImageUrl())
                    .apply(requestOptions)
                    .into(cardView.getOverlayImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) { }
}
