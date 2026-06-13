package org.me.dibs.controller;

import org.me.dibs.model.Item;
import org.me.dibs.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class ItemController {
    @Autowired
    ItemService itemService;
    @GetMapping("/")
    ResponseEntity<String> homePage(){
        return  new ResponseEntity<>("hi",HttpStatus.OK);
    }
    @PostMapping("/item")
    ResponseEntity<String> addItem(@RequestPart Item item, @RequestPart MultipartFile image, Principal principal) throws IOException {
        itemService.addItem(item,image,principal);
        return new ResponseEntity<>(principal.getName(),HttpStatus.ACCEPTED);
    }
    @GetMapping("/lostitems")
    ResponseEntity<List<Item>> getLostItems(){
        return  new ResponseEntity<>(itemService.getLostItems(),HttpStatus.OK);
    }
    @GetMapping("/founditems")
    ResponseEntity<List<Item>> getFoundItems(){
        return  new ResponseEntity<>(itemService.getFoundItems(),HttpStatus.OK);
    }
    @GetMapping("/mylostitems")
    ResponseEntity<List<Item>> getMyLostItems(Principal principal){
            return  new ResponseEntity<>(itemService.getMyLostItems(principal),HttpStatus.OK);
    }
    @GetMapping("/myfounditems")
    ResponseEntity<List<Item>> getMyFoundItems(Principal principal){
            return new ResponseEntity<>(itemService.getMyFoundItems(principal),HttpStatus.OK);
    }
    @DeleteMapping("deleteItem")
    ResponseEntity<String> removeItem(@RequestParam int i){
        itemService.removeItem(i);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/search/{name}")
    ResponseEntity<List<Item>> searchItem(@PathVariable String name,Principal principal){
        List<Item> items=itemService.searchItem(name,principal);
        return  new ResponseEntity<>(items,HttpStatus.OK);
    }
    @GetMapping("/claimitem/{itemid}")
    public ResponseEntity<?> claimItem (@PathVariable Integer itemid,Principal principal){
        itemService.claimItem(itemid,principal);
        return  ResponseEntity.ok("ok");
    }
}
