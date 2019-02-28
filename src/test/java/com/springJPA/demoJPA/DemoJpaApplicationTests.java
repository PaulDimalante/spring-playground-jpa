package com.springJPA.demoJPA;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoJpaApplicationTests {
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

    @Autowired
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonsController lessonsController;

    @Before
    public void beforeAll() {
        this.lessonRepository.deleteAll();
        this.lessonsController = new LessonsController(this.lessonRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(lessonsController).build();
    }

    @After
    public void afterAll() {
        this.lessonRepository.deleteAll();
    }

    @Test
    public void givenEmptyLessonRepositoryReturnEmptyList() throws Exception {
        String responseString = mockMvc.perform(get("/lessons"))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat("[]".equals(responseString), is(true));
    }

    @Test
    public void givenOneLessonRepositoryReturnOneList() throws Exception {
        //setup
        Lesson lesson = new Lesson();
        lesson.setTitle("Math");
        lesson.setDeliveredOn(new Date());
        lesson = lessonRepository.save(lesson);

        //exercise
        String responseString = mockMvc.perform(get("/lessons"))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Lesson> response = objectMapper.readValue(responseString, new TypeReference<List<Lesson>>() {
        });

        //assert
        assertThat(response.contains(lesson), is(true));

        //teardown
        lessonRepository.deleteAll();
    }

    @Test
    public void saveLessonRepositoryReturnSavedList() throws Exception {
        //setup
        Lesson lessonMath = new Lesson();
        lessonMath.setTitle("Math");
        lessonMath.setDeliveredOn(new Date());

        String requestString = objectMapper.writeValueAsString(lessonMath);

        //exercise
        String postString = mockMvc.perform(post("/lessons")
                .accept(MediaType.APPLICATION_JSON)
                .content(requestString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        lessonMath = objectMapper.readValue(postString, Lesson.class);

        String responseString = mockMvc.perform(get("/lessons"))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Lesson> response = objectMapper.readValue(responseString, new TypeReference<List<Lesson>>() {
        });

        //assert
        assertThat(response.contains(lessonMath), is(true));

        //teardown
        lessonRepository.deleteAll();
    }

}
