package com.example.dwkim.gomtalk.conversationlist;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.example.dwkim.gomtalk.data.ContactList;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
 * Created by dwkim on 2015-06-15.
 */
public class ConversationListItem {
    public final TextView mRecipientView;
    private final TextView mSnippetView;

    public static ConversationListItem from(View view) {
        if(view.getTag() == null){
            ConversationListItem listItem = new ConversationListItem(view);
            view.setTag(listItem);
            return listItem;
        } else {
            return (ConversationListItem)view.getTag();
        }
    }

    public ConversationListItem(View view) {
        mRecipientView = (TextView)view.findViewById(android.R.id.text1);
        mSnippetView = (TextView)view.findViewById(android.R.id.text2);
    }

    public void bind(Cursor cursor) {
        String addresses = cursor.getString(cursor.getColumnIndex(GomTalkProviderContract.Threads.RECIPIENTS));
        ContactList contactList = new ContactList(addresses);
        mRecipientView.setText(contactList.getFormattedAddress());

        String snippet = cursor.getString(cursor.getColumnIndex(GomTalkProviderContract.Threads.SNIPPET));
        mSnippetView.setText(snippet);
    }

//    public String getFormattedAddress(String[] addresses) {
//        boolean isFirst = true;
//        StringBuffer sb = new StringBuffer();
//
//        for(Contact contact : this){
//            if(isFirst){
//                isFirst = false;
//            } else {
//                sb.append(", ");
//            }
//
//            sb.append(contact.getFormattedAddress());
//
//        }
//
//        return sb.toString();
//    }
}
