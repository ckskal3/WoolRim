package org.woolrim.woolrim;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UniversityRecyclerViewAdapter extends RecyclerView.Adapter<UniversityItemViewHolder> {
    public ArrayList<UniversityItem> universityItems = new ArrayList<>();
    public int position = -1;

    public UniversityItem getItem(int position){
        return universityItems.get(position);
    }

    public void addItem(UniversityItem item) {
        universityItems.add(item);
    }

    @NonNull
    @Override
    public UniversityItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.school_listview_item,parent,false);
        return new UniversityItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityItemViewHolder holder, int position) {
       holder.universityNameTextView.setText(universityItems.get(position).universityName);

       this.position = getPosition(position);
    }

    @Override
    public int getItemCount() {
        if (universityItems == null) return 0;
        else return universityItems.size();
    }

    public int getPosition(int position){
        return position;
    }

}

class UniversityItemViewHolder extends RecyclerView.ViewHolder {
    public TextView universityNameTextView;

    public UniversityItemViewHolder(View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view) {
        universityNameTextView = view.findViewById(R.id.university_name_textview);
    }


}

class UniversityItem implements Parcelable {
    public String universityName;

    public UniversityItem(String universityName) {
        this.universityName = universityName;
    }

    protected UniversityItem(Parcel in) {
        universityName = in.readString();
    }

    public static final Creator<UniversityItem> CREATOR = new Creator<UniversityItem>() {
        @Override
        public UniversityItem createFromParcel(Parcel in) {
            return new UniversityItem(in);
        }

        @Override
        public UniversityItem[] newArray(int size) {
            return new UniversityItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(universityName);
    }
}
