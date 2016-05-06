package com;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.model.Expense;
import com.model.Role;
import com.model.Status;
import com.model.WSUser;
import com.repository.ExpensesRepository;
import com.repository.UserRepository;


/**
 * Created by 212457624 on 4/5/2016.
 */
@SpringBootApplication
//@Configuration
@ComponentScan("com.*")
@EnableAutoConfiguration 
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    @Autowired
    public InitializingBean insertDefaultUsers() {
        return new InitializingBean() {
            @Autowired
            private UserRepository userRepository;
            @Autowired
            ExpensesRepository expensesRepository;

            @Override
            public void afterPropertiesSet() {
                addUser("admin", "admin");
                addUser("user", "user");

                generateExpenses();
            }

            private void generateExpenses() {
                int howMany = 200;
                ArrayList<Expense> expenses = new ArrayList<>(howMany);
                Random random = new Random();
                List<String> merchants = Arrays.asList("Office supplies", "Electronics", "Rental car", "Airline",
                        "Hotel", "Restaurant", "Taxi", "Ride sharing", "Fast food",
                        "Parking", "Breakfast", "Shuttle");
                WSUser user = userRepository.findOneByUsername("user").orElseThrow(() -> new UsernameNotFoundException(""));
                long time = System.currentTimeMillis();
                long fiveDays = 5 * 24 * 60 * 60 * 1000;

                for (int i = 0; i < howMany; i++) {
                    time -= (random.nextDouble() * fiveDays);

                    Expense expense = new Expense();
                    expense.setUser(user);
                    expense.setMerchant(merchants.get(random.nextInt(merchants.size())));
                    BigDecimal total = new BigDecimal(random.nextDouble() * random.nextDouble() * 300 + 10);
                    expense.setTotal(total.setScale(2, BigDecimal.ROUND_HALF_DOWN));
                    expense.setDate(new Date(time));
                    expense.setComment("Expense from NYC business trip.");

                    if (i > 30) {
                        expense.setStatus(Status.REIMBURSED);
                    } else if (i > 15) {
                        expense.setStatus(Status.IN_PROGRESS);
                    } else {
                        expense.setStatus(Status.NEW);
                    }

                    expenses.add(expense);
                }

                expensesRepository.save(expenses);
            }

            private void addUser(String username, String password) {
                WSUser user = new WSUser();
                user.setUsername(username);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                user.setRole(username.equals("admin") ? Role.ADMIN : Role.USER);
                userRepository.save(user);
            }
        };
    }
}
