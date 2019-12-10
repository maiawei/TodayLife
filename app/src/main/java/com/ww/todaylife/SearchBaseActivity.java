package com.ww.todaylife;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.commonlibrary.view.NoScrollViewpager;
import com.ww.commonlibrary.view.widget.EditTextChangeListener;
import com.ww.todaylife.adapter.HotKeywordAdapter;
import com.ww.todaylife.adapter.RecommendKeywordAdapter;
import com.ww.todaylife.adapter.SearchTabPagerAdapter;
import com.ww.todaylife.adapter.TabPagerAdapter;
import com.ww.todaylife.base.BaseSwipeActivity;
import com.ww.todaylife.bean.ChanelCategory;
import com.ww.todaylife.bean.eventBean.SearchEvent;
import com.ww.todaylife.bean.httpResponse.KeyWordDetail;
import com.ww.todaylife.fragment.SearchImageListFragment;
import com.ww.todaylife.fragment.SearchNewsListFragment;
import com.ww.todaylife.fragment.SearchVideoListFragment;
import com.ww.todaylife.presenter.Iview.ISearchView;
import com.ww.todaylife.presenter.SearchPresenter;
import com.ww.todaylife.util.DataProcessUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


// activity内容过多 base处理逻辑  具体的处理数据
public abstract class SearchBaseActivity extends BaseSwipeActivity<SearchPresenter> implements ISearchView {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.searchEdit)
    EditText searchEdit;
    @BindView(R.id.searchTv)
    TextView searchTv;
    @BindView(R.id.keywordRv)
    RecyclerView keywordRv;
    ArrayList<KeyWordDetail> keyWordDetailList;
    RecommendKeywordAdapter keywordAdapter;
    EditTextChangeListener editTextListener;
    @BindView(R.id.tabLayout)
    XTabLayout tabLayout;
    @BindView(R.id.viewPage)
    NoScrollViewpager viewPage;
    @BindView(R.id.searchContentLayout)
    LinearLayout searchContentLayout;
    @BindView(R.id.keywordRvLayout)
    LinearLayout keywordRvLayout;
    @BindView(R.id.historySearchRv)
    RecyclerView historySearchRv;
    @BindView(R.id.ToggleButton)
    android.widget.ToggleButton ToggleButton;
    @BindView(R.id.hotSearchRv)
    RecyclerView hotSearchRv;
    @BindView(R.id.hotAndHistoryLayout)
    LinearLayout hotAndHistoryLayout;
    @BindView(R.id.deleteToggle)
    android.widget.ToggleButton deleteToggle;

    @BindView(R.id.toolbarLayout)
    LinearLayout toolbarLayout;
    private String[] category = {"资讯", "视频", "图片"};
    private String[] categoryCode = {"news", "video", "gallery"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<ChanelCategory> chanelCategories = new ArrayList<>();
    private SearchTabPagerAdapter tabPagerAdapter;
    ArrayList<String> historyList = new ArrayList<>();
    String currentKeyword = "";
    HotKeywordAdapter searchHistoryAdapter;


    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        for (int i = 0; i < category.length; i++) {
            ChanelCategory liveCategory = new ChanelCategory();
            liveCategory.name = category[i];
            liveCategory.typeCode = categoryCode[i];
            chanelCategories.add(liveCategory);
        }
        initTabFragment();
        tabLayout.setupWithViewPager(viewPage);
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(new SearchEvent(currentKeyword));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        setToolbar(toolbarLayout);
        historySearchRv.setNestedScrollingEnabled(false);
        hotSearchRv.setNestedScrollingEnabled(false);
        editTextListener = new EditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(String.valueOf(s))) {
                    if (hotAndHistoryLayout.getVisibility() == View.GONE) {
                        hotAndHistoryLayout.setVisibility(View.VISIBLE);
                        historyList = DataProcessUtils.dealRepeatSearchHistoryData(historyList);
                        searchHistoryAdapter.refreshList(historyList);
                        mPresenter.getHotKeyword();
                    }
                }
                mPresenter.getKeyword(String.valueOf(s));
            }
        };
        searchEdit.addTextChangedListener(editTextListener);
        keywordRv.setLayoutManager(new LinearLayoutManager(this));
        keywordRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        keyWordDetailList = new ArrayList<>();
        keywordAdapter = new RecommendKeywordAdapter(this, keyWordDetailList);
        keywordRv.setAdapter(keywordAdapter);
        keywordAdapter.setItemClickListener(position -> {
            editSetText(keyWordDetailList.get(position).keyword);
            startSearch();

        });
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch();
                }
                return false;
            }
        });
        keywordRvLayout.setOnTouchListener((v, event) -> {
            keywordRv.setVisibility(View.GONE);
            return false;
        });
        ToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                hotSearchRv.setVisibility(View.GONE);
            } else {
                hotSearchRv.setVisibility(View.VISIBLE);
            }

        });
        deleteToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            searchHistoryAdapter.setEdit(isChecked);
        });
    }

    public void editSetText(String s) {
        searchEdit.removeTextChangedListener(editTextListener);
        searchEdit.setText(s);
        searchEdit.setSelection(s.length());
        searchEdit.addTextChangedListener(editTextListener);
    }

    public void startSearch() {
        startSearch("");
    }

    public void startSearch(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            currentKeyword = keyword;
        } else {
            currentKeyword = searchEdit.getText().toString();
        }
        if (TextUtils.isEmpty(currentKeyword))
            return;
        historyList.add(0, currentKeyword);
        keywordRv.setVisibility(View.GONE);
        EventBus.getDefault().post(new SearchEvent(currentKeyword));
        UiUtils.hideSoftInput(SearchBaseActivity.this);
        hotAndHistoryLayout.setVisibility(View.GONE);
    }

    @OnClick({R.id.backImg, R.id.searchTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                this.finish();
                break;
            case R.id.searchTv:
                startSearch();
                break;

        }
    }

    private void initTabFragment() {
        fragments.add(SearchNewsListFragment.newInstance(categoryCode[0]));
        fragments.add(SearchVideoListFragment.newInstance(categoryCode[1]));
        fragments.add(SearchImageListFragment.newInstance(categoryCode[2]));
        viewPage.setOffscreenPageLimit(fragments.size());
        tabPagerAdapter = new SearchTabPagerAdapter(fragments, chanelCategories, getSupportFragmentManager());
        viewPage.setAdapter(tabPagerAdapter);
        viewPage.setOffscreenPageLimit(3);
    }
}
