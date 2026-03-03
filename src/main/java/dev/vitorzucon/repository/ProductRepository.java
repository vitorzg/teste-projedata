package dev.vitorzucon.repository;

import dev.vitorzucon.DTOs.ProductMaxProductionDTO;
import dev.vitorzucon.entity.ProductEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<ProductEntity,Long> {

    public List<ProductMaxProductionDTO> findMaxProduction(Integer page, Integer linesPerPage) {

        return getEntityManager()
                .createQuery("""
                    select new dev.vitorzucon.DTOs.ProductMaxProductionDTO(
                        p.id,
                        p.name,
                        p.price,
                        MIN(s.stock / psi.quantity)
                    )
                    from ProductEntity p
                    join p.supplyItems psi
                    join psi.supply s
                    where p.active = true
                    group by p.id, p.name
                    order by MIN(s.stock / psi.quantity) desc
                """, ProductMaxProductionDTO.class)
                .setFirstResult(page * linesPerPage)
                .setMaxResults(linesPerPage)
                .getResultList();
    }

}
