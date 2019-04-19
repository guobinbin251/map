package com.alibaba.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cxf on 2018/10/9.
 */

public class XiaoluAdapter extends RecyclerView.Adapter<XiaoluAdapter.Vh> {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ArrayList<Tiny2>> mTinyList;

    public XiaoluAdapter(Context context, ArrayList<ArrayList<Tiny2>> tinyList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTinyList = tinyList;
    }


    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_xiaolu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        if (position < mTinyList.size()) {
            vh.setData(mTinyList.get(position));
        } else {
            vh.setData(null);
        }

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView[] ivBetArr;

        public Vh(View itemView) {
            super(itemView);

            ivBetArr = new ImageView[6];
            ivBetArr[0] = itemView.findViewById(R.id.iv_bet1);
            ivBetArr[1] = itemView.findViewById(R.id.iv_bet2);
            ivBetArr[2] = itemView.findViewById(R.id.iv_bet3);
            ivBetArr[3] = itemView.findViewById(R.id.iv_bet4);
            ivBetArr[4] = itemView.findViewById(R.id.iv_bet5);
            ivBetArr[5] = itemView.findViewById(R.id.iv_bet6);

        }

        void setData(ArrayList<Tiny2> tiny) {
            if (tiny != null) {
                for (int i = 0; i < 6; i++) {
                    if (i < tiny.size()) {
                        if (tiny.get(i).getAskType() == MainActivity.ASK_NONE) {
                            switch (tiny.get(i).getBet1()) {
                                case MainActivity.ZHUANG_YIN:
                                    ivBetArr[i].setImageResource(R.mipmap.banker1);
                                    break;
                                case MainActivity.XIAN_YIN:
                                    ivBetArr[i].setImageResource(R.mipmap.player1);
                                    break;
                            }
                        } else if (tiny.get(i).getAskType() == MainActivity.ASK_DEMO) {
                            ivBetArr[i].setImageDrawable(null);
                        } else {
                            switch (tiny.get(i).getBet1()) {
                                case MainActivity.ZHUANG_YIN:
                                    ivBetArr[i].setImageResource(R.mipmap.banker1);
                                    break;
                                case MainActivity.XIAN_YIN:
                                    ivBetArr[i].setImageResource(R.mipmap.player1);
                                    break;
                            }
                            setFlickerAnimation(ivBetArr[i]);
                        }
                    } else {
                        ivBetArr[i].setImageDrawable(null);
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    ivBetArr[i].setImageDrawable(null);
                }
            }
        }
    }

    private void setFlickerAnimation(ImageView imgv) {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(750);//闪烁时间间隔
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        imgv.setAnimation(animation);
    }
}
