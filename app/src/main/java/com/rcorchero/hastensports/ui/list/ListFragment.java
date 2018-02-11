package com.rcorchero.hastensports.ui.list;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rcorchero.hastensports.R;
import com.rcorchero.hastensports.data.DataManager;
import com.rcorchero.hastensports.data.model.PlayerList;
import com.rcorchero.hastensports.ui.util.DisplayMetricsUtil;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements ListContract.ListView,
        SwipeRefreshLayout.OnRefreshListener,
        Spinner.OnItemSelectedListener{

    private static final int TAB_LAYOUT_SPAN_SIZE = 2;
    private static final int TAB_LAYOUT_ITEM_SPAN_SIZE = 1;
    private static final int SCREEN_TABLET_DP_WIDTH = 600;

    private AppCompatActivity mActivity;
    private ListPresenter mListPresenter;
    private ListAdapter mListAdapter;
    private ArrayAdapter<String> mSpinnerAdapter;

    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mContentLoadingProgress;

    private View mMessageLayout;
    private ImageView mMessageImage;
    private TextView mMessageText;
    private Button mMessageButton;

    private List<PlayerList> playerList;

    public ListFragment() {}

    public static ListFragment newInstance() {
        return newInstance(null);
    }

    public static ListFragment newInstance(@Nullable Bundle arguments) {

        ListFragment fragment = new ListFragment();
        if (arguments != null) {

            fragment.setArguments(arguments);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            fragment.setEnterTransition(new Slide());

        }
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        List <String> mSpinnerAdapterList = new ArrayList<>();
        mListPresenter = new ListPresenter(DataManager.getInstance());
        mListAdapter = new ListAdapter();
        mSpinnerAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, mSpinnerAdapterList);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initViews(view);
        mListPresenter.attachView(this);

        if (mListAdapter.isEmpty()) {

            mListPresenter.onInitialListRequested();

        }

        return view;
    }

    private void initViews(View view) {

        mActivity = (AppCompatActivity) getActivity();

        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner = view.findViewById(R.id.spinner_types);
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setAdapter(mSpinnerAdapter);

        mRecyclerView = view.findViewById(R.id.recycler_players);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setMotionEventSplittingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mListAdapter);

        boolean isTabletLayout = DisplayMetricsUtil.isScreenW(SCREEN_TABLET_DP_WIDTH);
        mRecyclerView.setLayoutManager(setUpLayoutManager(isTabletLayout));

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mContentLoadingProgress = view.findViewById(R.id.progress);
        mMessageLayout = view.findViewById(R.id.message_layout);
        mMessageImage = view.findViewById(R.id.iv_message);
        mMessageText = view.findViewById(R.id.tv_message);
        mMessageButton = view.findViewById(R.id.btn_try_again);
        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRefresh();

            }
        });

    }

    private RecyclerView.LayoutManager setUpLayoutManager(boolean isTabletLayout) {

        RecyclerView.LayoutManager layoutManager;

        if (!isTabletLayout) {

            layoutManager = new LinearLayoutManager(mActivity);

        } else {

            layoutManager = initGridLayoutManager(TAB_LAYOUT_SPAN_SIZE, TAB_LAYOUT_ITEM_SPAN_SIZE);

        }

        return layoutManager;
    }

    private RecyclerView.LayoutManager initGridLayoutManager(final int spanCount, final int itemSpanCount) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mListAdapter.getItemViewType(position)) {
                    case ListAdapter.VIEW_TYPE_LOADING:
                        // If it is a loading view we wish to accomplish a single item per row
                        return spanCount;
                    default:
                        // Else, define the number of items per row (considering TAB_LAYOUT_SPAN_SIZE).
                        return itemSpanCount;
                }
            }
        });

        return gridLayoutManager;
    }

    @Override
    public void onRefresh() {

        mListAdapter.removeAll();
        mListPresenter.onInitialListRequested();

    }


    @Override
    public void showPlayers(final List<PlayerList> playerList) {

        this.playerList = playerList;

        if (!mSwipeRefreshLayout.isActivated()) {

            mSwipeRefreshLayout.setEnabled(true);

        }

        int position = 0;
        if(mSpinner.getSelectedItemPosition() != -1) position = mSpinner.getSelectedItemPosition();

        mListAdapter.addItems(playerList.get(position).getPlayers());

        mSpinnerAdapter.clear();
        for (PlayerList player : playerList) {

            mSpinnerAdapter.add(player.getTitle());

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        mListAdapter.removeAll();
        mListAdapter.addItems(playerList.get(position).getPlayers());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void showProgress() {

        if (mListAdapter.isEmpty() && !mSwipeRefreshLayout.isRefreshing()) {
            mContentLoadingProgress.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideProgress() {

        mSwipeRefreshLayout.setRefreshing(false);
        mContentLoadingProgress.setVisibility(View.GONE);
        mListAdapter.removeLoadingView();

    }

    @Override
    public void showUnauthorizedError() {

        mMessageImage.setImageResource(R.drawable.ic_error_list);
        mMessageText.setText(getString(R.string.error_generic_server_error, "Unauthorized"));
        mMessageButton.setText(getString(R.string.action_try_again));
        showMessageLayout(true);

    }

    @Override
    public void showError(String errorMessage) {

        mMessageImage.setImageResource(R.drawable.ic_error_list);
        mMessageText.setText(getString(R.string.error_generic_server_error, errorMessage));
        mMessageButton.setText(getString(R.string.action_try_again));
        showMessageLayout(true);

    }

    @Override
    public void showEmpty() {

        mMessageImage.setImageResource(R.drawable.ic_clear);
        mMessageText.setText(getString(R.string.error_no_items_to_display));
        mMessageButton.setText(getString(R.string.action_check_again));
        showMessageLayout(true);

    }

    @Override
    public void showMessageLayout(boolean show) {

        mMessageLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSpinner.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {

        mRecyclerView.setAdapter(null);
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {

        mListPresenter.detachView();
        super.onDestroy();

    }
}