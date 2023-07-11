package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.Apply;
import VoThuanLoi2.demo.respository.ApplyRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {
    @Autowired
    private ApplyRespository repo;
    public List<Apply> getAll(){
        return repo.findAll();
    }
    public void add(Apply a){
        repo.save(a);
    }
    public Apply findById(Long id){
        return repo.findById(id).orElse(null);
    }
}
