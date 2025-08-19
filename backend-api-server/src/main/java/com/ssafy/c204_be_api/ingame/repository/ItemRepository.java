package com.ssafy.c204_be_api.ingame.repository;

import com.ssafy.c204_be_api.ingame.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}