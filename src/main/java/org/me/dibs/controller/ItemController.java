package org.me.dibs.controller;

import org.me.dibs.model.Item;
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
    @PostMapping("/item")
    ResponseEntity<String> addItem(@RequestPart Item item,@RequestPart MultipartFile image) throws IOException {
        itemService.addItem(item,image);
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
