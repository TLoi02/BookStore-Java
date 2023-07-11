package VoThuanLoi2.demo.controller;

import VoThuanLoi2.demo.entity.Apply;
import VoThuanLoi2.demo.entity.Category;
import VoThuanLoi2.demo.entity.Job;
import VoThuanLoi2.demo.services.ApplyService;
import VoThuanLoi2.demo.services.FileUpload;
import VoThuanLoi2.demo.services.JobService;
import VoThuanLoi2.demo.services.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tuyendung")
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private MailService mailService;
    private final FileUpload fileUpload;


    @GetMapping("")
    public String index(Model model){
        model.addAttribute("listJob",jobService.getAll());
        model.addAttribute("job",new Job());
        model.addAttribute("apply",new Apply());


        return "job/index";
    }

    @PostMapping("/nopdon")
    public String submitForm(@Valid Apply newApply,
                             @RequestParam MultipartFile imageCV,
                             BindingResult result,
                             Model model) throws IOException {
        if (result.hasErrors()){
            model.addAttribute("listJob",jobService.getAll());
            model.addAttribute("job",new Job());
            model.addAttribute("apply",new Apply());
            return "job/index";
        }
        if (imageCV != null && imageCV.getSize() > 0){
            String imageURL = fileUpload.uploadFile(imageCV);
            newApply.setImage(imageURL);
        }
        applyService.add(newApply);

        String nameJobApply = newApply.getJob().getTitle();
        //Handel notify to mail
        String body = "Thông tin ứng tuyển: " +
                "\nHọ và tên: " + newApply.getName() +
                ".\nEmail: " + newApply.getEmail() +
                ".\nĐiện thoại: " + newApply.getPhone() +
                ".\nLink CV: "+newApply.getImage()+".";
        //mail hr
        String mailAddress = "vothuanloi110802@gmail.com";
        //send mail
        mailService.sendNewMail(mailAddress,"Thông báo có nhân viên ứng tuyển", body);

        return "job/success";
    }
}
