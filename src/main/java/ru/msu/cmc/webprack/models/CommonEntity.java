package ru.msu.cmc.webprack.models;

import java.io.Serializable;

public interface CommonEntity<ID extends Serializable> {
    ID getId();

    void setId(ID id);
}
