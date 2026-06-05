package org.me.dibs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @Lob
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private byte[] image;
    private String location;
    private String contact;
    private String time;
    Boolean isLost;
}
