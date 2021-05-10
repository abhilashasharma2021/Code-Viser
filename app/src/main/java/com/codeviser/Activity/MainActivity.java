package com.codeviser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.codeviser.Fragment.HomeFragment;
import com.codeviser.Fragment.FeedsFragment;
import com.codeviser.Fragment.ProfileFragment;
import com.codeviser.Fragment.SearchFragment;
import com.codeviser.Fragment.SubscribtionFragment;
import com.codeviser.Fragment.SupportFragment;
import com.codeviser.R;
import com.codeviser.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    private Context context;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        view=binding.getRoot();
        setContentView(view);
        context=this;


        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedsFragment()).commit();
        }

        binding.customBottomBar.inflateMenu(R.menu.bottom_nav);
        Menu menuItem =  binding.customBottomBar.getMenu();
        menuItem.getItem(2).setChecked(true);
        binding.customBottomBar.setOnNavigationItemSelectedListener(this);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedsFragment()).commit();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.search:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SubscribtionFragment()).commit();
                break;

            case R.id.video:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SupportFragment()).commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                break;

        }
        return true;
    }
}