package com.rs.leanbacknative.layouts;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.leanback.app.VerticalGridFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.rs.leanbacknative.presenters.CardPresenterSelector;
import com.rs.leanbacknative.presenters.CategoryPresenter;
import com.rs.leanbacknative.presenters.GridPresenter;
import com.rs.leanbacknative.presenters.GridCardPresenter;
import com.rs.leanbacknative.utils.Constants;
import com.rs.leanbacknative.utils.DataManager;
import com.rs.leanbacknative.models.Card;
import java.util.List;

@SuppressLint("ViewConstructor")
public class LeanbackCategoryLayout extends FrameLayout {
    private ThemedReactContext mContext;
    private ArrayObjectAdapter mRowsAdapter;
    private VerticalGridFragment mVerticalGridFragment;

    public LeanbackCategoryLayout(@NonNull ThemedReactContext context, VerticalGridFragment verticalGridFragment, int numberOfColumns) {
        super(context);

        mContext = context;
        mVerticalGridFragment = verticalGridFragment;
        initializeAdapter(verticalGridFragment, numberOfColumns);
        setupEventListeners(verticalGridFragment);
    }

    private void initializeAdapter(VerticalGridFragment verticalGridFragment, int numberOfColumns) {
        VerticalGridPresenter verticalGridPresenter = new GridPresenter(false);
        verticalGridPresenter.setNumberOfColumns(numberOfColumns);
        verticalGridFragment.setGridPresenter(verticalGridPresenter);
        verticalGridPresenter.setShadowEnabled(false);

        mRowsAdapter = new ArrayObjectAdapter(new CategoryPresenter());

        FragmentManager fragmentManager = mContext.getCurrentActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(verticalGridFragment, "GridFragment").commit();
        fragmentManager.executePendingTransactions();
        verticalGridFragment.setAdapter(mRowsAdapter);
    }

    private void setupEventListeners(VerticalGridFragment verticalGridFragment) {
        verticalGridFragment.setOnItemViewClickedListener(new ItemViewClickedListener());
        verticalGridFragment.setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {

            if (item instanceof Card) {
                Card card = (Card) item;
                WritableMap event = Arguments.createMap();
                event.putString("item", card.toJSON());
                event.putInt("focusedRowIndex", mRowsAdapter.indexOf(row));
                mContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), Constants.EVENT_ON_FOCUS, event);
            }
        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Card) {
                Card card = (Card) item;
                WritableMap event = Arguments.createMap();
                event.putString("item", card.toJSON());
                mContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), Constants.EVENT_ON_PRESS, event);
            }
        }
    }

    public void setDataAndAttributes(ReadableMap dataAndAttributes) {
        ReadableArray data = dataAndAttributes.getArray("data");
        ReadableMap attributes = dataAndAttributes.getMap("attributes");

        List<Card> rows = DataManager.setupData(data, attributes);

        if (mRowsAdapter != null) {
            mRowsAdapter.clear();
        }

        CardPresenterSelector cardPresenterSelector = new CardPresenterSelector(mContext, attributes);
        mRowsAdapter = new ArrayObjectAdapter(cardPresenterSelector);

        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).setPresenterType(Card.Type.CATEGORY);
            mRowsAdapter.add(rows.get(i));
        }

        mVerticalGridFragment.setAdapter(mRowsAdapter);

        WritableMap event = Arguments.createMap();
        event.putString("data", DataManager.getViewIds().toString());
        mContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), Constants.EVENT_ON_DATA_IDS_READY, event);
    }
}
