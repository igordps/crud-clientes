package com.devsuperior.crudclientes.services;

import com.devsuperior.crudclientes.dto.ClientDTO;
import com.devsuperior.crudclientes.dto.CustomError;
import com.devsuperior.crudclientes.dto.ValidationError;
import com.devsuperior.crudclientes.entities.Client;
import com.devsuperior.crudclientes.repositories.ClientRepository;
import com.devsuperior.crudclientes.services.exceptions.DatabaseException;
import com.devsuperior.crudclientes.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        return new ClientDTO(repository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Cliente inexistente"))
        );
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        Page<Client> clients = repository.findAll(pageable);
        return clients.map(cli -> new ClientDTO(cli));
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        Client client = new Client(dto);
        client = repository.save(client);

        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {

        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Cliente inexistente");
        }

        Client entity = repository.getReferenceById(id);
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        return new ClientDTO(entity);

    }

    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setChildren(dto.getChildren());
        entity.setBirthDate(dto.getBirthDate());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Cliente inexistente");
        }

        try{
            repository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
