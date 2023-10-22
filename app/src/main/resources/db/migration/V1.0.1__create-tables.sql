------- fighter ------------------------

create sequence fighter_seq
  increment by 50;

alter sequence fighter_seq owner to lost;

create table fighter
(
  created_at timestamp(6) with time zone not null,
  id         bigint                      not null
    primary key,
  updated_at timestamp(6) with time zone,
  name       varchar(255)                not null
    constraint "uk-name"
      unique,
  skill      text[]
);

alter table fighter
  owner to lost;

------- match --------------------------

create sequence match_seq
  increment by 50;

alter sequence match_seq owner to lost;

create table match
(
  created_at timestamp(6) with time zone not null,
  id         bigint                      not null
    primary key,
  updated_at timestamp(6) with time zone,
  time_range tstzrange                   not null
);

alter table match
  owner to lost;

------- match_fighter ------------------

create sequence match_fighter_seq
  increment by 50;

alter sequence match_fighter_seq owner to lost;

create table match_fighter
(
  fighter_id bigint not null
    constraint fk5dm68s5rpwfuljxe0cxwvpmi0
      references fighter,
  id         bigint not null
    primary key,
  match_id   bigint not null
    constraint fkmhloeqgykq3m8puqqg8oiyw5e
      references match,
  time_range tstzrange,
  constraint "uk-match_id-fighter_id"
    unique (match_id, fighter_id),
  -- 同一个 fighter_id, time_range 不重复
  constraint match_fighter_fighter_id_time_range_excl
    exclude using gist (fighter_id with =, time_range with &&)
);

alter table match_fighter
  owner to lost;


------- match_round --------------------

create sequence match_round_seq
  increment by 50;

alter sequence match_round_seq owner to lost;

create table match_round
(
  id         bigint    not null
    primary key,
  match_id   bigint    not null
    constraint fk5hrpl3kmgvdn0lgq4spoindav
      references match,
  time_range tstzrange not null,
  -- 同一个 round_id, time_range 不重复
  constraint match_round_match_id_time_range_excl
    exclude using gist (match_id with =, time_range with &&)
);

alter table match_round
  owner to lost;

