package com.alibaba.map;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String NONE_VALUE = "0";
    public static final String ZHUANG_YIN = "1";
    public static final String XIAN_YIN = "2";
    public static final String HE = "3";
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(30);
        String json = getJson("maps.json", MainActivity.this);
        ArrayList<Tiny> tinyList = new ArrayList<>();
        try {
            ArrayList<String> shoeData = JsonUtils.fromJson(json, new TypeToken<ArrayList<String>>() {
            }.getType());
            for (String bet : shoeData) {
                Tiny ti = new Tiny();
                String[] p = bet.split(":");
                String[] p2 = p[1].split(",");
                ti.setGid(p[0]);
                ti.setBet1(p2[0]);
                ti.setBet2(p2[1]);
                ti.setBet3(p2[2]);
                tinyList.add(ti);
            }
        } catch (JsonErrorException e) {
            e.printStackTrace();
        }


        //initMap1(tinyList);

        initMap2(tinyList);


    }

    private void initMap2(ArrayList<Tiny> tinyList) {
        ArrayList<ArrayList<Tiny2>> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ArrayList<Tiny2> tempList = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                Tiny2 tt = new Tiny2();
                tt.setBet1(NONE_VALUE);
                tt.setHeAmount(0);
                tt.setHePre(false);
                tt.setHeAft(false);
                tempList.add(tt);
            }
            list.add(tempList);
        }


        int lie = 0;
        int hang = 0;
        int realHang = 0;
        for (int i = 0; i < tinyList.size(); i++) {
            String bet1 = tinyList.get(i).getBet1();
            if (i == 0) { //第0个，做特殊判断
                lie = 0;
                hang = 0;
                realHang = 0;
                if (bet1.equals(HE)) {
                    list.get(lie).get(hang).setHePre(true);
                    list.get(lie).get(hang).setHeAmount(list.get(lie).get(hang).getHeAmount() + 1);
                } else if (bet1.equals(ZHUANG_YIN)) {
                    list.get(lie).get(hang).setBet1(bet1);
                } else if (bet1.equals(XIAN_YIN)) {
                    list.get(lie).get(hang).setBet1(bet1);
                }
            } else {
                Tiny2 pre = list.get(lie).get(hang);
                if (bet1.equals(ZHUANG_YIN)) {
                    if (pre.getBet1().equals(ZHUANG_YIN)) {
                        if (lie < 5) {
                            lie++;
                        } else {
                            hang++;
                        }
                    } else if (pre.getBet1().equals(XIAN_YIN)) {
                        lie = 0;
                        realHang++;
                        hang = realHang;

                    } else if (pre.getBet1().equals(HE)) {
                        //此种情况只有第一行第一列是和才会产生
                    }
                    list.get(lie).get(hang).setBet1(ZHUANG_YIN);
                } else if (bet1.equals(XIAN_YIN)) {
                    if (pre.getBet1().equals(ZHUANG_YIN)) {
                        lie = 0;
                        realHang++;
                        hang = realHang;
                    } else if (pre.getBet1().equals(XIAN_YIN)) {
                        if (lie < 5) {
                            lie++;
                        } else {
                            hang++;
                        }

                    } else if (pre.getBet1().equals(HE)) {
                        //此种情况只有第一行第一列是和才会产生
                    }
                    list.get(lie).get(hang).setBet1(XIAN_YIN);

                } else if (bet1.equals(HE)) {
                    list.get(lie).get(hang).setHeAmount(list.get(lie).get(hang).getHeAmount()+1);
                    list.get(lie).get(hang).setHeAft(true);
                }
            }
        }


    }


    private void initMap1(ArrayList<Tiny> tinyList) {
        int listListSize;
        if (tinyList.size() % 6 == 0) {
            listListSize = tinyList.size() / 6;
        } else {
            listListSize = tinyList.size() / 6 + 1;
        }

        ArrayList<ArrayList<Tiny>> list = new ArrayList<>();
        for (int i = 0; i < listListSize; i++) {
            ArrayList<Tiny> one = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                int index = i * 6 + j;
                if (index < tinyList.size()) {
                    Tiny ti = tinyList.get(i * 6 + j);

                    one.add(ti);
                }
            }
            list.add(one);
        }

        mainAdapter = new MainAdapter(MainActivity.this, list);
        recyclerView.setAdapter(mainAdapter);
    }

    public String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
