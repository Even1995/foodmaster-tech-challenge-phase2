package br.com.app.foodmaster.repositories;

import br.com.app.foodmaster.entities.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, UUID> {

}
