package fi.academy.todo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToDoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    Tehtava tehtava = new Tehtava("Osta kukkia");
    private int apu;

    @Test
    public void testaaToDoEiOleNull() {
        ResponseEntity<List<Tehtava>> response =
                restTemplate.exchange("/api/todo", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Tehtava>>() {
                        });
        assertThat(response.getStatusCodeValue(), is(200));
        List<Tehtava> todo = response.getBody();
        assertThat(todo, is(notNullValue()));
    }

    @Test
    public void listaaKaikki() {
        ResponseEntity<List<Tehtava>> response =
                restTemplate.exchange("/api/todo", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Tehtava>>() {
                        });
        assertThat(response.getStatusCodeValue(), is(200));
        List<Tehtava> todo = response.getBody();
        assertTrue(!todo.isEmpty());
    }

    @Test
    public void luoUusi() {
        ResponseEntity<Tehtava> response = restTemplate
                .postForEntity(URI.create("/api/todo"), tehtava, Tehtava.class);
        assertEquals(response.getStatusCodeValue(), 201);
        Tehtava testi = response.getBody();
        assertThat(testi, is(notNullValue()));
        assertThat(testi.getTehtava(), is("Osta kukkia"));
        assertThat(apu, is(testi.getId()));
    }

    @Test
    public void muokkaa() throws URISyntaxException {
        ResponseEntity<List<Tehtava>> responseApu =
                restTemplate.exchange("/api/todo", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Tehtava>>() {
                        });
        List<Tehtava> todo = responseApu.getBody();
        apu = todo.get(todo.size()-1).getId();
        String url = String.format("/api/todo/%d",apu);
        RequestEntity<Tehtava> requestEntity =
                new RequestEntity<>(new Tehtava("Osta kivennäisvettä"), HttpMethod.PUT, new URI(url));
        ResponseEntity<Tehtava> response =
                restTemplate.exchange(requestEntity, Tehtava.class);
        assertEquals(200, response.getStatusCodeValue());
        Tehtava testi = response.getBody();
        assertThat(testi.getTehtava(), is("Osta kivennäisvettä"));
        assertThat(testi.getId(), is(apu));

        poista();
    }



    public void poista() {
        ResponseEntity<?> response = restTemplate.exchange("/api/todo/" + apu,
                HttpMethod.DELETE, null, Tehtava.class, "" + apu);
        assertEquals(200, response.getStatusCodeValue());
    }

}