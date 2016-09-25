package com.example.dwkim.gomtalk.data;

import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by dwkim on 2015-06-13.
 * Contact객체안에서 address는 항상 normalized상태로만 존재한다
 * 성능을 위해 캐시에서만 address <> Normalized address 매칭을 위해 일부 사용
 */

public class Contact {
    private static Cache sCache;
    private final NormalizedAddress mAddress;
    private String mFormattedAddress;

    public static Contact get(String address) {
        if(TextUtils.isEmpty(address)){
            throw new IllegalArgumentException("address is null or empty!");
        }

        return getCache().get(address);
    }

    private static Cache getCache() {
        if (sCache == null) {
            sCache = new Cache();
        }

        return sCache;
    }

    private Contact(NormalizedAddress address){
        mAddress = address;
    }

    public String getAddress(){
        return mAddress.getAddress();
    }

    //only for debug
    public static int getCacheCount(){
        return getCache().getCount();
    }

    public synchronized String getFormattedAddress() {
        if(mFormattedAddress == null){
            mFormattedAddress = PhoneNumberUtils.formatNumber(mAddress.getAddress(), Locale.getDefault().getCountry());
        }

        return mFormattedAddress;
    }

    private static class Cache{
        private HashMap<NormalizedAddress, Contact> mMap = new HashMap<NormalizedAddress, Contact>();
        private Cache(){

        }

        public Contact get(String address){
            NormalizedAddress na = NormalizedAddress.get(address);

            Contact contact = mMap.get(na);
            if(contact == null) {
                contact = new Contact(na);
                mMap.put(na, contact);
            }
            return contact;
        }

        public int getCount() {
            return mMap.size();
        }
    }

    private static class NormalizedAddress{
        private static HashMap<String, NormalizedAddress> sMap = new HashMap<String, NormalizedAddress>();

        public static NormalizedAddress get(String address){
            NormalizedAddress na = sMap.get(address);
            if(na == null){
                na = new NormalizedAddress(address);
                sMap.put(address, na);
            }

            return na;
        }

        private String mAddress;
        public NormalizedAddress(String address){
            mAddress = PhoneNumberUtils.stripSeparators(address);
        }

        public String getAddress(){
            return mAddress;
        }

        @Override
        public boolean equals(Object other) {
            boolean result = false;
            if(other == null){
                result = false;
            } else if(other instanceof NormalizedAddress) {
                result = mAddress.equals(((NormalizedAddress)other).getAddress());

            } else {
//                return false;
            }
            return result;
        }

        @Override
        public int hashCode() {
            return mAddress.hashCode();
        }
    }
}
