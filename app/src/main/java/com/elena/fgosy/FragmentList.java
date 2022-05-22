package com.elena.fgosy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

//основной фрагмент, который используется в main activity 
public class FragmentList extends Fragment {

    ArrayList<MyObject> myObjects = new ArrayList();
    MyObjectAdapter adapter;
    Activity activity;
    DBHelper dbHelper;
    private static final String FILE_NAME = "data.json";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = v.findViewById(R.id.listView);

        //создаем адаптерeiu
        activity = getActivity();
        dbHelper = new DBHelper(activity);
        //заполняем myObjects данными из бд
        fillData();
        adapter = new MyObjectAdapter(activity,R.layout.list_view_item, myObjects);
        //добавляем адаптер в listView
        listView.setAdapter(adapter);

        //обработчик события нажатия на элемент в списке
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //берем объекта из списка по его номеру
                MyObject obj = myObjects.get(position);
                Toast.makeText( getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                //getActivity().getFragmentManager().beginTransaction().remove(ListFragment.this).commit();

                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                //эта переменная нужна, чтобы передать ее на новый фрагмент для изменения данных
                Bundle bundle = new Bundle();
                //заполняем данные об объекте, который будем передавать
                bundle.putString("id", String.valueOf(obj.getId()));
                bundle.putString("name", obj.getObjectName());
                bundle.putString("number", String.valueOf(obj.getObjectNumber()));
                bundle.putString("info", obj.getObjectInfo());
                //открываем новый фрагмент и передаем в него выбранный объект
                FragmentChangeObject frag = new FragmentChangeObject();
                frag.setArguments(bundle);
                mFragmentTransaction.replace(R.id.fragment_conteiner, frag);
                mFragmentTransaction.commit();
            }
        });

        //кнопка для создания нового объекта
        Button buttonAddItem = v.findViewById(R.id.btn_add_item);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //запускаем новый фрагмент для поиска изображения
                getActivity().getFragmentManager().beginTransaction().remove(FragmentList.this).commit();
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                FragmentAddObject frag = new FragmentAddObject();
                mFragmentTransaction.replace(R.id.fragment_conteiner, frag);
                mFragmentTransaction.commit();
            }
        });

        //формируем отчет по нажатию на кнопку фильтр
        Button buttonFilter = v.findViewById(R.id.btn_filter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityOtchet.class);
                //Создаем новый список объектов, которые подходят под условия фильтра
                ArrayList<MyObject> search = new ArrayList<MyObject>();
                for (int i = 0; i < myObjects.size(); i++) {
                    //условие поиска - поле "номер" >=100
                    //if (myObjects.get(i).getObjectName() == "name") {
                   // if (myObjects.get(i).getObjectName().contains("1")) {
                     if (myObjects.get(i).getObjectNumber() >= 100) {
                        search.add(myObjects.get(i));
                    }
                }
                //передаем созданный список на ActivityOtchet
                intent.putExtra("MyClass", search);
                startActivity(intent);
            }
        });

        //кнопка, которая сохраняем данные в json файл
        Button buttonSaveJson = v.findViewById(R.id.btn_save_json);
        buttonSaveJson.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(myObjects);

                try(FileOutputStream fileOutputStream =
                            activity.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
                    System.out.println(getActivity().getDataDir());
                    fileOutputStream.write(jsonString.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //кнопка, которая обновляет базу данных - заменяем все данные в бд на те, что сохранены в json файле и обновляем список
        Button buttonImportJson = v.findViewById(R.id.btn_import_json);
        buttonImportJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try(FileInputStream fileInputStream = activity.openFileInput(FILE_NAME);
                    InputStreamReader streamReader = new InputStreamReader(fileInputStream)){
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<MyObject>>() {}.getType();
                    ArrayList<MyObject> Temp = gson.fromJson(streamReader, type);
                    myObjects.clear();
                    myObjects.addAll(Temp);
                    if (myObjects == null) {
                        myObjects = new ArrayList<>();
                    }
                    UpdateList();
                }
                catch ( IOException ex){
                    ex.printStackTrace();
                }
            }
        });

        return v;
    }


    //генерируем данные для адаптера (берем данные из бд)
    void fillData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable2", null, null, null, null, null, null);
        //проходимся по всем объекта в бд и записываем их в список
        while (c.moveToNext()) {
            myObjects.add(new MyObject(c.getInt(0), c.getString(1), c.getInt(2), c.getString(3)));
        }
    }

    //обновляем базу данных, если делаем импорт из json
    void updateDataBase(){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("mytable2", null, null);

        for (int i = 0; i < myObjects.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put("name", myObjects.get(i).getObjectName());
            cv.put("number", myObjects.get(i).getObjectNumber());
            cv.put("info", myObjects.get(i).getObjectInfo());
           // cv.put("id", myObjects.get(i).getId());
            long rowID = db.insert("mytable2", null, cv);
        }

        // закрываем подключение к БД
        dbHelper.close();

        Cursor c = db.query("mytable2", null, null, null, null, null, null);
        //проходимся по всем объекта в бд и записываем их в список
        while (c.moveToNext()) {
            myObjects.add(new MyObject(c.getInt(0), c.getString(1), c.getInt(2), c.getString(3)));
        }
    }

    public void UpdateList() {
        adapter.notifyDataSetChanged();
    }
}
