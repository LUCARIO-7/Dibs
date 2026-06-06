package org.me.dibs.service;

import org.me.dibs.Repository.ItemRepository;
import org.me.dibs.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;
    @Transactional
    public void  addItem(Item item, MultipartFile image) throws IOException {
        item.setImage(image.getBytes());
        itemRepository.save(item);
    }
    @Transactional(readOnly = true)
    public List<Item> getLostItems(){
        List<Item> items=itemRepository.findByIsLostTrue();
        return  items;
    }
    @Transactional(readOnly = true)
    public List<Item> getFoundItems(){
        List<Item> items=itemRepository.findByIsLostFalse();
        return items;
    }

    public void removeItem(int i) {
        Item it=itemRepository.findById(i).orElse(new Item());
        itemRepository.delete(it);
    }
}
