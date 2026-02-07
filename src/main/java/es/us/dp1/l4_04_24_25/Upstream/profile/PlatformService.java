package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;

import java.util.List;
@Service
public class PlatformService {
    private PlatformRepository repository;
    @Autowired
    public PlatformService(PlatformRepository repository){
        this.repository=repository;
    }
    public List<Platform> findAll(){
        return repository.findAllPlatform();
    }
    public Platform save(Platform platform){
        return repository.save(platform);
    }
    public void delete(int id){
         repository.delete(findById(id));
    }
    public Platform findById(Integer id){
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("platform","id",id));
    }
    
}