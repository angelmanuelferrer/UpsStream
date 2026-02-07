package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;

import java.util.List;
@Service
public class GenderService {
    private GenderRepository repository;
    @Autowired
    public GenderService(GenderRepository repository){
        this.repository=repository;
    }
    public List<Gender> findAll(){
        return repository.findAllGender();
    }
    public Gender save(Gender gender){
        return repository.save(gender);
    }
    public void delete(int id){
         repository.delete(findById(id));
    }
    public Gender findById(Integer id){
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("gender","id",id));
    }
    
}