package group09.SalesService.Repositories;

import group09.SalesService.Entities.InStoreSale;
import group09.SalesService.Entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
