package group09.SalesService.Repositories;

import group09.SalesService.Entities.OnlineSale;
import group09.SalesService.Entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineSaleRepository extends JpaRepository<OnlineSale, Long> {
}
