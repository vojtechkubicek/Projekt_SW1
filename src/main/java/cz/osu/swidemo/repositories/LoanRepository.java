package cz.osu.swidemo.repositories;

import cz.osu.swidemo.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    // Spring Boot automaticky pochopí, že má hledat v tabulce Loan podle username v entitě User
    List<Loan> findByUserUsername(String username);
}