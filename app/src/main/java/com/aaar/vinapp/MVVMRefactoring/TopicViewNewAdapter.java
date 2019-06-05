package com.aaar.vinapp.MVVMRefactoring;

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

import com.aaar.vinapp.MVVMRefactoring.DB.TopicNew;
import com.aaar.vinapp.R;
import com.aaar.vinapp.ViewTopicActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.aaar.vinapp.ShowAllTopicsActivity.VIEW_TOPIC_REQUEST_CODE;

public class TopicViewNewAdapter extends RecyclerView.Adapter<TopicViewNewAdapter.TopicViewHolder> {

    private final LayoutInflater mInflater;
    private List<TopicNew> mtopicNewList;
    //private ArrayList<Topic> topiclist;
    public String itemremoved;
    public long itemremovedid;
    TopicViewNewAdapter.DeleteTopicListener deleteTopicListener;
    TopicViewNewAdapter.StarClickListener starClickListener;
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
        final String currenttopic = mtopicNewList.get(i).getTopic();
        final TopicNew currentTopicObject = mtopicNewList.get(i);
        String revisionDate = mtopicNewList.get(i).getRevision1().toString();
        String trimmedRevisiondate = revisionDate.substring(0, revisionDate.length() - 12);
        revisionCount = mtopicNewList.get(i).getCount() + 1;
        revisionMessage = "Revision " + revisionCount + " due on " + trimmedRevisiondate;
        starImage = (ImageView) topicViewHolder.itemView.findViewById(R.id.image_star);
        topicViewHolder.topic.setText(currenttopic);
        topicViewHolder.revisionTime.setText(revisionMessage);
        if (mtopicNewList.get(i).getCount() == 0) {
            topicViewHolder.textviewrevisionCount.setText("0");
        } else {
            topicViewHolder.textviewrevisionCount.setText(Integer.toString(mtopicNewList.get(i).getCount()));
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
        return mtopicNewList.size();
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

    void setTopiclist(List<TopicNew>topicsNew){

        mtopicNewList = topicsNew;
        notifyDataSetChanged();

    }

    public void deleteTopic(int position) {
        Log.d("Deletebutton", "deletebutton");
        itemremoved = mtopicNewList.get(position).getTopic();
        itemremovedid = mtopicNewList.get(position).getIdentifier();
        Log.d("ItemRemoved", itemremoved);
        mtopicNewList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mtopicNewList.size());
        deleteTopicListener.deleteTopicFromDB(itemremovedid);
    }

    public interface DeleteTopicListener{

        public void deleteTopicFromDB(long topicId);

    }


    public interface StarClickListener{
        public void onStarClick(TopicNew topicNew, int position);
    }

}
