create table answer
(
    id         integer not null auto_increment,
    date_send  datetime(6),
    data       tinyblob,
    task_id    integer,
    student_id integer,
    primary key (id)
) engine = InnoDB;

create table chat
(
    id          integer not null auto_increment,
    description varchar(255),
    course_id   integer,
    creator_id  integer,
    primary key (id)
) engine = InnoDB;

create table course
(
    id          integer not null auto_increment,
    description varchar(255),
    name        varchar(255),
    primary key (id)
) engine = InnoDB;

create table message
(
    id        integer not null auto_increment,
    content   varchar(255),
    date_send datetime(6),
    chat_id   integer,
    user_id   integer,
    primary key (id)
) engine = InnoDB;

create table role
(
    id   integer not null auto_increment,
    name varchar(255),
    primary key (id)
) engine = InnoDB;

create table task
(
    id          integer not null auto_increment,
    date_ending datetime(6),
    description varchar(255),
    data        longblob,
    course_id   integer,
    creator_id  integer,
    primary key (id)
) engine = InnoDB;

create table user
(
    id         integer not null auto_increment,
    code       varchar(255),
    firstname  varchar(255),
    password   varchar(255),
    is_teacher bit,
    is_active  bit,
    mail       varchar(255),
    patronymic varchar(255),
    surname    varchar(255),
    primary key (id)
);

create table user_chat
(
    user_id integer not null,
    chat_id integer not null
);

create table user_course_agent
(
    course_id integer not null,
    role_id   integer not null,
    user_id   integer not null,
    primary key (course_id, role_id, user_id)
);

create table user_task
(
    task_id    integer not null,
    student_id integer not null,
    grade      integer,
    primary key (task_id, student_id)
);

alter table role
    add constraint UK_epk9im9l9q67xmwi4hbed25do unique (name);

alter table answer
    add constraint FK32s29gitnrc8dhk0m2qf218r3
        foreign key (task_id) references task (id);

alter table answer
    add constraint FK3sfmk2rhlucn6eu6ghhltgmjs
        foreign key (student_id) references user (id);

alter table chat
    add constraint FKtp0bjkh4hosn6npr4a9li1ta5
        foreign key (course_id) references course (id);

alter table chat
    add constraint FKl3wufhnqgvi8qhyjvgck3mo2w
        foreign key (creator_id) references user (id);

alter table message
    add constraint FKmejd0ykokrbuekwwgd5a5xt8a
        foreign key (chat_id) references chat (id);

alter table message
    add constraint FKb3y6etti1cfougkdr0qiiemgv
        foreign key (user_id) references user (id);

alter table task
    add constraint FKc06t5km0dmc9hktw7n3p6xd52
        foreign key (course_id) references course (id);

alter table task
    add constraint FKqc1galw66ryn480v0lygu3n4c
        foreign key (creator_id) references user (id);

alter table user_chat
    add constraint FKlr24iyc3pugqj18ybujh6hqmj
        foreign key (chat_id) references chat (id);

alter table user_chat
    add constraint FKojd9hqbl3e7kq3vvr9ym218i4
        foreign key (user_id) references user (id);

alter table user_course_agent
    add constraint FKk7k1h7q9taphfj21u3pyo9w1r
        foreign key (course_id) references course (id);

alter table user_course_agent
    add constraint FK7k0vcmwd2svvisg9c9y5i8jap
        foreign key (user_id) references user (id);

alter table user_course_agent
    add constraint FK6exbjr5bshbcps5u9kobx5to
        foreign key (role_id) references role (id);

alter table user_task
    add constraint FKs1eip225yhwgmi5io6m5naadt
        foreign key (student_id) references user (id);

alter table user_task
    add constraint FKvs34bjkmpbk2e54qlrol3ilt
        foreign key (task_id) references task (id);