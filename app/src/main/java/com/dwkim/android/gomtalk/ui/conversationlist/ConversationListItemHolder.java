package com.dwkim.android.gomtalk.ui.conversationlist;

import android.view.View;
import android.widget.TextView;

/**
 * Created by dwkim on 2015-06-15.
 */
public class ConversationListItemHolder {
    public final TextView recipientView;
    public final TextView snippetView;

    public static ConversationListItemHolder from(View view) {
        if(view.getTag() == null){
            ConversationListItemHolder listItem = new ConversationListItemHolder(view);
            view.setTag(listItem);
            return listItem;
        } else {
            return (ConversationListItemHolder)view.getTag();
        }
    }

    public ConversationListItemHolder(View view) {
        recipientView = (TextView)view.findViewById(android.R.id.text1);
        snippetView = (TextView)view.findViewById(android.R.id.text2);
    }
}
