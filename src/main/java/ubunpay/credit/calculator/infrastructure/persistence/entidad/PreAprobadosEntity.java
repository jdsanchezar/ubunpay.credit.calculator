package ubunpay.credit.calculator.infrastructure.persistence.entidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paprobados")
public class PreAprobadosEntity implements Serializable {

	private static final long serialVersionUID = -2463354084291480284L;

	@Id
	private Integer id;

	@Column(nullable = false)
	private String nombre;
	
	@Column(nullable = false)
	private String asociado;

	@Column(nullable = false, name = "rango_edad")
	private String rangoEdad;

	@Column(nullable = false, name = "resultado_modelo")
	private String resultadoModelo;

	@Column(nullable = false, name = "nivel_riesgo_post")
	private Integer nivelRiesgoPost;

	@Column(nullable = false, name = "nivel_riesgo_post_score")
	private String nivelRiesgoPostScore;

	@Column(nullable = false)
	private BigDecimal tasa;

	@Column(nullable = false, name = "capacidad_pago")
	private Double capacidadPago;

	@Column(nullable = false)
	private Double iva;

	@Column(nullable = false, name = "seguro_vida_por")
	private BigDecimal seguroVidaPor;

	@Column(nullable = false, name = "seguro_vida")
	private Double seguroVida;

	@Column(nullable = false, name = "nva_capacidad")
	private Double nvaCapacidad;

	@Column(nullable = false)
	private double validacion;

	@Column(nullable = false, name = "descuento_credito")
	private Double descuentoCredito;

	@Column(nullable = false, name = "monto_desembolsar")
	private Double montoDesembolsar;

	@Column(nullable = false)
	private String pagare;
}
