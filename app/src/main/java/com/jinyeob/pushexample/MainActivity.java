package com.jinyeob.pushexample;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button button;

    static int[] period = {1, 5, 5, 10, 10, 60, 60, 120, 120};
    Handler mainThreadHandler;
    static String formatDate = "";
    static TextView textPriod;
    static TextView textView3;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        textPriod=findViewById(R.id.text_priod);
        textView3=findViewById(R.id.textView3);

        final sosTask thread = new sosTask();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sosGlobalVar.setSos(true);

                sosGlobalVar.setCurrent(0);

                // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
                // nowDate 변수에 값을 저장한다.
                formatDate = "시작 -> " + sdfNow.format(date) + "\n";

                textPriod.setText(period[sosGlobalVar.getCurrent()] + "분");
                textView3.setText(formatDate);

                thread.start();

            }
        });

        mainThreadHandler = new MyHandler(this);

        try {
            Intent intent = getIntent();
            if ((Objects.requireNonNull(intent.getExtras()).getInt("chk") == 0)) {
                thread.interrupt();
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }

    } catch (NullPointerException e) {
    }
    }

    private class sosTask extends Thread {
        @Override
        public void run() {
            super.run();
            while (sosGlobalVar.getSos()) {
                sosGlobalVar.setWorkValue(sosGlobalVar.getWorkValue()+1);  // 작업스레드 값 증가
                try {
                    Thread.sleep(period[sosGlobalVar.getCurrent()] * 60 * 1000);
                    sosGlobalVar.setCurrent(sosGlobalVar.getCurrent()+1);
                    if (sosGlobalVar.getCurrent() == 9) {
                        sosGlobalVar.setCurrent(7);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(sosGlobalVar.getSos()) {
                    // Create a message in child thread.
                    Message childThreadMessage = new Message();
                    childThreadMessage.what = 1;
                    childThreadMessage.arg1 = sosGlobalVar.getWorkValue();
                    // Put the message in main thread message queue.
                    mainThreadHandler.sendMessage(childThreadMessage);
                }
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        private MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 1) {
                    textPriod.setText(period[sosGlobalVar.getCurrent()] + "분");

                    // 현재시간을 msec 으로 구한다.
                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date date = new Date(now);
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
                    // nowDate 변수에 값을 저장한다.
                    formatDate += "~ " + period[sosGlobalVar.getCurrent() - 1] + "분 -> " + sdfNow.format(date) + "\n";

                    textView3.setText(formatDate);
                }
            }
        }
    }
}
