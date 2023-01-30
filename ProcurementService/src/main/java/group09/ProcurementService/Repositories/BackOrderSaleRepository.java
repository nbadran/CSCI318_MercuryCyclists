package group09.ProcurementService.Repositories;

import group09.ProcurementService.Entities.BackOrderSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackOrderSaleRepository extends JpaRepository<BackOrderSale, Long> {
}
