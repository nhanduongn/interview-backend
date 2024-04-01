
-- create table

create table public.users
(
    id           integer not null primary key,
    username     varchar(255),
    email        varchar(255),
    password     varchar(255),
    role         varchar(255),
    created_by   varchar(255),
    created_date timestamp(6),
    updated_by   varchar(255),
    updated_date timestamp(6)
);
alter table public.users owner to postgres;

create table public.token
(
    id         integer not null primary key,
    expired    boolean not null,
    revoked    boolean not null,
    token      varchar(255) unique,
    token_type varchar(255),
    user_id    integer
        constraint fk_token_users references public.users
);

alter table public.token owner to postgres;

create table public.student
(
    id           integer not null primary key,
    user_id      integer unique
        constraint fk_student_user references public.users,
    created_by   varchar(255),
    created_date timestamp(6),
    updated_by   varchar(255),
    updated_date timestamp(6)
);

alter table public.student owner to postgres;

create table public.exams
(
    id           integer not null primary key,
    name         varchar(255),
    description  varchar(255),
    created_date timestamp(6),
    updated_date timestamp(6),
    created_by   varchar(255),
    updated_by   varchar(255)
);

alter table public.exams owner to postgres;

create table public.score
(
    id           integer not null primary key,
    result       double precision,
    exam_id      integer
        constraint fk_score_exam references public.exams,
    student_id   integer
        constraint fk_score_student references public.student,
    created_by   varchar(255),
    created_date timestamp(6),
    updated_by   varchar(255),
    updated_date timestamp(6)
);

alter table public.score owner to postgres;

create table public.question
(
    id           integer not null primary key,
    ques         varchar(255),
    exam_id      integer
        constraint fk_question_exam references public.exams,
    created_by   varchar(255),
    created_date timestamp(6),
    updated_by   varchar(255),
    updated_date timestamp(6)
);
alter table public.question owner to postgres;

create table public.exam_student
(
    id           integer not null primary key,
    exam_id      integer
        constraint fk_est_exam references public.exams,
    student_id   integer
        constraint fk_est_student references public.student,
    created_by   varchar(255),
    created_date timestamp(6),
    updated_by   varchar(255),
    updated_date timestamp(6)
);

alter table public.exam_student owner to postgres;

create table public.answer
(
    id           integer not null primary key,
    ans          varchar(255),
    is_correct   boolean not null,
    question_id  integer
        constraint fk_answer_question references public.question,
    created_by   varchar(255),
    created_date timestamp(6),
    updated_by   varchar(255),
    updated_date timestamp(6)
);

alter table public.answer owner to postgres;

create table public.answer_student
(
    id           integer not null primary key,
    answer_id    integer
        constraint fk_ast_answer references public.answer,
    student_id   integer
        constraint fk_ast_student references public.student,
    exam_id      integer
        constraint fk_ast_exam references public.exams,
    created_by   varchar(255),
    created_date timestamp(6),
    updated_by   varchar(255),
    updated_date timestamp(6)
);

alter table public.answer_student owner to postgres;



-- create sequence

create sequence public.answer_seq increment by 1;

alter sequence public.answer_seq owner to postgres;

create sequence public.answer_student_seq increment by 1;

alter sequence public.answer_student_seq owner to postgres;

create sequence public.exam_student_seq increment by 1;

alter sequence public.exam_student_seq owner to postgres;

create sequence public.exams_seq increment by 1;

alter sequence public.exams_seq owner to postgres;

create sequence public.question_seq increment by 1;

alter sequence public.question_seq owner to postgres;

create sequence public.score_seq increment by 1;

alter sequence public.score_seq owner to postgres;

create sequence public.student_seq increment by 1;

alter sequence public.student_seq owner to postgres;

create sequence public.token_seq increment by 1;

alter sequence public.token_seq owner to postgres;

create sequence public.users_seq increment by 1;

alter sequence public.users_seq owner to postgres;

insert into users (id, username, email, password, role, created_by, created_date, updated_by, updated_date)
VALUES (nextval('users_seq'), 'teacher', 'teacher@example.com', '$2a$10$OBlIJp8iW2xmT0EcCUw2dOH9nbsbT4V80zrCaK48Exn5Q758R3Emi', 'TEACHER', 'admin', now(), 'admin', now());
