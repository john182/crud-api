package com.crud.user.resource;

import com.crud.user.event.FeatureCreatedEvent;
import com.crud.user.model.User;
import com.crud.user.repository.UserRepository;
import com.crud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Autowired
    private ApplicationEventPublisher publisher;


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
    public Page<User> search(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user, HttpServletResponse response) {
        User userSaved = service.save(user);
        publisher.publishEvent(new FeatureCreatedEvent(this, response, userSaved.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_USER') and #oauth2.hasScope('read')")
    public ResponseEntity<User> buscarPeloCodigo(@PathVariable Integer id) {
        Optional<User> user = repository.findById(id);
        return user.isPresent() ? ResponseEntity.ok(user.get()) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("(hasAuthority('ROLE_DELETE_USER') or hasAuthority('ROLE_DELETE_ANY_USER')) and #oauth2.hasScope('write')")
    public void remover(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public ResponseEntity<User> atualizar(@PathVariable Integer id, @Valid @RequestBody User user) {
        User userSaved = service.update(id, user);
        return ResponseEntity.ok(userSaved);
    }

    @PutMapping("/{codigo}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public void atualizarPropriedadeAtivo(@PathVariable Integer id, @RequestBody Boolean active) {
        service.updatePropertyActive(id, active);
    }
}
