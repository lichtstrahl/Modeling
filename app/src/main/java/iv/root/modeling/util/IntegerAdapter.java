package iv.root.modeling.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import iv.root.modeling.R;

public class IntegerAdapter extends RecyclerView.Adapter<IntegerAdapter.IntegerViewHolder> {
    private List<Integer> list;
    private LayoutInflater inflater;

    public IntegerAdapter(List<Integer> list, LayoutInflater inflater) {
        this.list = list;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public IntegerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new IntegerViewHolder(inflater.inflate(R.layout.list_item_integer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IntegerViewHolder integerViewHolder, int i) {
        integerViewHolder.bind(list.get(i));
    }

    public List<Integer> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void append(Integer i) {
        int count = list.size();
        list.add(i);
        notifyItemInserted(count);
    }

    class IntegerViewHolder extends RecyclerView.ViewHolder {
        private TextView value;

        private IntegerViewHolder(@NonNull View itemView) {
            super(itemView);

            value = itemView.findViewById(R.id.value);

        }

        private void bind(Integer integer) {
            value.setText(integer != null ? integer.toString() : "NULL");
        }
    }
}
