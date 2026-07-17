package org.me.dibs.service;

import org.me.dibs.Repository.ItemRepository;
import org.me.dibs.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public List<Item> getMyLostItems(Principal principal){
        return itemRepository.findByUserUsernameAndIsLostTrue(principal.getName());
    }
    @Transactional(readOnly = true)
    public List<Item> getMyFoundItems(Principal principal){
        System.out.println(principal.getName());
        return itemRepository.findByUserUsernameAndIsLostFalse(principal.getName());
    }

    public void removeItem(int i) {
        Item it=itemRepository.findById(i).orElse(new Item());
        itemRepository.delete(it);
    }
    @Transactional(readOnly = true)
    public List<Item> searchItem(String name) {

        return  itemRepository.findByNameIsContaining(name);
    }
    @Transactional(readOnly = true)
    public Optional<Item> getItem(int id) {

        return  itemRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public List<Item> getLostItems() {
        return  itemRepository.findByIsLostTrue();
    }
    @Transactional(readOnly = true)
    public List<Item> getFoundItems() {
        return  itemRepository.findByIsLostFalse();
    }

    public void claimItem(Integer itemid, Principal principal) {
        Item item= itemRepository.findById(itemid).orElse(new Item());
        item.setIsClaimed(true);
        item.setUser1(UserService.getUser(principal.getName()));
        itemRepository.save(item);
    }
    @Transactional(readOnly = true)
    public List<Item> getClaimedItems(String name) {

        return itemRepository.findByUser1UsernameAndIsClaimedTrue(name);
    }
    @Transactional(readOnly = true)
    public List<Item> getItemsbyLocation(String location){
        return itemRepository.findByLocationAndIsLostFalse(location);
    }
    @Transactional(readOnly = true)
    public  List<Item> getItemsBytime(int days){
        List<Item> resItems,items;
        items=itemRepository.findByIsLostFalse();
        //TODO:implement logic
        resItems=items;
        return resItems;
    }
    @Transactional(readOnly = true)
    public List<Item> getItems(){
        List<Item> item=itemRepository.findAll();
        return  item;
    }
}
