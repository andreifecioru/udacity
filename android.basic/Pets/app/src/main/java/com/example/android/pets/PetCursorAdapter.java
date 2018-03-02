package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.pet_item, parent, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.nameTextView = rootView.findViewById(R.id.name_text_view);
        viewHolder.breedTextView = rootView.findViewById(R.id.breed_text_view);

        rootView.setTag(viewHolder);

        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
        String breed = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.nameTextView.setText(name);
        viewHolder.breedTextView.setText(breed);
    }

    private static final class ViewHolder {
        TextView nameTextView;
        TextView breedTextView;
    }
}
