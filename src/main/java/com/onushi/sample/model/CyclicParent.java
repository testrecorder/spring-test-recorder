package com.onushi.sample.model;

import java.util.List;

public class CyclicParent {
    public int id;
    public List<CyclicChild> childList;
}
