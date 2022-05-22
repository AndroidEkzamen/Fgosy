package com.elena.fgosy;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FragmentAddObject extends Fragment {


    Button btnAdd;
    EditText etName, etInfo, etNumber;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_object, container, false);

        etName = (EditText) v.findViewById(R.id.editTextName);
        etInfo = (EditText) v.findViewById(R.id.editTextInfo);
        etNumber = (EditText) v.findViewById(R.id.editTextNumber);

        //получаем контекст активити
        Activity activity = getActivity();

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);


        btnAdd = (Button) v.findViewById(R.id.btnAdd);

        // по кнопке добавляем новый объект в бд и открываем фрагмент со списком
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // создаем объект для данных
                ContentValues cv = new ContentValues();

                // получаем данные из полей ввода
                String name = etName.getText().toString();
                String info = etInfo.getText().toString();
                String number = etNumber.getText().toString();

                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                cv.put("name", name);
                cv.put("number", Integer.parseInt(number));
                cv.put("info", info);
                long rowID = db.insert("mytable2", null, cv);
                // закрываем подключение к БД
                dbHelper.close();

                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

                FragmentList frag = new FragmentList();
                mFragmentTransaction.replace(R.id.fragment_conteiner, frag);
                mFragmentTransaction.commit();
            }
        });

        return v;
    }
}