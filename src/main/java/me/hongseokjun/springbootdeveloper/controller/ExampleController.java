package me.hongseokjun.springbootdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller // 컨트롤러임을 명시적으로 표시
public class ExampleController {
    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model){ // 뷰로 데이터를 넘겨주는 모델 객체
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍석준");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동","독서"));

        model.addAttribute("person", examplePerson); // Person 객체 저장
        model.addAttribute("today", LocalDate.now());

        return "example"; // example.html 라는 뷰 조회
        // 클래스에 @Controller 이므로 뷰의 이름을 반환 -> 반환하는 값의 이름을 가진 뷰의 파일을 찾아라!!
        // 라고 이해하고 resource/templates 에서 example.html 을 찾아 웹에 띄움
    }

    @Setter
    @Getter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }

}
