create table comment (
    id bigint not null,
    commented_at timestamp(6),
    content varchar(255),
    owner_id bigint,
    post_id bigint not null,
    primary key (id)
);

create table contact (
    id bigint not null,
    email varchar(255),
    number varchar(255),
    primary key (id)
);

create table post (
    id bigint not null,
    content varchar(255),
    posted_at timestamp(6),
    user_id bigint not null,
    primary key (id)
);

create table post_stats (
    post_id bigint not null,
    comments_count bigint,
    primary key (post_id)
);

create table post_stats_reaction_counts (
    post_stats_post_id bigint not null,
    reaction_counts_post_id bigint not null,
    reaction_counts_reaction_type varchar(255) not null,
    primary key (post_stats_post_id, reaction_counts_post_id, reaction_counts_reaction_type)
);

create table reaction (
    post_id bigint not null,
    user_id bigint not null,
    reacted_at timestamp(6),
    type varchar(255) check (type in ('LIKE','HAPPY','DISLIKE','ANGRY','LAUGH','LOVE')),
    primary key (post_id, user_id)
);

create table reaction_count (
    post_id bigint not null,
    reaction_type varchar(255) not null check (reaction_type in ('LIKE','HAPPY','DISLIKE','ANGRY','LAUGH','LOVE')),
    "count" bigint,
    post_stats_post_id bigint,
    primary key (post_id, reaction_type)
);

create table users (
    id bigint not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    middle_name varchar(255),
    username varchar(255) not null,
    contact_id bigint,
    primary key (id)
);


alter table if exists contact
       add constraint unique_emailaddress unique (email);

alter table if exists contact
       add constraint unique_phonenumber unique (number);

alter table if exists post_stats_reaction_counts
       add constraint unqiue_post_reaction_stats unique (reaction_counts_post_id, reaction_counts_reaction_type);

alter table if exists users
       add constraint unique_username unique (username);

alter table if exists users
       add constraint unique_user_contact unique (contact_id);


create sequence comment_seq start with 1 increment by 1;
create sequence contact_seq start with 1 increment by 1;
create sequence post_seq start with 1 increment by 1;
create sequence user_seq start with 1 increment by 1;

alter table if exists comment
       add constraint comment_user_fk
       foreign key (owner_id)
       references users;

alter table if exists comment
       add constraint comment_post_fk
       foreign key (post_id)
       references post;

alter table if exists post
       add constraint post_user_fk
       foreign key (user_id)
       references users;

--alter table if exists post_stats
--       add constraint post_stats_post_fk
--       foreign key (post_id)
--       references post;

alter table if exists post_stats
       add constraint post_stats_reaction_post_stats_post_fk
       foreign key (post_id)
       references post;

-- alter table if exists post_stats_reaction_counts
--       add constraint post_stats_reaction_reaction_count_fk
--       foreign key (reaction_counts_post_id, reaction_counts_reaction_type)
--       references reaction_count;
--
--alter table if exists post_stats_reaction_counts
--       add constraint  post_stats_reaction_post_stats_post_fk
--       foreign key (post_stats_post_id)
--       references post_stats;

alter table if exists reaction
       add constraint reaction_user_fk
       foreign key (user_id)
       references users;

alter table if exists reaction
       add constraint reaction_post_fk
       foreign key (post_id)
       references post;

alter table if exists users
       add constraint user_contact_fk
       foreign key (contact_id)
       references contact;
