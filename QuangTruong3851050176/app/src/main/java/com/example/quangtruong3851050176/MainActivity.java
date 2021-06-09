package com.example.quangtruong3851050176;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.quangtruong3851050176.activities.AllTags;
import com.example.quangtruong3851050176.activities.AppSettings;
import com.example.quangtruong3851050176.activities.CompletedTodos;
import com.example.quangtruong3851050176.adapters.PendingTodoAdapter;
import com.example.quangtruong3851050176.helpers.IntentExtras;
import com.example.quangtruong3851050176.helpers.SettingsHelper;
import com.example.quangtruong3851050176.helpers.TagDBHelper;
import com.example.quangtruong3851050176.helpers.TodoDBHelper;
import com.example.quangtruong3851050176.models.PendingTodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private RecyclerView pendingTodos;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PendingTodoModel> pendingTodoModels;
    private PendingTodoAdapter pendingTodoAdapter;
    private FloatingActionButton addNewTodo;
    private TagDBHelper tagDBHelper;
    private String getTagTitleString;
    private TodoDBHelper todoDBHelper;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsHelper.applyTheme(this);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        SettingsHelper.applyThemeToolbar(findViewById(R.id.toolbar), this);
        setTitle(getString(R.string.app_title));
        showDrawerLayout();
        navigationMenuInit();
        loadPendingTodos();
    }

    //loading all the pending todos
    private void loadPendingTodos() {
        pendingTodos = (RecyclerView) findViewById(R.id.pending_todos_view);
        linearLayout = (LinearLayout) findViewById(R.id.no_pending_todo_section);
        tagDBHelper = new TagDBHelper(this);
        todoDBHelper = new TodoDBHelper(this);

        if (todoDBHelper.countTodos() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
            pendingTodos.setVisibility(View.GONE);
        } else {
            pendingTodoModels = new ArrayList<>();
            pendingTodoModels = todoDBHelper.fetchAllTodos();
            pendingTodoAdapter = new PendingTodoAdapter(pendingTodoModels, this);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        pendingTodos.setAdapter(pendingTodoAdapter);
        pendingTodos.setLayoutManager(linearLayoutManager);
        addNewTodo = (FloatingActionButton) findViewById(R.id.fabAddTodo);
        addNewTodo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAddTodo:
                if (tagDBHelper.countTags() == 0) {
                    showDialog();
                } else {
                    showNewTodoDialog();
                }
                break;
        }
    }

    //hien thi drawer
    private void showDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //khoi tao nav menu
    private void navigationMenuInit() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pending_task_options, menu);
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
                ArrayList<PendingTodoModel> newPendingTodoModels = new ArrayList<>();
                for (PendingTodoModel pendingTodoModel : pendingTodoModels) {
                    String getTodoTitle = pendingTodoModel.getTodoTitle().toLowerCase();
                    String getTodoContent = pendingTodoModel.getTodoContent().toLowerCase();
                    String getTodoTag = pendingTodoModel.getTodoTag().toLowerCase();

                    if (getTodoTitle.contains(newText) || getTodoContent.contains(newText) || getTodoTag.contains(newText)) {
                        newPendingTodoModels.add(pendingTodoModel);
                    }
                }
                pendingTodoAdapter.filterTodos(newPendingTodoModels);
                pendingTodoAdapter.notifyDataSetChanged();
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
            case R.id.all_tags:
                startActivity(new Intent(this, AllTags.class));
                return true;
            case R.id.completed:
                startActivity(new Intent(this, CompletedTodos.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, AppSettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.pending_todos) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.completed_todos) {
            startActivity(new Intent(this, CompletedTodos.class));
        } else if (id == R.id.tags) {
            startActivity(new Intent(this, AllTags.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(this, AppSettings.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //hien thi dialog yeu cau them danh muc neu chua co danh muc
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tag_create_dialog_title_text);
        builder.setMessage(R.string.no_tag_in_the_db_text);
        builder.setPositiveButton(R.string.create_new_tag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this, AllTags.class));
            }
        }).setNegativeButton(R.string.tag_edit_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //hien thi dialog them cong viec va thuc hien them cong viec
    private void showNewTodoDialog() {
        //getting current calendar credentials
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.add_new_todo_dialog, null);
        builder.setView(view);
        SettingsHelper.applyThemeTextView((TextView) view.findViewById(R.id.add_todo_dialog_title), this);
        final TextInputEditText todoTitle = (TextInputEditText) view.findViewById(R.id.todo_title);
        final TextInputEditText todoContent = (TextInputEditText) view.findViewById(R.id.todo_content);
        Spinner todoTags = (Spinner) view.findViewById(R.id.todo_tag);
        ArrayAdapter<String> tagsModelArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tagDBHelper.fetchTagStrings());
        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        todoTags.setAdapter(tagsModelArrayAdapter);
        todoTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTagTitleString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final TextInputEditText todoDate = (TextInputEditText) view.findViewById(R.id.todo_date);
        final TextInputEditText todoTime = (TextInputEditText) view.findViewById(R.id.todo_time);

        //lay ngay cong viec
        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);
                        todoDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //lay gio cong viec
        todoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        todoTime.setText(timeFormat);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView addTodo = (TextView) view.findViewById(R.id.add_new_todo);
        SettingsHelper.applyTextColor(cancel, this);
        SettingsHelper.applyTextColor(addTodo, this);

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting all the values from add new todos dialog
                String getTodoTitle = todoTitle.getText().toString();
                String getTodoContent = todoContent.getText().toString();
                int todoTagID = tagDBHelper.fetchTagID(getTagTitleString);
                String getTodoDate = todoDate.getText().toString();
                String getTime = todoTime.getText().toString();

                //kiem tra cac truong
                boolean isTitleEmpty = todoTitle.getText().toString().isEmpty();
                boolean isContentEmpty = todoContent.getText().toString().isEmpty();
                boolean isDateEmpty = todoDate.getText().toString().isEmpty();
                boolean isTimeEmpty = todoTime.getText().toString().isEmpty();

                //them cong viec
                if (isTitleEmpty) {
                    todoTitle.setError("Vui lòng nhập tiêu đề !");
                } else if (isContentEmpty) {
                    todoContent.setError("Vui lòng nhập mô tả !");
                } else if (isDateEmpty) {
                    todoDate.setError("Vui lòng nhập ngày");
                } else if (isTimeEmpty) {
                    todoTime.setError("Vui lòng nhập giờ !");
                } else if (todoDBHelper.addNewTodo(
                        new PendingTodoModel(getTodoTitle, getTodoContent, String.valueOf(todoTagID), getTodoDate, getTime)
                )) {
                    Toast.makeText(MainActivity.this, R.string.todo_title_add_success_msg, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        builder.create().show();
    }
}
