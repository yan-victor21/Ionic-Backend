package com.yan.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.yan.cursomc.domain.Cliente;
import com.yan.cursomc.domain.enums.TipoCliente;
import com.yan.cursomc.dto.ClienteNewDTO;
import com.yan.cursomc.repositories.ClienteRepository;
import com.yan.cursomc.resourses.exceptions.FieldMenssage;
import com.yan.cursomc.services.validation.utils.BR;

import org.springframework.beans.factory.annotation.Autowired;


public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
    
    @Autowired
    private ClienteRepository clienteRepository; 
     
    @Override
    public void initialize(ClienteInsert ann) {
    }

    @Override
    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
        List<FieldMenssage> list = new ArrayList<>();
        
        if(objDto.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())){
            list.add(new FieldMenssage("cpfOuCnpj","CPF invalido"));
        }
        if(objDto.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())){
            list.add(new FieldMenssage("cpfOuCnpj","CNPJ invalido"));
        }

        Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
        if (aux != null) {
            list.add(new FieldMenssage("email","Email ja existe"));
        }

        for (FieldMenssage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
            .addPropertyNode(e.getFildName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
