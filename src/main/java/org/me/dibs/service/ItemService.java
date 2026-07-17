package org.me.dibs.service;

import org.me.dibs.model.Item;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    void addItem(Item item, MultipartFile image, Principal principal) throws IOException;
    List<Item> getMyLostItems(Principal principal);
    List<Item> getMyFoundItems(Principal principal);
    void removeItem(int i);
    List<Item> searchItem(String name);
    Optional<Item> getItem(int id);
    List<Item> getLostItems();
    List<Item> getFoundItems();
    void claimItem(Integer itemid, Principal principal);
    List<Item> getClaimedItems(String name);
    List<Item> getItemsbyLocation(String location);
    List<Item> getItemsBytime(int days);
    List<Item> getItems();
}
