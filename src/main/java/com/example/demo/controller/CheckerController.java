package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.Checker;
import com.example.demo.entity.Student;
import com.example.demo.repotitory.StudentRepository;
import com.example.demo.service.checker.CheckerService;
import com.example.demo.ulti.ExcelExportUtil;
import com.example.demo.ulti.SecurityHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class CheckerController {

    private final CheckerService checkerService;

    private final ExcelExportUtil excelExportUtil;

    private final StudentRepository studentRepository;

    @GetMapping("/get-all")
    ResponseEntity<?> getAllChecker() {
        try {
            return ResponseEntity.ok(checkerService.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    public String index(Model model) {
        Account account = SecurityHelper.getAccount();
        model.addAttribute("loggedUser", SecurityHelper.getUser().getName());
        if (account.getRole().equals("ROLE_USER")) {
            Student student = studentRepository.findByAccount(account).orElse(null);
            if (student == null) {
                return "redirect:/logout";
            }
            model.addAttribute("checkers", checkerService.getAllByUser(student));
            return "student_checkin_his";
        }
        model.addAttribute("checkers", checkerService.getAll());
        return "student_checkin_manage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        SecurityHelper.setAuthentication(null);
        HttpSession session = request.getSession(true);
        session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        return "redirect:/";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Checker> dataList = checkerService.getAll();

        ByteArrayOutputStream excelFile = excelExportUtil.exportCheckerToExcel(dataList);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(excelFile.toByteArray());
    }
}
