package com.dji.sdk.sample.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.Employee;
import java.util.List;

/**
 * The type Spin fixer adapter.
 */
public class SpinFixerAdapter extends ArrayAdapter<List<Employee>>{

private final LayoutInflater mInflater;
private final Context mContext;
private final List<Employee> items;
private final int mResource;

    /**
     * Instantiates a new Spin fixer adapter.
     *
     * @param context  the context
     * @param resource the resource
     * @param objects  the objects
     */
    public SpinFixerAdapter(@NonNull Context context, @LayoutRes int resource,
                        @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        }
@Override
public View getDropDownView(int position, @Nullable View convertView,
@NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
        }

@Override
public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
        }

private View createItemView(int position, View convertView, ViewGroup parent){
final View view = mInflater.inflate(mResource, parent, false);

        TextView name = view.findViewById(R.id.tv_fixer_name);
        TextView sdt =view.findViewById(R.id.tv_fixer_sdt);

        Employee offerData = items.get(position);
        name.setText(offerData.getName());
        sdt.setText(offerData.getPhone());

        return view;
        }
        }