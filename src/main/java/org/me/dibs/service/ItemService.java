package org.me.dibs.service;

import org.me.dibs.Repository.ItemRepository;
import org.me.dibs.model.Item;
import org.me.dibs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    userService UserService;
    @Transactional
    public void  addItem(Item item, MultipartFile image, Principal principal) throws IOException {
        item.setImage(image.getBytes());
        item.setUser(UserService.getUser(principal.getName()));
        itemRepository.save(item);
    }
    @Transactional(readOnly = true)
    public List<Item> getLostItems(Principal principal){
        List<Item> items=itemRepository.findByUserUsernameAndIsLostTrue(principal.getName());
        return  items;
    }
    @Transactional(readOnly = true)
    public List<Item> getFoundItems(Principal principal){
        List<Item> items=itemRepository.findByUserUsernameAndIsLostFalse(principal.getName());
        return items;
    }

    public void removeItem(int i) {
        Item it=itemRepository.findById(i).orElse(new Item());
        itemRepository.delete(it);
    }
}
