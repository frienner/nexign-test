CREATE TABLE taskdb.public.tasks (
                                     id bigint NOT NULL,
                                     duration bigint,
                                     version bigint,
                                     name character varying(255),
                                     result character varying(255),
                                     PRIMARY KEY (id)
);

ALTER TABLE taskdb.public.tasks OWNER TO "user";