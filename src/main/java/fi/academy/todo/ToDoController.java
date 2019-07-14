package fi.academy.todo;

import fi.academy.todo.dao.ToDoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/todo")
public class ToDoController {
    private ToDoDao dao;

    @Autowired
    public ToDoController(ToDoDao dao) {
        this.dao = dao;
    }

    @GetMapping("")
    public List<Tehtava> listaaKaikki() {
        List<Tehtava> kaikki = dao.haeKaikki();
        System.out.printf("Haetaan tehtävät, tehtäviä: %d kpl\n", kaikki.size());
        return kaikki;
    }

    @PostMapping("")
    public ResponseEntity<?> luoUusi(@RequestBody Tehtava tehtava) {
        int id = dao.lisaa(tehtava);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(tehtava);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> poista(@PathVariable int id) {
        Tehtava poistettu = dao.poista(id);
        if (poistettu != null) {
            return ResponseEntity.ok(poistettu);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Virheviesti(String.format("Id %d ei löytynyt; ei poistettu.", id)));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> muokkaa(@RequestBody Tehtava tehtava, @PathVariable("id") int id) {
        boolean muuttiko = dao.muuta(id, tehtava);
        if (muuttiko) {
            tehtava.setId(id);
            return ResponseEntity.ok(tehtava);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Virheviesti(String.format("Id %d ei ole olemassa; ei muutettu", id)));
    }

}
