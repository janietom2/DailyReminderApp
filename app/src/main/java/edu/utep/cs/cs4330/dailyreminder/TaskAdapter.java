package edu.utep.cs.cs4330.dailyreminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.utep.cs.cs4330.dailyreminder.Models.Task;

public class TaskAdapter extends ArrayAdapter<Task> {

    private Context context;
    private ArrayList<Task> tasks;
    ArrayList<Task> tmpTasks;
    ArrayList<Task> suggestions;
    private static LayoutInflater inflater = null;


    public TaskAdapter(@NonNull Context context, /*@LayoutRes*/ ArrayList<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
        this.tmpTasks = new ArrayList<Task>(tasks);
        this.suggestions = new ArrayList<Task>(tasks);
    }

    //================================================================================
    // CRUD while results being filtered
    //================================================================================

    /**
     * Note: This CRUD methods work to update the list created for search.
     * For example, if a new item is added, this will also update the list that will be searched
     * hence needs to be update in order to user to search for the new items
     * for deleting and updating processes.
     */

    public void addItem(Task t) {
        this.tmpTasks.add(t);
    }

//    public void editItem(int position, String name, Date url) {
//        Task tmp = this.tmpTasks.get(position);
//        tmp.setTitle(name);
//        tmp.setDeadLine(url);
//    }

    public void removeItem(Integer position) {
        Task tmp = this.tmpTasks.get(position);
        this.tmpTasks.remove(tmp);
    }

    @Override
    public Filter getFilter(){
        return PriceFinderFilter;
    }

    @SuppressLint("SetTextI18n") // This was added by the IDE
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        DecimalFormat f = new DecimalFormat("##.00");

        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.task_list,parent,false);

        Task t = tasks.get(position); // Get current item(object) on the ArrayList of items

        TextView name = (TextView) listItem.findViewById(R.id.taskTitle);
        name.setText(t.getTitle());

        TextView price = (TextView) listItem.findViewById(R.id.taskDeadLine);
        price.setText(t.getDeadLine().toString());

//        String s;
//        TextView newPrice = (TextView) listItem.findViewById(R.id.itemPriceNew);

        ImageView productIcon = listItem.findViewById(R.id.productIcon);
        if(t.getPriority() == 0){
            productIcon.setImageResource(R.mipmap.important_tag_round);

        } else if(t.getPriority() == 1) {
            productIcon.setImageResource(R.mipmap.med_important_tag_round);

        }else if(t.getFinished() == 1){
            productIcon.setImageResource(R.mipmap.done_icon_round);
        }
        else {
            productIcon.setImageResource(R.mipmap.low_important_tag_round);
        }

//        ImageView iconImage = (ImageView) listItem.findViewById(R.id.productIcon);
//        if(item.getImage().contains("jpg") || item.getImage().contains("png")){
//            Picasso.get().load(item.getImage()).into(iconImage);
//        }

//        if(item.changePositive()) {
//            newPrice.setTextColor(Color.rgb(200, 0, 0));
//            s = "+";
//        }
//        else {
//            newPrice.setTextColor(Color.rgb(0,200,0));
//            s = "-";
//        }
//        newPrice.setText("$" + f.format(item.getNewPrice()) + " USD (" + s + f.format(item.calculatePrice())+"%)");

        return listItem;

    }

    private Filter PriceFinderFilter = new Filter() {

        /**
         * Method will do a search of the items (PriceFinder object name) and add them into a
         * temporal arraylist that will only contain objects that are related to users serach
         * @param constraint
         * @return FilterResults which contains the different results of the serach
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if (constraint != null) {
                suggestions.clear();
                for (Task pf : tmpTasks) {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    if (pf.getTitle().toLowerCase().contains(filterPattern)) {
                        suggestions.add(pf);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }

        }

        /**
         * Method to publish the filtered results of the search on the Arraylist of PriceFinder objects
         * @param constraint
         * @param results of the filtered search
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Task> p = (ArrayList<Task>) results.values;

            if (results.count > 0) {
                clear();
                for (Task pf : p) {
                    add(pf);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }

        }
    };


    public int getSize() {
        return tasks.size();
    }
}
