package com.rcorchero.hastensports.ui.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rcorchero.hastensports.R;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.list_container, ListFragment.newInstance())
                    .commit();

        }

    }

}
