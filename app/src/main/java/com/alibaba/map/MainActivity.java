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
import android.widget.ImageView;

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

    public static final int ASK_NONE = 0;
    public static final int ASK_DEMO = 1;
    public static final int ASK_REAL_ZHUANG = 2;
    public static final int ASK_REAL_XIAN = 3;

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    RecyclerView recyclerDalu;
    DaluAdapter daluAdapter;

    RecyclerView recyclerDayanzai;
    DayanzaiAdapter dayanzaiAdapter;

    RecyclerView recyclerXiaolu;
    XiaoluAdapter xiaoluAdapter;

    RecyclerView recyclerXiaoqiang;
    XiaoqiangAdapter xiaoqiangAdapter;

    ImageView ivAsk1;
    ImageView ivAsk2;
    ImageView ivAsk3;
    ImageView ivAsk4;
    ImageView ivAsk5;
    ImageView ivAsk6;


    private String bet1 = "1";
    private String zhuangdui = "0";
    private String xiandui = "1";

    private String askZhuangDayan = "0";
    private String askZhuangXiaolu = "0";
    private String askZhuangXiaoqiang = "0";

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

        ivAsk1 = findViewById(R.id.iv_ask1);
        ivAsk2 = findViewById(R.id.iv_ask2);
        ivAsk3 = findViewById(R.id.iv_ask3);
        ivAsk4 = findViewById(R.id.iv_ask4);
        ivAsk5 = findViewById(R.id.iv_ask5);
        ivAsk6 = findViewById(R.id.iv_ask6);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(30);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerDalu = findViewById(R.id.recycler_dalu);
        recyclerDalu.setLayoutManager(linearLayoutManager2);
        recyclerDalu.setItemViewCacheSize(50);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerDayanzai = findViewById(R.id.recycler_dayanzai);
        recyclerDayanzai.setLayoutManager(linearLayoutManager3);
        recyclerDayanzai.setItemViewCacheSize(50);

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerXiaolu = findViewById(R.id.recycler_xiaolu);
        recyclerXiaolu.setLayoutManager(linearLayoutManager4);
        recyclerXiaolu.setItemViewCacheSize(50);

        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerXiaoqiang = findViewById(R.id.recycler_xiaoqianglu);
        recyclerXiaoqiang.setLayoutManager(linearLayoutManager5);
        recyclerXiaoqiang.setItemViewCacheSize(50);

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


        refreshAllList(ASK_DEMO);


    }

    private void initMap2() {

        Log.d("Map", "--start------" + System.currentTimeMillis());
        boolean tongSeshuxiang = true;   //同色竖向，true表示竖向，false表示横向
        ArrayList<ArrayList<Tiny2>> list = new ArrayList<>();
        ArrayList<ArrayList<Tiny2>> sourceList = new ArrayList<>(); //给下三路做数据源用

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

        for (int i = 0; i < 30; i++) {
            ArrayList<Tiny2> tempList = new ArrayList<>();
            for (int j = 0; j < 30; j++) {
                Tiny2 tt = new Tiny2();
                tt.setBet1(NONE_VALUE);
                tt.setHeAmount(0);
                tt.setHePre(false);
                tt.setHeAft(false);
                tempList.add(tt);
            }
            sourceList.add(tempList);
        }

        int lie = 0;
        int hang = 0;
        int realHang = 0;

        int sourceLie = 0;
        int sourceHang = 0;

        for (int i = 0; i < tinyList.size(); i++) {
            int askType = tinyList.get(i).getAskType();
            String bet1 = tinyList.get(i).getBet1();
            if (i == 0) { //第0个，做特殊判断
                lie = 0;
                hang = 0;
                realHang = 0;

                sourceLie = 0;
                sourceHang = 0;
                if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHePre(true);
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                } else if (bet1.equals(ZHUANG_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
                    sourceList.get(sourceHang).get(sourceLie).setBet1(bet1);
                    sourceList.get(sourceHang).get(sourceLie).setAskType(askType);
                } else if (bet1.equals(XIAN_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
                    sourceList.get(sourceHang).get(sourceLie).setBet1(bet1);
                    sourceList.get(sourceHang).get(sourceLie).setAskType(askType);
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
                        sourceLie++;
                    } else if (pre.getBet1().equals(XIAN_YIN)) {
                        lie = 0;
                        realHang++;
                        hang = realHang;
                        tongSeshuxiang = true;

                        sourceHang++;
                        sourceLie = 0;
                    } else if (pre.getBet1().equals(HE)) {
                        //此种情况只有第一行第一列是和才会产生
                    }
                    list.get(hang).get(lie).setBet1(ZHUANG_YIN);
                    list.get(hang).get(lie).setAskType(askType);
                    sourceList.get(sourceHang).get(sourceLie).setBet1(ZHUANG_YIN);
                    sourceList.get(sourceHang).get(sourceLie).setAskType(askType);
                } else if (bet1.equals(XIAN_YIN)) {
                    if (pre.getBet1().equals(ZHUANG_YIN)) {
                        lie = 0;
                        realHang++;
                        hang = realHang;
                        tongSeshuxiang = true;
                        sourceHang++;
                        sourceLie = 0;
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
                        sourceLie++;
                    } else if (pre.getBet1().equals(HE)) {
                        //此种情况只有第一行第一列是和才会产生
                    }
                    list.get(hang).get(lie).setBet1(XIAN_YIN);
                    list.get(hang).get(lie).setAskType(askType);
                    sourceList.get(sourceHang).get(sourceLie).setBet1(XIAN_YIN);
                    sourceList.get(sourceHang).get(sourceLie).setAskType(askType);

                } else if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                    list.get(hang).get(lie).setHeAft(true);
                }
            }
        }
        daluAdapter = new DaluAdapter(MainActivity.this, list);
        recyclerDalu.setAdapter(daluAdapter);
        /*if (hang > 13) {
            recyclerDalu.smoothScrollToPosition(hang - 5);
        }
        */

        initDaYanZai(sourceList);

        initXiaolu(sourceList);

        initXiaoqiang(sourceList);

        Log.d("Map", "--end------" + System.currentTimeMillis());
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


    private void initDaYanZai(ArrayList<ArrayList<Tiny2>> sourceList) {
        ArrayList<Tiny> dayanList = new ArrayList<>();


        for (int i = 0; i < 30; i++) { // i表示行
            for (int j = 0; j < 30; j++) { //j表示列
                //第0列忽略不计
                if (i > 0) {
                    if (i == 1) {
                        //说明二行二列有值，开始些大眼仔路
                        if (j > 0) {
                            if (!sourceList.get(i).get(j).getBet1().equals(NONE_VALUE)) {
                                Tiny tt = new Tiny();
                                if (sourceList.get(i - 1).get(j).getBet1().equals(NONE_VALUE)) {
                                    //说明左边一格没有值，则第一个大眼仔为蓝色
                                    if (sourceList.get(i - 1).get(j - 1).getBet1().equals(NONE_VALUE)) {
                                        tt.setBet1(ZHUANG_YIN); //说明是直落，
                                    } else {
                                        tt.setBet1(XIAN_YIN);   //给直落，无
                                    }
                                } else {
                                    tt.setBet1(ZHUANG_YIN); //有
                                }
                                tt.setAskType(sourceList.get(i).get(j).getAskType());
                                dayanList.add(tt);
                            }
                        }

                    } else {
                        if (!sourceList.get(i).get(j).getBet1().equals(NONE_VALUE)) {
                            Tiny tt = new Tiny();
                            if (j == 0) {
                                //第一行，看前面两列的和对比
                                if (getLieAmount(sourceList.get(i - 1)) == getLieAmount(sourceList.get(i - 2))) {
                                    tt.setBet1(ZHUANG_YIN);
                                } else {
                                    tt.setBet1(XIAN_YIN);
                                }
                            } else {
                                //非第一行，看有无和直落
                                if (sourceList.get(i - 1).get(j).getBet1().equals(NONE_VALUE)) {
                                    //说明左边一格没有值，则第一个大眼仔为蓝色
                                    if (sourceList.get(i - 1).get(j - 1).getBet1().equals(NONE_VALUE)) {
                                        tt.setBet1(ZHUANG_YIN); //说明是直落，
                                    } else {
                                        tt.setBet1(XIAN_YIN);   //给直落，无
                                    }
                                } else {
                                    tt.setBet1(ZHUANG_YIN); //有
                                }
                            }
                            tt.setAskType(sourceList.get(i).get(j).getAskType());
                            dayanList.add(tt);
                        }
                    }
                }
            }
        }

        //此处顺便做好问路的预测
        for (Tiny ti : dayanList) {
            if (ti.getAskType() == ASK_REAL_ZHUANG || ti.getAskType() == ASK_DEMO) {
                askZhuangDayan = ti.getBet1();
            } else if (ti.getAskType() == ASK_REAL_XIAN) {
                askZhuangDayan = ti.getBet1().equals(ZHUANG_YIN) ? XIAN_YIN : ZHUANG_YIN;
            }
        }

        //大眼数据准备完毕，接下来要和大路做同样的算法
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


        for (int i = 0; i < dayanList.size(); i++) {
            String bet1 = dayanList.get(i).getBet1();
            int askType = dayanList.get(i).getAskType();
            if (i == 0) { //第0个，做特殊判断
                lie = 0;
                hang = 0;
                realHang = 0;

                if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHePre(true);
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                } else if (bet1.equals(ZHUANG_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
                } else if (bet1.equals(XIAN_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
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
                    list.get(hang).get(lie).setAskType(askType);
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
                    list.get(hang).get(lie).setAskType(askType);
                } else if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                    list.get(hang).get(lie).setHeAft(true);
                }
            }
        }

        dayanzaiAdapter = new DayanzaiAdapter(MainActivity.this, list);
        recyclerDayanzai.setAdapter(dayanzaiAdapter);


    }

    private void initXiaolu(ArrayList<ArrayList<Tiny2>> sourceList) {
        ArrayList<Tiny> xiaoluList = new ArrayList<>();

        for (int i = 0; i < 30; i++) { // i表示行
            for (int j = 0; j < 30; j++) { //j表示列
                //第0和1列忽略不计
                if (i > 1) {
                    if (i == 2) {
                        //说明三行二列有值，开始些大眼仔路
                        if (j > 0) {
                            if (!sourceList.get(i).get(j).getBet1().equals(NONE_VALUE)) {
                                Tiny tt = new Tiny();
                                if (sourceList.get(i - 2).get(j).getBet1().equals(NONE_VALUE)) {
                                    //说明左边一格没有值，则第一个大眼仔为蓝色
                                    if (sourceList.get(i - 2).get(j - 1).getBet1().equals(NONE_VALUE)) {
                                        tt.setBet1(ZHUANG_YIN); //说明是直落，
                                    } else {
                                        tt.setBet1(XIAN_YIN);   //给直落，无
                                    }
                                } else {
                                    tt.setBet1(ZHUANG_YIN); //有
                                }
                                tt.setAskType(sourceList.get(i).get(j).getAskType());
                                xiaoluList.add(tt);
                            }
                        }

                    } else {
                        if (!sourceList.get(i).get(j).getBet1().equals(NONE_VALUE)) {
                            Tiny tt = new Tiny();
                            if (j == 0) {
                                //第一行，看前面两列的和对比
                                if (getLieAmount(sourceList.get(i - 1)) == getLieAmount(sourceList.get(i - 3))) {
                                    tt.setBet1(ZHUANG_YIN);
                                } else {
                                    tt.setBet1(XIAN_YIN);
                                }
                            } else {
                                //非第一行，看有无和直落
                                if (sourceList.get(i - 2).get(j).getBet1().equals(NONE_VALUE)) {
                                    //说明左边一格没有值，则第一个大眼仔为蓝色
                                    if (sourceList.get(i - 2).get(j - 1).getBet1().equals(NONE_VALUE)) {
                                        tt.setBet1(ZHUANG_YIN); //说明是直落，
                                    } else {
                                        tt.setBet1(XIAN_YIN);   //给直落，无
                                    }
                                } else {
                                    tt.setBet1(ZHUANG_YIN); //有
                                }
                            }
                            tt.setAskType(sourceList.get(i).get(j).getAskType());
                            xiaoluList.add(tt);
                        }
                    }
                }
            }
        }


        //此处顺便做好问路的预测
        for (Tiny ti : xiaoluList) {
            if (ti.getAskType() == ASK_REAL_ZHUANG || ti.getAskType() == ASK_DEMO) {
                askZhuangXiaolu = ti.getBet1();
            } else if (ti.getAskType() == ASK_REAL_XIAN) {
                askZhuangXiaolu = ti.getBet1().equals(ZHUANG_YIN) ? XIAN_YIN : ZHUANG_YIN;
            }
        }

        //数据准备完毕，接下来要和大路做同样的算法


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


        for (int i = 0; i < xiaoluList.size(); i++) {
            String bet1 = xiaoluList.get(i).getBet1();
            int askType = xiaoluList.get(i).getAskType();
            if (i == 0) { //第0个，做特殊判断
                lie = 0;
                hang = 0;
                realHang = 0;

                if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHePre(true);
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                } else if (bet1.equals(ZHUANG_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
                } else if (bet1.equals(XIAN_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
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
                    list.get(hang).get(lie).setAskType(askType);
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
                    list.get(hang).get(lie).setAskType(askType);
                } else if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                    list.get(hang).get(lie).setHeAft(true);
                }
            }
        }

        xiaoluAdapter = new XiaoluAdapter(MainActivity.this, list);
        recyclerXiaolu.setAdapter(xiaoluAdapter);


    }


    private void initXiaoqiang(ArrayList<ArrayList<Tiny2>> sourceList) {
        ArrayList<Tiny> xiaoqiangList = new ArrayList<>();

        for (int i = 0; i < 30; i++) { // i表示行
            for (int j = 0; j < 30; j++) { //j表示列
                //第0和1列忽略不计
                if (i > 2) {
                    if (i == 3) {
                        //说明三行二列有值，开始些大眼仔路
                        if (j > 0) {
                            if (!sourceList.get(i).get(j).getBet1().equals(NONE_VALUE)) {
                                Tiny tt = new Tiny();
                                if (sourceList.get(i - 3).get(j).getBet1().equals(NONE_VALUE)) {
                                    //说明左边一格没有值，则第一个大眼仔为蓝色
                                    if (sourceList.get(i - 3).get(j - 1).getBet1().equals(NONE_VALUE)) {
                                        tt.setBet1(ZHUANG_YIN); //说明是直落，
                                    } else {
                                        tt.setBet1(XIAN_YIN);   //给直落，无
                                    }
                                } else {
                                    tt.setBet1(ZHUANG_YIN); //有
                                }
                                tt.setAskType(sourceList.get(i).get(j).getAskType());
                                xiaoqiangList.add(tt);
                            }
                        }

                    } else {
                        if (!sourceList.get(i).get(j).getBet1().equals(NONE_VALUE)) {
                            Tiny tt = new Tiny();
                            if (j == 0) {
                                //第一行，看前面两列的和对比
                                if (getLieAmount(sourceList.get(i - 1)) == getLieAmount(sourceList.get(i - 4))) {
                                    tt.setBet1(ZHUANG_YIN);
                                } else {
                                    tt.setBet1(XIAN_YIN);
                                }
                            } else {
                                //非第一行，看有无和直落
                                if (sourceList.get(i - 3).get(j).getBet1().equals(NONE_VALUE)) {
                                    //说明左边一格没有值，则第一个大眼仔为蓝色
                                    if (sourceList.get(i - 3).get(j - 1).getBet1().equals(NONE_VALUE)) {
                                        tt.setBet1(ZHUANG_YIN); //说明是直落，
                                    } else {
                                        tt.setBet1(XIAN_YIN);   //给直落，无
                                    }
                                } else {
                                    tt.setBet1(ZHUANG_YIN); //有
                                }
                            }
                            tt.setAskType(sourceList.get(i).get(j).getAskType());
                            xiaoqiangList.add(tt);
                        }
                    }
                }
            }
        }

        //此处顺便做好问路的预测
        for (Tiny ti : xiaoqiangList) {
            if (ti.getAskType() == ASK_REAL_ZHUANG || ti.getAskType() == ASK_DEMO) {
                askZhuangXiaoqiang = ti.getBet1();
            } else if (ti.getAskType() == ASK_REAL_XIAN) {
                askZhuangXiaoqiang = ti.getBet1().equals(ZHUANG_YIN) ? XIAN_YIN : ZHUANG_YIN;
            }
        }


        //大眼数据准备完毕，接下来要和大路做同样的算法


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


        for (int i = 0; i < xiaoqiangList.size(); i++) {
            String bet1 = xiaoqiangList.get(i).getBet1();
            int askType = xiaoqiangList.get(i).getAskType();
            if (i == 0) { //第0个，做特殊判断
                lie = 0;
                hang = 0;
                realHang = 0;

                if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHePre(true);
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                } else if (bet1.equals(ZHUANG_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
                } else if (bet1.equals(XIAN_YIN)) {
                    list.get(hang).get(lie).setBet1(bet1);
                    list.get(hang).get(lie).setAskType(askType);
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
                    list.get(hang).get(lie).setAskType(askType);
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
                    list.get(hang).get(lie).setAskType(askType);
                } else if (bet1.equals(HE)) {
                    list.get(hang).get(lie).setHeAmount(list.get(hang).get(lie).getHeAmount() + 1);
                    list.get(hang).get(lie).setHeAft(true);
                }
            }
        }
        xiaoqiangAdapter = new XiaoqiangAdapter(MainActivity.this, list);
        recyclerXiaoqiang.setAdapter(xiaoqiangAdapter);
    }

    private int getLieAmount(ArrayList<Tiny2> list) {
        int amount = 0;
        for (Tiny2 tn : list) {
            if (!tn.getBet1().equals(NONE_VALUE)) {
                amount++;
            }
        }
        return amount;
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
        refreshAllList(ASK_DEMO);
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
        refreshAllList(ASK_DEMO);
    }

    public void askZhuang(View view) {
        refreshAllList(ASK_REAL_ZHUANG);
    }

    public void askXian(View view) {
        refreshAllList(ASK_REAL_XIAN);
    }

    private void refreshAllList(int askType) {
        //先清除掉问路的数据
        askZhuangDayan = "0";
        askZhuangXiaolu = "0";
        askZhuangXiaoqiang = "0";


        int index = -1;
        for (int i = 0; i < tinyList.size(); i++) {
            if (tinyList.get(i).getAskType() != 0) {
                //不等于0的全部都是问路数据
                index = i;
            }
        }
        if (index >= 0) {
            tinyList.remove(index);
        }


        //新增一个问路数据
        Tiny askTiny = new Tiny();

        askTiny.setAskType(askType);
        if (askType == ASK_DEMO || askType == ASK_REAL_ZHUANG) {
            askTiny.setBet1(ZHUANG_YIN);
        } else if (askType == ASK_REAL_XIAN) {
            askTiny.setBet1(XIAN_YIN);
        }
        askTiny.setBet2(NONE_VALUE);
        askTiny.setBet3(NONE_VALUE);
        tinyList.add(askTiny);

        initMap1();
        initMap2();

        if (askZhuangDayan.equals("1")) {
            ivAsk1.setImageResource(R.mipmap.dl_red);
            ivAsk4.setImageResource(R.mipmap.dl_blue);
            ivAsk1.setVisibility(View.VISIBLE);
            ivAsk4.setVisibility(View.VISIBLE);
        } else if (askZhuangDayan.equals("2")) {
            ivAsk1.setImageResource(R.mipmap.dl_blue);
            ivAsk4.setImageResource(R.mipmap.dl_red);
            ivAsk1.setVisibility(View.VISIBLE);
            ivAsk4.setVisibility(View.VISIBLE);
        } else {
            ivAsk1.setVisibility(View.INVISIBLE);
            ivAsk4.setVisibility(View.INVISIBLE);
        }


        if (askZhuangXiaolu.equals("1")) {
            ivAsk2.setImageResource(R.mipmap.banker1);
            ivAsk5.setImageResource(R.mipmap.player1);
            ivAsk2.setVisibility(View.VISIBLE);
            ivAsk5.setVisibility(View.VISIBLE);
        } else if (askZhuangXiaolu.equals("2")) {
            ivAsk2.setImageResource(R.mipmap.player1);
            ivAsk5.setImageResource(R.mipmap.banker1);
            ivAsk2.setVisibility(View.VISIBLE);
            ivAsk5.setVisibility(View.VISIBLE);
        } else {
            ivAsk2.setVisibility(View.INVISIBLE);
            ivAsk5.setVisibility(View.INVISIBLE);
        }


        if (askZhuangXiaoqiang.equals("1")) {
            ivAsk3.setImageResource(R.mipmap.gaza_red);
            ivAsk6.setImageResource(R.mipmap.gaza_blue);
            ivAsk3.setVisibility(View.VISIBLE);
            ivAsk6.setVisibility(View.VISIBLE);
        } else if (askZhuangXiaoqiang.equals("2")) {
            ivAsk3.setImageResource(R.mipmap.gaza_blue);
            ivAsk6.setImageResource(R.mipmap.gaza_red);
            ivAsk3.setVisibility(View.VISIBLE);
            ivAsk6.setVisibility(View.VISIBLE);
        } else {
            ivAsk3.setVisibility(View.INVISIBLE);
            ivAsk6.setVisibility(View.INVISIBLE);
        }


    }


}
