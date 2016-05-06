package com.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Expense;
import com.model.WSUser;


@Repository  // hlltarakci added
public interface ExpensesRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserOrderByDateDesc(WSUser user);
}
