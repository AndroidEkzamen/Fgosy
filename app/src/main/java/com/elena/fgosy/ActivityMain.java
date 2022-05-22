package com.elena.fgosy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class ActivityMain extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //добавляем в MainActivity -  FragmentList
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        FragmentList frag = new FragmentList();
        mFragmentTransaction.add(R.id.fragment_conteiner, frag);
        mFragmentTransaction.commit();
    }
}