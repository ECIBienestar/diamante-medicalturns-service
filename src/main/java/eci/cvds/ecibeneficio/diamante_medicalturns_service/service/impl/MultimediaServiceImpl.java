package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Multimedia;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.MultimediaRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.MultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultimediaServiceImpl implements MultimediaService {

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Override
    public void createMultimedia(CreateMultimediaRequest request) {
        Multimedia multimedia = new Multimedia();
        multimedia.setName(request.getName());
        multimedia.setUrl(request.getUrl());
        multimedia.setDuration(request.getDuration());
        multimedia.setType(request.getType());
        multimediaRepository.save(multimedia);
    }

    @Override
    public MultimediaResponse getMultimedia(Long id) {
        Multimedia multimedia = multimediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multimedia no encontrada"));
        return mapToResponse(multimedia);
    }

    @Override
    public List<MultimediaResponse> getAllMultimedia() {
        return multimediaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMultimedia(Long id) {
        multimediaRepository.deleteById(id);
    }

    private MultimediaResponse mapToResponse(Multimedia multimedia) {
        MultimediaResponse response = new MultimediaResponse();
        response.setId(multimedia.getId());
        response.setName(multimedia.getName());
        response.setUrl(multimedia.getUrl());
        response.setDuration(multimedia.getDuration());
        response.setType(multimedia.getType());
        return response;
    }
}
