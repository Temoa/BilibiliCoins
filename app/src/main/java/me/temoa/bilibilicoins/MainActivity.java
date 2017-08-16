package me.temoa.bilibilicoins;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import me.temoa.mariocoins.MarioCoinsView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MarioCoinsView marioCoinsView = (MarioCoinsView) findViewById(R.id.mario_view);
        marioCoinsView.setCallback(new MarioCoinsView.MarioGetCoinsCallback() {
            @Override
            public void get(int coins) {
                Toast.makeText(MainActivity.this, "投币成功!" + coins, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
