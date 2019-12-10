package com.ww.todaylife;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.util.SystemUtils;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.commonlibrary.view.ToastView;
import com.ww.commonlibrary.view.nestedViewGroup.NestedScrollWebView;
import com.ww.commonlibrary.view.nestedViewGroup.NestedWebViewRecyclerViewGroup;
import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;
import com.ww.todaylife.util.PicJavaScript;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by wang.wei on 2019-12-06
 */
public class NewsDetailActivity extends NewsDetailBaseActivity {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.titleImg)
    CircleImageView titleImg;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.titleLayout)
    LinearLayout titleLayout;
    @BindView(R.id.toolbarLayout)
    LinearLayout toolbarLayout;
    @BindView(R.id.webView)
    NestedScrollWebView webView;
    @BindView(R.id.detailParent)
    NestedWebViewRecyclerViewGroup detailParent;


    @Override
    public int setContentId() {
        return R.layout.news_detail_activity_layout2;
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    public void initData(Bundle savedInstanceState) {
        webView.setPageFinishedListener((WebView view) -> {
            addPicClickListener(view);
            if (loadingView != null)
                loadingView.hide();
        });

        webView.addJavascriptInterface(new PicJavaScript(this), "picJavaScript");
        loadingView.setRetryListener(this::loadData);
        super.initData(savedInstanceState);
        if(newsDetail.htmlString!=null){
            webView.loadHtml(newsDetail.htmlString);
        }
    }

    @Override
    public void initView() {
        SystemUtils.setWindowStatusBarColor(this, R.color.toolbar_bg);
        setToolbar(toolbarLayout);
        titleLayout.setVisibility(View.GONE);
        detailParent.setOnScrollListener(y -> {
            if (y > 450) {
                titleLayout.setVisibility(View.VISIBLE);
            } else {
                titleLayout.setVisibility(View.GONE);
            }
        });
        super.initView();

    }

    @Override
    public void onGetNews(String html, NewsContentBean newsContentBean) {
        newsDetail.htmlString=html;
        updateNewsDetail();
        userId = String.valueOf(newsContentBean.data.creator_uid);
        webView.loadHtml(html);
        if (newsContentBean.data.media_user != null) {
            titleTv.setText(newsContentBean.data.media_user.screen_name);
            Glide.with(this).load(newsContentBean.data.media_user.avatar_url).into(titleImg);
        }
    }
    @Override
    public void onError(String msg) {
       super.onError(msg);
        if (!newsDetail.has_video) {
            loadingView.showErrorView();
        }
    }

    @OnClick({R.id.backImg, R.id.titleLayout, R.id.commentCountLayout})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.commentCountLayout) {
            detailParent.switchView(0);
        } else if (view.getId() == R.id.backImg) {
            this.finish();
        } else {
            gotoUserDetail();
        }
    }

    public void addPicClickListener(WebView view) {
        view.loadUrl("javascript:(function  pic(){" +
                "var imgList = \"\";" +
                "var imgs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "var img = imgs[i];" +
                "imgList = imgList + img.src +\";\";" +
                "img.onclick = function(){" +
                "window.picJavaScript.openImg(this.src);" +
                "}" +
                "}" +
                "window.picJavaScript.getImgArray(imgList);" +
                "})()");
        view.loadUrl("javascript:(function  user(){" +
                "var userDiv = document.getElementById(" + userId + ");" +
                "userDiv.onclick = function(){" +
                "window.picJavaScript.gotoUserDetail(this.id);" +
                "}" +
                "})()");
    }

    @Override
    public void onGetVideoContent(VideoContentBean videoContent) {

    }

}
