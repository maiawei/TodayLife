package com.ww.todaylife.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.todaylife.R;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.util.DataProcessUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeletePopWindow extends PopupWindow {
    @BindView(R.id.topTriangle)
    ImageView topTriangle;
    @BindView(R.id.keywordLayout)
    RelativeLayout keywordLayout;
    @BindView(R.id.firstLayout)
    LinearLayout firstLayout;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    @BindView(R.id.returnLayout)
    RelativeLayout returnLayout;
    @BindView(R.id.secondLayout)
    LinearLayout secondLayout;
    @BindView(R.id.bottomTriangle)
    ImageView bottomTriangle;
    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;
    @BindView(R.id.blackAuthor)
    TextView blackAuthor;
    @BindView(R.id.keywordTv)
    TextView keywordTv;
    @BindView(R.id.returnImage)
    ImageView returnImage;
    @BindView(R.id.keywordTv1)
    TextView keywordTv1;
    @BindView(R.id.keywordTv2)
    TextView keywordTv2;
    @BindView(R.id.containerView)
    RelativeLayout containerView;

    private Window window;
    private WindowManager.LayoutParams layoutParams;
    private Context context;
    private Unbinder unbinder;
    private int heightY;
    private boolean isShowUp;
    private NewsDetail newsDetail;
    private DeleteCallBack deleteCallBack;

    public void showPop(View target, NewsDetail detail, DeleteCallBack callBack) {
        heightY = calculatePopWindowLocation(target, rootLayout);
        this.newsDetail = detail;
        this.deleteCallBack = callBack;
        initData();
        if (isShowUp) {
            setAnimationStyle(R.style.deletePopWindowFromBottom);
            containerView.setGravity(Gravity.BOTTOM);
            showAtLocation(target, Gravity.BOTTOM, 0, heightY);
        } else {
            setAnimationStyle(R.style.deletePopWindowFromTop);
            containerView.setGravity(Gravity.TOP);
            showAtLocation(target, Gravity.TOP, 0, heightY);
        }
    }

    public void initData() {
        blackAuthor.setText(newsDetail.media_name == null ? "拉黑作者:" + newsDetail.source : "拉黑作者:" + newsDetail.media_name);
        ArrayList<String> keys = DataProcessUtils.dealHiddenWords(newsDetail.filter_words);
        if (keys.size() > 1) {
            keywordTv.setText(keys.get(0) + "、" + keys.get(1));
            keywordTv1.setText(keys.get(0));
            keywordTv2.setText(keys.get(1));
        } else {
            keywordTv.setText(keys.get(0));
            keywordTv1.setText(keys.get(0));
            keywordTv2.setVisibility(View.GONE);
        }
    }

    private void initSetting() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.delete_news_layout, null);
        unbinder = ButterKnife.bind(this, rootView);
        setContentView(rootView);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ScreenUtils.getScreenWidth());
        window = ((Activity) context).getWindow();
        layoutParams = window.getAttributes();
        layoutParams.alpha = 0.6f;
        window.setAttributes(layoutParams);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void showSecondLayout() {
        Animation secondShow = AnimationUtils.loadAnimation(context, R.anim.in_from_right);
        Animation firstHidden = AnimationUtils.loadAnimation(context, R.anim.out_from_left);
        firstLayout.startAnimation(firstHidden);
        firstLayout.setVisibility(View.GONE);
        secondLayout.startAnimation(secondShow);
        secondLayout.setVisibility(View.VISIBLE);

    }

    private void showFirstLayout() {
        Animation firstShow = AnimationUtils.loadAnimation(context, R.anim.in_from_left);
        Animation secondHidden = AnimationUtils.loadAnimation(context, R.anim.out_from_right);
        secondLayout.startAnimation(secondHidden);
        secondLayout.setVisibility(View.GONE);
        firstLayout.startAnimation(firstShow);
        firstLayout.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.keywordLayout, R.id.containerView, R.id.blackAuthorLayout, R.id.returnLayout, R.id.noInterestLayout, R.id.trashContentLayout, R.id.keywordTv1, R.id.keywordTv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.containerView:
                dismiss();
                break;
            case R.id.noInterestLayout:
                deleteCallBack.deleteItemType("不感兴趣");
                dismiss();
                break;
            case R.id.trashContentLayout:
                deleteCallBack.deleteItemType("垃圾内容");
                dismiss();
                break;
            case R.id.keywordTv1:
                deleteCallBack.deleteItemType("屏蔽" + keywordTv1.getText());
                dismiss();
                break;
            case R.id.keywordTv2:
                deleteCallBack.deleteItemType("屏蔽" + keywordTv2.getText());
                dismiss();
                break;
            case R.id.keywordLayout:
                showSecondLayout();
                break;
            case R.id.returnLayout:
                showFirstLayout();
                break;
            case R.id.blackAuthorLayout:
                deleteCallBack.deleteItemType("拉黑作者");
                dismiss();
                break;
        }
    }

    private int calculatePopWindowLocation(final View target, final View contentView) {
        int[] contentViewLocation = new int[2];
        int[] targetLocation = new int[2];
        target.getLocationInWindow(targetLocation);
        int targetHeight = target.getHeight();
        int targetWidth = target.getHeight();
        int screenHeight = ScreenUtils.getScreenHeight();
        isShowUp = (targetLocation[1] > screenHeight / 2);
        RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) topTriangle.getLayoutParams();
        RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) bottomTriangle.getLayoutParams();
        if (isShowUp) {
            bottomTriangle.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            bottomParams.leftMargin = targetLocation[0];
            topTriangle.setVisibility(View.GONE);
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            contentViewLocation[1] = screenHeight - targetLocation[1];
        } else {
            topTriangle.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            topParams.leftMargin = targetLocation[0];
            bottomTriangle.setVisibility(View.GONE);
            contentViewLocation[1] = targetLocation[1] + targetHeight;
        }
        return contentViewLocation[1];
    }

    public DeletePopWindow(Context context) {
        this(context, null);
    }

    private DeletePopWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private DeletePopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initSetting();
    }


    @Override
    public void dismiss() {
        layoutParams.alpha = 1.0f;
        window.setAttributes(layoutParams);
        unbinder.unbind();
        super.dismiss();
    }


    public interface DeleteCallBack {
        void deleteItemType(String type);
    }

}
