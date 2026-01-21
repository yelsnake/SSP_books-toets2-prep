create table member_likes
(
    liked_by_id integer not null,
    likes_id integer not null,
    primary key (liked_by_id, likes_id),
    constraint fk_member_likes_member foreign key (liked_by_id) references member (id),
    constraint fk_member_likes_book foreign key (likes_id) references book (id)
);