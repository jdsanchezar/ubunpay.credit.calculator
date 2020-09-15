package ubunpay.credit.calculator.infrastructure.persistence.repositorio.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

import ubunpay.commons.domain.model.calc.PreAprobadosEntity;

public interface RepositorioPreAprobadosJPA extends JpaRepository<PreAprobadosEntity, Integer> {


}
