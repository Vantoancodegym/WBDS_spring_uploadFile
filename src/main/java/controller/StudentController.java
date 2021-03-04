package controller;

import model.Student;
import model.StudentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.student.IStudentSevice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private IStudentSevice studentSevice;

    @Autowired
    private Environment environment;

    @GetMapping()
    public ModelAndView index(){
        ModelAndView index = new ModelAndView("index");
        index.addObject("list", studentSevice.findAll());
        return index;
    }

    @GetMapping("/create")
    public ModelAndView createForm(){
        ModelAndView create = new ModelAndView("create", "s", new StudentForm());
        return create;
    }

    @PostMapping("/create")
    public ModelAndView creatNewStudent(@ModelAttribute StudentForm s) throws IOException {
        ModelAndView modelAndView = new ModelAndView("create");
        MultipartFile[] files = s.getAvatar();

        List<String> avatar=new ArrayList<>();
        String thu_muc = environment.getProperty("file_download").toString();
        System.out.println(thu_muc);
        for (MultipartFile f:files) {
            avatar.add(f.getOriginalFilename());
            FileCopyUtils.copy(f.getBytes(), new File(thu_muc+f.getOriginalFilename()));
        }

        Student s1 = new Student(s.getName(), s.getAddress(), avatar);
        studentSevice.save(s1);
        modelAndView.addObject( "s",new StudentForm() );
        modelAndView.addObject( "mess","Thanh cong");
        return modelAndView;

    }
}