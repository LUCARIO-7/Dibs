package org.me.dibs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.me.dibs.Repository.ItemRepository;
import org.me.dibs.model.Item;
import org.me.dibs.service.ItemService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, TestResultLoggerExtension.class})
public class ItemServiceTests {
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    ItemService itemService;

    @Test
    void testGetItem() {
        Item mockItem = new Item();
        mockItem.setId(5);
        mockItem.setIsClaimed(false);
        when(itemRepository.findById(5)).thenReturn(Optional.of(mockItem));
        Item item = itemService.getItem(5).orElse(null);
        assertNotNull(item);
        assertThat(item.getId()).isEqualTo(5);
        assertThat(item.getIsClaimed()).isFalse();
    }

    @Test
    void testFoundItems() {
        Item mockItem = new Item();
        mockItem.setId(1);
        mockItem.setIsLost(false);
        when(itemRepository.findByIsLostFalse()).thenReturn(List.of(mockItem));

        List<Item> foundItems = itemService.getFoundItems();
        assertNotNull(foundItems);
        assertThat(foundItems).hasSize(1);
        assertThat(foundItems.get(0).getId()).isEqualTo(1);
        verify(itemRepository).findByIsLostFalse();
    }

    @Test
    void testLostItems() {
        Item mockItem = new Item();
        mockItem.setId(2);
        mockItem.setIsLost(true);
        when(itemRepository.findByIsLostTrue()).thenReturn(List.of(mockItem));

        List<Item> lostItems = itemService.getLostItems();
        assertNotNull(lostItems);
        assertThat(lostItems).hasSize(1);
        assertThat(lostItems.get(0).getId()).isEqualTo(2);
        verify(itemRepository).findByIsLostTrue();
    }

    @Test
    void testRemoveItem() {
        Item mockItem = new Item();
        mockItem.setId(5);
        when(itemRepository.findById(5)).thenReturn(Optional.of(mockItem));

        itemService.removeItem(5);

        verify(itemRepository).findById(5);
        verify(itemRepository).delete(mockItem);
    }
}