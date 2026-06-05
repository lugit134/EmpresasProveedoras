package cl.GestionDrones.v1.EmpresasProveedoras.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.EmpresasProveedoras.dto.AeronaveResponse;
import cl.GestionDrones.v1.EmpresasProveedoras.dto.BitacoraResponse;
import cl.GestionDrones.v1.EmpresasProveedoras.dto.CreateEmpresaRequest;
import cl.GestionDrones.v1.EmpresasProveedoras.dto.PilotoResponse;
import cl.GestionDrones.v1.EmpresasProveedoras.dto.UpdateEmpresaRequest;
import cl.GestionDrones.v1.EmpresasProveedoras.model.EmpresaProveedora;
import cl.GestionDrones.v1.EmpresasProveedoras.service.EmpresasProveedorasService;
import cl.GestionDrones.v1.EmpresasProveedoras.webclient.AeronavesWebClient;
import cl.GestionDrones.v1.EmpresasProveedoras.webclient.BitacorasWebClient;
import cl.GestionDrones.v1.EmpresasProveedoras.webclient.PilotosWebClient;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/empresas-proveedoras")
public class EmpresasProveedorasController {

    @Autowired
    private EmpresasProveedorasService empresaProveedorasService;

    @GetMapping
    public ResponseEntity<List<EmpresaProveedora>> getAllEmpresas() {

        return new ResponseEntity<>(
                empresaProveedorasService.getEmpresasProveedoras(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmpresaById(@PathVariable Long id) {

        try {

            EmpresaProveedora empresa =
                    empresaProveedorasService.getEmpresaProveedoraById(id);

            return new ResponseEntity<>(empresa, HttpStatus.OK);

        } catch (Exception e) {

            Map<String, String> error = new HashMap<>();

            error.put("error", "No encontrado");
            error.put("mensaje", e.getMessage());

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEmpresa(
            @Valid @RequestBody CreateEmpresaRequest request,
            BindingResult result) {

        if (result.hasErrors()) {

            Map<String, String> errores = new HashMap<>();

            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage())
            );

            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

        try {

            EmpresaProveedora nuevaEmpresa =
                    empresaProveedorasService.saveEmpresaProveedora(request);

            return new ResponseEntity<>(nuevaEmpresa, HttpStatus.CREATED);

        } catch (Exception e) {

            Map<String, String> error = new HashMap<>();

            error.put("error", "Error al crear");
            error.put("mensaje", e.getMessage());

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateEmpresa(
            @Valid @RequestBody UpdateEmpresaRequest request,
            BindingResult result) {

        if (result.hasErrors()) {

            Map<String, String> errores = new HashMap<>();

            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage())
            );

            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

        try {

            EmpresaProveedora empresaActualizada =
                    empresaProveedorasService.updateEmpresaProveedora(request);

            return new ResponseEntity<>(empresaActualizada, HttpStatus.OK);

        } catch (Exception e) {

            Map<String, String> error = new HashMap<>();

            error.put("error", "Error al actualizar");
            error.put("mensaje", e.getMessage());

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmpresa(@PathVariable Long id) {

        try {

            String resultado =
                    empresaProveedorasService.deleteEmpresaProveedora(id);

            Map<String, String> respuesta = new HashMap<>();

            respuesta.put("mensaje", resultado);

            return new ResponseEntity<>(respuesta, HttpStatus.OK);

        } catch (Exception e) {

            Map<String, String> error = new HashMap<>();

            error.put("error", "Error al eliminar");
            error.put("mensaje", e.getMessage());

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getEmpresaPorEstado(
            @PathVariable String estado) {

        List<EmpresaProveedora> empresas =
                empresaProveedorasService.getEmpresaPorEstado(estado);

        if (empresas.isEmpty()) {

            Map<String, String> error = new HashMap<>();

            error.put("error", "No encontrado");
            error.put("mensaje",
                    "No hay empresas proveedoras con estado: " + estado);

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(empresas, HttpStatus.OK);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<?> getEmpresaPorRut(
            @PathVariable String rut) {

        List<EmpresaProveedora> empresas =
                empresaProveedorasService.getEmpresaPorRut(rut);

        if (empresas.isEmpty()) {

            Map<String, String> error = new HashMap<>();

            error.put("error", "No encontrado");
            error.put("mensaje",
                    "No hay empresas proveedoras con RUT: " + rut);

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(empresas, HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalEmpresas() {

        return new ResponseEntity<>(
                empresaProveedorasService.totalEmpresasProveedoras(),
                HttpStatus.OK
        );
    }

    @Autowired
    private AeronavesWebClient aeronavesWebClient;

    @GetMapping("/aeronaves/seguros-por-vencer")
    public ResponseEntity<?> getAeronavesConSeguroPorVencer() {

        List<AeronaveResponse> aeronaves =
                aeronavesWebClient.obtenerSegurosPorVencer();

        if (aeronaves == null || aeronaves.isEmpty()) {

            Map<String, String> error = new HashMap<>();
            error.put("error", "Sin resultados");
            error.put("mensaje",
                    "No existen aeronaves con seguros que venzan en los próximos 10 días");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(aeronaves);

        
    }//seguross

    @Autowired
    private PilotosWebClient pilotosWebClient;
    @GetMapping("/pilotos/por-vencer")
    public ResponseEntity<?> pilotosPorVencer() {

        List<PilotoResponse> pilotos =
                pilotosWebClient.obtenerPilotosPorVencer();

        if (pilotos == null || pilotos.isEmpty()) {

            Map<String, String> error = new HashMap<>();
            error.put("error", "Sin resultados");
            error.put("mensaje",
                    "No existen pilotos con certificaciones que venzan en los próximos 10 días");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(pilotos);

        
    }

    
//bitttt
    @Autowired
    private BitacorasWebClient bitacorasWebClient;

    @GetMapping("/bitacoras")
    public ResponseEntity<?> listarBitacoras() {

        List<BitacoraResponse> bitacoras =
                bitacorasWebClient.obtenerTodasLasBitacoras();

        if (bitacoras == null || bitacoras.isEmpty()){

            Map<String, String> error = new HashMap<>();
            error.put("error", "Sin resultados");
            error.put("mensaje",
                    "No existen aeronaves con seguros que venzan en los próximos 10 días");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

            return ResponseEntity.ok(bitacoras);
    }

}
