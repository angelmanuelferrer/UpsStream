package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;

import java.util.List;
@Service
public class SagaService {
    private SagaRepository repository;
    @Autowired
    public SagaService(SagaRepository repository){
        this.repository=repository;
    }
    public List<Saga> findAll(){
        return repository.findAllSaga();
    }
    public Saga save(Saga saga){
        return repository.save(saga);
    }
    public void delete(int id){
         repository.delete(findById(id));
    }
    public Saga findById(Integer id){
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("saga","id",id));
    }
    
}