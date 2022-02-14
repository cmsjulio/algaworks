package com.example.algamoney.api.resource;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@RestController //Declarando que trata-se de um REST: requisitção e reposta com json.
@RequestMapping("/categorias") //Este objeto é mapeado no endereço /categorias
public class CategoriaResource {

    @Autowired //em vez de ... = new CategoriaRepository -- interface.
    private CategoriaRepository categoriaRepository; //Importa métodos do JPA repository.

    @GetMapping //este método é associado ao verbo GET, no protocolo HTTP.
    public List<Categoria> listar(){
        return categoriaRepository.findAll();

    }

    @GetMapping("/outro")
    public String outro (){
        return "OK";
    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED) //para retornar o status 201 CREATED ao se executar o método.
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response){
        //@RequestBody pra converter a entrada do json direto num objeto Categoria.
        //HttpServletResponse pra enviar informação de como acessar categoria recém criada no header.
       Categoria categoriaSalva = categoriaRepository.save(categoria);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(categoriaSalva.getCodigo()).toUri();
        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(categoriaSalva); //usando o responseentity, inutiliza-se o @ResponseStatus

}

    @GetMapping(path = "/{codigo}")
    public Categoria buscarPeloCodigo(@PathVariable long codigo){
        return categoriaRepository.findById(codigo)
                .orElseThrow(null);
    }



}
