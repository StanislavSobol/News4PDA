package com.gmal.sobol.i.stanislav.news4pda;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmal.sobol.i.stanislav.news4pda.parser.NewsDTO;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.Holder> {

    private MainActivity mainActivity;

    public static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
            titleTextVew = (TextView) itemView.findViewById(R.id.titleTextVew);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        private TextView titleTextVew;
        private TextView descriptionTextView;
        private ImageView imageView;
    }

    public NewsListAdapter(NewsDTO news, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.news = new NewsDTO();
        this.news.addAll(news);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final NewsDTO.Item item = news.get(position);
        if (item != null) {

            holder.titleTextVew.setText(item.getTitle());
            holder.descriptionTextView.setText(item.getDescription());

            if (!item.getImageURL().isEmpty()) {
                RequestCreator requestCreator = Picasso.with(mainActivity)
                        .load(item.getImageURL())
                        .placeholder(R.drawable.ic_menu_gallery);
                if (!News4PDAApplication.isOnlineWithToast(false)) {
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
        return news.size();
    }

    boolean addNews(NewsDTO srcData) {
        if (srcData.size() > news.size()) {
            int left = news.size();
            for (int i = left; i < srcData.size(); i++) {
                news.add(srcData.get(i));
            }
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private NewsDTO news;
}
