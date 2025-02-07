package com.example.demo.controller;

import com.example.demo.dto.AddAccountRequest;
import com.example.demo.dto.SearchResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.Student;
import com.example.demo.repotitory.AccountRepository;
import com.example.demo.service.checker.CheckerService;
import com.example.demo.service.student.StudentService;
import com.example.demo.ulti.ExcelExportUtil;
import com.example.demo.ulti.SecurityHelper;
import com.example.demo.ulti.StringHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    private final ExcelExportUtil excelExportUtil;
    private final CheckerService checkerService;
    private final AccountRepository accountRepository;

    @GetMapping("/reg-list")
    public String listFingerResgiter(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "student_reg";
    }

    @GetMapping("/manage")
    public String listStudent(Model model) {
        model.addAttribute("addStudentForm", new Student());
        model.addAttribute("editStudentForm", new Student());

        model.addAttribute("students", studentService.getAll());
        return "student_manage";
    }

    @PostMapping("/upload/excel")
    public ResponseEntity<?> uploadExcel(@RequestParam(name = "file") MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select an Excel file to upload.");
            }
            return ResponseEntity.ok(readExcelFile(multipartFile));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    private List<List<String>> readExcelFile(MultipartFile file) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                rowData.add(cell.toString());
            }
            rows.add(rowData);
        }
        workbook.close();
        return rows;
    }

    @GetMapping("/search-user")
    public String search(Model model, @RequestParam(name = "search") String query) {
        SearchResponse searchResponse = new SearchResponse();
        Student student = studentService.getById(query).orElse(null);
        searchResponse.setCheckers(checkerService.getAllByUser(student));
        searchResponse.setStudent(student == null ? new Student() : student);
        model.addAttribute("result", searchResponse);
        return "search";
    }

    @PostMapping("/import/excel")
    public ResponseEntity<?> importFromExcel(@RequestBody List<List<String>> data) throws Exception {
        try {
            List<Student> students = new ArrayList<>();
            List<Account> accounts = new ArrayList<>();
            for (List<String> item : data) {
                // create account tho
                Student student = new Student();
                Account account = new Account();
                account.setUsername(item.get(2));
                account.setPassword(new BCryptPasswordEncoder().encode("123456"));
                account.setRawPassword("123456");
                account.setRole("ROLE_USER");
                student.setName(item.get(0));
                student.setClassName(item.get(1));
                student.setId(item.get(2));
                student.setPhone(item.get(3));
                student.setSex(item.get(4));
                student.setEmail(item.get(5));
                students.add(student);
                accounts.add(account);
                student.setAccount(account);
            }
            accountRepository.saveAll(accounts);
            studentService.addAll(students);
            return ResponseEntity.ok(new HashMap<>());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Student> dataList = studentService.getAll();
        ByteArrayOutputStream excelFile = excelExportUtil.exportStudentToExcel(dataList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile.toByteArray());
    }

    @PostMapping("/add-new-student")
    public String addStudent(@ModelAttribute("addStudentForm") Student student) {
        try {
            studentService.addStudent(student);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/student/manage";
    }

    @PostMapping("/edit-student")
    public String editStudent(@ModelAttribute("editStudentForm") Student student) {
        try {
            studentService.editStudent(student);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/student/manage";
    }

    @PostMapping("/delete-student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable(name = "id") String id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok().body(new HashMap<>());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/profile-edit")
    public String studentProfileEdit(Model model) {
        // get student info
        Account account = SecurityHelper.getAccount();
        Student student = studentService.findByAccount(account);
        if (student == null) {
            return "redirect:/logout";
        }
        model.addAttribute("student", student);
        return "student_profile_edit";
    }

    @PostMapping("/profile-edit-post")
    public String updateStudentProfile(@ModelAttribute("student") Student student) {
        try {
            studentService.editStudent(student);
            return "redirect:/student/profile-edit";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/student/profile-edit";
    }

    @PutMapping("/add-account-to-student/{id}")
    public ResponseEntity<?> addAccountToStudent(@RequestBody AddAccountRequest request, @PathVariable(name = "id") String Id) {
        try {
            return studentService.addAccountToStudent(Id, request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
