package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportService<T> {

    JpaRepository<T, Long> getReportRepository();

    default void save(T entity) {
        try {
            getReportRepository().save(entity);
        } catch (Exception e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
}
