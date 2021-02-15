package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class SetPropertyTest
{
    public static class Student
    {
        private String name;
        private Integer age;
        private Double score;
        private Boolean male;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Integer getAge()
        {
            return age;
        }

        public void setAge(Integer age)
        {
            this.age = age;
        }

        public Double getScore()
        {
            return score;
        }

        public void setScore(Double score)
        {
            this.score = score;
        }

        public Boolean getMale()
        {
            return male;
        }

        public void setMale(Boolean male)
        {
            this.male = male;
        }
    }

    @Test
    public void test()
    {
        Component c = constructor(Student.class)
                .setProperty("name", value("byx"))
                .setProperty("age", value(21))
                .setProperty("score", value(97.5))
                .setProperty("male", value(true));
        Student s = (Student) c.create();
        assertEquals("byx", s.getName());
        assertEquals(21, s.getAge());
        assertEquals(97.5, s.getScore());
        assertTrue(s.getMale());
        assertEquals(Student.class, c.getType());
    }
}
