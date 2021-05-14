package com.uodreams.tmgutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.uodreams.tmgutils.adapter.GenericSpinnerAdapter;
import com.uodreams.tmgutils.conf.Const;

import java.util.ArrayList;

public class ChampAlertActivity extends AppCompatActivity {
    public static final String CHAAAMP_ACTION = Const.PKG + ".action.CHAAAMP_ACTION";
    private Boolean[] mSkullStates = { false, false, false, false, false };
    private Boolean[] mWhose = { false, false, false, false };
    private Boolean[] mRaiders = { false, false, false, false };
    private String[] mChamps = {
            "Abyss", "Barracoon", "Bucca", "Dragon Turtle", "Harrower",
            "Mephitis", "Neira", "Oaks", "Osiredon", "Primeval Lich", "Semidar"
    };

    private String[] mLocations = {
            "Original", "City", "Ice East", "Ice West", "Khaldun", "Oasis", "Swamp", "Terra Sanctum", "Tortoise"
    };

    private TextView ivPrimo;
    private TextView ivSecondo;
    private TextView ivTerzo;
    private TextView ivQuarto;
    private TextView ivFuori;

    private Spinner spChampions;
    private Spinner spLocations;

    private TextView ivWCom;
    private TextView ivWMin;
    private TextView ivWSL;
    private TextView ivWTB;

    private TextView ivRCom;
    private TextView ivRMin;
    private TextView ivRSL;
    private TextView ivRTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_alert);

        this.ivPrimo = findViewById(R.id.ivPrimo);
        this.ivSecondo = findViewById(R.id.ivSecondo);
        this.ivTerzo = findViewById(R.id.ivTerzo);
        this.ivQuarto = findViewById(R.id.ivQuarto);
        this.ivFuori = findViewById(R.id.ivFuori);

        GenericSpinnerAdapter champAdapter = new GenericSpinnerAdapter(this, mChamps);
        this.spChampions = (Spinner) findViewById(R.id.spChampions);
        this.spChampions.setAdapter(champAdapter);

        GenericSpinnerAdapter locAdapter = new GenericSpinnerAdapter(this, mLocations);
        this.spLocations = (Spinner) findViewById(R.id.spLocation);
        this.spLocations.setAdapter(locAdapter);

        this.ivWCom = (TextView) findViewById(R.id.ivWCom);
        this.ivWMin = (TextView) findViewById(R.id.ivWMin);
        this.ivWSL = (TextView) findViewById(R.id.ivWSL);
        this.ivWTB = (TextView) findViewById(R.id.ivWTB);

        this.ivRCom = (TextView) findViewById(R.id.ivRCom);
        this.ivRMin = (TextView) findViewById(R.id.ivRMin);
        this.ivRSL = (TextView) findViewById(R.id.ivRSL);
        this.ivRTB = (TextView) findViewById(R.id.ivRTB);
    }

    public void skull_Click(View view) {
        int index = Integer.parseInt(view.getTag().toString());
        if (this.mSkullStates[index]) {
            switch (index) {
                case 4:
                    this.ivFuori.setTextColor(getResources().getColor(R.color.pal_II));
                    this.mSkullStates[4] = false;
                case 3:
                    if (this.mSkullStates[4]) {
                        this.ivFuori.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[4] = false;
                        break;
                    }
                    this.ivQuarto.setTextColor(getResources().getColor(R.color.pal_II));
                    this.mSkullStates[3] = false;
                case 2:
                    if (this.mSkullStates[3]) {
                        this.ivFuori.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[4] = false;
                        this.ivQuarto.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[3] = false;
                        break;
                    }
                    this.ivTerzo.setTextColor(getResources().getColor(R.color.pal_II));
                    this.mSkullStates[2] = false;
                case 1:
                    if (this.mSkullStates[2]) {
                        this.ivFuori.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[4] = false;
                        this.ivQuarto.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[3] = false;
                        this.ivTerzo.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[2] = false;
                        break;
                    }
                    this.ivSecondo.setTextColor(getResources().getColor(R.color.pal_II));
                    this.mSkullStates[1] = false;
                case 0:
                    if (this.mSkullStates[1]) {
                        this.ivFuori.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[4] = false;
                        this.ivQuarto.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[3] = false;
                        this.ivTerzo.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[2] = false;
                        this.ivSecondo.setTextColor(getResources().getColor(R.color.pal_II));
                        this.mSkullStates[1] = false;
                        break;
                    }
                    this.ivPrimo.setTextColor(getResources().getColor(R.color.pal_II));
                    this.mSkullStates[0] = false;
            }
        } else {
            switch (index) {
                case 4:
                    this.ivFuori.setTextColor(getResources().getColor(R.color.pal_I));
                    this.mSkullStates[4] = true;
                case 3:
                    this.ivQuarto.setTextColor(getResources().getColor(R.color.pal_I));
                    this.mSkullStates[3] = true;
                case 2:
                    this.ivTerzo.setTextColor(getResources().getColor(R.color.pal_I));
                    this.mSkullStates[2] = true;
                case 1:
                    this.ivSecondo.setTextColor(getResources().getColor(R.color.pal_I));
                    this.mSkullStates[1] = true;
                case 0:
                    this.ivPrimo.setTextColor(getResources().getColor(R.color.pal_I));
                    this.mSkullStates[0] = true;
                    break;
            }
        }
    }

    public void belongClick(View view) {
        int index = Integer.parseInt(view.getTag().toString());
        if (this.mWhose[index])
            index = -1;
        final TextView[] textViews = {this.ivWCom,this.ivWMin,this.ivWSL,this.ivWTB};
        for (int i = 0; i < mWhose.length; ++i) {
            this.mWhose[i] = (i == index);
            textViews[i].setTextColor(getResources().getColor((index == i) ? R.color.pal_I : R.color.pal_II));
        }
    }

    public void raiderClick(View view) {
        int index = Integer.parseInt(view.getTag().toString());
        if(this.mRaiders[index])
            index = -1;
        final TextView[] textViews = {this.ivRCom,this.ivRMin,this.ivRSL,this.ivRTB};
        for (int i = 0; i < mRaiders.length; ++i) {
            this.mRaiders[i] = (i == index);
            textViews[i].setTextColor(getResources().getColor((index == i) ? R.color.pal_I : R.color.pal_II));
        }
    }
}
