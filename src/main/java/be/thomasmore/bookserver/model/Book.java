package be.thomasmore.bookserver.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"authors", "likedBy"})
@ToString(exclude = {"authors", "likedBy"})
@Entity
public class Book {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @NotBlank(message = "Book Title should not be blank")
    @NotNull
    private String title;

    @Column(length=1024)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Author> authors;

    //member가 owner이기 때문에 mappedBy
    @ManyToMany(mappedBy = "likes", fetch = FetchType.LAZY)
    private Set<Member> likedBy = new HashSet<>();
}

