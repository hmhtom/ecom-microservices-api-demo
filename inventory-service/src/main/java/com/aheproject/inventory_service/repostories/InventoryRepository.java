package com.aheproject.inventory_service.repostories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aheproject.inventory_service.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findBySkuCodeIn(List<String> skuCode);

}
