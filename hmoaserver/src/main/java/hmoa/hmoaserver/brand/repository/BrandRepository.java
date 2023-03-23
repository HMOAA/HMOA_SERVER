package hmoa.hmoaserver.brand.repository;

import hmoa.hmoaserver.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
