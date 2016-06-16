package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Rohan Garg on 15-06-2016.
 */
public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    WidgetViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        String[] projection = new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP};
        String selection = QuoteColumns.ISCURRENT + " = ?";
        String[] selectionArgs = new String[]{"1"};
        mCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        if (mContext != null)
            mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
            view.setTextViewText(R.id.stock_symbol, mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
            view.setTextViewText(R.id.bid_price, mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            view.setTextViewText(R.id.change, mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));

            if (mCursor.getInt(mCursor.getColumnIndex(QuoteColumns.ISUP)) == 1) {
                view.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                view.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }
            return view;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
