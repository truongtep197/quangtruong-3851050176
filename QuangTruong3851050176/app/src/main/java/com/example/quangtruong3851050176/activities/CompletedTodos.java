package com.example.quangtruong3851050176.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quangtruong3851050176.R;
import com.example.quangtruong3851050176.adapters.CompletedTodoAdapter;
import com.example.quangtruong3851050176.helpers.SettingsHelper;
import com.example.quangtruong3851050176.helpers.TodoDBHelper;
import com.example.quangtruong3851050176.models.CompletedTodoModel;

import java.util.ArrayList;

public class CompletedTodos extends AppCompatActivity {
    private RecyclerView completedTodos;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<CompletedTodoModel> completedTodoModels;
    private CompletedTodoAdapter completedTodoAdapter;
    private LinearLayout linearLayout;
    private TodoDBHelper todoDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsHelper.applyTheme(this);
        setContentView(R.layout.activity_completed_todos);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SettingsHelper.applyThemeToolbar(findViewById(R.id.toolbar), this);
        setTitle(getString(R.string.complete_todo_activity_title));
        loadCompletedTodos();
    }

    //tai cac cong viec da hoan thanh
    private void loadCompletedTodos() {
        completedTodos = (RecyclerView) findViewById(R.id.completed_todos_view);
        todoDBHelper = new TodoDBHelper(this);
        linearLayout = (LinearLayout) findViewById(R.id.no_completed_todo_section);
        if (todoDBHelper.countCompletedTodos() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
            completedTodos.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            completedTodos.setVisibility(View.VISIBLE);
            completedTodoModels = new ArrayList<>();
            completedTodoModels = todoDBHelper.fetchCompletedTodos();
            completedTodoAdapter = new CompletedTodoAdapter(completedTodoModels, this);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        completedTodos.setAdapter(completedTodoAdapter);
        completedTodos.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.completed_task_options, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<CompletedTodoModel> newCompletedTodoModels = new ArrayList<>();
                for (CompletedTodoModel completedTodoModel : completedTodoModels) {
                    String getTodoTitle = completedTodoModel.getTodoTitle();
                    String getTodoContent = completedTodoModel.getTodoContent();
                    String getTodoTag = completedTodoModel.getTodoTag();

                    if (getTodoTitle.contains(newText) || getTodoContent.contains(newText) || getTodoTag.contains(newText)) {
                        newCompletedTodoModels.add(completedTodoModel);
                    }
                }
                completedTodoAdapter.filterCompletedTodos(newCompletedTodoModels);
                completedTodoAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            case R.id.delete_all:
                deleteDialog();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, AppSettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Hien thi dialog xac nhan xoa
    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa tất cả công việc đã hoàn thành?");
        builder.setPositiveButton("Xóa tất cả", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (todoDBHelper.removeCompletedTodos()) {
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(CompletedTodos.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CompletedTodos.this, "Xóa thất bại !", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }
}