package ma.ensa.bank.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    private TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/")
    public Map<String, String> getTest(){
        testRepository.save(new Test(null,"docker"));
        Map<String, String> data = new HashMap<>();
        data.put("result","done");
        return data;
    }
}
