package org.me.dibs.contrller;

import org.me.dibs.model.Item;
import org.me.dibs.Repository.ItemRepository;
import org.me.dibs.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class ItemController {
    @Autowired
    ItemService itemService;
    @PostMapping("/")
    ResponseEntity<String> addItem(@RequestParam("image") MultipartFile file,
                                   @RequestParam("name") String name,
                                   @RequestParam("description") String description,
                                   @RequestParam("location") String location,
                                   @RequestParam("contact") String contact,
                                   @RequestParam("time") String time,
                                   @RequestParam("isLost") Boolean isLost) throws IOException {
        Item newItem = new Item();
        newItem.setName(name);
        newItem.setDescription(description);

        // Extract the raw binary data from the uploaded file
        newItem.setImage(file.getBytes());

        newItem.setLocation(location);
        newItem.setContact(contact);
        newItem.setTime(time);
        newItem.setIsLost(isLost);

        itemService.addItem(newItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("lostitems")
    ResponseEntity<List<Item>> getLostItems(){
            return  new ResponseEntity<>(itemService.getLostItems(),HttpStatus.OK);
    }
    @GetMapping("founditems")
    ResponseEntity<List<Item>> getFoundItems(){
            return new ResponseEntity<>(itemService.getFoundItems(),HttpStatus.OK);
    }
    @DeleteMapping("deleteItem")
    ResponseEntity<String> removeItem(@RequestParam int i){
        itemService.removeItem(i);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
