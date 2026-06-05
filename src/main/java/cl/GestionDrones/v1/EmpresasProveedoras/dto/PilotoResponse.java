package cl.GestionDrones.v1.EmpresasProveedoras.dto;

import java.time.LocalDate;

public record PilotoResponse (

    Integer id,
    String run,
    String nombres,
    String apellidos,
    String telefono,
    String numeroCertificadoDgac,
    LocalDate fechaVencimientoCertificacion
)
{
}
