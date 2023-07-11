package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.Type;
import VoThuanLoi2.demo.respository.TypeRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    private TypeRespository repo;

    public List<Type> getAll(){
        return repo.findAll();
    }
}
