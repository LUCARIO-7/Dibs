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
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ImageService imageService;

    @Override
    @Transactional
    public void addItem(Item item, MultipartFile image, Principal principal) throws IOException {
        item.setImage(imageService.extractBytes(image));
        item.setUser(userService.getUser(principal.getName()));
        itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getMyLostItems(Principal principal){
        return itemRepository.findByUserUsernameAndIsLostTrue(principal.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getMyFoundItems(Principal principal){
        System.out.println(principal.getName());
        return itemRepository.findByUserUsernameAndIsLostFalse(principal.getName());
    }

    @Override
    public void removeItem(int i) {
        Item it = itemRepository.findById(i).orElse(new Item());
        itemRepository.delete(it);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> searchItem(String name) {
        return itemRepository.findByNameIsContaining(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> getItem(int id) {
        return itemRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getLostItems() {
        return itemRepository.findByIsLostTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getFoundItems() {
        return itemRepository.findByIsLostFalse();
    }

    @Override
    public void claimItem(Integer itemid, Principal principal) {
        Item item = itemRepository.findById(itemid).orElse(new Item());
        item.setIsClaimed(true);
        item.setUser1(userService.getUser(principal.getName()));
        itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getClaimedItems(String name) {
        return itemRepository.findByUser1UsernameAndIsClaimedTrue(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getItemsbyLocation(String location){
        return itemRepository.findByLocationAndIsLostFalse(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getItemsBytime(int days){
        List<Item> resItems, items;
        items = itemRepository.findByIsLostFalse();
        //TODO: implement logic
        resItems = items;
        return resItems;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getItems(){
        return itemRepository.findAll();
    }
}
