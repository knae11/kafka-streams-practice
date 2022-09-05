package com.deliverystreams.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum District {
    SONG_PA(111),
    GANG_NAM(112),
    GANG_DONG(113),
    @Deprecated
    GAND_DONG(114);

    private int code;

    District(int code) {
        this.code = code;
    }
}
