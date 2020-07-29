package ubunpay.credit.calculator.infrastructure.persistence.repositorio.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import ubunpay.credit.calculator.infrastructure.persistence.entidad.PreAprobadosEntity;

public interface RepositorioPreAprobadosJPA extends JpaRepository<PreAprobadosEntity, Integer> {


}
