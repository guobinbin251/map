package com.alibaba.map;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    RecyclerView recyclerDalu;
    DaluAdapter daluAdapter;

    private String bet1 = "1";
    private String zhuangdui = "0";
    private String xiandui = "1";

    ArrayList<Tiny> tinyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(30);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerDalu = findViewById(R.id.recycler_dalu);
        recyclerDalu.setLayoutManager(linearLayoutManager2);
        recyclerDalu.setItemViewCacheSize(50);

        String json = getJson("maps.json", MainActivity.this);

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


        initMap1();

        initMap2();


    }

    private void initMap2() {
        boolean tongSeshuxiang = true;   //同色竖向，true表示竖向，false表示横向
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
                    list.get(hang).get(lie).setHePre(true);
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                } else if (bet1.equals(ZHUANG_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                } else if (bet1.equals(XIAN_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                }
            } else {
                Tiny2 pre = list.get(hang).get(lie);
                if (bet1.equals(ZHUANG_YIN)) {
                    if (pre.getBet1().equals(ZHUANG_YIN)) {
                        if (lie < 5) {
                            if (list.get(hang).get(lie + 1).getBet1().equals(XIAN_YIN)
                                    || list.get(hang).get(lie + 1).getBet1().equals(ZHUANG_YIN)
                                    || !tongSeshuxiang) {
                                hang++;
                                tongSeshuxiang = false;
                                if (lie == 0) {
                                    realHang = hang;
                                }
                            } else {
                                lie++;
                                tongSeshuxiang = true;
                            }
                        } else {
                            hang++;
                            tongSeshuxiang = false;
                        }
                    } else if (pre.getBet1().equals(XIAN_YIN)) {
                        lie = 0;
                        realHang++;
                        hang = realHang;
                        tongSeshuxiang = true;

                    } else if (pre.getBet1().equals(HE)) {
                        //此种情况只有第一行第一列是和才会产生
                    }
                    list.get(hang).get(lie).setBet1(ZHUANG_YIN);
                } else if (bet1.equals(XIAN_YIN)) {
                    if (pre.getBet1().equals(ZHUANG_YIN)) {
                        lie = 0;
                        realHang++;
                        hang = realHang;
                        tongSeshuxiang = true;
                    } else if (pre.getBet1().equals(XIAN_YIN)) {
                        if (lie < 5) {
                            if (list.get(hang).get(lie + 1).getBet1().equals(ZHUANG_YIN)
                                    || list.get(hang).get(lie + 1).getBet1().equals(XIAN_YIN)
                                    || !tongSeshuxiang) {
                                hang++;
                                tongSeshuxiang = false;
                                if (lie == 0) {
                                    realHang = hang;
                                }

                            } else {
                                lie++;
                                tongSeshuxiang = true;
                            }
                        } else {
                            hang++;
                            tongSeshuxiang = false;
                        }

                    } else if (pre.getBet1().equals(HE)) {
                        //此种情况只有第一行第一列是和才会产生
                    }
                    list.get(hang).get(lie).setBet1(XIAN_YIN);

                } else if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                    list.get(hang).get(lie).setHeAft(true);
                }
            }
        }
        daluAdapter = new DaluAdapter(MainActivity.this, list);
        recyclerDalu.setAdapter(daluAdapter);
        if (hang > 13) {
            recyclerDalu.smoothScrollToPosition(hang - 5);
        }

    }


    private void initMap1() {
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

    public void clear(View view) {
        tinyList.clear();
        refreshAllList();
    }

    public void zhuang(View view) {
        bet1 = "1";
    }

    public void xian(View view) {
        bet1 = "2";
    }

    public void he(View view) {
        bet1 = "3";
    }

    public void zhuangdui(View view) {
        if (zhuangdui.equals("0")) {
            zhuangdui = "1";
        } else {
            zhuangdui = "0";
        }

    }

    public void xiandui(View view) {
        if (xiandui.equals("0")) {
            xiandui = "1";
        } else {
            xiandui = "0";
        }
    }

    public void queding(View view) {
        Tiny tiny = new Tiny();
        tiny.setBet1(bet1);
        tiny.setBet2(zhuangdui);
        tiny.setBet3(xiandui);
        tinyList.add(tiny);
        refreshAllList();
    }

    private void refreshAllList() {
        initMap1();
        initMap2();

    }


}
