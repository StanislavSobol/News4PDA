package com.gmal.sobol.i.stanislav.news4pda.view.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmal.sobol.i.stanislav.news4pda.MApplication;
import com.gmal.sobol.i.stanislav.news4pda.R;
import com.gmal.sobol.i.stanislav.news4pda.dto.ItemDTO;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.Holder> {

    private MainActivity mainActivity;
    private List<ItemDTO> items = new ArrayList<>();

    public NewsListAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    void addItem(ItemDTO itemDTO) {
        items.add(itemDTO);
        notifyItemInserted(items.size() - 1);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final ItemDTO item = items.get(position);

        if (item != null) {

            holder.titleTextVew.setText("Page: " + item.getPageNum() + "  " + item.getTitle());
            holder.descriptionTextView.setText(item.getDescription());

            if (!item.getImageURL().isEmpty()) {
                RequestCreator requestCreator = Picasso.with(mainActivity)
                        .load(item.getImageURL())
                        .placeholder(R.drawable.ic_menu_gallery);
                if (!MApplication.isOnlineWithToast(false)) {
                    requestCreator.networkPolicy(NetworkPolicy.OFFLINE);
                }
                requestCreator.into(holder.imageView);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(true);
                    notifyItemChanged(position);
                    mainActivity.showDetailedNew(item);
                }
            });
        }
        mainActivity.checkForNextPage(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    boolean addNews(NewsDTO_old srcData) {
//        if (srcData.size() > items.size()) {
//            int left = items.size();
//            for (int i = left; i < srcData.size(); i++) {
//                items.add(srcData.get(i));
//            }
//            notifyDataSetChanged();
//            return true;
//        }
//        return false;
//    }

    static class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.titleTextVew)
        TextView titleTextVew;
        @Bind(R.id.descriptionTextView)
        TextView descriptionTextView;
        @Bind(R.id.imageView)
        ImageView imageView;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
