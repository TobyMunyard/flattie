package com.example.flattie.service;

import com.example.flattie.repository.ChoreListRepository;
import com.example.flattie.model.ChoreList;
import com.example.flattie.model.ChoreListItem;
import com.example.flattie.model.Flat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoreListService {
    private final ChoreListRepository choreListRepository;

    @Autowired
    public ChoreListService(ChoreListRepository choreListRepository) {
        this.choreListRepository = choreListRepository;
    }

    public List<ChoreListItem> getChoresForFlat(Long flatId) {
        return choreListRepository.findByFlatId(flatId);
    }

    public void addChoreToFlat(Flat flat, ChoreListItem choreItem) {
        ChoreList choreList = new ChoreList(flat, choreItem);
        choreListRepository.save(choreList);
    }
}
