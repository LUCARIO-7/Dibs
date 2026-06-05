package org.me.dibs.service;

import org.me.dibs.Repository.ItemRepository;
import org.me.dibs.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;
    public void  addItem(Item item){
        itemRepository.save(item);
    }

    public List<Item> getLostItems(){
        List<Item> items=itemRepository.findByIsLostTrue();
        return  items;
    }
    public List<Item> getFoundItems(){
        List<Item> items=itemRepository.findByIsLostFalse();
        return items;
    }

    public void removeItem(int i) {
        Item it=itemRepository.findById(i).orElse(new Item());
        itemRepository.delete(it);
    }
}
