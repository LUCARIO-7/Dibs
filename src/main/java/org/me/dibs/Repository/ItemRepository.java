package org.me.dibs.Repository;

import org.me.dibs.model.Item;
import org.me.dibs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
   List<Item> findByIsLostTrue();
   List<Item> findByIsLostFalse();
   List<Item> findByUserUsernameAndIsLostTrue(String username);
   List<Item> findByUserUsernameAndIsLostFalse(String username);
}
