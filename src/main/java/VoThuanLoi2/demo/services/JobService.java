package VoThuanLoi2.demo.services;

import VoThuanLoi2.demo.entity.Job;
import VoThuanLoi2.demo.respository.JobRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRespository repo;
    public List<Job> getAll(){
        return repo.findAll();
    }
    public void saveJob(Job j){
        repo.save(j);
    }
    public void deleteJob(Long id){
        repo.deleteById(id);
    }
    public Job getJobById(Long id){
        return repo.findById(id).orElse(null);
    }
}
