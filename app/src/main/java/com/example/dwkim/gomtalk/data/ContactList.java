package com.example.dwkim.gomtalk.data;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by dwkim on 2015-06-13.
 */
public class ContactList extends ArrayList<Contact>{
    public ContactList(String semiSeperatedAddress) {

        String[] arrAddress = TextUtils.split(semiSeperatedAddress, ";");
        for(String address : arrAddress) {
            add(Contact.get(address));
        }
    }

    public ContactList() {

    }

    public String getSemiSeparatedAddresses() {
        boolean isFirst = true;
        StringBuffer sb = new StringBuffer();

        for(Contact contact : this){
            sb.append(contact.getAddress());

            if(isFirst){
                isFirst = false;
            } else {
                sb.append(';');
            }
        }

        return sb.toString();
    }

    public String getFormattedAddress() {
        boolean isFirst = true;
        StringBuffer sb = new StringBuffer();

        for(Contact contact : this){
            if(isFirst){
                isFirst = false;
            } else {
                sb.append(", ");
            }

            sb.append(contact.getFormattedAddress());

        }

        return sb.toString();
    }


    public String[] getAddresses() {
        int count = size();

        String[] addresses = new String[count];
        for(int i=0; i<count; i++){
            addresses[i] = get(i).getAddress();
        }

        return addresses;
    }

    @Override
    public String toString() {
        if(isEmpty()) {
            return "ContactList Empty";
        } else {
            return getSemiSeparatedAddresses();
        }
    }
}
