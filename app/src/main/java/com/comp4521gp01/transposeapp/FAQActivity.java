//# COMP 4521    #  CHEUNG, Wai Man Raymond   20199778   wmcheungaa@connect.ust.hk
//# COMP 4521    #  LAW, Chiu Kwan  20212087   cklawad@connect.ust.hk
//# COMP 4521    #  WONG, Ho Yin Calvin  20196726  hycwong@connect.ust.hk
package com.comp4521gp01.transposeapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by waimancheung on 24/5/2017.
 */

public class FAQActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expand_list);

        final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
            private String[] generalsTypes = new String[] { "    How to transpose a chord image", "    How to improve the quality of scan", "    How to get image in Web", "    How to manually adjust the border", "    How to manage documents in home screen" };
            private String[][] generals = new String[][] {
                    { "In the home page, tap the + icon to choose where you want to get the image from.\n" +
                            "Take a photo, get an image online or get an image from your gallery.\n\n" +
                            "Transpose App will then let you adjust the border of the image manually. Click OK to process\n\n" +
                            "Transpose App will scan all text in the image and show the result. If the detected texts cannot be satisfied you will also be able to edit them manually. The Refresh button is here to help yor renew the spacing.\n\n" +
                            "After that, you can transpose the chords into any key you want by clicking two transpose buttons." +
                            "If you are happy with the key, you can click ok and enter a name for the image and save it."},
                    { "The quality of scan depends on two factors: device camera capabilities and the light.\n" +
                            "To make a high quality scan, you could follow these tips:\n" +
                            "1. Ensure that your document is flat\n" +
                            "2. Ensure the environment is brightly enough.\n" +
                            "3. Ensure you device had the time to autofocus (usually wait 1 or 2 seconds until the picture appears sharp).\n" +
                            "4. Try to remain stable and hold your device at least 12 inches away; if you bring it too close, your photo will be blurry.\n" +
                            "5. Place the document on a contrasting and uniform background." },
                    { "The Web in Transpose App works similarly to other browser application. You may type in a URL or the phrase you want to search in the space at the top" +
                            "\n previous page, back page, refresh, ok button are provided" },
                    { "After you take the photo, Transpose App will let you adjust the border manually. Drag the border to match you texts. You may also rotate the image when it is necessary." },
                    { "1. Click on any images in home screen to view the image in full screen.\n" +
                            "2. Longtouch in full screen mode, you can delete the image." }

            };

            TextView getTextView() {
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, 180);
                TextView textView = new TextView(
                        FAQActivity.this);
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(36, 0, 0, 0);
                textView.setTextSize(20);
                return textView;
            }

            TextView getTextViewC() {
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(
                        FAQActivity.this);
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(36, 0, 0, 0);
                textView.setTextSize(18);
                return textView;
            }

            @Override
            public int getGroupCount() {
                // TODO Auto-generated method stub
                return generalsTypes.length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                // TODO Auto-generated method stub
                return generalsTypes[groupPosition];
            }

            @Override
            public long getGroupId(int groupPosition) {
                // TODO Auto-generated method stub
                return groupPosition;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                // TODO Auto-generated method stub
                return generals[groupPosition].length;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return generals[groupPosition][childPosition];
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                                     View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                LinearLayout ll = new LinearLayout(
                        FAQActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                TextView textView = getTextView();
                textView.setText(getGroup(groupPosition).toString());
                textView.setBackgroundColor(Color.parseColor("#55b53f"));
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                ll.addView(textView);

                return ll;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                LinearLayout llc = new LinearLayout(
                        FAQActivity.this);
                llc.setOrientation(LinearLayout.VERTICAL);
                TextView textView = getTextViewC();
                textView.setText(getChild(groupPosition, childPosition)
                        .toString());
                llc.addView(textView);
                return llc;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {
                //collapse the old expanded group, if not the same
                //as new group to expand
                for (int i = 0; i < getGroupCount(); i++) {
                    if (groupPosition != i) {
                        expandableListView.collapseGroup(i);
                    }
                    super.onGroupExpanded(groupPosition);
                }
            }

            @Override
            public boolean isChildSelectable(int groupPosition,
                                             int childPosition) {
                // TODO Auto-generated method stub
                return true;
            }

        };

        expandableListView.setAdapter(adapter);


     expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
        int groupPosition, int childPosition, long id) {

            return false;
        }


    });
}
}
