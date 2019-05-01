package com.yan.cursomc.services;

import java.util.List;
import java.util.Optional;

import com.yan.cursomc.domain.Cidade;
import com.yan.cursomc.domain.Cliente;
import com.yan.cursomc.domain.Endereco;
import com.yan.cursomc.domain.enums.Perfil;
import com.yan.cursomc.domain.enums.TipoCliente;
import com.yan.cursomc.dto.ClienteDTO;
import com.yan.cursomc.dto.ClienteNewDTO;
import com.yan.cursomc.repositories.ClienteRepository;
import com.yan.cursomc.repositories.EnderecoRepository;
import com.yan.cursomc.security.UserSS;
import com.yan.cursomc.services.exceptions.AuthorizationException;
import com.yan.cursomc.services.exceptions.DataIntegrityException;
import com.yan.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Cliente insert(Cliente obj) {

        obj.setId(null);
        obj = repo.save(obj);
        enderecoRepository.saveAll(obj.getEnderecos());
        return obj;
    }

    public Cliente find(Integer id) {
        UserSS user = UserService.authenticated();
        if(user == null|| !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())){
            throw new AuthorizationException("Acesso Negado");
        }
        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    public Cliente update(Cliente obj) {
        Cliente newObj = find(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Não é possivel excluir um cliente que possui produtos");
        }
        
    }

    public List<Cliente> findAll(){
        return repo.findAll();
    }

    public Cliente findByEmail(String email){
        UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}

		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
    }

    public Page<Cliente> findPage(Integer page,Integer linesPerPage,String orderBy,String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDto){
       return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null,null);
    }

    public Cliente fromDTO(ClienteNewDTO objDto){
        Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(),
         objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipoCliente()),pe.encode(objDto.getSenha()));
        Cidade cid = new Cidade(objDto.getCidadeId(),null,null);
        Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),
          objDto.getBairro(), objDto.getCep(), cli, cid);
        cli.getEnderecos().add(end);
        cli.getTelefones().add(objDto.getTelefone1());
        if (objDto.getTelefone2() != null) {
            cli.getTelefones().add(objDto.getTelefone2());    
        }
        if (objDto.getTelefone3() != null) {
            cli.getTelefones().add(objDto.getTelefone3());    
        }
        return cli;
    }

    private void updateData(Cliente newObj, Cliente obj){
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }

    
}