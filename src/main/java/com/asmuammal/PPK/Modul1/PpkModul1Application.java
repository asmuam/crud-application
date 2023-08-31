package com.asmuammal.PPK.Modul1;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringBootApplication
@Controller
public class PpkModul1Application {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    public static void main(String[] args) {
        SpringApplication.run(PpkModul1Application.class, args);
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Mahasiswa> mahasiswaList;
        mahasiswaList = mahasiswaRepository.findAll();
        model.addAttribute("mahasiswaList", mahasiswaList);
        return "index";
    }

    @PostMapping("/add")
    public String addMahasiswa(@ModelAttribute Mahasiswa mahasiswa, BindingResult result, RedirectAttributes redirectAttributes) {
        // Validasi NIM uniqueness
        Mahasiswa existingMahasiswa = mahasiswaRepository.findByNim(mahasiswa.getNim());
        if (existingMahasiswa != null) {
            result.rejectValue("nim", "error.nim", "NIM sudah ada");
            redirectAttributes.addFlashAttribute("result", result);
            redirectAttributes.addFlashAttribute("mahasiswa", mahasiswa);
            return "redirect:/";
        }

        // Validasi NIM length
        if (mahasiswa.getNim().length() != 9 || !mahasiswa.getNim().matches("\\d{9}")) {
            result.rejectValue("nim", "error.nim", "Panjang NIM harus 9 digit angka");
            redirectAttributes.addFlashAttribute("result", result);
            redirectAttributes.addFlashAttribute("mahasiswa", mahasiswa);
            return "redirect:/";
        }

        // Validasi nama tidak boleh kosong atau hanya terdiri dari spasi
        if (mahasiswa.getNama().trim().isEmpty()) {
            result.rejectValue("nama", "error.nama", "Nama belum diisi");
            redirectAttributes.addFlashAttribute("result", result);
            redirectAttributes.addFlashAttribute("mahasiswa", mahasiswa);
            return "redirect:/";
        }
        // Save the new Mahasiswa
        mahasiswaRepository.save(mahasiswa);
        return "redirect:/";
    }

    @GetMapping("/edit/{nim}")
    @Transactional
    public String editMahasiswa(@PathVariable String nim, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findByNim(nim);
        model.addAttribute("mahasiswa", mahasiswa);
        return "edit";
    }

    @GetMapping("/delete/{nim}")
    @Transactional
    public String deleteMahasiswa(@PathVariable String nim) {
        mahasiswaRepository.deleteByNim(nim);
        return "redirect:/";
    }

    @GetMapping("/details/{nim}")
    @Transactional
    public String detailMahasiswa(@PathVariable String nim, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findByNim(nim);
        model.addAttribute("mahasiswa", mahasiswa);
        return "details";
    }

    @PostMapping("/update")
    public String updateMahasiswa(@ModelAttribute Mahasiswa mahasiswa, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        // Validasi nama tidak boleh kosong atau hanya terdiri dari spasi
        if (mahasiswa.getNama().trim().isEmpty()) {
            result.rejectValue("nama", "error.nama", "Nama belum diisi");
            redirectAttributes.addFlashAttribute("result", result);
            redirectAttributes.addFlashAttribute("mahasiswa", mahasiswa);
            return "redirect:/";
        }

        // Update the existing Mahasiswa
        mahasiswaRepository.save(mahasiswa);
        return "redirect:/"; // Redirect to the root path after updating
    }

    @PostMapping("/search")
    public String searchMahasiswa(@RequestParam("search") String searchQuery, Model model) {
        List<Mahasiswa> searchResults = mahasiswaRepository.findByNimContainingIgnoreCaseOrNamaContainingIgnoreCase(searchQuery, searchQuery);
        model.addAttribute("mahasiswaList", searchResults);
        model.addAttribute("searchQuery", searchQuery); // Tambahkan query pencarian ke model
        return "search_result"; // Buat halaman "search_result.html" untuk menampilkan hasil pencarian
    }

}
