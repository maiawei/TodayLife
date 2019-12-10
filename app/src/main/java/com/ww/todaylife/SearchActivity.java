package com.ww.todaylife;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.SystemUtils;
import com.ww.commonlibrary.view.widget.DividerGridItemDecoration;
import com.ww.todaylife.adapter.HotKeywordAdapter;
import com.ww.todaylife.bean.httpResponse.KeyWordDetail;
import com.ww.todaylife.dao.SearchHistoryDao;
import com.ww.todaylife.util.DataProcessUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends SearchBaseActivity {


    private ArrayList<String> hotList = new ArrayList<>();
    HotKeywordAdapter hotKeywordAdapter;


    @Override
    public int setContentId() {
        return R.layout.search_activity_layout;
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mPresenter.getHotKeyword();
    }

    @Override
    public void initView() {
        SystemUtils.setWindowStatusBarColor(this,R.color.toolbar_bg);
        super.initView();
        if(SearchHistoryDao.getInstance().getSearchHistory(this) !=null)
        historyList= (ArrayList<String>) SearchHistoryDao.getInstance().getSearchHistory(this);
        hotKeywordAdapter = new HotKeywordAdapter(this, hotList);
        hotSearchRv.setLayoutManager(new GridLayoutManager(this, 2));
        DividerGridItemDecoration offsetsItemDecoration = new DividerGridItemDecoration(this, R.drawable.grid_divider_bg);
        hotSearchRv.addItemDecoration(offsetsItemDecoration);
        hotSearchRv.setAdapter(hotKeywordAdapter);
        searchHistoryAdapter = new HotKeywordAdapter(this, historyList);
        historySearchRv.setLayoutManager(new GridLayoutManager(this, 2));
        historySearchRv.addItemDecoration(offsetsItemDecoration);
        historySearchRv.setAdapter(searchHistoryAdapter);

        searchHistoryAdapter.setItemClickListener(position -> {
            if(searchHistoryAdapter.getHasEdit()){
                searchHistoryAdapter.remove(position);
                return;
            }
            editSetText(historyList.get(position));
            startSearch(historyList.get(position));
        });
        hotKeywordAdapter.setItemClickListener(position -> {
            editSetText(hotList.get(position));
            startSearch(hotList.get(position));
        });
    }


    @Override
    public void onGetKeyword(List<KeyWordDetail> mList) {
        LogUtils.e("onGetKeyword");
        if (keywordRv.getVisibility() == View.GONE) {
            keywordRv.setVisibility(View.VISIBLE);
        }
        keywordAdapter.refreshList(mList);
    }


    @Override
    public void onError() {

    }

    @Override
    public void OnGetHotKeyWord(List<String> mList) {
        hotKeywordAdapter.refreshList(mList);
    }

    @Override
    protected void onStop() {
        historyList= DataProcessUtils.dealRepeatSearchHistoryData(historyList);
        SearchHistoryDao.getInstance().saveSearchHistory(historyList,this);
        super.onStop();
    }
}
