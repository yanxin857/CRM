package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Tran;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);
}
