package dev.vitorzucon.repository;

import dev.vitorzucon.entity.SupplyEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplyRepository implements PanacheRepositoryBase<SupplyEntity,Long> {
}
