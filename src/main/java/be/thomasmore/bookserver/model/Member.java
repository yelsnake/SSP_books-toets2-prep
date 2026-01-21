package be.thomasmore.bookserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"likes"})
@ToString(exclude = {"likes"})
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "member_number", nullable = false)
    private String memberNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    // member쪽으로 member.likes가 owner되도록
    @JoinTable(
            name = "member_likes",
            joinColumns = @JoinColumn(name = "liked_by_id"),
            inverseJoinColumns = @JoinColumn(name = "likes_id")
    )
    private Set<Book> likes = new HashSet<>();
}
