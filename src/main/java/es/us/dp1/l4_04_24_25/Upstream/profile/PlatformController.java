package es.us.dp1.l4_04_24_25.Upstream.profile;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
@RestController
@RequestMapping("api/v1/platform")
public class PlatformController {
    private PlatformService service;
    @Autowired
    public PlatformController(PlatformService service){
        this.service=service;
    }
    @GetMapping
    public ResponseEntity<List<Platform>> findAll(){
        return new ResponseEntity<>(service.findAll(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Platform> findById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(service.findById(id),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Platform> edit(@PathVariable("id") Integer id,@RequestBody @Valid Platform platform){
        
        return new ResponseEntity<>(service.save(platform),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Platform> create(@RequestBody @Valid Platform platform){
        return new ResponseEntity<>(service.save(platform),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        service.delete(id);
       return ResponseEntity.ok().build();
    }
}