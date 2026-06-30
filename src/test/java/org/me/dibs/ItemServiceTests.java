package org.me.dibs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.me.dibs.Repository.ItemRepository;
import org.me.dibs.model.Item;
import org.me.dibs.service.ItemService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class ItemServiceTests {
    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemService itemService;
    @Test
    void testGetItem(){
        //Principal principal=mock(Principal.class);
        Optional<Item> item=itemService.getItem(5);
        assertNotNull(item);
    }
    @Test
    void TestFoundItems(){
        List<Item> foundItems=itemService.getFoundItems();
        assertNotNull(foundItems);
    }
    @Test
    void TestLostItems(){
        List<Item> foundItems=itemService.getLostItems();
        assertNotNull(foundItems);
    }
    @Test
    void testRemoveItems(){
        Item item=itemService.getItem(5).orElse(null);
        itemService.removeItem(5);
        Item item1=itemService.getItem(5).orElse(null);
        assertThat(item).isEqualTo(null);
    }
}
//