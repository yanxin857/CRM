package com.bjpowernode.workbench.service;

import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
