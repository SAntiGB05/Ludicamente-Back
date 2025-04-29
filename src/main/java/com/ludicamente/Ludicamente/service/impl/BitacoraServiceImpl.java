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
            bitacora.setAccion(bitacoraActualizada.getAccion());
            bitacora.setTablaAfectada(bitacoraActualizada.getTablaAfectada());
            bitacora.setIdRegistroAfectado(bitacoraActualizada.getIdRegistroAfectado());
            bitacora.setDatosAnteriores(bitacoraActualizada.getDatosAnteriores());
            bitacora.setDatosNuevos(bitacoraActualizada.getDatosNuevos());
            bitacora.setFechaHora(bitacoraActualizada.getFechaHora());
            bitacora.setEmpleado(bitacoraActualizada.getEmpleado());
            bitacora.setIpConexion(bitacoraActualizada.getIpConexion());
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