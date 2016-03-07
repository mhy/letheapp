package com.hiphople.letheapp.util;

import java.util.StringTokenizer;

/**
 * TODO write javadoc
 * Created by MHY on 3/7/16.
 */
public class LeServerMessage {
    public final static String DOCUMENT_SRL = "docsrl";

    private String mBoard;
    private String mDocSrl;
    private String mTitle;

    public LeServerMessage(String rawMsg){
        //rawMsg - e.g. board_name,title,srl_aka_id
        StringTokenizer st = new StringTokenizer(rawMsg, ",");
        mBoard = st.nextToken();
        mTitle = st.nextToken();
        mDocSrl = st.nextToken();
    }

    public String getBoard() {
        return mBoard;
    }

    public String getDocSrl() {
        return mDocSrl;
    }

    public String getTitle() {
        return mTitle;
    }
}
