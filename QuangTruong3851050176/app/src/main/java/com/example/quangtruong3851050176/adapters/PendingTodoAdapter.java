package com.example.quangtruong3851050176.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quangtruong3851050176.MainActivity;
import com.example.quangtruong3851050176.R;
import com.example.quangtruong3851050176.activities.CompletedTodos;
import com.example.quangtruong3851050176.helpers.SettingsHelper;
import com.example.quangtruong3851050176.helpers.TagDBHelper;
import com.example.quangtruong3851050176.helpers.TodoDBHelper;
import com.example.quangtruong3851050176.models.PendingTodoModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingTodoAdapter extends RecyclerView.Adapter<PendingTodoAdapter.PendingDataHolder> {
    private ArrayList<PendingTodoModel> pendingTodoModels;
    private Context context;
    private String getTagTitleString;
    private TagDBHelper tagDBHelper;
    private TodoDBHelper todoDBHelper;

    public PendingTodoAdapter(ArrayList<PendingTodoModel> pendingTodoModels, Context context) {
        this.pendingTodoModels = pendingTodoModels;
        this.context = context;
    }

    @Override
    public PendingDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pending_todo_layout, parent, false);
        return new PendingDataHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingDataHolder holder, int position) {
        todoDBHelper = new TodoDBHelper(context);
        final PendingTodoModel pendingTodoModel = pendingTodoModels.get(position);
        holder.todoTitle.setText(pendingTodoModel.getTodoTitle());
        holder.todoContent.setText(pendingTodoModel.getTodoContent());
        holder.todoDate.setText(pendingTodoModel.getTodoDate());
        holder.todoTag.setText(pendingTodoModel.getTodoTag());
        holder.todoTime.setText(pendingTodoModel.getTodoTime());
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.todo_edit_del_options, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edit:
                                showDialogEdit(pendingTodoModel.getTodoID(), position);
                                return true;
                            case R.id.delete:
                                showDeleteDialog(pendingTodoModel.getTodoID(), position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
        holder.makeCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCompletedDialog(pendingTodoModel.getTodoID(), position);
            }
        });
    }

    //xac nhan xoa cong viec
    private void showDeleteDialog(final int tagID, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa ?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (todoDBHelper.removeTodo(tagID)) {
                    Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                    pendingTodoModels.remove(position);
                    notifyDataSetChanged();
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    @Override
    public int getItemCount() {
        return pendingTodoModels.size();
    }

    //showing edit dialog for editing todos according to the todoid
    private void showDialogEdit(final int todoID, int position) {
        todoDBHelper = new TodoDBHelper(context);
        tagDBHelper = new TagDBHelper(context);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.edit_todo_dialog, null);
        builder.setView(view);
        SettingsHelper.applyThemeTextView((TextView) view.findViewById(R.id.edit_todo_dialog_title), context);
        final TextInputEditText todoTitle = (TextInputEditText) view.findViewById(R.id.todo_title);
        final TextInputEditText todoContent = (TextInputEditText) view.findViewById(R.id.todo_content);
        Spinner todoTags = (Spinner) view.findViewById(R.id.todo_tag);
        //luu tat ca danh muc vao mang
        ArrayAdapter<String> tagsModelArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tagDBHelper.fetchTagStrings());
        //set danh sach dropdown
        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //hien thi loading
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

        //set gia tri cho edit text
        todoTitle.setText(todoDBHelper.fetchTodoTitle(todoID));
        todoContent.setText(todoDBHelper.fetchTodoContent(todoID));
        todoDate.setText(todoDBHelper.fetchTodoDate(todoID));
        todoTime.setText(todoDBHelper.fetchTodoTime(todoID));

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);

        //lay ngay cong viec
        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);
                        todoDate.setText(DateFormat
                                .getDateInstance(DateFormat.MEDIUM)
                                .format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //lay gio cua cong viec
        todoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.set(Calendar.HOUR, i);
                        newCalendar.set(Calendar.MINUTE, i1);
                        String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(newCalendar.getTime());
                        todoTime.setText(timeFormat);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView addTodo = (TextView) view.findViewById(R.id.add_new_todo);
        SettingsHelper.applyTextColor(cancel, context);
        SettingsHelper.applyTextColor(addTodo, context);

        AlertDialog dialog = builder.create();

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting all the values from add new todos dialog
                String getTodoTitle = todoTitle.getText().toString();
                String getTodoContent = todoContent.getText().toString();
                int todoTagID = tagDBHelper.fetchTagID(getTagTitleString);
                String getTodoDate = todoDate.getText().toString();
                String getTime = todoTime.getText().toString();

                //kiem tra cac truong rong
                boolean isTitleEmpty = todoTitle.getText().toString().isEmpty();
                boolean isContentEmpty = todoContent.getText().toString().isEmpty();
                boolean isDateEmpty = todoDate.getText().toString().isEmpty();
                boolean isTimeEmpty = todoTime.getText().toString().isEmpty();

                PendingTodoModel todoModel = new PendingTodoModel(
                        todoID,
                        getTodoTitle,
                        getTodoContent,
                        String.valueOf(todoTagID),
                        getTodoDate,
                        getTime);

                //them cong viec
                if (isTitleEmpty) {
                    todoTitle.setError("Vui lòng nhập tiêu đề");
                } else if (isContentEmpty) {
                    todoContent.setError("Vui lòng nhập mô tả !");
                } else if (isDateEmpty) {
                    todoDate.setError("Vui lòng nhập ngày !");
                } else if (isTimeEmpty) {
                    todoTime.setError("Vui lòng nhập giờ !");
                } else if (todoDBHelper.updateTodo(todoModel)) {
                    Toast.makeText(context, R.string.todo_title_add_success_msg, Toast.LENGTH_SHORT).show();
                    pendingTodoModels.set(position, todoModel);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //hien thi dialog xac nhan cong viec da hoan thanh
    private void showCompletedDialog(final int tagID, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận hoàn thành");
        builder.setMessage("Bạn có chắc hoàn thành công việc này ?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (todoDBHelper.makeCompleted(tagID)) {
//                    context.startActivity(new Intent(context, CompletedTodos.class));
                    pendingTodoModels.remove(position);
                    notifyDataSetChanged();
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    public class PendingDataHolder extends RecyclerView.ViewHolder {
        TextView todoTitle, todoContent, todoTag, todoDate, todoTime;
        ImageView option, makeCompleted;

        public PendingDataHolder(View itemView) {
            super(itemView);
            todoTitle = (TextView) itemView.findViewById(R.id.pending_todo_title);
            todoContent = (TextView) itemView.findViewById(R.id.pending_todo_content);
            todoTag = (TextView) itemView.findViewById(R.id.todo_tag);
            todoDate = (TextView) itemView.findViewById(R.id.todo_date);
            todoTime = (TextView) itemView.findViewById(R.id.todo_time);
            option = (ImageView) itemView.findViewById(R.id.option);
            makeCompleted = (ImageView) itemView.findViewById(R.id.make_completed);
        }
    }

    //loc cong viec
    public void filterTodos(ArrayList<PendingTodoModel> newPendingTodoModels) {
        pendingTodoModels = new ArrayList<>();
        pendingTodoModels.addAll(newPendingTodoModels);
        notifyDataSetChanged();
    }
}
