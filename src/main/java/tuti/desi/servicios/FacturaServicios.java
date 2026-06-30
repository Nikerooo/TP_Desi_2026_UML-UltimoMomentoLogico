package tuti.desi.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.enums.EstadoContrato;
import tuti.desi.enums.EstadoFactura;
import tuti.desi.historial.HistorialEstadoFactura;
import tuti.desi.persistencia.ContratoPersistencia;
import tuti.desi.persistencia.FacturaPersistencia;

/**
 * @author IvanBenitez - 39633003
 */

@Service
public class FacturaServicios {
    @Autowired
    private FacturaPersistencia facturaRepo;

    @Autowired
    private ContratoPersistencia contratoRepo;
    
    // ALTA DE FACTURA
    public Factura crearFactura(Factura factura) {
        // Validamos que el contrato existe
        Contrato contrato = contratoRepo.findById(factura.getContrato().getId())
                .orElseThrow(() -> new RuntimeException("El contrato seleccionado no existe"));

        // Validamos que el contrato este activo
        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new RuntimeException("No se puede facturar sobre un contrato que no esta activo");
        }

        // Validamos fechas
        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new RuntimeException("La fecha de vencimiento tiene que ser posterior a la fecha de emision");
        }

        // Validamos que el importe sea positivo
        if (factura.getImporte() == null || factura.getImporte().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El importe tiene que ser un numero positivo");
        }

        // Crear Factura
        Factura facturaNueva = new Factura();
        facturaNueva.setFechaEmision(factura.getFechaEmision());
        facturaNueva.setFechaVencimiento(factura.getFechaVencimiento());
        facturaNueva.setImporte(factura.getImporte());
        facturaNueva.setContrato(contrato);
        facturaNueva.setConceptoFacturado(factura.getConceptoFacturado());
        facturaNueva.setEliminada(false);
        facturaNueva.setEstado(EstadoFactura.PENDIENTE);
        facturaNueva.setFechaPago(null);
        facturaNueva.setMedio(null);
        facturaNueva.setImportePago(null);
        facturaNueva.setInteres(null);
        agregarHistorialDeEstado(facturaNueva, EstadoFactura.PENDIENTE);

        // Guardar Factura
        return facturaRepo.save(factura);
    }
    
    
    // MODIFICACION DE FACTURA
    public Factura actualizarFactura(Long id, Factura facturaRecibida) {
        // Buscar Factura
        Factura factura = facturaRepo.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada."));

        // Antes de acturalizar validamos que no este anulada o pagada
        if (factura.getEstado() == EstadoFactura.PAGADA) { throw new RuntimeException("La factura esta pagada"); }
        if (factura.getEstado() == EstadoFactura.ANULADA) { throw new RuntimeException("La factura esta anulada"); }

        // Si no esta, la actualizamos
        factura.setConceptoFacturado(facturaRecibida.getConceptoFacturado());
        factura.setFechaEmision(facturaRecibida.getFechaEmision());
        factura.setFechaVencimiento(facturaRecibida.getFechaVencimiento());
        factura.setImporte(facturaRecibida.getImporte());
        
        // Guardamos cambios
        return facturaRepo.save(factura);
    }
    
    
    // ELIMINACION DE FACTURA
    public void eliminarFactura(Long id) {
        // Verificamos que exista
        Factura factura = facturaRepo.findById(id).orElseThrow(() -> new RuntimeException("No existe la factura con ese ID"));

        // Validamos que no se haya pagado
        if (factura.getEstado() == EstadoFactura.PAGADA) {throw new RuntimeException("No se puede eliminar una factura que esta pagada");}

        // Eliminamos una vez que paso los filtros
        int facturaEliminada = facturaRepo.fueEliminada(id);
        if (facturaEliminada == 0) { throw new RuntimeException("No se puede eliminar la factura porque esta pagada"); }
    }
    
    
    // LISTADO DE FACTURAS
    public List<Factura> listarFacturas(Contrato contrato, LocalDate fechaDesde, LocalDate fechaHasta, Persona inquilino, Propiedad propiedad, EstadoFactura estado ) {
    	return facturaRepo.buscar(contrato, fechaDesde, fechaHasta, inquilino, propiedad, estado);
	}
    
    
    // METODOS
    private void agregarHistorialDeEstado(Factura factura, EstadoFactura estado) {
        HistorialEstadoFactura historial = new HistorialEstadoFactura();
        historial.setFactura(factura);
        historial.setEstado(estado);
        historial.setFechaCambio(LocalDateTime.now());
        factura.getHistorialEstados().add(historial);
    }
    
    public Factura obtenerFacturaPorId(Long id) {
     return facturaRepo.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada con el id" + id));
    }
}
