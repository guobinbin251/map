package com.alibaba.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public Vh(View itemView) {
            super(itemView);
            ivBetArr = new ImageView[6];
            ivBetArr[0] = (ImageView) itemView.findViewById(R.id.iv_result1);
            ivBetArr[1] = (ImageView) itemView.findViewById(R.id.iv_result2);
            ivBetArr[2] = (ImageView) itemView.findViewById(R.id.iv_result3);
            ivBetArr[3] = (ImageView) itemView.findViewById(R.id.iv_result4);
            ivBetArr[4] = (ImageView) itemView.findViewById(R.id.iv_result5);
            ivBetArr[5] = (ImageView) itemView.findViewById(R.id.iv_result6);
        }

        void setData(ArrayList<Tiny> tiny) {
            if (tiny != null) {
                for (int i = 0; i < 6; i++) {
                    if (i < tiny.size()) {
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
