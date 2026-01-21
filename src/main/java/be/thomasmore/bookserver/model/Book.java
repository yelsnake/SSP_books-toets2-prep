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
@EqualsAndHashCode(exclude = {"authors"})
@ToString(exclude = {"authors"})
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

    @ManyToMany(mappedBy = "likes")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //likedBy여야함
    //@Data 사용시 양방향 관계에서 재귀로 터질 수 있어 exclude 처리가 안전
    private Set<Member> likedBy = new HashSet<>();
}

