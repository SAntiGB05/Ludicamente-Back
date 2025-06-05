package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.repository.BitacoraRepository;
import com.ludicamente.Ludicamente.service.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BitacoraServiceImpl implements BitacoraService {

    private final BitacoraRepository bitacoraRepository;

    @Autowired
    public BitacoraServiceImpl(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    @Override
    public Bitacora crearBitacora(Bitacora bitacora) {
        return bitacoraRepository.save(bitacora);
    }

    @Override
    public List<Bitacora> listarBitacoras() {
        return bitacoraRepository.findAll();
    }

    @Override
    public Optional<Bitacora> actualizarBitacora(Integer id, Bitacora bitacoraActualizada) {
        Optional<Bitacora> bitacoraExistente = bitacoraRepository.findById(id);
        if (bitacoraExistente.isPresent()) {
            Bitacora bitacora = bitacoraExistente.get();

            bitacora.setDescripcionGeneral(bitacoraActualizada.getDescripcionGeneral());
            bitacora.setOportunidades(bitacoraActualizada.getOportunidades());
            bitacora.setDebilidades(bitacoraActualizada.getDebilidades());
            bitacora.setAmenazas(bitacoraActualizada.getAmenazas());
            bitacora.setFortalezas(bitacoraActualizada.getFortalezas());
            bitacora.setObjetivos(bitacoraActualizada.getObjetivos());
            bitacora.setHabilidades(bitacoraActualizada.getHabilidades());
            bitacora.setSeguimiento(bitacoraActualizada.getSeguimiento());
            bitacora.setHistorialActividad(bitacoraActualizada.getHistorialActividad());
            bitacora.setEmpleado(bitacoraActualizada.getEmpleado());

            return Optional.of(bitacoraRepository.save(bitacora));
        }
        return Optional.empty();
    }


    @Override
    public boolean eliminarBitacora(Integer id) {
        if (bitacoraRepository.existsById(id)) {
            bitacoraRepository.deleteById(id);
            return true;
        }
        return false;
    }
}