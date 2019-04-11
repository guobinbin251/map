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

public class DaluAdapter extends RecyclerView.Adapter<DaluAdapter.Vh> {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ArrayList<Tiny2>> mTinyList;

    public DaluAdapter(Context context, ArrayList<ArrayList<Tiny2>> tinyList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTinyList = tinyList;
    }


    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Vh(mInflater.inflate(R.layout.item_dalu, parent, false));
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
        ImageView[] ivHePre;
        ImageView[] ivHeAft;
        TextView[] tvHe;

        public Vh(View itemView) {
            super(itemView);

            ivBetArr = new ImageView[6];
            ivBetArr[0] = (ImageView) itemView.findViewById(R.id.iv_bet1);
            ivBetArr[1] = (ImageView) itemView.findViewById(R.id.iv_bet2);
            ivBetArr[2] = (ImageView) itemView.findViewById(R.id.iv_bet3);
            ivBetArr[3] = (ImageView) itemView.findViewById(R.id.iv_bet4);
            ivBetArr[4] = (ImageView) itemView.findViewById(R.id.iv_bet5);
            ivBetArr[5] = (ImageView) itemView.findViewById(R.id.iv_bet6);

            ivHePre = new ImageView[6];
            ivHePre[0] = (ImageView) itemView.findViewById(R.id.iv_he_pre1);
            ivHePre[1] = (ImageView) itemView.findViewById(R.id.iv_he_pre2);
            ivHePre[2] = (ImageView) itemView.findViewById(R.id.iv_he_pre3);
            ivHePre[3] = (ImageView) itemView.findViewById(R.id.iv_he_pre4);
            ivHePre[4] = (ImageView) itemView.findViewById(R.id.iv_he_pre5);
            ivHePre[5] = (ImageView) itemView.findViewById(R.id.iv_he_pre6);

            ivHeAft = new ImageView[6];
            ivHeAft[0] = (ImageView) itemView.findViewById(R.id.iv_he_aft1);
            ivHeAft[1] = (ImageView) itemView.findViewById(R.id.iv_he_aft2);
            ivHeAft[2] = (ImageView) itemView.findViewById(R.id.iv_he_aft3);
            ivHeAft[3] = (ImageView) itemView.findViewById(R.id.iv_he_aft4);
            ivHeAft[4] = (ImageView) itemView.findViewById(R.id.iv_he_aft5);
            ivHeAft[5] = (ImageView) itemView.findViewById(R.id.iv_he_aft6);

            tvHe = new TextView[6];
            tvHe[0] = (TextView) itemView.findViewById(R.id.tv_he_1);
            tvHe[1] = (TextView) itemView.findViewById(R.id.tv_he_2);
            tvHe[2] = (TextView) itemView.findViewById(R.id.tv_he_3);
            tvHe[3] = (TextView) itemView.findViewById(R.id.tv_he_4);
            tvHe[4] = (TextView) itemView.findViewById(R.id.tv_he_5);
            tvHe[5] = (TextView) itemView.findViewById(R.id.tv_he_6);
        }

        void setData(ArrayList<Tiny2> tiny) {
            if (tiny != null) {
                for (int i = 0; i < 6; i++) {
                    if (i < tiny.size()) {
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
                        if(tiny.get(i).isHePre()){
                            ivHePre[i].setVisibility(View.VISIBLE);
                        }else{
                            ivHePre[i].setVisibility(View.GONE);
                        }

                        if(tiny.get(i).isHeAft()){
                            ivHeAft[i].setVisibility(View.VISIBLE);
                        }else{
                            ivHeAft[i].setVisibility(View.GONE);
                        }

                        if(tiny.get(i).getHeAmount()>1){
                            tvHe[i].setText(String.valueOf(tiny.get(i).getHeAmount()));
                            tvHe[i].setVisibility(View.VISIBLE);
                        }else{
                            tvHe[i].setText("");
                            tvHe[i].setVisibility(View.GONE);

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
