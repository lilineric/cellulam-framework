package com.cellulam.uid.metadata;

import com.cellulam.uid.UidGenerator;

public interface UidAppendGenerator extends UidGenerator {
    /**
     * append the pid to the generated uid
     * @param pid
     * @return
     */
    long nextId(long pid);
}
