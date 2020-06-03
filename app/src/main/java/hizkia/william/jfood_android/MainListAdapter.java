package hizkia.william.jfood_android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MainListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<Seller> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Seller, ArrayList<Food>> _listDataChild;

    /**
     * Constructor for main list adapter
     * @param context variable context the adapter is called
     * @param listDataHeader array list that will contain parent list
     * @param listChildData array list that will contain child list
     */
    public MainListAdapter(Context context, ArrayList<Seller> listDataHeader, HashMap<Seller, ArrayList<Food>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    /**
     * Method to get child object
     * @param groupPosition for parent id
     * @param childPosition for child id
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    /**
     * Method to get child id
     * @param groupPosition for parent id
     * @param childPosition for child id
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Method to get child view
     * @param groupPosition for parent id
     * @param childPosition for child id
     * @param convertView for containing view
     * @param isLastChild for checking the last child on parent list
     * @param parent for object parent
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Food childText = (Food) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_food, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        String s = "Food name\t\t\t: " + childText.getName() +
                "\nFood Price\t\t\t\t: Rp. " + childText.getPrice() +
                "\nFood Category\t: " + childText.getCategory();
        txtListChild.setText(s);
        return convertView;
    }

    /**
     * Method to get count children in group list
     * @param groupPosition for parent id
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    /**
     * Method to get group object
     * @param groupPosition for parent id
     * @return group object
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    /**
     * Method to get count group
     * @return size of date header or group
     */
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    /**
     * Method to get group id
     * @param groupPosition int for group position id
     * @return id of group
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Method to get group view
     * @param groupPosition for tracking the group position
     * @param parent for view group of parent
     * @param convertView for converting the current view
     * @param isExpanded for expending parent with child data
     * @return converted view
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final Seller headerTitle = (Seller) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_seller, null);
        }

        TextView ListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ListHeader.setTypeface(null, Typeface.BOLD);
        ListHeader.setText("" + headerTitle.getName());

        return convertView;
    }

    /**
     * Method to get if list has stable id
     * @return boolean false
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Method to get if child is selected or no
     * @param groupPosition to know the group position
     * @param childPosition to know the child position
     * @return boolean true
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
