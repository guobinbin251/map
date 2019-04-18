package com.alibaba.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cxf on 2018/10/9.
 */

public class DayanzaiAdapter extends RecyclerView.Adapter<DayanzaiAdapter.Vh> {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ArrayList<Tiny2>> mTinyList;

    public DayanzaiAdapter(Context context, ArrayList<ArrayList<Tiny2>> tinyList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTinyList = tinyList;
    }


    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_dayanzai, parent, false));
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
            ivBetArr[0] = (ImageView) itemView.findViewById(R.id.iv_bet1);
            ivBetArr[1] = (ImageView) itemView.findViewById(R.id.iv_bet2);
            ivBetArr[2] = (ImageView) itemView.findViewById(R.id.iv_bet3);
            ivBetArr[3] = (ImageView) itemView.findViewById(R.id.iv_bet4);
            ivBetArr[4] = (ImageView) itemView.findViewById(R.id.iv_bet5);
            ivBetArr[5] = (ImageView) itemView.findViewById(R.id.iv_bet6);

        }

        void setData(ArrayList<Tiny2> tiny) {
            if (tiny != null) {
                for (int i = 0; i < 6; i++) {
                    if (i < tiny.size() && !tiny.get(i).isAsk()) {
                        switch (tiny.get(i).getBet1()) {
                            case MainActivity.ZHUANG_YIN:
                                ivBetArr[i].setImageResource(R.mipmap.dl_red);
                                break;
                            case MainActivity.XIAN_YIN:
                                ivBetArr[i].setImageResource(R.mipmap.dl_blue);
                                break;
                            case MainActivity.HE:
                                ivBetArr[i].setImageResource(R.mipmap.dl_pre);
                                break;
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
}
