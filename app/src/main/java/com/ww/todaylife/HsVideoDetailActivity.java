package com.ww.todaylife;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.ClickAnimImage;
import com.ww.commonlibrary.view.widget.DragCloseHelper;
import com.ww.todaylife.base.BaseActivity;
import com.ww.todaylife.base.BaseFullBottomSheetFragment;
import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.HsVideoRootBean;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;
import com.ww.todaylife.fragment.dialogfragment.HsVideoCommentDialogFragment;
import com.ww.todaylife.presenter.Iview.IDetailBaseView;
import com.ww.todaylife.presenter.NewsDetailPresenter;
import com.ww.todaylife.view.HSDetailJzvdStd;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

public class HsVideoDetailActivity extends BaseActivity<NewsDetailPresenter> implements IDetailBaseView {

    String thumbUrl;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.videoTitle)
    TextView videoTitle;
    HsVideoRootBean video;
    @BindView(R.id.jzPlayer)
    HSDetailJzvdStd jzPlayer;
    boolean isSharedElementEnd, hasVideoContent;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.starImage)
    ClickAnimImage starImage;
    @BindView(R.id.commentLayout)
    FrameLayout commentLayout;
    @BindView(R.id.videoDesc)
    TextView videoDesc;
    HsVideoCommentDialogFragment dialogFragment;
    boolean isShowDialog;
    @BindView(R.id.sliderLayout)
    RelativeLayout sliderLayout;
    DragCloseHelper dragCloseHelper;
    @BindView(R.id.videoInfoLayout)
    LinearLayout videoInfoLayout;
    @BindView(R.id.comment_bottom)
    LinearLayout commentBottomLayout;
    Handler mHandler;

    @Override
    public int setContentId() {
        return R.layout.hs_video_detail_layout;
    }

    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Glide.with(this).load(video.raw_data.user.info.avatar_url).into(avatar);
        videoTitle.setText(video.raw_data.title);
        author.setText(video.raw_data.user.info.name);
        commentCount.setText(String.valueOf(video.raw_data.action.comment_count));
        if (!TextUtils.isEmpty(video.raw_data.user.info.desc)) {
            videoDesc.setVisibility(View.VISIBLE);
            videoDesc.setText(StringUtils.getMarqueeText(video.raw_data.user.info.desc, (int) videoDesc.getTextSize()));
        }
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        thumbUrl = bundle.getString("thumb");
        video = (HsVideoRootBean) bundle.getSerializable("HsVideoRootBean");
        mPresenter.getVideoContent(video.raw_data.video.video_id);
        ViewGroup.LayoutParams jzParam = jzPlayer.thumbImageView.getLayoutParams();
        jzParam.width = ScreenUtils.getScreenWidth();
        jzParam.height = jzParam.width * video.raw_data.video.height / video.raw_data.video.width;
        jzPlayer.thumbImageView.setLayoutParams(jzParam);
        jzPlayer.setRelatedCallBack(new HSDetailJzvdStd.RelatedCallBack() {
            @Override
            public void cancelPlay() {
                onBackPressed();
            }

            @Override
            public void showCommentDialog() {
                if (!isShowDialog) {
                    showDialog();
                }
            }
        });
        dealAnimation();
        initDragCloseHelper();
    }

    public void initDragCloseHelper() {
        dragCloseHelper = new DragCloseHelper(this);
        dragCloseHelper.setShareElementMode(true);
        dragCloseHelper.setCanScaleMode(true);
        dragCloseHelper.setCanVideoMode(true);
        dragCloseHelper.setDragCloseViews(sliderLayout, jzPlayer);
        dragCloseHelper.setDragCloseListener(new DragCloseHelper.DragCloseListener() {
            @Override
            public boolean intercept() {
                return false;
            }

            @Override
            public void dragStart() {
                videoInfoLayout.setVisibility(View.GONE);
                commentBottomLayout.setVisibility(View.GONE);
            }

            @Override
            public void dragging(float percent) {

            }

            @Override
            public void dragCancel() {
                videoInfoLayout.setVisibility(View.VISIBLE);
                commentBottomLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void dragClose(boolean isShareElementMode) {
                HsVideoDetailActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void onError(String msg) {

    }

    public void dealAnimation() {
        supportPostponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(150);
            getWindow().setSharedElementEnterTransition(changeBounds);
        }
        //结束动画防止网络慢导致界面卡住
        mHandler = new Handler();
        mHandler.postDelayed(this::supportStartPostponedEnterTransition, 150);
        ViewCompat.setTransitionName(jzPlayer.thumbImageView, "thumb");
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_default).dontAnimate().centerCrop();
        Glide.with(this)
                .load(thumbUrl)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(jzPlayer.thumbImageView);
        ActivityCompat.setEnterSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                isSharedElementEnd = true;
                startVideo();
            }
        });
    }

    @Override
    public void onGetVideoContent(VideoContentBean videoContent) {
        hasVideoContent = true;
        jzPlayer.setSource(videoContent.data.video_source);
        startVideo();
    }

    @Override
    public void onGetNewsComment(CommentResponse commentResponse, int loadType, boolean hasMore) {
        dialogFragment.handleData(commentResponse, loadType, hasMore);
    }

    public void startVideo() {
        if (hasVideoContent && isSharedElementEnd) {
            jzPlayer.startVideo();
        }
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        jzPlayer.thumbImageView.setVisibility(View.VISIBLE);
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (jzPlayer.mediaInterface != null)
            jzPlayer.mediaInterface.start();
    }

    @Override
    protected void onPause() {
        if (jzPlayer.mediaInterface != null)
            jzPlayer.mediaInterface.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        Jzvd.releaseAllVideos();
        super.onDestroy();
    }

    public void showDialog() {
        isShowDialog = true;
        if (dialogFragment == null) {
            dialogFragment = HsVideoCommentDialogFragment.newInstance(video);
            dialogFragment.setPresenter(mPresenter);
            dialogFragment.setHeight(ScreenUtils.getScreenHeight() * 2 / 3);
            dialogFragment.setDismissListenerListener(new BaseFullBottomSheetFragment.DismissListener() {
                @Override
                public void onDismiss() {
                    isShowDialog=false;
                }
            });
        }
        dialogFragment.show(getSupportFragmentManager(), "HsVideoCommentDialogFragment");
    }


    @OnClick({R.id.commentLayout, R.id.authorInfoLayout})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.commentLayout) {
            showDialog();
        } else {
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("userId", String.valueOf(video.raw_data.user.info.user_id));
            startActivity(intent);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragCloseHelper.handleEvent(event)) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

}
