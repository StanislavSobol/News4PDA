package com.gmal.sobol.i.stanislav.news4pda;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmal.sobol.i.stanislav.news4pda.parser.NewsItemDTO;

public class NewsListAdapter  extends RecyclerView.Adapter<NewsListAdapter.Holder> {

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

    public NewsListAdapter(NewsItemDTO newsItemDTO) {
        this.newsItemDTO = new NewsItemDTO();
        this.newsItemDTO.addAll(newsItemDTO);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        NewsItemDTO.Item item = newsItemDTO.get(position);
        if (item != null) {

            holder.titleTextVew.setText(item.getTitle());
            holder.descriptionTextView.setText(item.getDescription());


        }
    }

    @Override
    public int getItemCount() {
        return newsItemDTO.size();
    }

    private NewsItemDTO newsItemDTO;

}
