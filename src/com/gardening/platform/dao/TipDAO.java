package com.gardening.platform.dao;

import com.gardening.platform.model.Tip;
import java.util.List;

public interface TipDAO {
    void addTip(Tip tip);
    List<Tip> getAllTips();
    List<Tip> getTipsByUserId(int userId);
    void deleteTip(int id);
}
