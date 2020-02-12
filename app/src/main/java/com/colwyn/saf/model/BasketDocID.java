package com.colwyn.saf.model;

import androidx.annotation.NonNull;

public class BasketDocID {

    public String docID;

    public <T extends BasketDocID> T withId(@NonNull final String id) {
        this.docID = id;
        return (T) this;
    }
}
