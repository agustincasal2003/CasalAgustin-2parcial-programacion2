package cine.modelo;

import java.math.BigDecimal;

/** Entrada de estudiante: 20 % de descuento sobre el precio general de la función. */
public class EntradaEstudiante extends Entrada {

    private static final long serialVersionUID = 1L;

    /** Descuento documentado del grupo: 20 % (coincide con el ejemplo de los wireframes). */
    public static final BigDecimal DESCUENTO = new BigDecimal("0.20");

    public EntradaEstudiante(Butaca butaca, BigDecimal precioBase) {
        super(butaca, precioBase);
    }

    @Override
    protected BigDecimal calcularPrecio(BigDecimal precioBase) {
        return precioBase.multiply(BigDecimal.ONE.subtract(DESCUENTO));
    }

    @Override
    public TipoEntrada getTipo() { return TipoEntrada.ESTUDIANTE; }
}
