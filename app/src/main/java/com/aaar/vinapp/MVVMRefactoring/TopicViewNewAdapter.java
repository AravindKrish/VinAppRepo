package com.aaar.vinapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaar.vinapp.database.Topic;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.aaar.vinapp.ShowAllTopicsActivity.VIEW_TOPIC_REQUEST_CODE;

public class TopicViewNewAdapter extends RecyclerView.Adapter<TopicViewNewAdapter.TopicViewHolder> {

    private final LayoutInflater mInflater;
    private List<Topic> mtopicList;
    private ArrayList<Topic> topiclist;    public String itemremoved;
    public long itemremovedid;
    TopicViewAdapter.DeleteTopicListener deleteTopicListener;
    TopicViewAdapter.StarClickListener starClickListener;
    public static final String TOPIC_CLICKED = "Topic Tapped";
    private ImageView starImage;
    private int revisionCount;
    private String revisionMessage;
    private CardView cardView;
    public Context context;


    TopicViewNewAdapter(Context context)
    {mInflater = LayoutInflater.from(context);
    this.context = context;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemView = mInflater.inflate(R.layout.topic_single_item, parent, false);
         return new TopicViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder topicViewHolder, final int i) {
        final String currenttopic = topiclist.get(i).getTopic();
        final Topic currentTopicObject = topiclist.get(i);
        String revisionDate = topiclist.get(i).getRevision1().toString();
        String trimmedRevisiondate = revisionDate.substring(0, revisionDate.length() - 12);
        revisionCount = topiclist.get(i).getCount() + 1;
        revisionMessage = "Revision " + revisionCount + " due on " + trimmedRevisiondate;
        starImage = (ImageView) topicViewHolder.itemView.findViewById(R.id.image_star);
        topicViewHolder.topic.setText(currenttopic);
        topicViewHolder.revisionTime.setText(revisionMessage);
        if (topiclist.get(i).getCount() == 0) {
            topicViewHolder.textviewrevisionCount.setText("0");
        } else {
            topicViewHolder.textviewrevisionCount.setText(Integer.toString(topiclist.get(i).getCount()));
        }
        cardView = topicViewHolder.itemView.findViewById(R.id.cardView_item);
        topicViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PMFromTopicViewAdapter", Long.toString(currentTopicObject.getIdentifier()));
                Intent intent = new Intent(context, ViewTopicActivity.class);
                intent.putExtra("currentTopic", currentTopicObject);
                intent.putExtra("position", i);
                ((Activity) context).startActivityForResult(intent, VIEW_TOPIC_REQUEST_CODE);

            }
        });
        starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starClickListener.onStarClick(currentTopicObject, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return topiclist.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        public TextView topic;
        public TextView revisionTime;
        private TextView textviewrevisionCount;
        /*final TopicViewAdapter mAdapter;*/
        private Button buttonMarkDone;
        
        public TopicViewHolder(@NonNull View itemView /*final TopicViewAdapter topicViewAdapter*/) {
            super(itemView);
            topic = (TextView) itemView.findViewById(R.id.topic_singleitem_textview);
            revisionTime = (TextView) itemView.findViewById(R.id.textview_singleitem_revision);
            textviewrevisionCount = (TextView) itemView.findViewById(R.id.tv_revisioncount);
            //this.mAdapter = topicViewAdapter;
        }
    }

    void setTopiclist(List<Topic>topics){
        this.topiclist = (ArrayList<Topic>) topics;
        notifyDataSetChanged();

    }

    public void deleteTopic(int position) {
        Log.d("Deletebutton", "deletebutton");
        itemremoved = topiclist.get(position).getTopic();
        itemremovedid = topiclist.get(position).getIdentifier();
        Log.d("ItemRemoved", itemremoved);
        topiclist.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, topiclist.size());
        deleteTopicListener.deleteTopicFromDB(itemremovedid);
    }
}
