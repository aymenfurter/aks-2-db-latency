package dbtest.dbtest;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBTestController {
   @Autowired
   private JdbcTemplate jdbcTemplateObject;

   @GetMapping("/mock-test")
    String test() {
        return "ok";
    }
    
    @GetMapping("/db-test")
    String testDb() {
        String sql = "SELECT 1";
        Integer result = jdbcTemplateObject.queryForObject(sql, Integer.class);
        if (result == 1) {
            return "ok";
        } else {
            return "error";
        }
    }
}
