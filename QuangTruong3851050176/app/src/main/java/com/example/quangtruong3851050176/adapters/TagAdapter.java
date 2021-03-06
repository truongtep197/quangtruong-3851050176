package com.example.quangtruong3851050176.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quangtruong3851050176.R;
import com.example.quangtruong3851050176.helpers.TagDBHelper;
import com.example.quangtruong3851050176.models.TagsModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagDataHolder> {
    private ArrayList<TagsModel> tagsModels;
    private Context context;
    private TagDBHelper tagDBHelper;

    public TagAdapter(ArrayList<TagsModel> tagsModels, Context context) {
        this.tagsModels = tagsModels;
        this.context = context;
    }

    @Override
    public TagDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_all_tags_layout, parent, false);
        return new TagDataHolder(view);
    }

    @Override
    public void onBindViewHolder(TagDataHolder holder, int position) {
        final TagsModel tagsModel = tagsModels.get(position);
        holder.tag_title.setText(tagsModel.getTagTitle());
        tagDBHelper = new TagDBHelper(context);
        holder.tag_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.tag_edit_del_option, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edit:
                                editTag(tagsModel.getTagID(), position);
                                return true;
                            case R.id.delete:
                                removeTag(tagsModel.getTagID(), position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsModels.size();
    }

    public class TagDataHolder extends RecyclerView.ViewHolder {
        TextView tag_title;
        ImageView tag_option;

        public TagDataHolder(View itemView) {
            super(itemView);
            tag_title = (TextView) itemView.findViewById(R.id.tag_title);
            tag_option = (ImageView) itemView.findViewById(R.id.tags_option);
        }
    }

    //remove tag
    private void removeTag(final int tagID, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.tag_delete_dialog_title);
        builder.setMessage(R.string.tag_delete_dialog_msg);
        builder.setPositiveButton(R.string.tag_delete_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (tagDBHelper.removeTag(tagID)) {
                    Toast.makeText(context, R.string.tag_deleted_success, Toast.LENGTH_SHORT).show();
                    tagsModels.remove(position);
                    notifyDataSetChanged();
                }
            }
        }).setNegativeButton(R.string.tag_delete_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, R.string.tag_no_delete, Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    //update tag
    private void editTag(final int tagID, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.edit_tag_dialog, null);
        builder.setView(view);
        final TextInputEditText tagEditTitle = (TextInputEditText) view.findViewById(R.id.edit_tag_title);
        tagEditTitle.setText(tagDBHelper.fetchTagTitle(tagID));
        final TextView cancel = (TextView) view.findViewById(R.id.cancel);
        final TextView editNewtag = (TextView) view.findViewById(R.id.edit_new_tag);

        AlertDialog dialog = builder.create();

        editNewtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTagTitle = tagEditTitle.getText().toString();
                boolean isTagEmpty = tagEditTitle.getText().toString().isEmpty();
                boolean tagExists = tagDBHelper.tagExists(getTagTitle);

                if (isTagEmpty) {
                    tagEditTitle.setError("Vui l??ng nh???p ti??u ????? !");
                } else if (tagExists) {
                    tagEditTitle.setError("Danh m???c ???? t???n t???i!");
                } else if (tagDBHelper.saveTag(new TagsModel(tagID, getTagTitle))) {
                    Toast.makeText(context, R.string.tag_saved_success, Toast.LENGTH_SHORT).show();
                    tagsModels.get(position).setTagTitle(getTagTitle);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, R.string.tag_no_save, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onAddTag(TagsModel tagsModel) {
        tagsModels.add(tagsModel);
        notifyDataSetChanged();
    }

    //search filter
    public void filterTags(ArrayList<TagsModel> newTagsModels) {
        tagsModels = new ArrayList<>();
        tagsModels.addAll(newTagsModels);
        notifyDataSetChanged();
    }
}
