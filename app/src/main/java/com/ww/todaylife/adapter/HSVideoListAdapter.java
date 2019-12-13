package com.ww.todaylife.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.GlideUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.todaylife.HsVideoDetailActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.bean.httpResponse.HsVideoRootBean;

import java.util.List;

public class HSVideoListAdapter extends BaseAdapter<HsVideoRootBean, HSVideoListAdapter.VideoVh> {


    public HSVideoListAdapter(Context context, List<HsVideoRootBean> list) {
        super(context, list);
    }

    @Override
    public void bindViewHolderData(VideoVh vh, int position) {
        HsVideoRootBean item = mItems.get(position);
        if (item != null) {
            ViewCompat.setTransitionName(vh.thumb, item.raw_data.thumb_image_list.get(0).url_list.get(0).url);
            ViewCompat.setTransitionName(vh.author, "author");
            ViewCompat.setTransitionName(vh.title, "title");
            ViewCompat.setTransitionName(vh.avatar, "avatar");
            ViewGroup.LayoutParams params = vh.thumb.getLayoutParams();
            params.width = ScreenUtils.getScreenWidth() / 2;
            params.height = params.width*item.raw_data.video.height/item.raw_data.video.width;
            vh.thumb.setLayoutParams(params);
            vh.author.setText(item.raw_data.user.info.name);
            vh.playCount.setText(StringUtils.getCountStr(item.raw_data.action.play_count) + "次播放");
            GlideUtils.loadImageList(mContext,item.raw_data.user.info.avatar_url,vh.avatar);
            GlideUtils.loadImageList(mContext,item.raw_data.thumb_image_list.get(0).url_list.get(0).url,vh.thumb);
            vh.title.setText(item.raw_data.title);
            vh.thumb.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, HsVideoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("HsVideoRootBean", item);
                bundle.putString("thumb", item.raw_data.thumb_image_list.get(0).url_list.get(0).url);
                intent.putExtras(bundle);
                Pair<View, String> thumb = new Pair<>(vh.thumb, "thumb");
                Pair<View, String> name = new Pair<>(vh.author, "author");
                Pair<View, String> title = new Pair<>(vh.title, "title");
                Pair<View, String> avatar = new Pair<>(vh.avatar, "avatar");

                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity)mContext, thumb,title, name,avatar);
                mContext.startActivity(intent, optionsCompat.toBundle());
            });

        }

    }


    @NonNull
    @Override
    public VideoVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hs_video_item_layout, parent, false);
        return new VideoVh(view);
    }

    public class VideoVh extends RecyclerView.ViewHolder {
        ImageView thumb;
        CircleImageView avatar;
        TextView author;
        TextView playCount;
        TextView title;

        public VideoVh(@NonNull View view) {
            super(view);
            thumb = view.findViewById(R.id.thumb);
            avatar = view.findViewById(R.id.avatar);
            author = view.findViewById(R.id.author);
            playCount = view.findViewById(R.id.playCount);
            title = view.findViewById(R.id.title);
        }
    }

}
