package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.MultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/multimedia")
public class MultimediaController {

    @Autowired
    private MultimediaService multimediaService;

    @PostMapping
    public ResponseEntity<Void> uploadMultimedia(@RequestBody CreateMultimediaRequest request) {
        multimediaService.createMultimedia(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultimediaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(multimediaService.getMultimedia(id));
    }

    @GetMapping
    public ResponseEntity<List<MultimediaResponse>> getAll() {
        return ResponseEntity.ok(multimediaService.getAllMultimedia());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        multimediaService.deleteMultimedia(id);
        return ResponseEntity.noContent().build();
    }
}
