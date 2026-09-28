package com.example.vendas_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vendas_service.models.Venda;
import org.springframework.stereotype.Repository;

@Repository
public interface VendasRepository extends JpaRepository<Venda, Long> {}
