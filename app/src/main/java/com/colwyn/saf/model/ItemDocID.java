package com.colwyn.saf.model;

import androidx.annotation.NonNull;

public class ItemDocID {

    public String docID;

    public <T extends ItemDocID> T withId(@NonNull final String id) {
        this.docID = id;
        return (T) this;
    }
}
