package in.sunilpaulmathew.rootfilepicker.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.topjohnwu.superuser.io.SuFile;

import java.util.List;

import in.sunilpaulmathew.rootfilepicker.R;
import in.sunilpaulmathew.rootfilepicker.utils.FilePicker;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on April 15, 2021
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private static ClickListener clickListener;

    private final List<String> data;

    public RecycleViewAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_filepicker, parent, false);
        return new ViewHolder(rowItem);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (SuFile.open(this.data.get(position)).isDirectory()) {
            holder.mIcon.setImageDrawable(holder.mTitle.getContext().getResources().getDrawable(R.drawable.ic_folder));
            holder.mIcon.setBackground(holder.mIcon.getContext().getResources().getDrawable(R.drawable.ic_circle));
            holder.mIcon.setColorFilter(FilePicker.getThemeAccentColor(holder.mTitle.getContext()));
            holder.mDescription.setVisibility(View.GONE);
        } else {
            if (FilePicker.isImageFile(data.get(position))) {
                if (FilePicker.getImageURI(data.get(position)) != null) {
                    holder.mIcon.setImageURI(FilePicker.getImageURI(data.get(position)));
                } else {
                    holder.mIcon.setImageDrawable(holder.mIcon.getContext().getResources().getDrawable(R.drawable.ic_file));
                }
            } else {
                holder.mIcon.setImageDrawable(holder.mIcon.getContext().getResources().getDrawable(R.drawable.ic_file));
            }
            holder.mIcon.setBackground(null);
            holder.mIcon.setColorFilter(FilePicker.isDarkTheme(holder.mTitle.getContext()) ? Color.WHITE : Color.BLACK);
            holder.mDescription.setText(FilePicker.getFileSize(this.data.get(position)));
        }
        holder.mTitle.setText(SuFile.open(this.data.get(position)).getName());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final AppCompatImageButton mIcon;
        private final MaterialTextView mTitle, mDescription;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.mIcon = view.findViewById(R.id.icon);
            this.mTitle = view.findViewById(R.id.title);
            this.mDescription = view.findViewById(R.id.description);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecycleViewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
    
}