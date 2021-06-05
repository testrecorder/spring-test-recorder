package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

@Getter
public class ObjectInfoStatus {
    // avoid cyclic traversal for object init
    private boolean initPrepared = false;
    private boolean initDone = false;

    public void setInitPrepared(boolean initPrepared) {
        this.initPrepared = initPrepared;
    }

    public void setInitDone(boolean initDone) {
        this.initDone = initDone;
    }
}
