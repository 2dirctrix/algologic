package com.ssafy.c204_be_api.ingame.repository;

import com.ssafy.c204_be_api.ingame.domain.Spell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpellRepository extends JpaRepository<Spell, Long> {
}
