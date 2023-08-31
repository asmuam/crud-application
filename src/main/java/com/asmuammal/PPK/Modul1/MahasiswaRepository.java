package com.asmuammal.PPK.Modul1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
    Mahasiswa findByNim(String nim);
    void deleteByNim(String nim);
    List<Mahasiswa> findByNimContainingIgnoreCaseOrNamaContainingIgnoreCase(String search, String search0);
}
