package com.elena.fgosy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentChangeObject extends Fragment {
    Button btnAdd;
    EditText etName, etInfo, etNumber;

    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_object, container, false);

        //получаем данные об объекте который будем менять с прошлого фрагмента
        String getname = getArguments().getString("name");
        String getid = getArguments().getString("id");
        String getnumber = getArguments().getString("number");
        String getinfo = getArguments().getString("info");

        //заполняем поля полученными данными
        etName = (EditText) v.findViewById(R.id.editTextName);
        etInfo = (EditText) v.findViewById(R.id.editTextInfo);
        etNumber = (EditText) v.findViewById(R.id.editTextNumber);

        etName.setText(getname);
        etInfo.setText(getinfo);
        etNumber.setText(getnumber);

        //получаем контекст активити
        Activity activity = getActivity();

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);

        btnAdd = (Button) v.findViewById(R.id.btnAdd);

        // по кнопке добавляем новый объект в бд и открываем фрагмент со списком
        btnAdd.setOnClickListener(new View.OnClickListener() {
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
                //заменяем наш объект на новый с данными, которые ввели в текстбоксы
                int rowID = db.update("mytable2", cv,"id = ?", new String[]{getid});
                // закрываем подключение к БД
                dbHelper.close();
                //открываем предыдущий фрагмент со списком
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
