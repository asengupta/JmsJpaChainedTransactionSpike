package hello;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Email {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;

    @JsonProperty
    public String to;

    @JsonProperty
    public String message;

    public Email() {
    }

    public Email(String to, String message) {

        this.to = to;
        this.message = message;
    }
}
