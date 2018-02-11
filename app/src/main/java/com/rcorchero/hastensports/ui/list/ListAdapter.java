package com.rcorchero.hastensports.ui.list;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rcorchero.hastensports.R;
import com.rcorchero.hastensports.data.model.Player;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} populated with {@link Player}.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = ListAdapter.class.getSimpleName();

    private final List<Player> mPlayerList;

    /**
     * ViewTypes serve as a mapping point to which layout should be inflated
     */
    public static final int VIEW_TYPE_LIST = 1;
    public static final int VIEW_TYPE_LOADING = 2;

    @IntDef({VIEW_TYPE_LOADING, VIEW_TYPE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {}

    @ViewType
    private int mViewType;

    public ListAdapter() {

        mPlayerList = new ArrayList<>();
        mViewType = VIEW_TYPE_LIST;

    }

    @Override
    public int getItemViewType(int position) {

        return mPlayerList.get(position) == null ? VIEW_TYPE_LOADING : mViewType;

    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    @Override
    public long getItemId(int position) {
        return mPlayerList.size() >= position ? position : -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_LOADING) {

            return onIndicationViewHolder(parent);

        }

        return onGenericItemViewHolder(parent, viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == VIEW_TYPE_LOADING) {

            return; // no-op

        }

        onBindGenericItemViewHolder((PlayerViewHolder) holder, position);

    }

    private RecyclerView.ViewHolder onIndicationViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar,
                parent, false);
        return new ProgressBarViewHolder(view);

    }

    private RecyclerView.ViewHolder onGenericItemViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        switch (viewType) {

            case VIEW_TYPE_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_list,
                        parent, false);
                break;

        }

        return new PlayerViewHolder(view);

    }

    private void onBindGenericItemViewHolder(final PlayerViewHolder holder, int position) {

        String date = "";
        if (mPlayerList.get(position).getDate() != null) date = mPlayerList.get(position).getDate();

        holder.name.setText(mPlayerList.get(position).getFullname());
        holder.date.setText(date);

        String characterImageUrl = mPlayerList.get(position).getImage();
        if (!TextUtils.isEmpty(characterImageUrl)) {
            Picasso.with(holder.listItem.getContext())
                    .load(characterImageUrl)
                    .centerCrop()
                    .fit()
                    .into(holder.image);

        }

    }

    public void add(Player item) {
        add(null, item);
    }

    public void add(@Nullable Integer position, Player item) {

        if (position != null) {

            mPlayerList.add(position, item);
            notifyItemInserted(position);

        } else {

            mPlayerList.add(item);
            notifyItemInserted(mPlayerList.size() - 1);

        }

    }

    public void addItems(List<Player> itemsList) {

        mPlayerList.addAll(itemsList);
        notifyItemRangeInserted(getItemCount(), mPlayerList.size() - 1);

    }

    public void remove(int position) {

        if (mPlayerList.size() < position) {

            Log.w(TAG, "The item at position: " + position + " doesn't exist");
            return;

        }

        mPlayerList.remove(position);
        notifyItemRemoved(position);

    }

    public void removeAll() {

        mPlayerList.clear();
        notifyDataSetChanged();

    }

    public boolean removeLoadingView() {

        if (mPlayerList.size() > 1) {

            int loadingViewPosition = mPlayerList.size() - 1;
            if (getItemViewType(loadingViewPosition) == VIEW_TYPE_LOADING) {

                remove(loadingViewPosition);
                return true;

            }

        }

        return false;

    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(@ViewType int viewType) {
        mViewType = viewType;
    }


    /**
     * ViewHolders
     */
    public class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        public final ProgressBar progressBar;

        public ProgressBarViewHolder(View view) {

            super(view);
            progressBar = view.findViewById(R.id.progress_bar);

        }

    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        public final View listItem;
        public final TextView name;
        public final TextView date;
        public final AppCompatImageView image;

        public PlayerViewHolder(View view) {

            super(view);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);
            listItem = view;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }

    }

}
