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

//import static com.aaar.vinapp.ShowAllTopicsActivity.EDIT_TOPIC_REQUEST_CODE;

public class TopicViewAdapter extends RecyclerView.Adapter<TopicViewAdapter.TopicViewHolder> {

    public ArrayList<Topic> topiclist;
    public Context context;
    public String itemremoved;
    public long itemremovedid;
    DeleteTopicListener deleteTopicListener;
    StarClickListener starClickListener;
    public static final String TOPIC_CLICKED = "Topic Tapped";
    private ImageView starImage;
    private int revisionCount;
    private String revisionMessage;
    private CardView cardView;

    /*private TopicClickListener mtopicClickListener;*/

    public TopicViewAdapter(Context context, ArrayList <Topic> topiclist,DeleteTopicListener deleteTopicListener){
        this.topiclist = topiclist;
        this.context = context;
        this.deleteTopicListener = (DeleteTopicListener) context;
        this.starClickListener = (StarClickListener) context;

    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.topic_single_item, viewGroup, false);
        return new TopicViewHolder(itemview,this);

    }

    @Override
    public void onBindViewHolder(TopicViewHolder topicViewHolder,final int i) {
        final String currenttopic = topiclist.get(i).getTopic();
        final Topic currentTopicObject = topiclist.get(i);
        String revisionDate = topiclist.get(i).getRevision1().toString();
        String trimmedRevisiondate = revisionDate.substring(0, revisionDate.length()-12);
        revisionCount = topiclist.get(i).getCount()+1;
        revisionMessage = "Revision "+revisionCount+" due on "+trimmedRevisiondate;
        starImage = (ImageView) topicViewHolder.itemView.findViewById(R.id.image_star);
        topicViewHolder.topic.setText(currenttopic);
        topicViewHolder.revisionTime.setText(revisionMessage);
        if (topiclist.get(i).getCount() == 0) {
            topicViewHolder.textviewrevisionCount.setText("0");
        }
        else{
            topicViewHolder.textviewrevisionCount.setText(Integer.toString(topiclist.get(i).getCount()));
        }
        cardView = topicViewHolder.itemView.findViewById(R.id.cardView_item);
        topicViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Bundle bundle = new Bundle();
                bundle.putSerializable("TappedTopic", currentTopicObject);*/
                Log.d("PMFromTopicViewAdapter", Long.toString(currentTopicObject.getIdentifier()));
                Intent intent = new Intent(context, ViewTopicActivity.class);
                intent.putExtra("currentTopic", currentTopicObject);
                intent.putExtra("position", i);
                //Log.d("PositionVandaaaa", ""+i);
                //context.startActivity(intent);
                ((Activity)context).startActivityForResult(intent, VIEW_TOPIC_REQUEST_CODE);

            }
        });

              starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starClickListener.onStarClick(currentTopicObject, i);


            }
        });
/*       if(i %2 == 1){
        cardView.setCardBackgroundColor(Color.parseColor("#FF5252"));
  //                topicViewHolder.itemView.setBackgroundColor(Color.parseColor("#FF5252"));
              }

              else{
//                 topicViewHolder.itemView.setBackgroundColor(Color.parseColor("#FF8A80"));
        cardView.setCardBackgroundColor(Color.parseColor("#FF8A80"));

            }*/





    }

@Override
    public void onBindViewHolder(@NonNull TopicViewHolder topicViewHolder, final int i, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()){

            super.onBindViewHolder(topicViewHolder, i, payloads);
        }
        else{
            //payloads.get(position);
            final String currenttopic = (String) ((Topic)payloads.get(0)).getTopic();
            final Topic currentTopicObject = (Topic) payloads.get(0);
            String revisionDate = currentTopicObject.getRevision1().toString();
            String trimmedRevisiondate = revisionDate.substring(0, revisionDate.length()-12);
            revisionCount = currentTopicObject.getCount()+1;
            revisionMessage = "Revision "+revisionCount+" due on "+trimmedRevisiondate;
            topicViewHolder.topic.setText(currenttopic);
            topicViewHolder.revisionTime.setText(revisionMessage);
            if(topiclist.get(i).getCount() != 0){
                topicViewHolder.textviewrevisionCount.setText(Integer.toString(topiclist.get(i).getCount()));
            }
            topicViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                /*Bundle bundle = new Bundle();
                bundle.putSerializable("TappedTopic", currentTopicObject);*/
                    Log.d("PMFromTopicViewAdapter", Long.toString(currentTopicObject.getIdentifier()));
                    Intent intent = new Intent(context, ViewTopicActivity.class);
                    intent.putExtra("currentTopic", currentTopicObject);
                    intent.putExtra("position", i);
                    //Log.d("PositionVandaaaa", ""+i);
                    //context.startActivity(intent);
                    ((Activity)context).startActivityForResult(intent, VIEW_TOPIC_REQUEST_CODE);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return topiclist.size();
    }



    public class TopicViewHolder extends RecyclerView.ViewHolder {


        public TextView topic;
        public TextView revisionTime;
        private TextView textviewrevisionCount;
        final TopicViewAdapter mAdapter;
        private Button buttonMarkDone;

        //TopicClickListener topicClickListener;

        public TopicViewHolder(View itemView, final TopicViewAdapter topicViewAdapter) {
            super(itemView);
            topic = (TextView) itemView.findViewById(R.id.topic_singleitem_textview);
            revisionTime = (TextView) itemView.findViewById(R.id.textview_singleitem_revision) ;
            textviewrevisionCount = (TextView) itemView.findViewById(R.id.tv_revisioncount);

            this.mAdapter = topicViewAdapter;
            //this.topicClickListener = topicClickListener;

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditTopicActivity.class);
                    intent.putExtra(TOPIC_CLICKED, topiclist.get(topicVi));
                    context.startActivity(intent);
                }
            });*/




            //buttonMarkDone = itemView.findViewById(R.id.button_markdone);
/*            buttonMarkDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonMarstartkDone.setText("Done");
                    Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show();
                }
            });*/

        }

/*        @Override
        public void onClick(View view) {
//            topicClickListener.onTopicClick(getAdapterPosition());
            Intent intent = new Intent(context, EditTopicActivity.class);
            context.startActivity(intent);
        }*/
    }

    public void deleteTopic(int position){
        Log.d("Deletebutton", "deletebutton");

        itemremoved = topiclist.get(position).getTopic();
        itemremovedid = topiclist.get(position).getIdentifier();

        Log.d("ItemRemoved", itemremoved);
        //Log.d("ItemRemovedID", Integer.toString(itemremovedid));
        topiclist.remove(position);
//        Log.d("Good", topiclist.get(position).getTopic());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, topiclist.size());
//        notifyDataSetChanged();
        deleteTopicListener.deleteTopicFromDB(itemremovedid);




    }

    public interface DeleteTopicListener{

        public void deleteTopicFromDB(long topicId);

    }


    public interface StarClickListener{
        public void onStarClick(Topic topic, int position);
    }

 /*   public interface TopicClickListener{
          void onTopicClick(int position);
    }*/
}
