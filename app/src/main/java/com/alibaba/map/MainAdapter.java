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

import java.util.ArrayList;

/**
 * Created by cxf on 2018/10/9.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Vh> {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ArrayList<Tiny>> mTinyList;

    public MainAdapter(Context context, ArrayList<ArrayList<Tiny>> tinyList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTinyList = tinyList;
    }


    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Vh(mInflater.inflate(R.layout.item_main, parent, false));
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
        return 30;
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView[] ivBetArr;
        ImageView[] ivZhuangDui;
        ImageView[] ivXianDui;

        public Vh(View itemView) {
            super(itemView);
            ivBetArr = new ImageView[6];
            ivBetArr[0] = (ImageView) itemView.findViewById(R.id.iv_result1);
            ivBetArr[1] = (ImageView) itemView.findViewById(R.id.iv_result2);
            ivBetArr[2] = (ImageView) itemView.findViewById(R.id.iv_result3);
            ivBetArr[3] = (ImageView) itemView.findViewById(R.id.iv_result4);
            ivBetArr[4] = (ImageView) itemView.findViewById(R.id.iv_result5);
            ivBetArr[5] = (ImageView) itemView.findViewById(R.id.iv_result6);

            ivZhuangDui = new ImageView[6];
            ivZhuangDui[0] = (ImageView) itemView.findViewById(R.id.iv_zhuangdui_1);
            ivZhuangDui[1] = (ImageView) itemView.findViewById(R.id.iv_zhuangdui_2);
            ivZhuangDui[2] = (ImageView) itemView.findViewById(R.id.iv_zhuangdui_3);
            ivZhuangDui[3] = (ImageView) itemView.findViewById(R.id.iv_zhuangdui_4);
            ivZhuangDui[4] = (ImageView) itemView.findViewById(R.id.iv_zhuangdui_5);
            ivZhuangDui[5] = (ImageView) itemView.findViewById(R.id.iv_zhuangdui_6);

            ivXianDui = new ImageView[6];
            ivXianDui[0] = (ImageView) itemView.findViewById(R.id.iv_xiandui_1);
            ivXianDui[1] = (ImageView) itemView.findViewById(R.id.iv_xiandui_2);
            ivXianDui[2] = (ImageView) itemView.findViewById(R.id.iv_xiandui_3);
            ivXianDui[3] = (ImageView) itemView.findViewById(R.id.iv_xiandui_4);
            ivXianDui[4] = (ImageView) itemView.findViewById(R.id.iv_xiandui_5);
            ivXianDui[5] = (ImageView) itemView.findViewById(R.id.iv_xiandui_6);

        }

        void setData(ArrayList<Tiny> tiny) {
            if (tiny != null) {
                for (int i = 0; i < 6; i++) {
                    if (i < tiny.size()) {

                        if (tiny.get(i).getAskType() == MainActivity.ASK_NONE) {

                            switch (tiny.get(i).getBet1()) {
                                case "1":
                                    ivBetArr[i].setImageResource(R.mipmap.banker);
                                    break;
                                case "2":
                                    ivBetArr[i].setImageResource(R.mipmap.player);
                                    break;
                                case "3":
                                    ivBetArr[i].setImageResource(R.mipmap.tie);
                                    break;
                            }

                            if (tiny.get(i).getBet2().equals("1")) {
                                ivZhuangDui[i].setVisibility(View.VISIBLE);
                            } else {
                                ivZhuangDui[i].setVisibility(View.GONE);
                            }

                            if (tiny.get(i).getBet3().equals("1")) {
                                ivXianDui[i].setVisibility(View.VISIBLE);
                            } else {
                                ivXianDui[i].setVisibility(View.GONE);
                            }

                        } else if (tiny.get(i).getAskType() == MainActivity.ASK_DEMO) {
                            ivBetArr[i].setImageDrawable(null);
                            ivZhuangDui[i].setVisibility(View.GONE);
                            ivXianDui[i].setVisibility(View.GONE);
                        } else {
                            switch (tiny.get(i).getBet1()) {
                                case "1":
                                    ivBetArr[i].setImageResource(R.mipmap.banker);
                                    break;
                                case "2":
                                    ivBetArr[i].setImageResource(R.mipmap.player);
                                    break;
                            }
                            setFlickerAnimation(ivBetArr[i]);
                        }


                    } else {
                        ivBetArr[i].setImageDrawable(null);
                        ivZhuangDui[i].setVisibility(View.GONE);
                        ivXianDui[i].setVisibility(View.GONE);
                    }


                }
            } else {
                for (int i = 0; i < 6; i++) {
                    ivBetArr[i].setImageDrawable(null);
                    ivZhuangDui[i].setVisibility(View.GONE);
                    ivXianDui[i].setVisibility(View.GONE);
                }
            }

        }
    }

    public void update(ArrayList<ArrayList<Tiny>> tinyList) {
        mTinyList = tinyList;
        notifyDataSetChanged();
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
